import { createContext, useContext, ReactNode, useState, useRef, useEffect } from "react";
import debounce from "lodash.debounce";

// TODO: test this

interface AutocompleteProviderProps {
  fetchOptions: (query: string, key: string) => Promise<any[]>;
  skipConditions?: Record<string, (query: string) => boolean>;
  children: ReactNode;
}

interface AutocompleteContextType {
  options: Record<string, any[]>;
  loading: boolean;
  error: string | null;
  fetchOptions: (query: string, key: string) => void;
  setOptions: (key: string, items: any[]) => void;
}

const AutocompleteContext = createContext<AutocompleteContextType | undefined>(undefined);

export const AutocompleteProvider = ({ fetchOptions, skipConditions, children }: AutocompleteProviderProps) => {
  const [options, setOptionsState] = useState<Record<string, any[]>>({});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const debouncedFetchMap = useRef<Map<string, (query: string) => void>>(new Map());

  const setOptions = (key: string, items: any[]) => {    
    setOptionsState((prev) => ({ ...prev, [key]: items }));
  };

  const createDebouncedFetch = (key: string) => {    
    return debounce(async (query: string) => {      
      try {        
        // If no key or query, return
        if(!key || !query) return;
        // If skipConditions are present and the condition is met, return, this avoids overwriting the existing
        // data when a condition to skip is met
        if (skipConditions && skipConditions[key] && skipConditions[key](query)) {
          return;
        }
        const fetchedOptions = await fetchOptions(query, key);
        setOptions(key, fetchedOptions);
      } catch (fetchError) {
        setError("Error fetching options");
      } finally {
        console.log('Disabling loading');
        setLoading(false);
      }
    }, 450);
  };

  const fetchAndSetOptions = (query: string, key: string) => {
    setLoading(true);
    if (!debouncedFetchMap.current.has(key)) {
      debouncedFetchMap.current.set(key, createDebouncedFetch(key));
    }
    const debouncedFetch = debouncedFetchMap.current.get(key);
    debouncedFetch!(query); // Call the specific debounced function for this key
  };


  // Cleanup debounce on unmount
  useEffect(() => {
    return () => {
      debouncedFetchMap.current.forEach((debounced) => debounced.cancel());
      debouncedFetchMap.current.clear();
    };
  }, []);

  return (
    <AutocompleteContext.Provider value={{ options, loading, error, fetchOptions: fetchAndSetOptions, setOptions }}>
      {children}
    </AutocompleteContext.Provider>
  );
};

export const useAutocomplete = (): AutocompleteContextType => {
  const context = useContext(AutocompleteContext);
  if (!context) {
    throw new Error("useAutocomplete must be used within an AutocompleteProvider");
  }
  return context;
};
