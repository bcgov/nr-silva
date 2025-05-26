import React, { createContext, useContext, useEffect, useState } from "react";
import { useQuery, useMutation } from "@tanstack/react-query";
import { UserPreference } from "@/types/PreferencesType";
import {
  loadUserPreference,
  saveUserPreference,
} from "@/utils/localStorageUtils";

// 1. Define an interface for the context value
interface PreferenceContextType {
  userPreference: UserPreference;
  updatePreferences: (preference: Partial<UserPreference>) => void;
}

// 2. Define an interface for the provider's props
interface PreferenceProviderProps {
  children: React.ReactNode;
}

// 3. Create the context with a default value of `undefined`
const PreferenceContext = createContext<PreferenceContextType | undefined>(
  undefined
);

// 4. Create the PreferenceProvider component with explicit typing
export const PreferenceProvider: React.FC<PreferenceProviderProps> = ({
  children,
}) => {
  const [userPreference, setUserPreference] = useState<UserPreference>(
    loadUserPreference()
  );

  const queryUserPreference = useQuery({
    queryKey: ["userPreference"],
    queryFn: loadUserPreference,
    refetchOnMount: true,
  });

  const mutateUserPreference = useMutation({
    mutationFn: async (updatedPreferences: UserPreference) => {
      return Promise.resolve(saveUserPreference(updatedPreferences));
    },
  });

  const updatePreferences = (preference: Partial<UserPreference>) => {
    const updatedPreferences = {
      ...userPreference,
      ...preference,
    } as UserPreference;
    setUserPreference(updatedPreferences);
    mutateUserPreference.mutate(updatedPreferences);
  };

  useEffect(() => {
    queryUserPreference.isSuccess &&
      setUserPreference(queryUserPreference.data as UserPreference);
  }, []);

  const contextValue = {
    userPreference,
    updatePreferences,
  };

  return (
    <PreferenceContext.Provider value={contextValue}>
      {children}
    </PreferenceContext.Provider>
  );
};

// 5. Create a custom hook to use the PreferenceContext
export const usePreference = () => {
  const context = useContext(PreferenceContext);
  if (context === undefined) {
    throw new Error("usePreference must be used within a PreferenceProvider");
  }
  return context;
};
