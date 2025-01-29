import React from 'react';
import '@testing-library/jest-dom';
import { screen } from '@testing-library/react';
import { render, fireEvent, waitFor } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach, Mock } from 'vitest';
import { MemoryRouter, Navigate, Route, Routes } from 'react-router-dom';
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
    (useGetAuth as Mock).mockReturnValue({ isLoggedIn: false, login: vi.fn() });
    (useLottie as Mock).mockReturnValue({ View: <div>Lottie Animation</div> });

    const { getByTestId, getByText } = render(<Landing />);

    expect(getByTestId('landing-title').textContent).toBe('Welcome to SILVA');
    expect(getByTestId('landing-subtitle').textContent).toBe('Plan, report, and analyze your silviculture activities');
    expect(getByText('Lottie Animation')).toBeDefined();
  });

  it('should call login with "idir" when Login with IDIR button is clicked', () => {
    const mockLogin = vi.fn();
    (useGetAuth as Mock).mockReturnValue({ isLoggedIn: false, login: mockLogin });
    (useLottie as Mock).mockReturnValue({ View: <div>Lottie Animation</div> });

    const { getByTestId } = render(<Landing />);

    fireEvent.click(getByTestId('landing-button__idir'));
    expect(mockLogin).toHaveBeenCalledWith('idir');
  });

  it('should call login with "bceid" when Login with Business BCeID button is clicked', () => {
    const mockLogin = vi.fn();
    (useGetAuth as Mock).mockReturnValue({ isLoggedIn: false, login: mockLogin });
    (useLottie as Mock).mockReturnValue({ View: <div>Lottie Animation</div> });

    const { getByTestId } = render(<Landing />);

    fireEvent.click(getByTestId('landing-button__bceid'));
    expect(mockLogin).toHaveBeenCalledWith('bceid');
  });


  it('should redirect to /opening if user is already logged in', async () => {
    (useGetAuth as Mock).mockReturnValue({ isLoggedIn: true });

    render(
      <MemoryRouter initialEntries={['/']}>
        <Routes>
          <Route path="/" element={<Navigate to="/opening" />} />
          <Route path="/opening" element={<div data-testid="opening-page">Opening Page</div>} />
        </Routes>
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByTestId('opening-page')).toBeInTheDocument();
    });
  });
});
