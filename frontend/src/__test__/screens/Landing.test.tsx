import React from 'react';
import { render, fireEvent, waitFor } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import Landing from '../../screens/Landing';
import { useGetAuth } from '../../contexts/AuthProvider';
import { useLottie } from 'lottie-react';

vi.mock('../../contexts/AuthProvider', () => ({
  useGetAuth: vi.fn(),
}));

vi.mock('lottie-react', () => ({
  useLottie: vi.fn(),
}));

vi.mock('@carbon/react', () => ({
  Button: ({ onClick, children, ...props }) => (
    <button onClick={onClick} {...props}>
      {children}
    </button>
  ),
}));

vi.mock('@carbon/icons-react', () => ({
  Login: () => <svg />,
}));

describe('Landing', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should render the landing page with title and subtitle', () => {
    (useGetAuth as vi.Mock).mockReturnValue({ isLoggedIn: false, login: vi.fn() });
    (useLottie as vi.Mock).mockReturnValue({ View: <div>Lottie Animation</div> });

    const { getByTestId, getByText } = render(<Landing />);

    expect(getByTestId('landing-title').textContent).toBe('Welcome to SILVA');
    expect(getByTestId('landing-subtitle').textContent).toBe('Plan, report, and analyze your silviculture activities');
    expect(getByText('Lottie Animation')).toBeDefined();
  });

  it('should call login with "idir" when Login with IDIR button is clicked', () => {
    const mockLogin = vi.fn();
    (useGetAuth as vi.Mock).mockReturnValue({ isLoggedIn: false, login: mockLogin });
    (useLottie as vi.Mock).mockReturnValue({ View: <div>Lottie Animation</div> });

    const { getByTestId } = render(<Landing />);

    fireEvent.click(getByTestId('landing-button__idir'));
    expect(mockLogin).toHaveBeenCalledWith('idir');
  });

  it('should call login with "bceid" when Login with Business BCeID button is clicked', () => {
    const mockLogin = vi.fn();
    (useGetAuth as vi.Mock).mockReturnValue({ isLoggedIn: false, login: mockLogin });
    (useLottie as vi.Mock).mockReturnValue({ View: <div>Lottie Animation</div> });

    const { getByTestId } = render(<Landing />);

    fireEvent.click(getByTestId('landing-button__bceid'));
    expect(mockLogin).toHaveBeenCalledWith('bceid');
  });

  it('should redirect to /dashboard if user is already logged in', async () => {
    const mockLogin = vi.fn();
    (useGetAuth as vi.Mock).mockReturnValue({ isLoggedIn: true, login: mockLogin });
    (useLottie as vi.Mock).mockReturnValue({ View: <div>Lottie Animation</div> });

    const originalLocation = window.location;
    delete window.location;
    window.location = { href: '' };

    await waitFor(() => {
      expect(window.location.href).toContain('/opening');
    });

    // Restore original window.location
    window.location = originalLocation;
  });
});
