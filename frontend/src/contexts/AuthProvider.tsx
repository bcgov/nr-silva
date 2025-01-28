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
    FamLoginUser,
    setAuthIdToken
} from "../services/AuthService";
import { extractGroups } from "../utils/famUtils";
import { env } from "../env";
import { JWT } from "../types/amplify";

// 1. Define an interface for the context value
interface AuthContextType {
    user: FamLoginUser | undefined;
    userRoles: string[] | undefined;
    isLoggedIn: boolean;
    isLoading: boolean;
    login: (provider: string) => void;
    logout: () => void;
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
    const [userRoles, setUserRoles] = useState<string[] | undefined>(undefined);
    const [isLoading, setIsLoading] = useState(true);

    const appEnv = env.VITE_ZONE ?? "DEV";

    const refreshUserState = async () => {
        setIsLoading(true);
        try {
            const idToken = await loadUserToken();
            if (idToken) {
                setUser(parseToken(idToken));
                setUserRoles(extractGroups(idToken.payload));
            } else {
                setUser(undefined);
                setUserRoles(undefined);
            }
        } catch {
            setUser(undefined);
            setUserRoles(undefined);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        refreshUserState();
        const interval = setInterval(loadUserToken, 3 * 60 * 1000);
        return () => clearInterval(interval);
    }, []);

    const login = async (provider: string) => {
        const envProvider =
            provider.localeCompare("idir") === 0
                ? `${appEnv.toLocaleUpperCase()}-IDIR`
                : `${appEnv.toLocaleUpperCase()}-BCEIDBUSINESS`;

        signInWithRedirect({
            provider: { custom: envProvider.toUpperCase() }
        });
    };

    const logout = async () => {
        await signOut();
        setUser(undefined);
        setUserRoles(undefined);
        window.location.href = "/"; // Optional redirect after logout
    };

    const contextValue = useMemo(
        () => ({
            user,
            userRoles,
            isLoggedIn: !!user,
            isLoading,
            login,
            logout
        }),
        [user, userRoles, isLoading]
    );

    return (
        <AuthContext.Provider value={contextValue}>
            {children}
        </AuthContext.Provider>
    );
};

// This is a helper hook to use the Auth context more easily
// 5. Create a custom hook to consume the context safely
export const useGetAuth = (): AuthContextType => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useGetAuth must be used within an AuthProvider");
    }
    return context;
};

const loadUserToken = async (): Promise<JWT | undefined> => {
    if (env.NODE_ENV !== "test") {
        const { idToken } = (await fetchAuthSession()).tokens ?? {};
        setAuthIdToken(idToken?.toString() || null);
        return idToken;
    } else {
        // This is for test only
        const token = getUserTokenFromCookie();
        if (token) {
            const jwtBody = JSON.parse(atob(token.split(".")[1]));
            return { payload: jwtBody };
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
    return cookie ? cookie.split("=")[1] : "";
};
