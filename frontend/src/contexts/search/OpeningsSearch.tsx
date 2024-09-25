import React, { createContext, useState, useContext, ReactNode } from 'react';

// Define the shape of the search filters context
interface OpeningsSearchContextProps {
  filters: any;
  setFilters: React.Dispatch<React.SetStateAction<any>>;
  searchTerm: string;
  setSearchTerm: React.Dispatch<React.SetStateAction<string>>
  clearFilters: () => void;
  clearIndividualField: (key:string) => void;
}

// Create the context with a default value of undefined
const OpeningsSearchContext = createContext<OpeningsSearchContextProps | undefined>(undefined);

// Create the provider component
export const OpeningsSearchProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const defaultFilters = {
    startDate: null as Date | null,
    endDate: null as Date | null,
    orgUnit: null as string | null,
    category: null as string | null,
    clientAcronym: "",
    blockStatus: "",
    cutBlock: "",
    cuttingPermit: "",
    grossArea: "",
    timberMark: "",
    status: null as string | null,
    openingFilters: [] as string[],
    blockStatuses: [] as string[],
  };

  const [filters, setFilters] = useState(defaultFilters);
  const [searchTerm, setSearchTerm] = useState("");

  // Function to clear individual filter field by key
  const clearIndividualField = (key: string) => {
    setFilters((prevFilters) => ({
      ...prevFilters,
      [key]: defaultFilters[key as keyof typeof defaultFilters],
    }));
  };

  const clearFilters = () => setFilters(defaultFilters);

  return (
    <OpeningsSearchContext.Provider value={{ filters, setFilters, searchTerm, setSearchTerm, clearFilters, clearIndividualField }}>
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
