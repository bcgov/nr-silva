import React, { createContext, useState, useContext, useEffect, ReactNode } from 'react';
import { fetchAuthSession, signInWithRedirect, signOut } from "aws-amplify/auth";
import { parseToken, FamLoginUser } from "../services/AuthService";
import { env } from '../env';

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
    const { idToken } = (await fetchAuthSession()).tokens ?? {}; //TODO: make a way to set through tests

    if(idToken){
      return Promise.resolve(parseToken(idToken));
    }
    return Promise.reject('No user details found');
  };

  const login = async (provider: string) => {
    const envProvider = (provider.localeCompare('idir') === 0) 
    ?`${(appEnv).toLocaleUpperCase()}-IDIR`
    : `${(appEnv).toLocaleUpperCase()}-BCEIDBUSINESS`;

      signInWithRedirect({
        provider: { custom: envProvider.toUpperCase() } //TODO: Change environment
      });    
  };

  const logout = async () => {    
      await signOut();
      setIsLoggedIn(false);
      window.location.href = '/'; // Optional redirect after logout    
  };

  return (
    <AuthContext.Provider value={{ user, userRoles, isLoggedIn, isLoading, login, logout, userDetails }}>
      {children}
    </AuthContext.Provider>
  );
};

// This is a helper hook to use the Auth context more easily
// 5. Create a custom hook to consume the context safely
export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export const getAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('AuthProvider not found');
  }
  return context;
};