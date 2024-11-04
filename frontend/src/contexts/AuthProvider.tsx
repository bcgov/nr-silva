import React, { createContext, useState, useContext, useEffect, useMemo, ReactNode } from 'react';
import { fetchAuthSession, signInWithRedirect, signOut } from "aws-amplify/auth";
import { parseToken, FamLoginUser } from "../services/AuthService";
import { extractGroups } from '../utils/famUtils';
import { env } from '../env';
import { JWT } from '../types/amplify';

// 1. Define an interface for the context value
interface AuthContextType {
  user: FamLoginUser | undefined;
  userRoles: string[] | undefined;
  isLoggedIn: boolean;
  isLoading: boolean;
  login: (provider: string) => void;
  logout: () => void;
  userDetails: () => Promise<FamLoginUser | undefined> 

}

// 2. Define an interface for the provider's props
interface AuthProviderProps {
  children: ReactNode;
}

// 3. Create the context with a default value of `undefined`
const AuthContext = createContext<AuthContextType | undefined>(undefined);

// 4. Create the AuthProvider component with explicit typing
export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [user, setUser] = useState<FamLoginUser | undefined>(undefined);
  const [userRoles, setUserRoles] = useState<string[] | undefined>(undefined);
  const [isLoading, setIsLoading] = useState(true);

  const appEnv = env.VITE_ZONE ?? 'DEV';


  useEffect(() => {          
      const checkUser = async () => {
        try{
          const session = await fetchAuthSession();          
          setIsLoggedIn(!!session.tokens);
          setIsLoading(false);
          if(session.tokens){
            setUser(parseToken(session.tokens.idToken));
            setUserRoles(extractGroups(session.tokens?.idToken?.payload));
          }
        }catch(error){
          setIsLoggedIn(false);
          setUser(parseToken(undefined));
          setIsLoading(false);
        }
      };  
      checkUser();
  }, []);


  const userDetails = async (): Promise<FamLoginUser | undefined> => {
    const idToken = await loadUserToken();

    if(idToken){
      return Promise.resolve(parseToken(idToken));
    }
    return Promise.reject(new Error('No user details found'));
  };

  const login = async (provider: string) => {
    const envProvider = (provider.localeCompare('idir') === 0) 
    ?`${(appEnv).toLocaleUpperCase()}-IDIR`
    : `${(appEnv).toLocaleUpperCase()}-BCEIDBUSINESS`;

      signInWithRedirect({
        provider: { custom: envProvider.toUpperCase() }
      });    
  };

  const logout = async () => {    
      await signOut();
      setIsLoggedIn(false);
      window.location.href = '/'; // Optional redirect after logout    
  };

  const contextValue = useMemo(() => ({
    user,
    userRoles,
    isLoggedIn,
    isLoading,
    login,
    logout,
    userDetails
  }), [user, userRoles, isLoggedIn, isLoading]);

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
    throw new Error('useGetAuth must be used within an AuthProvider');
  }
  return context;
};

const loadUserToken = async () : Promise<JWT|undefined> => {
  if(env.NODE_ENV !== 'test'){
    const {idToken} = (await fetchAuthSession()).tokens ?? {};    
    return Promise.resolve(idToken);
  } else {
    // This is for test only
    const token = getUserTokenFromCookie();
    if (token) {
      const jwtBody = token
        ? JSON.parse(atob(token.split(".")[1]))
        : null;
      return Promise.resolve({ payload: jwtBody });
    } else {
      return Promise.resolve(undefined);
    }
  }
};

const getUserTokenFromCookie = (): string|undefined => {
  const baseCookieName = `CognitoIdentityServiceProvider.${env.VITE_USER_POOLS_WEB_CLIENT_ID}`;
  const userId = encodeURIComponent(getCookie(`${baseCookieName}.LastAuthUser`));
  if (userId) {
    const idTokenCookieName = `${baseCookieName}.${userId}.idToken`;
    const idToken = getCookie(idTokenCookieName);
    return idToken;
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