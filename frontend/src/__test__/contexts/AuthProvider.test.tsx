import React from 'react';
import { render, act, waitFor } from '@testing-library/react';
import { describe, expect, it, vi, beforeEach } from 'vitest';
import { AuthProvider, useGetAuth } from '../../contexts/AuthProvider';
import { signInWithRedirect, signOut } from 'aws-amplify/auth';
import { parseToken } from '../../services/AuthService';
import { extractGroups } from '../../utils/famUtils';
import { env } from '../../env';

vi.mock('aws-amplify/auth', () => ({
  signInWithRedirect: vi.fn(),
  signOut: vi.fn(),
}));

function setAuthCookies(value: string | null) {
  const cookieName = `CognitoIdentityServiceProvider.${env.VITE_USER_POOLS_WEB_CLIENT_ID}`;
  document.cookie = `${cookieName}.LastAuthUser=; path=/; max-age=0`;
  document.cookie = `${cookieName}.abci21.idToken=; path=/; max-age=0`;

  if (value) {
    document.cookie = `${cookieName}.LastAuthUser=abci21; path=/`;
    document.cookie = `${cookieName}.abci21.idToken=${value}; path=/`;
  }
}

const sampleAuthToken = 'eyJhbGciOiJIUzI1NiJ9.eyJjb2duaXRvOmdyb3VwcyI6WyJncm91cDEiLCJncm91cDIiXSwicHJlZmVycmVkX3VzZXJuYW1lIjoiYjVlY2RiMDk0ZGZiNDE0OWE2YTg0NDVhMDFhOTZiZjBAaWRpciIsImN1c3RvbTppZHBfdXNlcl9pZCI6IkI1RUNEQjA5NERGQjQxNDlBNkE4NDQ1QTAxQTk2QkYwIiwiY3VzdG9tOmlkcF91c2VybmFtZSI6IkpSWUFOIiwiY3VzdG9tOmlkcF9kaXNwbGF5X25hbWUiOiJSeWFuLCBKYWNrIENJQTpJTiIsImVtYWlsIjoiamFjay5yeWFuQGdvdi5iYy5jYSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiY3VzdG9tOmlkcF9uYW1lIjoiaWRpciIsImdpdmVuX25hbWUiOiJKYWNrIiwibmFtZSI6IkphY2sgUnlhbiIsImZhbWlseV9uYW1lIjoiUnlhbiJ9.cLEC8Yh08HErgP2x33pgt2koYJlFNRfi7ja7etcabrM';

describe('AuthProvider', () => {
  const mockUser = { firstName: 'John', lastName: 'Doe' };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should initialize correctly', async () => {
    setAuthCookies(sampleAuthToken);

    const TestComponent = () => {
      const { user, isLoggedIn, isLoading } = useGetAuth();
      return (
        <div>
          <span>{isLoading ? 'Loading' : 'Loaded'}</span>
          <span>{isLoggedIn ? 'Logged In' : 'Logged Out'}</span>
          <span>{user?.firstName}</span>
        </div>
      );
    };

    let getByText;
    await act(async () => {
      ({ getByText } = render(
        <AuthProvider>
          <TestComponent />
        </AuthProvider>
      ));
    });

    await waitFor(() => expect(getByText('Loaded')).toBeDefined());
    expect(getByText('Logged In')).toBeDefined();
    expect(getByText('Jack')).toBeDefined();
  });

  it('should find no token', async () => {
    setAuthCookies(null);

    const TestComponent = () => {
      const { user, isLoggedIn, isLoading } = useGetAuth();
      return (
        <div>
          <span>{isLoading ? 'Loading' : 'Loaded'}</span>
          <span>{isLoggedIn ? 'Logged In' : 'Logged Out'}</span>
          <span>Welcome {user?.firstName || 'nobody'}</span>
        </div>
      );
    };

    let getByText;
    await act(async () => {
      ({ getByText } = render(
        <AuthProvider>
          <TestComponent />
        </AuthProvider>
      ));
    });

    await waitFor(() => expect(getByText('Loaded')).toBeDefined());
    expect(getByText('Logged Out')).toBeDefined();
    expect(getByText('Welcome nobody')).toBeDefined();
  });

  it('should handle login correctly', async () => {
    setAuthCookies(sampleAuthToken);
    const provider = 'idir';
    const envProvider = `${env.VITE_ZONE ?? 'DEV'}-IDIR`;

    const TestComponent = () => {
      const { login } = useGetAuth();
      return <button onClick={() => login(provider)}>Login</button>;
    };

    let getByText;
    await act(async () => {
      ({ getByText } = render(
        <AuthProvider>
          <TestComponent />
        </AuthProvider>
      ));
    });

    act(() => {
      getByText('Login').click();
    });

    expect(signInWithRedirect).toHaveBeenCalledWith({
      provider: { custom: envProvider.toUpperCase() },
    });
  });

  it('should handle logout correctly', async () => {
    setAuthCookies(sampleAuthToken);

    const TestComponent = () => {
      const { logout } = useGetAuth();
      return <button onClick={logout}>Logout</button>;
    };

    let getByText;
    await act(async () => {
      ({ getByText } = render(
        <AuthProvider>
          <TestComponent />
        </AuthProvider>
      ));
    });

    act(() => {
      getByText('Logout').click();
    });

    await waitFor(() => expect(signOut).toHaveBeenCalled());
  });

  it('should handle userDetails correctly', async () => {
    setAuthCookies(sampleAuthToken);

    const TestComponent = () => {
      const { user } = useGetAuth();
      return <div>{user ? user.firstName : 'No User'}</div>;
    };

    let getByText;
    await act(async () => {
      ({ getByText } = render(
        <AuthProvider>
          <TestComponent />
        </AuthProvider>
      ));
    });

    await waitFor(() => expect(getByText('Jack')).toBeDefined());
  });

  it('should throw error if useGetAuth is used outside AuthProvider', () => {
    const TestComponent = () => {
      useGetAuth();
      return <div>Test</div>;
    };

    expect(() => render(<TestComponent />)).toThrow(
      'useGetAuth must be used within an AuthProvider'
    );
  });
});