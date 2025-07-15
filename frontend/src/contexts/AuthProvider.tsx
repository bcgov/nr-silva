import React, {
  createContext,
  useState,
  useContext,
  useEffect,
  useMemo,
  ReactNode
} from "react";
import {
  fetchAuthSession,
  signInWithRedirect,
  signOut
} from "aws-amplify/auth";
import {
  parseToken,
  setAuthIdToken
} from "@/services/AuthService";
import { env } from "@/env";
import { JWT } from "@/types/amplify";
import { FamLoginUser, IdpProviderType } from "@/types/AuthTypes";
import { REDIRECT_KEY } from "@/constants";

// 1. Define an interface for the context value
interface AuthContextType {
  user: FamLoginUser | undefined;
  isLoggedIn: boolean;
  isLoading: boolean;
  login: (provider: IdpProviderType) => void;
  logout: () => void;
  setSelectedClient: React.Dispatch<React.SetStateAction<string | undefined>>
  selectedClient?: string;
}

// 2. Define an interface for the provider's props
interface AuthProviderProps {
  children: ReactNode;
}

// 3. Create the context with a default value of `undefined`
const AuthContext = createContext<AuthContextType | undefined>(undefined);

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

  const refreshUserState = async () => {
    setIsLoading(true);
    try {
      const idToken = await loadUserToken();
      if (idToken) {
        const parsedUser = parseToken(idToken);
        setUser(parsedUser);
        if (parsedUser?.associatedClients.length === 1) {
          setSelectedClient(parsedUser.associatedClients[0])
        }
      } else {
        setUser(undefined);
      }
    } catch {
      setUser(undefined);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    refreshUserState();
    const interval = setInterval(loadUserToken, 3 * 60 * 1000);
    return () => clearInterval(interval);
  }, []);

  const login = async (provider: IdpProviderType) => {
    const envProvider =
      provider === 'IDIR'
        ? `${appEnv.toLocaleUpperCase()}-IDIR`
        : `${appEnv.toLocaleUpperCase()}-BCEIDBUSINESS`;

    // Store current path to redirect after login
    const currentPath = window.location.pathname + window.location.search;
    if (currentPath && currentPath !== "/") {
      localStorage.setItem(REDIRECT_KEY, currentPath);
    }

    signInWithRedirect({
      provider: { custom: envProvider.toUpperCase() }
    });
  };

  const logout = async () => {
    await signOut();
    setUser(undefined);
  };

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

const loadUserToken = async (): Promise<JWT | undefined> => {
  if (env.NODE_ENV !== "test") {
    const { idToken } = (await fetchAuthSession()).tokens ?? {};
    setAuthIdToken(idToken?.toString() ?? null);
    return idToken;
  } else {
    // This is for test only
    const token = getUserTokenFromCookie();
    if (token) {
      const splittedToken = token.split(".")[1];
      if (splittedToken) {
        const jwtBody = JSON.parse(atob(splittedToken));
        return { payload: jwtBody };
      }
      throw new Error("Error parsing token");
    }
    throw new Error("No token found");
  }
};

const getUserTokenFromCookie = (): string | undefined => {
  const baseCookieName = `CognitoIdentityServiceProvider.${env.VITE_USER_POOLS_WEB_CLIENT_ID}`;
  const userId = encodeURIComponent(
    getCookie(`${baseCookieName}.LastAuthUser`)
  );
  if (userId) {
    return getCookie(`${baseCookieName}.${userId}.idToken`);
  } else {
    return undefined;
  }
};

const getCookie = (name: string): string => {
  const cookie = document.cookie
    .split(";")
    .find((cookieValue) => cookieValue.trim().startsWith(name));
  return cookie ? cookie.split("=")[1] ?? "" : "";
};
