import React from 'react';
import { render, act, waitFor, fireEvent, screen } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import MyProfile from '../../components/MyProfile';
import { useThemePreference } from '../../utils/ThemePreference';
import { useGetAuth } from '../../contexts/AuthProvider';

// Mock dependencies
vi.mock('../../utils/ThemePreference', () => ({
  useThemePreference: vi.fn(),
}));

vi.mock('../../contexts/AuthProvider', () => ({
  useGetAuth: vi.fn(),
}));

describe('MyProfile Component', () => {
  const mockSetTheme = vi.fn();
  const mockLogout = vi.fn();
  const mockAuthUser = {
    firstName: 'John',
    lastName: 'Doe',
    userName: 'johndoe',
    email: 'john.doe@example.com',
  };

  beforeEach(() => {
    (useThemePreference as vi.Mock).mockReturnValue({
      theme: 'g10',
      setTheme: mockSetTheme,
    });
    (useGetAuth as vi.Mock).mockReturnValue({
      logout: mockLogout,
      user: mockAuthUser,
    });

    // Mock localStorage
    vi.spyOn(Storage.prototype, 'setItem');
  });

  it('should render user information correctly', () => {
    render(<MyProfile />);
    expect(screen.getByText('John Doe')).toBeDefined();
    expect(screen.getByText('IDIR: johndoe')).toBeDefined();
    expect(screen.getByText('Email: john.doe@example.com')).toBeDefined();
  });

  it('should change theme when "Change theme" button is clicked', () => {
    render(<MyProfile />);
    const changeThemeButton = screen.getByText('Change theme');
    fireEvent.click(changeThemeButton);
    expect(mockSetTheme).toHaveBeenCalledWith('g100');
    expect(localStorage.setItem).toHaveBeenCalledWith('mode', 'dark');
  });

  it('should call logout function when "Log out" button is clicked', () => {
    render(<MyProfile />);
    const logoutButton = screen.getByText('Log out');
    fireEvent.click(logoutButton);
    expect(mockLogout).toHaveBeenCalled();
  });

  it('should render options section', () => {
    render(<MyProfile />);
    expect(screen.getByText('Options')).toBeDefined();
  });

  it('should toggle theme between light and dark', () => {
    (useThemePreference as vi.Mock).mockReturnValueOnce({
      theme: 'g100',
      setTheme: mockSetTheme,
    });
    render(<MyProfile />);
    const changeThemeButton = screen.getByText('Change theme');
    fireEvent.click(changeThemeButton);
    expect(mockSetTheme).toHaveBeenCalledWith('g10');
    expect(localStorage.setItem).toHaveBeenCalledWith('mode', 'light');
  });
});
