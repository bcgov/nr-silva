import React, { useState, useEffect } from "react";
import { ComboBox } from "@carbon/react";
import { useMutation } from "@tanstack/react-query";

import { fetchClientLocations, fetchClientsByNameAcronymNumber, ForestClientAutocomplete } from "../../services/OpeningClientLocationService";
import { getClientLabel, getClientLocationLabel } from "../../utils/ForestClientUtils";
import { ComboBoxEvent } from "../../types/CarbonTypes";
import { TextInputEvent } from "../../types/GeneralTypes";
import { createTextInputEvent } from "../../utils/InputUtils";

import './styles.scss';
import CodeDescriptionDto from "../../types/CodeDescriptionType";

const emptyTextInputEvent = createTextInputEvent('');

type ForestClientProps = {
  clientInputId: string;
  locationInputId: string;
  clientNumber: string | null | undefined;
  setClientNumber: (event: TextInputEvent) => void,
  locationCode: string | null | undefined;
  setClientLocationCode: (event: TextInputEvent) => void,
}

/**
 * A component containing two text inputs:
 * - The first input allows searching and validating a forest client by name, acronym, or number.
 * - The second input provides a list of valid location codes under the validated client.
 */
const ForestClientInput = ({
  clientInputId,
  locationInputId,
  clientNumber,
  setClientNumber,
  locationCode,
  setClientLocationCode
}: ForestClientProps) => {
  const [clientSearchTerm, setClientSearchTerm] = useState<string>('');
  const [selectedClient, setSelectedClient] = useState<ForestClientAutocomplete | null>(null);
  const [matchingClients, setMatchingClients] = useState<ForestClientAutocomplete[]>([]);
  const [selectedLocation, setSelectedLocation] = useState<CodeDescriptionDto | null>(null);
  const [matchingLocationCodes, setMatchingLocationCodes] = useState<CodeDescriptionDto[]>([]);

  const clientMutation = useMutation({
    mutationKey: ["forest-clients", "byNameAcronymNumber"],
    mutationFn: (searchParam: string) => fetchClientsByNameAcronymNumber(searchParam),
    onSuccess: (data) => setMatchingClients(data),
  });

  const locationCodeMutation = useMutation({
    mutationFn: (clientId: string) => fetchClientLocations(clientId),
    onSuccess: (data) => setMatchingLocationCodes(data)
  })

  // Clear the input based on parent's state
  useEffect(() => {
    if (!clientNumber) {
      setSelectedClient(null)
    }
  }, [clientNumber])


  // Clear the input based on parent's state
  useEffect(() => {
    if (!locationCode) {
      setSelectedLocation(null)
    }
  }, [locationCode])

  /* Debounce the API call by 300ms */
  useEffect(() => {
    if (clientSearchTerm.length <= 2) return;

    const handler = setTimeout(() => {
      clientMutation.mutate(clientSearchTerm);
    }, 300);

    // Clear timeout if user types again
    return () => clearTimeout(handler);
  }, [clientSearchTerm]);

  const handleClientInput = (inputText: string) => {
    if (selectedClient && getClientLabel(selectedClient) === inputText) return;
    // Reset selected client and location code when typing
    setSelectedClient(null);
    setSelectedLocation(null);
    setClientNumber(emptyTextInputEvent);
    setClientLocationCode(emptyTextInputEvent)
    setMatchingLocationCodes([]);
    setClientSearchTerm(inputText);
  };

  const handleClientSelection = (e: ComboBoxEvent<ForestClientAutocomplete>) => {
    const { selectedItem } = e;
    if (selectedItem) {
      setSelectedClient(selectedItem);
      setClientSearchTerm(getClientLabel(selectedItem));
      setClientNumber(createTextInputEvent(selectedItem.id))
      locationCodeMutation.mutate(selectedItem.id);
    }
  };

  const handleLocationSelection = (e: ComboBoxEvent<CodeDescriptionDto>) => {
    const { selectedItem } = e;
    if (selectedItem) {
      setSelectedLocation(selectedItem);
      setClientLocationCode(createTextInputEvent(selectedItem.code));
    }
  };

  return (
    <div className="forest-client-input-container">
      <ComboBox
        id={clientInputId}
        className="client-name-combobox"
        helperText="Search by client name, number, or acronym"
        items={matchingClients}
        titleText="Client"
        onChange={handleClientSelection}
        onInputChange={handleClientInput}
        itemToString={getClientLabel}
        readOnly={locationCodeMutation.isPending}
        selectedItem={selectedClient ?? undefined}
      />
      <ComboBox
        className="location-code-combobox"
        id={locationInputId}
        items={matchingLocationCodes}
        titleText="Location code"
        onChange={handleLocationSelection}
        disabled={!selectedClient}
        itemToString={getClientLocationLabel}
        selectedItem={selectedLocation ?? undefined}
      />
    </div>
  );
};

export default ForestClientInput;
