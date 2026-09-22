import React from 'react';
import { render, act, fireEvent } from '@testing-library/react';
import { MemoryRouter, Navigate } from 'react-router-dom';
import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import ErrorHandling from '../../screens/ErrorHandling';
import { useRouteError, isRouteErrorResponse } from 'react-router-dom';

const mockNavigate = vi.fn();
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    useRouteError: vi.fn(),
    isRouteErrorResponse: vi.fn(),
    useNavigate: () => mockNavigate,
    Navigate: vi.fn(({ to, replace }) => <div>Navigate to {to} - {replace}</div>),
  };
});

describe('ErrorHandling', () => {
  let reloadSpy: ReturnType<typeof vi.fn>;

  beforeEach(() => {
    sessionStorage.clear();
    reloadSpy = vi.fn();
    mockNavigate.mockClear();
    vi.stubGlobal('location', { ...window.location, reload: reloadSpy });
  });

  afterEach(() => {
    vi.unstubAllGlobals();
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

  it('should auto-reload once on chunk load failure (stale deployment)', async () => {
    const chunkError = new TypeError('Failed to fetch dynamically imported module: https://example.com/assets/index-abc123.js');
    (useRouteError as vi.Mock).mockReturnValue(chunkError);
    (isRouteErrorResponse as vi.Mock).mockReturnValue(false);

    let container!: HTMLElement;
    await act(async () => {
      ({ container } = render(
        <MemoryRouter>
          <ErrorHandling />
        </MemoryRouter>
      ));
    });

    expect(reloadSpy).toHaveBeenCalledOnce();
    expect(container.firstChild).toBeNull();
    expect(sessionStorage.getItem('silva_chunk_reload_attempted')).toBe('1');
  });

  it('should show error UI on chunk load failure if reload was already attempted', () => {
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

  it('navigates to / when "Go to dashboard" button is clicked', () => {
    (useRouteError as vi.Mock).mockReturnValue({ status: 404 });
    (isRouteErrorResponse as vi.Mock).mockReturnValue(true);

    const { getByRole } = render(
      <MemoryRouter>
        <ErrorHandling />
      </MemoryRouter>
    );

    const btn = getByRole('button', { name: /Go to dashboard/i });
    fireEvent.click(btn);
    expect(mockNavigate).toHaveBeenCalledWith('/');
  });

  it('handles chunk error when message includes "Importing a module script failed"', async () => {
    const chunkError = new Error('Importing a module script failed');
    (useRouteError as vi.Mock).mockReturnValue(chunkError);
    (isRouteErrorResponse as vi.Mock).mockReturnValue(false);

    await act(async () => {
      render(
        <MemoryRouter>
          <ErrorHandling />
        </MemoryRouter>
      );
    });

    expect(reloadSpy).toHaveBeenCalledOnce();
    expect(sessionStorage.getItem('silva_chunk_reload_attempted')).toBe('1');
  });

  it('handles chunk error when err.name is "ChunkLoadError"', async () => {
    const chunkError = new Error('Loading chunk 5 failed');
    chunkError.name = 'ChunkLoadError';
    (useRouteError as vi.Mock).mockReturnValue(chunkError);
    (isRouteErrorResponse as vi.Mock).mockReturnValue(false);

    await act(async () => {
      render(
        <MemoryRouter>
          <ErrorHandling />
        </MemoryRouter>
      );
    });

    expect(reloadSpy).toHaveBeenCalledOnce();
  });

  it('handles non-Error objects gracefully without attempting reload', () => {
    (useRouteError as vi.Mock).mockReturnValue('A raw string error');
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
