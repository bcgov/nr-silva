import React from 'react';
import { render } from '@testing-library/react';
import { MemoryRouter, Navigate } from 'react-router-dom';
import { describe, it, expect, vi } from 'vitest';
import ErrorHandling from '../../screens/ErrorHandling';
import { useRouteError, isRouteErrorResponse } from 'react-router-dom';

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    useRouteError: vi.fn(),
    isRouteErrorResponse: vi.fn(),
    Navigate: vi.fn(({ to, replace }) => <div>Navigate to {to} - {replace}</div>),
  };
});

describe('ErrorHandling', () => {
  it('should navigate to / when error status is 401', () => {
    (useRouteError as vi.Mock).mockReturnValue({ status: 401 });
    (isRouteErrorResponse as vi.Mock).mockReturnValue(true);

    const { getByText } = render(
      <MemoryRouter>
        <ErrorHandling />
      </MemoryRouter>
    );

    expect(getByText('Navigate to / -')).toBeDefined();
  });

  it('should display Unauthorized when error status is 403', () => {
    (useRouteError as vi.Mock).mockReturnValue({ status: 403 });
    (isRouteErrorResponse as vi.Mock).mockReturnValue(true);

    const { getByText } = render(
      <MemoryRouter>
        <ErrorHandling />
      </MemoryRouter>
    );

    expect(getByText('Unauthorized')).toBeDefined();
  });

  it('should display Page Not Found when error status is 404', () => {
    (useRouteError as vi.Mock).mockReturnValue({ status: 404 });
    (isRouteErrorResponse as vi.Mock).mockReturnValue(true);

    const { getByText } = render(
      <MemoryRouter>
        <ErrorHandling />
      </MemoryRouter>
    );

    expect(getByText('Page Not Found')).toBeDefined();
  });

  it('should display default error message for other types of errors', () => {
    (useRouteError as vi.Mock).mockReturnValue({ status: 500 });
    (isRouteErrorResponse as vi.Mock).mockReturnValue(true);

    const { getByText } = render(
      <MemoryRouter>
        <ErrorHandling />
      </MemoryRouter>
    );

    expect(getByText('Oops! Something went wrong')).toBeDefined();
  });

  it('should display default error message when error is not a route error response', () => {
    (useRouteError as vi.Mock).mockReturnValue(new Error('Test error'));
    (isRouteErrorResponse as vi.Mock).mockReturnValue(false);

    const { getByText } = render(
      <MemoryRouter>
        <ErrorHandling />
      </MemoryRouter>
    );

    expect(getByText('Oops! Something went wrong')).toBeDefined();
  });
});