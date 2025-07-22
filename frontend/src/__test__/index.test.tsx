import { describe, it, expect, vi } from 'vitest';
import React from 'react';
import { createRoot } from 'react-dom/client';
import { Amplify } from 'aws-amplify';
import amplifyconfig from '../amplifyconfiguration';
import { CookieStorage } from 'aws-amplify/utils';
import { cognitoUserPoolsTokenProvider } from 'aws-amplify/auth/cognito';

vi.mock('../env', () => ({
  env: {
    VITE_ZONE: 'DEV',
  },
}));
vi.mock('aws-amplify');
vi.mock('aws-amplify/utils');
vi.mock('aws-amplify/auth/cognito');
vi.mock('@tanstack/react-query', () => ({
  QueryClient: vi.fn(() => ({
    defaultOptions: {
      queries: {},
    },
  })),
  QueryClientProvider: ({ children }) => <div>{children}</div>,
}));
vi.mock('react-dom/client', () => ({
  createRoot: vi.fn(() => ({
    render: vi.fn(),
  })),
}));

describe('index.tsx', () => {
  it('should initialize the app correctly', async () => {
    const container = document.createElement('div');
    container.id = 'root';
    document.body.appendChild(container);

    // Import the index file to execute its code
    await import('../index');

    expect(createRoot).toHaveBeenCalledWith(container);
    expect(Amplify.configure).toHaveBeenCalledWith(amplifyconfig);
    expect(cognitoUserPoolsTokenProvider.setKeyValueStorage).toHaveBeenCalledWith(expect.any(CookieStorage));
  });
});
