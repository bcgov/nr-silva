import React from 'react';
import { render, waitFor } from '@testing-library/react';
import { MemoryRouter, Navigate } from 'react-router-dom';
import { describe, it, expect, vi } from 'vitest';
import DashboardRedirect from '../../screens/DashboardRedirect';
import { useGetAuth } from '../../contexts/AuthProvider';

vi.mock('../../contexts/AuthProvider', () => ({
  useGetAuth: vi.fn(),
}));

vi.mock('@carbon/react', () => ({
  Loading: vi.fn(() => <div>Loading...</div>),
}));

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    Navigate: vi.fn(({ to, replace }) => <div>Navigate to {to} - {replace}</div>),
  };
});

describe('DashboardRedirect', () => {
  it('should render Loading component when user is not defined', () => {
    (useGetAuth as vi.Mock).mockReturnValue({ isLoggedIn: false });

    const { getByText } = render(
      <MemoryRouter>
        <DashboardRedirect />
      </MemoryRouter>
    );

    expect(getByText('Loading...')).toBeDefined();
    expect(Navigate).not.toHaveBeenCalled();
  });

  it('should navigate to /opening when user is defined', async () => {
    (useGetAuth as vi.Mock).mockReturnValue({ isLoggedIn: true });

    const { getByText } = render(
      <MemoryRouter>
        <DashboardRedirect />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(getByText('Navigate to /opening -')).toBeDefined();
    });
  });
});