import { createContext, useContext, ReactNode, useState, useRef, useEffect } from "react";
import { InlineLoading } from "@carbon/react";
import { debounce, DebouncedFunc } from "lodash";

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
  updateOptions: (key: string, items: any[]) => void;
}

const searchingForItems = [{
  id: "",
  label: (<InlineLoading status="active" iconDescription="Loading" description="Loading data..." />)
}];

const noDataFound = [{ id: "", label: "No results found" }];

const AutocompleteContext = createContext<AutocompleteContextType | undefined>(undefined);

export const AutocompleteProvider = ({ fetchOptions, skipConditions, children }: AutocompleteProviderProps) => {
  const [options, setOptions] = useState<Record<string, any[]>>({});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const debouncedFetchMap = useRef<Map<string, DebouncedFunc<(query: string) => Promise<void>>>>(new Map());

  const updateOptions = (key: string, items: any[]) => {    
    setOptions((prev) => ({ ...prev, [key]: items }));
  };

  const createDebouncedFetch = (key: string) => {    
    return debounce(async (query: string) => {      
      try {        
        // If no key or query, return
        if(!key || !query) return;
        // If skipConditions are present and the condition is met, return, this avoids overwriting the existing
        // data when a condition to skip is met
        if (skipConditions?.[key]?.(query)) {
          return;
        }
        setLoading(true);
        setOptions((prev) => ({ ...prev, [key]: searchingForItems }));
        const fetchedOptions = await fetchOptions(query, key);
        setOptions((prev) => ({ ...prev, [key]: fetchedOptions.length ? fetchedOptions : noDataFound }));
      } catch (fetchError) {
        setError("Error fetching options");
      } finally {
        setLoading(false);
      }
    }, 450);
  };

  const fetchAndSetOptions = (query: string, key: string) => {
    if(!key || !query) return;
    if (skipConditions?.[key]?.(query)) {
      return;
    }
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
    <AutocompleteContext.Provider value={{ options, loading, error, fetchOptions: fetchAndSetOptions, updateOptions }}>
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
