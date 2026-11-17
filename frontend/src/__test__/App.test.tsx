import React, { StrictMode } from 'react';
import { act, render, screen, waitFor } from '@testing-library/react';
import type { User } from 'oidc-client-ts';
import { beforeEach, describe, expect, it, vi } from 'vitest';

const mockUserManager = {
  getUser: vi.fn(),
  signinRedirect: vi.fn(),
  signinRedirectCallback: vi.fn(),
  signoutRedirect: vi.fn(),
  signinSilent: vi.fn(),
  removeUser: vi.fn(),
};

vi.mock('@/services/keycloak', () => ({
  getUserManager: () => mockUserManager,
  ensureFreshUser: vi.fn(),
}));

vi.mock('@tanstack/react-query-devtools', () => ({
  ReactQueryDevtools: () => null,
}));

vi.mock('@/components/Modals/ModalRenderer', () => ({
  default: () => null,
}));

vi.mock('@/contexts/ModalContext', () => ({
  ModalProvider: ({ children }: { children: React.ReactNode }) => <>{children}</>,
}));

vi.mock('@/layouts/SideLayout', () => ({
  default: ({ pageContent }: { pageContent: React.ReactNode }) => (
    <div>{pageContent}</div>
  ),
}));

vi.mock('@/screens/Landing', () => ({
  default: () => <div>Silva Landing Page</div>,
}));

vi.mock('@/screens/Dashboard', async () => {
  const { useAuth } = await import('@/contexts/AuthProvider');
  return {
    default: () => {
      const { logout } = useAuth();
      return (
        <div>
          <span>Silva Dashboard Page</span>
          <button onClick={() => void logout()}>Trigger Logout</button>
        </div>
      );
    },
  };
});

import App from '@/App';
import { ACCESS_TOKEN_KEY, REDIRECT_KEY } from '@/constants';
import { AuthProvider } from '@/contexts/AuthProvider';
import { ensureFreshUser } from '@/services/keycloak';

const toBase64Url = (obj: Record<string, unknown>): string => {
  const bytes = new TextEncoder().encode(JSON.stringify(obj));
  const binary = Array.from(bytes, (b) => String.fromCharCode(b)).join('');
  return btoa(binary).replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/, '');
};

const createSampleOidcUser = (): User => {
  const payload = {
    identity_provider: 'azureidir',
    idir_username: 'JRYAN',
    display_name: 'Ryan, Jack CIA:IN',
    email: 'jack.ryan@gov.bc.ca',
    given_name: 'Jack',
    family_name: 'Ryan',
    client_roles: ['SILVA_VIEWER'],
  };
  const header = toBase64Url({ alg: 'RS256' });
  const body = toBase64Url(payload);
  return {
    access_token: `${header}.${body}.sig`,
    profile: {},
    expires_at: 1999999999,
    expires_in: 300,
    expired: false,
  } as unknown as User;
};

describe('App router auth state transitions', () => {
  beforeEach(() => {
    vi.resetAllMocks();
    sessionStorage.clear();
    localStorage.clear();
    document.cookie = `${ACCESS_TOKEN_KEY}=; path=/; max-age=0`;
    mockUserManager.removeUser.mockResolvedValue(undefined);
  });

  it('renders Dashboard instead of a blank page after /authCallback completes login', async () => {
    const oidcUser = createSampleOidcUser();
    mockUserManager.signinRedirectCallback.mockResolvedValueOnce(oidcUser);
    localStorage.setItem(REDIRECT_KEY, '/dashboard');
    window.history.replaceState({}, '', '/authCallback?code=abc&state=xyz');

    render(
      <StrictMode>
        <AuthProvider>
          <App />
        </AuthProvider>
      </StrictMode>
    );

    await waitFor(() =>
      expect(screen.getByText('Silva Dashboard Page')).toBeInTheDocument()
    );
    expect(window.location.pathname).toBe('/dashboard');
  });

  it('renders Landing page when local logout fallback transitions auth state to logged out', async () => {
    const oidcUser = createSampleOidcUser();
    vi.mocked(ensureFreshUser).mockResolvedValue(oidcUser);
    mockUserManager.signoutRedirect.mockRejectedValueOnce(new Error('Keycloak unavailable'));
    window.history.replaceState({}, '', '/dashboard');

    render(
      <StrictMode>
        <AuthProvider>
          <App />
        </AuthProvider>
      </StrictMode>
    );

    await waitFor(() =>
      expect(screen.getByText('Silva Dashboard Page')).toBeInTheDocument()
    );

    await act(async () => {
      screen.getByText('Trigger Logout').click();
    });

    await waitFor(() =>
      expect(screen.getByText('Silva Landing Page')).toBeInTheDocument()
    );
  });
});
