import React, { useEffect, useState, useImperativeHandle, forwardRef } from "react";
import { ComboBox } from "@carbon/react";
import { useAutocomplete } from "../../contexts/AutocompleteProvider";
import { 
  fetchClientsByNameAcronymNumber,
  fetchClientLocations,
  ForestClientAutocomplete,
  ForestClientLocation 
} from "../../services/OpeningClientLocationService";

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
  const [valueTyped, setValueTyped] = useState<string | null>(null);
  const [locationName, setLocationName] = useState<string | null>(null);

  const clearClient = () => {
    updateOptions("locations", []);
    updateOptions("clients", []);
    setClient(null);
    setValue(null);
    setIsActive(false);
    setLocation(null);
  };

  const selectClient = (selectedItem: AutocompleteProps) => {
    if (selectedItem) {
      setIsActive(true);
      setClient(selectedItem);
      fetchOptions(selectedItem.id, "locations");
    }else{
      clearClient();
    }
  }

  const handleClientChange = (autocompleteEvent: AutocompleteComboboxProps) => {
    selectClient(autocompleteEvent.selectedItem)
  };

  // This is here because when deployed the ComboBox tends to fail to call the onChange
  useEffect(() => {
    // We use the useEffect to do a lookup on the clients options
    // It will fill with the autocomplete triggering the fetchOptions
    const selectedItem = options["clients"]?.find((item: AutocompleteProps) => item.label === valueTyped);

    // If there's text typed and we found a client based on the label selected, we select it
    if(valueTyped && selectedItem){
      selectClient(selectedItem);
    }
    
    // This also triggers the autocomplete to fetch the options
    if(valueTyped)
      fetchOptions(valueTyped, "clients")
  },[valueTyped]);

  // This is here because when deployed the ComboBox tends to fail to call the onChange
  useEffect(() => {
    // We use the useEffect to do a lookup on the locations options
    const selectedItem = options["locations"]?.find((item: AutocompleteProps) => item.label === locationName);
    // If there's text typed and we found a location based on the label selected, we select it
    if(locationName && selectedItem){
      // This triggers the location useEffect to set the value
      setLocation(selectedItem);
    }
  }, [locationName]);

  useImperativeHandle(ref, () => ({
    reset: () => setLocation(null)
  }));

  // Why do we keep this then? Because of the onChange start to work, this will work as it was intended
  useEffect(() => {
    setValue(location?.id ?? null);
  }, [location]);

  return (
    <div className="d-flex w-100 gap-1 mt-2">
      <ComboBox
        id="client-name"
        className="flex-fill"
        allowCustomValue={false}
        selectedItem={client}
        onInputChange={setValueTyped}
        onChange={handleClientChange}
        itemToElement={(item: AutocompleteProps) => item.label}
        helperText="Search by client name, number or acronym"
        items={options["clients"] || []}
        titleText="Client" 
      />
      <ComboBox
        disabled={!isActive}
        id="client-location"
        className="flex-fill"
        onInputChange={setLocationName}
        onChange={(item: AutocompleteComboboxProps) => setLocation(item.selectedItem)}
        itemToElement={(item: AutocompleteProps) => item.label}
        items={options["locations"] || [{ id: "", label: "No results found" }]}
        titleText="Location code" />
      </div>
  );
});

AutocompleteClientLocation.displayName = "AutocompleteClientLocation";

export default AutocompleteClientLocation;