import React, { useState, useEffect } from "react";
import { ComboBox, InlineLoading } from "@carbon/react";
import { useAutocomplete } from "../../contexts/AutocompleteProvider";
import { fetchClientsByNameAcronymNumber, fetchClientLocations, ForestClientAutocomplete, ForestClientLocation } from "../../services/OpeningClientLocationService";


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

// Defines when the fetch should be skipped for a specific key
export const skipConditions = {
  // Skip when the query value matches the selected text by the user on the dropdown
  clients: (query: string) => query.match(/^[a-zA-Z\s]*,\s[a-zA-Z\s]*,*/) ? true : false,  
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

const AutocompleteClientLocation: React.FC<AutocompleteComponentProps> = ({ setValue }) => {
  const { options, fetchOptions, setOptions } = useAutocomplete();
  const [isActive, setIsActive] = useState(false);
  const [location, setLocation] = useState<AutocompleteProps | null>(null);

  const handleClientChange = async (e: AutocompleteComboboxProps) => {    

    const selectedItem = e.selectedItem;
    if (selectedItem) {
      setIsActive(true);
      await fetchOptions(selectedItem.id, "locations");
    }else{
      setOptions("locations", []);
      setLocation(null);
      setIsActive(false);
    }
  };

  const handleLocationSelection = (e: AutocompleteComboboxProps) => {    
    setValue(e?.selectedItem?.id as string || null)
    setLocation(e?.selectedItem || null);
  };

  return (
    <div className="d-flex w-100 gap-1 mt-2">
      <ComboBox
        id="client-name"
        className="flex-fill"
        allowCustomValue
        onInputChange={(e: string) => fetchOptions(e, "clients")}
        onChange={handleClientChange}
        helperText="Search by client name, number or acronym"
        items={options["clients"] || []}
        titleText="Client"
        typeahead />

      <ComboBox
        disabled={!isActive}
        id="client-location"
        className="flex-fill"
        allowCustomValue
        selectedItem={location}
        onChange={handleLocationSelection}        
        items={options["locations"] || []}
        titleText="Location code"
        typeahead />
      </div>
  );
}

export default AutocompleteClientLocation;