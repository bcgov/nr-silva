import React from 'react';
import '@testing-library/jest-dom';
import { render, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach, Mock } from 'vitest';
import Landing from '../../screens/Landing';
import { useAuth } from '../../contexts/AuthProvider';
import { useLottie } from 'lottie-react';

vi.mock('../../contexts/AuthProvider', () => ({
  useAuth: vi.fn(),
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
    (useAuth as Mock).mockReturnValue({ isLoggedIn: false, login: vi.fn() });
    (useLottie as Mock).mockReturnValue({ View: <div>Lottie Animation</div> });

    const { getByTestId, getByText } = render(<Landing />);

    expect(getByTestId('landing-title').textContent).toBe('Welcome to SILVA');
    expect(getByTestId('landing-subtitle').textContent).toBe('Plan, report, and analyze your silviculture activities');
    expect(getByText('Lottie Animation')).toBeDefined();
  });

  it('should call login with "idir" when Login with IDIR button is clicked', () => {
    const mockLogin = vi.fn();
    (useAuth as Mock).mockReturnValue({ isLoggedIn: false, login: mockLogin });
    (useLottie as Mock).mockReturnValue({ View: <div>Lottie Animation</div> });

    const { getByTestId } = render(<Landing />);

    fireEvent.click(getByTestId('landing-button__idir'));
    expect(mockLogin).toHaveBeenCalledWith('idir');
  });

  it('should call login with "bceid" when Login with Business BCeID button is clicked', () => {
    const mockLogin = vi.fn();
    (useAuth as Mock).mockReturnValue({ isLoggedIn: false, login: mockLogin });
    (useLottie as Mock).mockReturnValue({ View: <div>Lottie Animation</div> });

    const { getByTestId } = render(<Landing />);

    fireEvent.click(getByTestId('landing-button__bceid'));
    expect(mockLogin).toHaveBeenCalledWith('bceid');
  });
});
