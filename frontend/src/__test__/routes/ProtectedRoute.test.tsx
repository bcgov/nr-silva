import React from 'react';
import { render } from '@testing-library/react';
import { MemoryRouter, Routes, Route } from 'react-router-dom';
import { describe, it, expect, vi } from 'vitest';
import ProtectedRoute from '../../routes/ProtectedRoute';
import { useGetAuth } from '../../contexts/AuthProvider';

vi.mock('../../contexts/AuthProvider', () => ({
  useGetAuth: vi.fn(),
}));

vi.mock('@carbon/react', () => ({
  Loading: vi.fn(() => <div>Loading...</div>),
}));

describe('ProtectedRoute', () => {
  it('should redirect to unauthorized when requiredRoles are not met', () => {
    (useGetAuth as vi.Mock).mockReturnValue({ isLoggedIn: true, userRoles: ['user'] });

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
    (useGetAuth as vi.Mock).mockReturnValue({ isLoggedIn: true, userRoles: ['admin'] });

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
