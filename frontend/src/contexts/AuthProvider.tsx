import React, { createContext, useState, useContext, useEffect, useMemo, ReactNode } from "react";
import { fetchAuthSession, signInWithRedirect, signOut } from "aws-amplify/auth";
import { parseToken } from "@/services/AuthService";
import { env } from "@/env";
import { FamLoginUser, IdpProviderType } from "@/types/AuthTypes";
import { REDIRECT_KEY, SELECTED_CLIENT_KEY, ACCESS_TOKEN_KEY } from "@/constants";
import { setCookie } from "@/utils/CookieUtils";

// 1. Define an interface for the context value
interface AuthContextType {
  user: FamLoginUser | undefined;
  isLoggedIn: boolean;
  isLoading: boolean;
  login: (provider: IdpProviderType, redirectPath?: string) => void;
  logout: () => void;
  setSelectedClient: React.Dispatch<React.SetStateAction<string | undefined>>
  selectedClient?: string;
}

// 2. Define an interface for the provider's props
interface AuthProviderProps {
  children: ReactNode;
}

// 3. Create the context with a default value of `undefined`
export const AuthContext = createContext<AuthContextType | undefined>(undefined);

// 4. Create the AuthProvider component with explicit typing
export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<FamLoginUser | undefined>(undefined);
  const [isLoading, setIsLoading] = useState(true);
  const [selectedClient, setSelectedClient] = useState<string | undefined>();

  /**
   * Resolves the application environment.
   * Defaults to "TEST" if VITE_ZONE is not set or is numeric from PR deployments.
   */
  const appEnv = isNaN(Number(env.VITE_ZONE)) ? env.VITE_ZONE ?? "TEST" : "TEST";

  const login = async (provider: IdpProviderType, redirectPath?: string) => {
    const envProvider =
      provider === 'IDIR'
        ? `${appEnv.toLocaleUpperCase()}-IDIR`
        : `${appEnv.toLocaleUpperCase()}-BCEIDBUSINESS`;

    // Store current path to redirect after login
    const targetPath = redirectPath ?? (window.location.pathname + window.location.search);
    if (targetPath && targetPath !== "/") {
      localStorage.setItem(REDIRECT_KEY, targetPath);
    }

    try {
      await signInWithRedirect({
        provider: { custom: envProvider.toUpperCase() }
      });
    } catch (err: unknown) {
      // If Amplify thinks a user is already signed in (stale/expired session),
      // clear it and retry the redirect.
      if (err instanceof Error && err.name === 'UserAlreadyAuthenticatedException') {
        await signOut();
        await signInWithRedirect({
          provider: { custom: envProvider.toUpperCase() }
        });
        return;
      } else {
        throw err;
      }
    }
  };

  const logout = async () => {
    await signOut();
    setUser(undefined);
  };

  // On mount, load current user session once. Token refresh is handled by tanstackConfig when needed.
  useEffect(() => {
    let mounted = true;
    let redirected = false;
    (async () => {
      try {
        // Prefer to fetch full session to read accessToken if present
        const session = await fetchAuthSession();
        const tokens = (session.tokens ?? {}) as { idToken?: any; accessToken?: any };
        const accessToken = tokens.accessToken?.toString() ?? null;

        // Persist access token for OpenAPI token provider
        if (accessToken) {
          setCookie(ACCESS_TOKEN_KEY, accessToken);
        }

        const idToken = tokens.idToken;
        if (!mounted) return;
        if (idToken) {
          const parsedUser = parseToken(idToken);
          // restore/select client similar to previous logic
          const storedClientId = localStorage.getItem(SELECTED_CLIENT_KEY);
          if (storedClientId) {
            if (parsedUser?.associatedClients.includes(storedClientId)) {
              setSelectedClient(storedClientId);
            } else {
              localStorage.removeItem(SELECTED_CLIENT_KEY);
            }
          } else if (parsedUser?.associatedClients.length === 1) {
            const singleClientId = parsedUser.associatedClients[0]!;
            localStorage.setItem(SELECTED_CLIENT_KEY, singleClientId);
            setSelectedClient(singleClientId);
          }
          setUser(parsedUser);

          // Clean up idp_hint if present (already logged in)
          const searchParams = new URLSearchParams(window.location.search);
          if (searchParams.has("idp_hint")) {
            searchParams.delete("idp_hint");
            const searchStr = searchParams.toString();
            const cleanPath = window.location.pathname + (searchStr ? `?${searchStr}` : "");
            window.history.replaceState({}, "", cleanPath);
          }
        } else {
          setUser(undefined);

          // Handle silent sign-on
          const searchParams = new URLSearchParams(window.location.search);
          const idpHint = searchParams.get("idp_hint")?.toLowerCase();
          if (idpHint && (idpHint === "idir" || idpHint === "bceid" || idpHint === "bceidbusiness")) {
            const alreadyAttempted = sessionStorage.getItem("silent_login_attempted") === "true";
            if (!alreadyAttempted) {
              sessionStorage.setItem("silent_login_attempted", "true");

              let provider: IdpProviderType | null = null;
              if (idpHint === "idir") {
                provider = "IDIR";
              } else if (idpHint === "bceid" || idpHint === "bceidbusiness") {
                provider = "BCEIDBUSINESS";
              }

              if (provider) {
                redirected = true;
                const cleanSearchParams = new URLSearchParams(window.location.search);
                cleanSearchParams.delete("idp_hint");
                const searchStr = cleanSearchParams.toString();
                const cleanPath = window.location.pathname + (searchStr ? `?${searchStr}` : "");

                login(provider, cleanPath);
              }
            } else {
              // Already attempted once. Clean the query parameter and render the landing page.
              const cleanSearchParams = new URLSearchParams(window.location.search);
              cleanSearchParams.delete("idp_hint");
              const searchStr = cleanSearchParams.toString();
              const cleanPath = window.location.pathname + (searchStr ? `?${searchStr}` : "");
              window.history.replaceState({}, "", cleanPath);
            }
          }
        }
      } catch {
        setUser(undefined);
      } finally {
        if (mounted && !redirected) {
          setIsLoading(false);
        }
      }
    })();
    return () => {
      mounted = false;
    };
  }, []);

  const contextValue: AuthContextType = useMemo(
    () => ({
      user,
      isLoggedIn: !!user,
      isLoading,
      login,
      logout,
      selectedClient,
      setSelectedClient
    }),
    [user, isLoading, selectedClient]
  );

  return (
    <AuthContext.Provider value={contextValue}>
      {children}
    </AuthContext.Provider>
  );
};

// This is a helper hook to use the Auth context more easily
// 5. Create a custom hook to consume the context safely
export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};
