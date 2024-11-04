import React from 'react';
import { render, waitFor, act } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import { AuthProvider, useGetAuth } from '../../contexts/AuthProvider';
import { fetchAuthSession, signInWithRedirect, signOut } from 'aws-amplify/auth';
import { parseToken } from '../../services/AuthService';
import { extractGroups } from '../../utils/famUtils';
import {env} from '../../env';

vi.mock('aws-amplify/auth', () => ({  
  signInWithRedirect: vi.fn(),
  signOut: vi.fn(),
}));

vi.mock('../../services/AuthService', () => ({
  parseToken: vi.fn(),
}));

vi.mock('../../utils/famUtils', () => ({
  extractGroups: vi.fn(),
}));

describe('AuthProvider', () => {
  const mockUser = { firstName: 'John', lastName: 'Doe' };
  const mockToken = { payload: { sub: '123' } };
  const mockSession = { tokens: { idToken: mockToken } };

  beforeEach(() => {
    vi.clearAllMocks();
    // set a cookie to simulate a logged in user
    const cookieName = `CognitoIdentityServiceProvider.${env.VITE_USER_POOLS_WEB_CLIENT_ID}`;
    document.cookie = `${cookieName}.LastAuthUser=abci21`;    
    document.cookie = `${cookieName}.abci21.idToken=eyJhbGciOiJIUzI1NiJ9.eyJjb2duaXRvOmdyb3VwcyI6WyJncm91cDEiLCJncm91cDIiXSwicHJlZmVycmVkX3VzZXJuYW1lIjoiYjVlY2RiMDk0ZGZiNDE0OWE2YTg0NDVhMDFhOTZiZjBAaWRpciIsImN1c3RvbTppZHBfdXNlcl9pZCI6IkI1RUNEQjA5NERGQjQxNDlBNkE4NDQ1QTAxQTk2QkYwIiwiY3VzdG9tOmlkcF91c2VybmFtZSI6IkpSWUFOIiwiY3VzdG9tOmlkcF9kaXNwbGF5X25hbWUiOiJSeWFuLCBKYWNrIENJQTpJTiIsImVtYWlsIjoiamFjay5yeWFuQGdvdi5iYy5jYSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiY3VzdG9tOmlkcF9uYW1lIjoiaWRpciIsImdpdmVuX25hbWUiOiJKYWNrIiwibmFtZSI6IkphY2sgUnlhbiIsImZhbWlseV9uYW1lIjoiUnlhbiJ9.cLEC8Yh08HErgP2x33pgt2koYJlFNRfi7ja7etcabrM`;

  });

  it('should initialize correctly', async () => {
    (parseToken as vi.Mock).mockReturnValue(mockUser);
    (extractGroups as vi.Mock).mockReturnValue(['group1', 'group2']);

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

    const { getByText } = render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    await waitFor(() => expect(getByText('Loaded')).toBeDefined());
    expect(getByText('Logged In')).toBeDefined();
    expect(getByText('John')).toBeDefined();
  });

  it('should handle login correctly', async () => {
    const provider = 'idir';
    const envProvider = 'DEV-IDIR';

    const TestComponent = () => {
      const { login } = useGetAuth();
      return <button onClick={() => login(provider)}>Login</button>;
    };

    const { getByText } = render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    act(() => {
      getByText('Login').click();
    });

    expect(signInWithRedirect).toHaveBeenCalledWith({
      provider: { custom: envProvider.toUpperCase() },
    });
  });

  it('should handle logout correctly', async () => {
    const TestComponent = () => {
      const { logout } = useGetAuth();
      return <button onClick={logout}>Logout</button>;
    };

    const { getByText } = render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    act(() => {
      getByText('Logout').click();
    });

    await waitFor(() => expect(signOut).toHaveBeenCalled());
  });

  it('should handle userDetails correctly', async () => {
    (parseToken as vi.Mock).mockReturnValue(mockUser);

    const TestComponent = () => {
      const { user } = useGetAuth();

      return <div>{user ? user.firstName : 'No User'}</div>;
    };

    const { getByText } = render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    await waitFor(() => expect(getByText('John')).toBeDefined());
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