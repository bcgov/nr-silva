import React from 'react';
import { createRoot } from 'react-dom/client';
import { describe, expect, it, vi } from 'vitest';

const renderSpy = vi.fn();

vi.mock('../env', () => ({
  env: {
    VITE_ZONE: 'DEV',
  },
}));
vi.mock('@tanstack/react-query', () => ({
  QueryClient: vi.fn(function () {
    return {
      defaultOptions: {
        queries: {},
      },
    };
  }),
  QueryClientProvider: ({ children }: { children: React.ReactNode }) => <div>{children}</div>,
  QueryCache: class QueryCache {
    constructor() {
      /* noop */
    }
  },
  MutationCache: class MutationCache {
    constructor() {
      /* noop */
    }
  },
}));
vi.mock('react-dom/client', () => ({
  createRoot: vi.fn(() => ({
    render: renderSpy,
  })),
}));
vi.mock('../contexts/ToastProvider', () => ({
  default: ({ children }: { children: React.ReactNode }) => <>{children}</>,
}));

describe('index.tsx', () => {
  it('should initialize the app root cleanly without Amplify', async () => {
    const container = document.createElement('div');
    container.id = 'root';
    document.body.appendChild(container);

    await import('../index');

    expect(createRoot).toHaveBeenCalledWith(container);
    expect(renderSpy).toHaveBeenCalledTimes(1);
  }, 15_000);
});
