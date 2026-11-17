import { render, waitFor } from '@testing-library/react';
import React from 'react';
import { beforeEach, describe, expect, it, vi } from 'vitest';

const mockNavigate = vi.fn();
const mockCompleteLogin = vi.fn();

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual<typeof import('react-router-dom')>('react-router-dom');
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  };
});

vi.mock('@/contexts/AuthProvider', () => ({
  useAuth: () => ({
    completeLogin: mockCompleteLogin,
  }),
}));

import { REDIRECT_KEY } from '@/constants';
import AuthCallback from '@/pages/AuthCallback';

describe('AuthCallback page', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    localStorage.clear();
  });

  it('processes callback once under StrictMode and redirects to stored internal path', async () => {
    localStorage.setItem(REDIRECT_KEY, '/openings/123');
    mockCompleteLogin.mockImplementationOnce(async (onBefore?: () => void) => {
      onBefore?.();
      return { userName: 'JRYAN' };
    });

    render(
      <React.StrictMode>
        <AuthCallback />
      </React.StrictMode>
    );

    await waitFor(() => expect(mockNavigate).toHaveBeenCalledWith('/openings/123', { replace: true }));
    expect(mockCompleteLogin).toHaveBeenCalledTimes(1);
    expect(localStorage.getItem(REDIRECT_KEY)).toBeNull();
  });

  it('sanitizes external redirect targets to /dashboard', async () => {
    localStorage.setItem(REDIRECT_KEY, '//evil.example.com');
    mockCompleteLogin.mockImplementationOnce(async (onBefore?: () => void) => {
      onBefore?.();
      return { userName: 'JRYAN' };
    });

    render(<AuthCallback />);

    await waitFor(() => expect(mockNavigate).toHaveBeenCalledWith('/dashboard', { replace: true }));
  });

  it('renders error notification with home action when callback fails', async () => {
    mockCompleteLogin.mockRejectedValueOnce(new Error('Invalid authorization code'));

    const { getByText } = render(<AuthCallback />);

    await waitFor(() => expect(getByText('Sign-in failed')).toBeInTheDocument());
    getByText('Return to Home').click();
    expect(mockNavigate).toHaveBeenCalledWith('/', { replace: true });
  });
});
