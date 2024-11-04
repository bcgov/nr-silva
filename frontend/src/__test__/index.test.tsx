import { describe, it, expect, vi } from 'vitest';
import React from 'react';
import { render } from '@testing-library/react';
import { createRoot } from 'react-dom/client';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { Provider } from 'react-redux';
import { Amplify } from 'aws-amplify';
import App from '../App';
import store from '../store';
import { ThemePreference } from '../utils/ThemePreference';
import PaginationProvider from '../contexts/PaginationProvider';
import { OpeningsSearchProvider } from '../contexts/search/OpeningsSearch';
import { AuthProvider } from '../contexts/AuthProvider';
import amplifyconfig from '../amplifyconfiguration';
import { CookieStorage } from 'aws-amplify/utils';
import { cognitoUserPoolsTokenProvider } from 'aws-amplify/auth/cognito';
import { useLottie } from 'lottie-react';

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
vi.mock('lottie-react', () => ({
  useLottie: vi.fn(),
}));

describe('index.tsx', () => {
  it('should initialize the app correctly', async () => {
    (useLottie as vi.Mock).mockReturnValue({ View: <div>Lottie Animation</div> });
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
