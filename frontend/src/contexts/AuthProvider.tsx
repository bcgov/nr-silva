import React, {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useRef,
  useState,
  type ReactNode,
} from 'react';
import type { User } from 'oidc-client-ts';

import { ACCESS_TOKEN_KEY, REDIRECT_KEY } from '@/constants';
import { AUTH_CALLBACK_PATH } from '@/routes/paths';
import { parseToken, setAuthIdToken } from '@/services/AuthService';
import { ensureFreshUser, getUserManager } from '@/services/keycloak';
import { FamLoginUser, IdpProviderType } from '@/types/AuthTypes';
import { deleteCookie, setCookie } from '@/utils/CookieUtils';

const SILENT_LOGIN_ATTEMPTED_KEY = 'silent_login_attempted';
const OIDC_USER_STORAGE_PREFIX = 'oidc.user:';

interface AuthContextType {
  user: FamLoginUser | undefined;
  isLoggedIn: boolean;
  isLoading: boolean;
  login: (provider: IdpProviderType, redirectPath?: string) => Promise<void>;
  completeLogin: (onBeforeStateUpdate?: () => void) => Promise<FamLoginUser | undefined>;
  logout: () => Promise<void>;
}

interface AuthProviderProps {
  children: ReactNode;
  onIdentityChange?: () => void;
}

export const AuthContext = createContext<AuthContextType | undefined>(undefined);

const resolveKcIdpHint = (provider: IdpProviderType): string => {
  return provider === 'IDIR' ? 'azureidir' : 'bceidbusiness';
};

export const AuthProvider: React.FC<AuthProviderProps> = ({ children, onIdentityChange }) => {
  const [user, setUser] = useState<FamLoginUser | undefined>(undefined);
  const [isLoading, setIsLoading] = useState(true);
  const currentUserRef = useRef<FamLoginUser | undefined>(undefined);
  const isCompletingLoginRef = useRef(false);
  const onIdentityChangeRef = useRef(onIdentityChange);
  onIdentityChangeRef.current = onIdentityChange;

  const commitUserState = useCallback((nextUser: FamLoginUser | undefined) => {
    const prevUser = currentUserRef.current;
    const identityChanged =
      prevUser !== undefined &&
      (nextUser === undefined || prevUser.providerUsername !== nextUser.providerUsername);

    currentUserRef.current = nextUser;
    setUser(nextUser);

    if (identityChanged) {
      onIdentityChangeRef.current?.();
    }
  }, []);

  const syncFromOidcUser = useCallback(
    (oidcUser: User | null | undefined): FamLoginUser | undefined => {
      if (oidcUser?.access_token && !oidcUser.expired) {
        setCookie(ACCESS_TOKEN_KEY, oidcUser.access_token);
        const parsedUser = parseToken(oidcUser);
        commitUserState(parsedUser);
        return parsedUser;
      }

      deleteCookie(ACCESS_TOKEN_KEY);
      setAuthIdToken(null);
      commitUserState(undefined);
      return undefined;
    },
    [commitUserState]
  );

  const login = useCallback(async (provider: IdpProviderType, redirectPath?: string) => {
    const targetPath = redirectPath ?? `${window.location.pathname}${window.location.search}`;
    if (targetPath && targetPath !== '/' && !targetPath.startsWith(AUTH_CALLBACK_PATH)) {
      localStorage.setItem(REDIRECT_KEY, targetPath);
    }

    await getUserManager().signinRedirect({
      extraQueryParams: {
        kc_idp_hint: resolveKcIdpHint(provider),
      },
    });
  }, []);

  const completeLogin = useCallback(
    async (onBeforeStateUpdate?: () => void | Promise<void>): Promise<FamLoginUser | undefined> => {
      isCompletingLoginRef.current = true;
      try {
        const oidcUser = await getUserManager().signinRedirectCallback();
        if (oidcUser?.access_token) {
          setCookie(ACCESS_TOKEN_KEY, oidcUser.access_token);
        }

        const parsedUser = parseToken(oidcUser);
        sessionStorage.removeItem(SILENT_LOGIN_ATTEMPTED_KEY);
        await onBeforeStateUpdate?.();
        commitUserState(parsedUser);
        return parsedUser;
      } finally {
        isCompletingLoginRef.current = false;
      }
    },
    [commitUserState]
  );

  const logout = useCallback(async () => {
    sessionStorage.removeItem(SILENT_LOGIN_ATTEMPTED_KEY);
    deleteCookie(ACCESS_TOKEN_KEY);
    setAuthIdToken(null);
    try {
      await getUserManager().signoutRedirect();
    } catch (err) {
      console.error('Keycloak signout redirect failed:', err);
      await getUserManager()
        .removeUser()
        .catch(() => undefined);
      commitUserState(undefined);
    }
  }, [commitUserState]);

  useEffect(() => {
    const manager = getUserManager();
    const events = manager.events;

    const handleUserLoaded = (loadedUser: User) => {
      if (isCompletingLoginRef.current) {
        return;
      }
      syncFromOidcUser(loadedUser);
    };

    const handleUserUnloaded = () => {
      if (isCompletingLoginRef.current) {
        return;
      }
      syncFromOidcUser(null);
    };

    const handleStorageChange = (event: StorageEvent) => {
      if (
        event.storageArea === window.localStorage &&
        (event.key === null || event.key.startsWith(OIDC_USER_STORAGE_PREFIX))
      ) {
        void manager
          .getUser()
          .then((storedUser) => {
            syncFromOidcUser(storedUser);
          })
          .catch(() => {
            syncFromOidcUser(null);
          });
      }
    };

    events?.addUserLoaded?.(handleUserLoaded);
    events?.addUserUnloaded?.(handleUserUnloaded);
    window.addEventListener('storage', handleStorageChange);

    return () => {
      events?.removeUserLoaded?.(handleUserLoaded);
      events?.removeUserUnloaded?.(handleUserUnloaded);
      window.removeEventListener('storage', handleStorageChange);
    };
  }, [syncFromOidcUser]);

  useEffect(() => {
    let mounted = true;
    let redirected = false;

    void (async () => {
      try {
        if (window.location.pathname === AUTH_CALLBACK_PATH) {
          return;
        }

        const storedUser = await ensureFreshUser();
        if (!mounted) {
          return;
        }

        if (storedUser?.access_token) {
          syncFromOidcUser(storedUser);
          sessionStorage.removeItem(SILENT_LOGIN_ATTEMPTED_KEY);

          const searchParams = new URLSearchParams(window.location.search);
          if (searchParams.has('idp_hint')) {
            searchParams.delete('idp_hint');
            const searchStr = searchParams.toString();
            const cleanPath = window.location.pathname + (searchStr ? `?${searchStr}` : '');
            window.history.replaceState({}, '', cleanPath);
          }
          return;
        }

        syncFromOidcUser(null);

        const searchParams = new URLSearchParams(window.location.search);
        const idpHint = searchParams.get('idp_hint')?.toLowerCase();
        if (idpHint && (idpHint === 'idir' || idpHint === 'bceid' || idpHint === 'bceidbusiness')) {
          const alreadyAttempted = sessionStorage.getItem(SILENT_LOGIN_ATTEMPTED_KEY) === 'true';
          if (!alreadyAttempted) {
            sessionStorage.setItem(SILENT_LOGIN_ATTEMPTED_KEY, 'true');
            const provider: IdpProviderType = idpHint === 'idir' ? 'IDIR' : 'BCEIDBUSINESS';
            const cleanSearchParams = new URLSearchParams(window.location.search);
            cleanSearchParams.delete('idp_hint');
            const searchStr = cleanSearchParams.toString();
            const cleanPath = window.location.pathname + (searchStr ? `?${searchStr}` : '');

            redirected = true;
            await login(provider, cleanPath);
          } else {
            const cleanSearchParams = new URLSearchParams(window.location.search);
            cleanSearchParams.delete('idp_hint');
            const searchStr = cleanSearchParams.toString();
            const cleanPath = window.location.pathname + (searchStr ? `?${searchStr}` : '');
            window.history.replaceState({}, '', cleanPath);
          }
        }
      } catch {
        syncFromOidcUser(null);
      } finally {
        if (mounted && !redirected) {
          setIsLoading(false);
        }
      }
    })();

    return () => {
      mounted = false;
    };
  }, [login, syncFromOidcUser]);

  const contextValue: AuthContextType = useMemo(
    () => ({
      user,
      isLoggedIn: !!user,
      isLoading,
      login,
      completeLogin,
      logout,
    }),
    [user, isLoading, login, completeLogin, logout]
  );

  return <AuthContext.Provider value={contextValue}>{children}</AuthContext.Provider>;
};

export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
