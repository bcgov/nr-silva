import React from 'react';
import { render } from '@testing-library/react';
import { MemoryRouter, Navigate } from 'react-router-dom';
import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
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
  beforeEach(() => {
    sessionStorage.clear();
  });

  afterEach(() => {
    sessionStorage.clear();
    vi.restoreAllMocks();
  });
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

    expect(getByText('Access Denied')).toBeDefined();
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

    expect(getByText('Oops! Something Went Wrong')).toBeDefined();
  });

  it('should display default error message when error is not a route error response', () => {
    (useRouteError as vi.Mock).mockReturnValue(new Error('Test error'));
    (isRouteErrorResponse as vi.Mock).mockReturnValue(false);

    const { getByText } = render(
      <MemoryRouter>
        <ErrorHandling />
      </MemoryRouter>
    );

    expect(getByText('Oops! Something Went Wrong')).toBeDefined();
  });

  it('should auto-reload once on chunk load failure (stale deployment)', () => {
    const reloadSpy = vi.fn();
    Object.defineProperty(window, 'location', {
      value: { ...window.location, reload: reloadSpy },
      writable: true,
    });

    const chunkError = new TypeError('Failed to fetch dynamically imported module: https://example.com/assets/index-abc123.js');
    (useRouteError as vi.Mock).mockReturnValue(chunkError);
    (isRouteErrorResponse as vi.Mock).mockReturnValue(false);

    const { container } = render(
      <MemoryRouter>
        <ErrorHandling />
      </MemoryRouter>
    );

    expect(reloadSpy).toHaveBeenCalledOnce();
    expect(container.firstChild).toBeNull();
    expect(sessionStorage.getItem('silva_chunk_reload_attempted')).toBe('1');
  });

  it('should show error UI on chunk load failure if reload was already attempted', () => {
    const reloadSpy = vi.fn();
    Object.defineProperty(window, 'location', {
      value: { ...window.location, reload: reloadSpy },
      writable: true,
    });

    sessionStorage.setItem('silva_chunk_reload_attempted', '1');

    const chunkError = new TypeError('Failed to fetch dynamically imported module: https://example.com/assets/index-abc123.js');
    (useRouteError as vi.Mock).mockReturnValue(chunkError);
    (isRouteErrorResponse as vi.Mock).mockReturnValue(false);

    const { getByText } = render(
      <MemoryRouter>
        <ErrorHandling />
      </MemoryRouter>
    );

    expect(reloadSpy).not.toHaveBeenCalled();
    expect(getByText('Oops! Something Went Wrong')).toBeDefined();
  });
});
