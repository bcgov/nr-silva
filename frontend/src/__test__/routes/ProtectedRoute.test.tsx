import React from 'react';
import { render } from '@testing-library/react';
import { MemoryRouter, Routes, Route } from 'react-router-dom';
import { describe, it, expect, vi } from 'vitest';
import ProtectedRoute from '../../routes/ProtectedRoute';
import { useGetAuth } from '../../contexts/AuthProvider';
import { Loading } from "@carbon/react";

vi.mock('../../contexts/AuthProvider', () => ({
  useGetAuth: vi.fn(),
}));

vi.mock('@carbon/react', () => ({
  Loading: vi.fn(() => <div>Loading...</div>),
}));

describe('ProtectedRoute', () => {
  it('should render loading component when isLoading is true', () => {
    (useGetAuth as vi.Mock).mockReturnValue({ isLoading: true });

    const { getByText } = render(
      <MemoryRouter>
        <ProtectedRoute />
      </MemoryRouter>
    );

    expect(getByText('Loading...')).toBeDefined();
  });

  it('should redirect to login when requireAuth is true and user is not logged in', () => {
    (useGetAuth as vi.Mock).mockReturnValue({ isLoading: false, isLoggedIn: false });

    const { container } = render(
      <MemoryRouter initialEntries={['/protected']}>
        <Routes>
          <Route path="/login" element={<div>Login Page</div>} />
          <Route path="/protected" element={<ProtectedRoute requireAuth redirectTo="/login" />} />
        </Routes>
      </MemoryRouter>
    );

    expect(container.innerHTML).toContain('Login Page');
  });

  it('should redirect to unauthorized when requiredRoles are not met', () => {
    (useGetAuth as vi.Mock).mockReturnValue({ isLoading: false, isLoggedIn: true, userRoles: ['user'] });

    const { container } = render(
      <MemoryRouter initialEntries={['/protected']}>
        <Routes>
          <Route path="/unauthorized" element={<div>Unauthorized Page</div>} />
          <Route path="/protected" element={<ProtectedRoute requiredRoles={['admin']} />} />
        </Routes>
      </MemoryRouter>
    );

    expect(container.innerHTML).toContain('Unauthorized Page');
  });

  it('should render child routes when all checks pass', () => {
    (useGetAuth as vi.Mock).mockReturnValue({ isLoading: false, isLoggedIn: true, userRoles: ['admin'] });

    const { container } = render(
      <MemoryRouter initialEntries={['/protected']}>
        <Routes>
          <Route path="/protected" element={<ProtectedRoute requiredRoles={['admin']} />}>
            <Route path="" element={<div>Protected Content</div>} />
          </Route>
        </Routes>
      </MemoryRouter>
    );

    expect(container.innerHTML).toContain('Protected Content');
  });
});