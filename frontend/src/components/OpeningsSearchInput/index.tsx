import { OpeningSearchParamsType } from "@/types/OpeningTypes";
import { Column, Grid, TextInput } from "@carbon/react";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useRef } from "react";
import API from "@/services/API";
import { codeDescriptionToDisplayText } from "@/utils/multiSelectUtils";
import CustomMultiSelect from "../CustomMultiSelect";

import './styles.scss';

type props = {
  searchParams: OpeningSearchParamsType | undefined;
  onSearchParamsChange: (field: keyof OpeningSearchParamsType, value: unknown) => void;
}
const OpeningsSearchInput = ({ searchParams, onSearchParamsChange }: props) => {

  const openingIdInputRef = useRef<HTMLInputElement>(null);

  const categoryQuery = useQuery({
    queryKey: ["codes", "opening-categories"],
    queryFn: () => API.CodesEndpointService.getOpeningCategories(),
  });

  const orgUnitQuery = useQuery({
    queryKey: ["codes", "org-units"],
    queryFn: API.CodesEndpointService.getOpeningOrgUnits
  });

  // Imperatively update the opening ID input when searchParams changes
  // This avoids re-renders on keystroke but ensures reset works.
  // The same goes for other text inputs here.
  useEffect(() => {
    if (openingIdInputRef.current) {
      const value = searchParams?.openingId;
      openingIdInputRef.current.value = value ? String(value) : '';
    }
  }, [searchParams?.openingId]);

  return (
    <Grid className="opening-search-input-grid">
      {/* Opening ID */}
      <Column sm={4} md={4} lg={6} max={4}>
        <TextInput
          ref={openingIdInputRef}
          id="opening-id-input"
          labelText="Opening ID"
          placeholder="Enter opening ID"
          onBlur={(e) => onSearchParamsChange('openingId', e.target.value ? Number(e.target.value) : undefined)}
        />
      </Column>

      {/* Opening Categories */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          placeholder={searchParams?.categories && searchParams.categories.length > 0 ? searchParams.categories.join(', ') : 'Choose one or more options'}
          titleText="Opening category"
          id="category-multi-select"
          className="opening-search-multi-select"
          items={categoryQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={(selected) => {
            const selectedCodes = selected.selectedItems
              .filter((item): item is NonNullable<typeof item> => item !== undefined && !!item.code)
              .map(item => item.code);
            onSearchParamsChange('categories', selectedCodes.length > 0 ? selectedCodes : undefined);
          }}
          selectedItems={categoryQuery.data?.filter(data => searchParams?.categories?.includes(data.code ?? '')) ?? []}
        />
      </Column>
    </Grid>
  );
}

export default OpeningsSearchInput;
