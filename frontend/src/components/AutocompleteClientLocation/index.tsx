import React, { useEffect, useState, useImperativeHandle, forwardRef } from "react";
import { ComboBox } from "@carbon/react";
import { useAutocomplete } from "../../contexts/AutocompleteProvider";
import { 
  fetchClientsByNameAcronymNumber,
  fetchClientLocations,
  ForestClientAutocomplete,
  ForestClientLocation 
} from "../../services/OpeningClientLocationService";
import { update } from "lodash";

interface AutocompleteProps {
  id: string,
  label: string,
}

interface AutocompleteComboboxProps{
  selectedItem: AutocompleteProps
}

interface AutocompleteComponentProps {
  setValue: (value: string | null) => void;
}

export interface AutocompleteComponentRefProps {
  reset: () => void;
}

// Defines when the fetch should be skipped for a specific key
export const skipConditions = {
  // Skip when the query value matches the selected text by the user on the dropdown
  clients: (query: string) => {
    const regex = /^[a-zA-Z\s]*,\s[a-zA-Z\s]*,*/;
    return regex.exec(query) !== null;
  },
  // Never skips for locations  
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  locations: (query: string) => false
};

// Fetch options for the Autocomplete component
export const fetchValues = async (query: string, key: string) => {

  // If there is no query, return an empty array
  if(!key || !query) return [];
  
  // For clients, it will do the autocomplete search based on the name, acronym, or number
  if (key === "clients") {
    const response = await fetchClientsByNameAcronymNumber(query);
    const apiresponse = response;
    return apiresponse.map((item: ForestClientAutocomplete) => ({
      id: item.id,
      label: `${item.name}, ${item.id}, ${item.acronym? item.acronym : ''}`
    }));
  } 

  // For locations, it will just load the value based on the selected client id
  if (key === "locations") {
    const response = await fetchClientLocations(query);
    const apiresponse = response;
    return apiresponse.map((item: ForestClientLocation) => ({
      id: item.id,
      label: `${item.id} - ${item.name}`
    }));
  }

  return [];
};

const AutocompleteClientLocation: React.ForwardRefExoticComponent<AutocompleteComponentProps & React.RefAttributes<AutocompleteComponentRefProps>> = forwardRef<AutocompleteComponentRefProps, AutocompleteComponentProps>(
  ({ setValue }, ref) => 
    {
  const { options, fetchOptions, updateOptions } = useAutocomplete();
  const [isActive, setIsActive] = useState(false);
  const [location, setLocation] = useState<AutocompleteProps | null>(null);
  const [client, setClient] = useState<AutocompleteProps | null>(null);

  const clearClient = () => {
    updateOptions("locations", []);
    updateOptions("clients", []);
    setClient(null);
    setValue(null);
    setIsActive(false);
    setLocation(null);
  };

  const handleClientChange = (autocompleteEvent: AutocompleteComboboxProps) => {    

    const selectedItem = autocompleteEvent.selectedItem;
    if (selectedItem) {
      setIsActive(true);
      setClient(selectedItem);
      fetchOptions(selectedItem.id, "locations");
    }else{
      clearClient();
    }
  };

  useImperativeHandle(ref, () => ({
    reset: () => setLocation(null)
  }));

  useEffect(() => {
    setValue(location?.id || null);
  }, [location]);

  return (
    <div className="d-flex w-100 gap-1 mt-2">
      <ComboBox
        id="client-name"
        className="flex-fill"
        allowCustomValue={false}
        selectedItem={client}
        onInputChange={(value: string) => fetchOptions(value, "clients")}
        onChange={handleClientChange}
        itemToElement={(item: AutocompleteProps) => item.label}
        helperText="Search by client name, number or acronym"
        items={options["clients"] || []}
        titleText="Client" />

      <ComboBox
        disabled={!isActive}
        id="client-location"
        className="flex-fill"
        onChange={(item: AutocompleteComboboxProps) => setLocation(item.selectedItem)}
        itemToElement={(item: AutocompleteProps) => item.label}
        items={options["locations"] || [{ id: "", label: "No results found" }]}
        titleText="Location code" />
      </div>
  );
});

AutocompleteClientLocation.displayName = "AutocompleteClientLocation";

export default AutocompleteClientLocation;