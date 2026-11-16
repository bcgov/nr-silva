import { render, act, waitFor } from '@testing-library/react';
import { beforeEach, describe, expect, it, vi } from 'vitest';

const userLoadedListeners = new Set<(user: User) => void>();
const userUnloadedListeners = new Set<() => void>();

const mockUserManager = {
  getUser: vi.fn(),
  signinRedirect: vi.fn(),
  signinRedirectCallback: vi.fn(),
  signoutRedirect: vi.fn(),
  signinSilent: vi.fn(),
  removeUser: vi.fn(),
  events: {
    addUserLoaded: vi.fn((cb: (user: User) => void) => {
      userLoadedListeners.add(cb);
    }),
    removeUserLoaded: vi.fn((cb: (user: User) => void) => {
      userLoadedListeners.delete(cb);
    }),
    addUserUnloaded: vi.fn((cb: () => void) => {
      userUnloadedListeners.add(cb);
    }),
    removeUserUnloaded: vi.fn((cb: () => void) => {
      userUnloadedListeners.delete(cb);
    }),
  },
};

vi.mock('@/services/keycloak', () => ({
  getUserManager: () => mockUserManager,
  ensureFreshUser: vi.fn(),
}));

import { ACCESS_TOKEN_KEY, REDIRECT_KEY } from '@/constants';
import { AuthProvider, useAuth } from '@/contexts/AuthProvider';
import { ensureFreshUser } from '@/services/keycloak';
import type { User } from 'oidc-client-ts';

const toBase64Url = (obj: Record<string, unknown>): string => {
  const bytes = new TextEncoder().encode(JSON.stringify(obj));
  const binary = Array.from(bytes, (b) => String.fromCharCode(b)).join('');
  return btoa(binary).replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/, '');
};

const createSampleOidcUser = (overrides: Record<string, unknown> = {}): User => {
  const payload = {
    identity_provider: 'azureidir',
    idir_username: 'JRYAN',
    display_name: 'Ryan, Jack CIA:IN',
    email: 'jack.ryan@gov.bc.ca',
    given_name: 'Jack',
    family_name: 'Ryan',
    client_roles: ['SILVA_VIEWER', 'SILVA_SUBMITTER_FOREST_CLIENT-00001012'],
    ...overrides,
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

describe('AuthProvider', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    userLoadedListeners.clear();
    userUnloadedListeners.clear();
    sessionStorage.clear();
    localStorage.clear();
    document.cookie = `${ACCESS_TOKEN_KEY}=; path=/; max-age=0`;
    window.history.replaceState({}, '', '/');
    mockUserManager.removeUser.mockResolvedValue(undefined);
  });

  it('initializes logged-in user and persists access token cookie', async () => {
    const oidcUser = createSampleOidcUser();
    vi.mocked(ensureFreshUser).mockResolvedValueOnce(oidcUser);

    const TestComponent = () => {
      const { user, isLoggedIn, isLoading } = useAuth();
      return (
        <div>
          <span>{isLoading ? 'Loading' : 'Loaded'}</span>
          <span>{isLoggedIn ? 'Logged In' : 'Logged Out'}</span>
          <span>{user?.firstName}</span>
          <span>{user?.idpProvider}</span>
        </div>
      );
    };

    const { getByText } = render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    await waitFor(() => expect(getByText('Loaded')).toBeInTheDocument());
    expect(getByText('Logged In')).toBeInTheDocument();
    expect(getByText('Jack')).toBeInTheDocument();
    expect(getByText('IDIR')).toBeInTheDocument();
    expect(document.cookie).toContain(`${ACCESS_TOKEN_KEY}=${oidcUser.access_token}`);
  });

  it('renders logged-out state when no session exists', async () => {
    vi.mocked(ensureFreshUser).mockResolvedValueOnce(null);

    const TestComponent = () => {
      const { user, isLoggedIn, isLoading } = useAuth();
      return (
        <div>
          <span>{isLoading ? 'Loading' : 'Loaded'}</span>
          <span>{isLoggedIn ? 'Logged In' : 'Logged Out'}</span>
          <span>Welcome {user?.firstName ?? 'nobody'}</span>
        </div>
      );
    };

    const { getByText } = render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    await waitFor(() => expect(getByText('Loaded')).toBeInTheDocument());
    expect(getByText('Logged Out')).toBeInTheDocument();
    expect(getByText('Welcome nobody')).toBeInTheDocument();
  });

  it('skips ensureFreshUser while mounted on /authCallback', async () => {
    window.history.replaceState({}, '', '/authCallback?code=abc&state=xyz');

    const TestComponent = () => {
      const { isLoading, isLoggedIn } = useAuth();
      return (
        <div>
          <span>{isLoading ? 'Loading' : 'Loaded'}</span>
          <span>{isLoggedIn ? 'Logged In' : 'Logged Out'}</span>
        </div>
      );
    };

    const { getByText } = render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    await waitFor(() => expect(getByText('Loaded')).toBeInTheDocument());
    expect(ensureFreshUser).not.toHaveBeenCalled();
    expect(getByText('Logged Out')).toBeInTheDocument();
  });

  it('triggers signinRedirect with kc_idp_hint=azureidir for IDIR and bceidbusiness for BCeID', async () => {
    vi.mocked(ensureFreshUser).mockResolvedValueOnce(null);
    mockUserManager.signinRedirect.mockResolvedValue(undefined);
    window.history.replaceState({}, '', '/openings?foo=1');

    const TestComponent = () => {
      const { login } = useAuth();
      return (
        <div>
          <button onClick={() => void login('IDIR')}>Login IDIR</button>
          <button onClick={() => void login('BCEIDBUSINESS', '/dashboard')}>Login BCeID</button>
        </div>
      );
    };

    const { getByText } = render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    await waitFor(() => expect(ensureFreshUser).toHaveBeenCalled());

    await act(async () => {
      getByText('Login IDIR').click();
    });

    expect(localStorage.getItem(REDIRECT_KEY)).toBe('/openings?foo=1');
    expect(mockUserManager.signinRedirect).toHaveBeenCalledWith({
      extraQueryParams: { kc_idp_hint: 'azureidir' },
    });

    await act(async () => {
      getByText('Login BCeID').click();
    });

    expect(localStorage.getItem(REDIRECT_KEY)).toBe('/dashboard');
    expect(mockUserManager.signinRedirect).toHaveBeenCalledWith({
      extraQueryParams: { kc_idp_hint: 'bceidbusiness' },
    });
  });

  it('completes OIDC callback login, stores cookie, invokes callback, and sets user', async () => {
    vi.mocked(ensureFreshUser).mockResolvedValueOnce(null);
    const oidcUser = createSampleOidcUser();
    mockUserManager.signinRedirectCallback.mockResolvedValueOnce(oidcUser);
    const onBeforeStateUpdate = vi.fn();

    const TestComponent = () => {
      const { completeLogin, isLoggedIn, user } = useAuth();
      return (
        <div>
          <button onClick={() => void completeLogin(onBeforeStateUpdate)}>Complete</button>
          <span>{isLoggedIn ? `User:${user?.userName}` : 'Anon'}</span>
        </div>
      );
    };

    const { getByText } = render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    await waitFor(() => expect(getByText('Anon')).toBeInTheDocument());

    await act(async () => {
      getByText('Complete').click();
    });

    expect(mockUserManager.signinRedirectCallback).toHaveBeenCalled();
    expect(onBeforeStateUpdate).toHaveBeenCalled();
    expect(getByText('User:JRYAN')).toBeInTheDocument();
    expect(document.cookie).toContain(`${ACCESS_TOKEN_KEY}=${oidcUser.access_token}`);
  });

  it('calls signoutRedirect without clearing user first and clears in catch fallback', async () => {
    const oidcUser = createSampleOidcUser();
    vi.mocked(ensureFreshUser).mockResolvedValueOnce(oidcUser);
    mockUserManager.signoutRedirect.mockResolvedValueOnce(undefined);

    const TestComponent = () => {
      const { logout, isLoggedIn } = useAuth();
      return (
        <div>
          <button onClick={() => void logout()}>Logout</button>
          <span>{isLoggedIn ? 'Logged In' : 'Logged Out'}</span>
        </div>
      );
    };

    const { getByText } = render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    await waitFor(() => expect(getByText('Logged In')).toBeInTheDocument());

    await act(async () => {
      getByText('Logout').click();
    });

    expect(mockUserManager.signoutRedirect).toHaveBeenCalledTimes(1);
    expect(mockUserManager.removeUser).not.toHaveBeenCalled();
    expect(document.cookie).not.toContain(oidcUser.access_token);

    mockUserManager.signoutRedirect.mockRejectedValueOnce(new Error('Network fail'));
    await act(async () => {
      getByText('Logout').click();
    });

    await waitFor(() => expect(mockUserManager.removeUser).toHaveBeenCalled());
    expect(getByText('Logged Out')).toBeInTheDocument();
  });

  it('handles errors during session fetch cleanly', async () => {
    vi.mocked(ensureFreshUser).mockRejectedValueOnce(new Error('Session error'));

    const TestComponent = () => {
      const { user, isLoading } = useAuth();
      return (
        <div>
          <span>{isLoading ? 'Loading' : 'Loaded'}</span>
          <span>{user ? 'Has User' : 'No User'}</span>
        </div>
      );
    };

    const { getByText } = render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    await waitFor(() => expect(getByText('Loaded')).toBeInTheDocument());
    expect(getByText('No User')).toBeInTheDocument();
  });

  it('throws when useAuth is used outside AuthProvider', () => {
    const TestComponent = () => {
      useAuth();
      return <div />;
    };

    expect(() => render(<TestComponent />)).toThrow(
      'useAuth must be used within an AuthProvider'
    );
  });

  describe('Silent Sign-On (idp_hint)', () => {
    it('triggers silent login for idp_hint=idir once and stores clean redirect path', async () => {
      vi.mocked(ensureFreshUser).mockResolvedValueOnce(null);
      mockUserManager.signinRedirect.mockResolvedValueOnce(undefined);
      window.history.replaceState({}, '', '/openings?idp_hint=idir');

      render(
        <AuthProvider>
          <div>Child</div>
        </AuthProvider>
      );

      await waitFor(() =>
        expect(mockUserManager.signinRedirect).toHaveBeenCalledWith({
          extraQueryParams: { kc_idp_hint: 'azureidir' },
        })
      );
      expect(sessionStorage.getItem('silent_login_attempted')).toBe('true');
      expect(localStorage.getItem(REDIRECT_KEY)).toBe('/openings');
    });

    it('triggers silent login for idp_hint=bceid with bceidbusiness hint', async () => {
      vi.mocked(ensureFreshUser).mockResolvedValueOnce(null);
      mockUserManager.signinRedirect.mockResolvedValueOnce(undefined);
      window.history.replaceState({}, '', '/dashboard?idp_hint=bceid');

      render(
        <AuthProvider>
          <div>Child</div>
        </AuthProvider>
      );

      await waitFor(() =>
        expect(mockUserManager.signinRedirect).toHaveBeenCalledWith({
          extraQueryParams: { kc_idp_hint: 'bceidbusiness' },
        })
      );
      expect(localStorage.getItem(REDIRECT_KEY)).toBe('/dashboard');
    });

    it('cleans idp_hint query parameter without looping when already attempted', async () => {
      vi.mocked(ensureFreshUser).mockResolvedValueOnce(null);
      sessionStorage.setItem('silent_login_attempted', 'true');
      window.history.replaceState({}, '', '/openings?idp_hint=idir');

      const TestComponent = () => {
        const { isLoading } = useAuth();
        return <span>{isLoading ? 'Loading' : 'Loaded'}</span>;
      };

      const { getByText } = render(
        <AuthProvider>
          <TestComponent />
        </AuthProvider>
      );

      await waitFor(() => expect(getByText('Loaded')).toBeInTheDocument());
      expect(mockUserManager.signinRedirect).not.toHaveBeenCalled();
      expect(window.location.search).toBe('');
      expect(window.location.pathname).toBe('/openings');
    });
  });

  describe('Session synchronization', () => {
    it('syncs updated roles on userLoaded without clearing query cache, and clears on userUnloaded', async () => {
      const initialUser = createSampleOidcUser({ client_roles: ['SILVA_VIEWER'] });
      vi.mocked(ensureFreshUser).mockResolvedValueOnce(initialUser);
      const onIdentityChange = vi.fn();

      const TestComponent = () => {
        const { user, isLoggedIn } = useAuth();
        return (
          <div>
            <span>{isLoggedIn ? user?.roles?.join(',') : 'SignedOut'}</span>
          </div>
        );
      };

      const { getByText } = render(
        <AuthProvider onIdentityChange={onIdentityChange}>
          <TestComponent />
        </AuthProvider>
      );

      await waitFor(() => expect(getByText('SILVA_VIEWER')).toBeInTheDocument());

      const renewedSameUser = createSampleOidcUser({
        client_roles: ['SILVA_VIEWER', 'SILVA_SUBMITTER_FOREST_CLIENT-00012797'],
      });
      await act(async () => {
        userLoadedListeners.forEach((cb) => cb(renewedSameUser));
      });

      expect(getByText('SILVA_VIEWER,SILVA_SUBMITTER_FOREST_CLIENT-00012797')).toBeInTheDocument();
      expect(onIdentityChange).not.toHaveBeenCalled();

      await act(async () => {
        userUnloadedListeners.forEach((cb) => cb());
      });

      expect(getByText('SignedOut')).toBeInTheDocument();
      expect(onIdentityChange).toHaveBeenCalledTimes(1);
    });

    it('syncs cross-tab localStorage changes and notifies onIdentityChange when account switches or signs out', async () => {
      const firstAccount = createSampleOidcUser({ idir_username: 'ALICE' });
      vi.mocked(ensureFreshUser).mockResolvedValueOnce(firstAccount);
      const onIdentityChange = vi.fn();

      const TestComponent = () => {
        const { user, isLoggedIn } = useAuth();
        return (
          <span>{isLoggedIn ? `User:${user?.userName}` : 'SignedOut'}</span>
        );
      };

      const { getByText } = render(
        <AuthProvider onIdentityChange={onIdentityChange}>
          <TestComponent />
        </AuthProvider>
      );

      await waitFor(() => expect(getByText('User:ALICE')).toBeInTheDocument());

      const secondAccount = createSampleOidcUser({ idir_username: 'BOB' });
      mockUserManager.getUser.mockResolvedValueOnce(secondAccount);
      await act(async () => {
        window.dispatchEvent(
          new StorageEvent('storage', {
            key: 'oidc.user:https://dev.loginproxy.gov.bc.ca/auth/realms/standard:silva-6737',
            storageArea: window.localStorage,
          })
        );
      });

      await waitFor(() => expect(getByText('User:BOB')).toBeInTheDocument());
      expect(document.cookie).toContain(`${ACCESS_TOKEN_KEY}=${secondAccount.access_token}`);
      expect(onIdentityChange).toHaveBeenCalledTimes(1);

      mockUserManager.getUser.mockResolvedValueOnce(null);
      await act(async () => {
        window.dispatchEvent(
          new StorageEvent('storage', {
            key: 'oidc.user:https://dev.loginproxy.gov.bc.ca/auth/realms/standard:silva-6737',
            storageArea: window.localStorage,
          })
        );
      });

      await waitFor(() => expect(getByText('SignedOut')).toBeInTheDocument());
      expect(onIdentityChange).toHaveBeenCalledTimes(2);
    });
  });
});
