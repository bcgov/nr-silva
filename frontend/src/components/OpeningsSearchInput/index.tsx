import { OpeningSearchParamsType } from "@/types/OpeningTypes";
import { Column, Grid, TextInput } from "@carbon/react";
import { useQuery } from "@tanstack/react-query";
import API from "@/services/API";
import { codeDescriptionToDisplayText } from "@/utils/multiSelectUtils";
import CustomMultiSelect from "../CustomMultiSelect";

import './styles.scss';

type props = {
  searchParams: OpeningSearchParamsType | undefined;
  onSearchParamsChange: (field: keyof OpeningSearchParamsType, value: unknown) => void;
}
const OpeningsSearchInput = ({ searchParams, onSearchParamsChange }: props) => {

  const categoryQuery = useQuery({
    queryKey: ["codes", "opening-categories"],
    queryFn: () => API.CodesEndpointService.getOpeningCategories(),
  });

  const orgUnitQuery = useQuery({
    queryKey: ["codes", "org-units"],
    queryFn: API.CodesEndpointService.getOpeningOrgUnits
  });

  return (
    <Grid className="opening-search-input-grid">
      {/* Opening ID */}
      <Column sm={4} md={4} lg={6} max={4}>
        <TextInput
          id="opening-id-input"
          labelText="Opening ID"
          placeholder="Enter opening ID"
          defaultValue={searchParams?.openingId}
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
              .map(item => item.code) as string[];
            onSearchParamsChange('categories', selectedCodes.length > 0 ? selectedCodes : undefined);
          }}
          selectedItems={categoryQuery.data?.filter(data => searchParams?.categories?.includes(data.code ?? '')) ?? []}
        />
      </Column>
    </Grid>
  );
}

export default OpeningsSearchInput;
