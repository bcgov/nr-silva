import { useRef, useEffect, useState } from 'react';
import { useMutation, useQuery } from '@tanstack/react-query';
import API from '@/services/API';
import { ForestClientAutocompleteResultDto } from '@/services/OpenApi';
import CustomMultiSelect from '../CustomMultiSelect';
import TooltipLabel from '../TooltipLabel';
import { getClientLabel, getClientSimpleLabel } from '@/utils/ForestClientUtils';
import useBreakpoint from '@/hooks/UseBreakpoint';

type ForestClientMultiSelectProps = {
  selectedClientNumbers?: string[];
  onChange: (clientNumbers: string[] | undefined) => void;
};

const ForestClientMultiSelect = ({ selectedClientNumbers, onChange }: ForestClientMultiSelectProps) => {
  const breakpoint = useBreakpoint();
  const [clientSearchTerm, setClientSearchTerm] = useState<string>('');
  const [matchingClients, setMatchingClients] = useState<ForestClientAutocompleteResultDto[]>([]);

  // Initialize with current prop value if available (for components that mount after searchParams resolves),
  // or capture it asynchronously via effect (for components that are always mounted).
  const [initialClientNumbers, setInitialClientNumbers] = useState<string[] | undefined>(
    selectedClientNumbers?.length ? selectedClientNumbers : undefined
  );
  const hasLoadedInitialClientsRef = useRef(false);

  // For always-mounted components: capture the first non-empty selectedClientNumbers.
  useEffect(() => {
    if (!hasLoadedInitialClientsRef.current && !initialClientNumbers && selectedClientNumbers?.length) {
      setInitialClientNumbers(selectedClientNumbers);
    }
  }, [selectedClientNumbers]);

  // Prefetch client data when selectedClientNumbers is provided
  const initialClientsQuery = useQuery({
    queryKey: ['forest-clients', 'byClientNumbers', initialClientNumbers],
    queryFn: () => API.ForestClientEndpointService.searchByClientNumbers(
      initialClientNumbers!,
      0,
      initialClientNumbers!.length
    ),
    enabled: !hasLoadedInitialClientsRef.current && !!(initialClientNumbers && initialClientNumbers.length > 0),
  });

  // Merge prefetched clients into matchingClients
  useEffect(() => {
    if (initialClientsQuery.data && initialClientsQuery.data.length > 0) {
      setMatchingClients((prev) => {
        const existing = prev ?? [];
        const existingIds = new Set(existing.map((c) => c.id));
        const toAdd = initialClientsQuery.data
          .filter((c) => c.clientNumber != null && !existingIds.has(c.clientNumber))
          .map((c) => ({
            id: c.clientNumber,
            acronym: c.acronym,
            name: c.name,
          }));
        return existing.concat(toAdd);
      });
      hasLoadedInitialClientsRef.current = true;
    }
  }, [initialClientsQuery.data]);

  const clientMutation = useMutation({
    mutationKey: ['forest-clients', 'byNameAcronymNumber'],
    mutationFn: (searchParam: string) => API.ForestClientEndpointService.searchForestClients(searchParam),
    onSuccess: (data: ForestClientAutocompleteResultDto[] | null) => {
      if (!data || !Array.isArray(data) || data.length === 0) return;

      setMatchingClients((prev) => {
        const existing = prev ?? [];
        const existingIds = new Set(existing.map((c) => c.id));
        const toAdd = data.filter((c) => c.id != null && !existingIds.has(c.id));
        return existing.concat(toAdd);
      });
    },
  });

  /* Debounce the API call by 200ms */
  useEffect(() => {
    if (clientSearchTerm.length <= 2) return;

    const handler = setTimeout(() => {
      clientMutation.mutate(clientSearchTerm);
    }, 200);

    // Clear timeout if user types again
    return () => clearTimeout(handler);
  }, [clientSearchTerm]);

  return (
    <CustomMultiSelect
      key={String(hasLoadedInitialClientsRef.current)}
      placeholder={
        selectedClientNumbers && selectedClientNumbers.length > 0
          ? selectedClientNumbers.map((num) => getClientSimpleLabel(matchingClients.find((c) => c.id === num))).join(', ')
          : 'Choose one or more options'
      }
      titleText={
        <TooltipLabel
          align={breakpoint === 'sm' ? 'top' : 'top-left'}
          label="Client"
          tooltip="Type at least 2 characters to search clients, matching options will be loaded."
        />
      }
      id="client-multi-select"
      className="default-search-multi-select"
      items={matchingClients}
      itemToString={getClientLabel}
      onChange={(selected: { selectedItems: ForestClientAutocompleteResultDto[] }) => {
        const selectedClientNumbers = selected.selectedItems.map((item) => item.id) as string[];
        onChange(selectedClientNumbers.length > 0 ? selectedClientNumbers : undefined);
      }}
      onInputValueChange={(changes) => {
        setClientSearchTerm(String(changes));
      }}
      selectedItems={matchingClients.filter((client) => selectedClientNumbers?.includes(client.id ?? '')) ?? []}
      showSkeleton={initialClientsQuery.isLoading}
    />
  );
};

export default ForestClientMultiSelect;
