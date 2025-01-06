import React, { createContext, useState, useContext, ReactNode } from 'react';
import { OpeningFilters,openingFiltersKeys } from '../../services/search/openings';

// Define the shape of the search filters context
interface OpeningsSearchContextProps {
  filters: OpeningFilters;
  setFilters: React.Dispatch<React.SetStateAction<OpeningFilters>>;
  searchTerm: string;
  setSearchTerm: React.Dispatch<React.SetStateAction<string>>
  clearFilters: () => void;
  clearIndividualField: (key:string) => void;
  setIndividualClearFieldFunctions: React.Dispatch<React.SetStateAction<Record<string, () => void>>>;
}

// Create the context with a default value of undefined
const OpeningsSearchContext = createContext<OpeningsSearchContextProps | undefined>(undefined);

// Create the provider component
export const OpeningsSearchProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [filters, setFilters] = useState<OpeningFilters>({status:[], openingFilters:[]});
  const [searchTerm, setSearchTerm] = useState<string>("");
  const [individualClearFieldFunctions, setIndividualClearFieldFunctions] = useState<Record<string, () => void>>({});

  // Function to clear individual filter field by key
  const clearIndividualField = (key: string) => {
    setFilters((prevFilters) => ({
      ...prevFilters,
      [key]: undefined
    }));
    individualClearFieldFunctions[key] && individualClearFieldFunctions[key]();
  };

  const clearFilters = () => {
    setFilters({status:[], openingFilters:[]});
    openingFiltersKeys.forEach((key) => {
      individualClearFieldFunctions[key] && individualClearFieldFunctions[key]();
    });
    
    setFilters(defaultFilters);
  
  };

  return (
    <OpeningsSearchContext.Provider value={{ filters, setFilters, searchTerm, setSearchTerm, clearFilters, clearIndividualField, setIndividualClearFieldFunctions }}>
      {children}
    </OpeningsSearchContext.Provider>
  );
};

// Custom hook to use the context
export const useOpeningsSearch = (): OpeningsSearchContextProps => {
  const context = useContext(OpeningsSearchContext);
  if (!context) {
    throw new Error('useOpeningsSearch must be used within an OpeningsSearchProvider');
  }
  return context;
};