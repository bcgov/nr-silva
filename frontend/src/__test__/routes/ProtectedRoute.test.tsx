import React from 'react';
import { render } from '@testing-library/react';
import { MemoryRouter, Routes, Route } from 'react-router-dom';
import { describe, it, expect, vi, Mock } from 'vitest';
import ProtectedRoute from '../../routes/ProtectedRoute';
import { useAuth } from '../../contexts/AuthProvider';

vi.mock('../../contexts/AuthProvider', () => ({
  useAuth: vi.fn(),
}));

vi.mock('@carbon/react', () => ({
  Loading: vi.fn(() => <div>Loading...</div>),
}));

describe('ProtectedRoute', () => {
  it('should render child routes when all checks pass', () => {
    (useAuth as Mock).mockReturnValue({ isLoggedIn: true });

    const { container } = render(
      <MemoryRouter initialEntries={['/protected']}>
        <Routes>
          <Route path="/protected" element={<ProtectedRoute />}>
            <Route path="" element={<div>Protected Content</div>} />
          </Route>
        </Routes>
      </MemoryRouter>
    );

    expect(container.innerHTML).toContain('Protected Content');
  });

  it('should log out and redirect to login when user is not logged in', () => {
    const mockLogout = vi.fn();
    (useAuth as Mock).mockReturnValue({ isLoggedIn: false, logout: mockLogout });

    const { container } = render(
      <MemoryRouter initialEntries={['/protected']}>
        <Routes>
          <Route path="/" element={<div>Login Page</div>} />
          <Route path="/protected" element={<ProtectedRoute />} />
        </Routes>
      </MemoryRouter>
    );

    expect(mockLogout).toHaveBeenCalled();
    expect(container.innerHTML).toContain('Login Page');
  });
});
