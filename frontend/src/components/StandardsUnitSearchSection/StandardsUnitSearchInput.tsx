import { useRef } from 'react';
import { Column, Grid, TextInput } from '@carbon/react';
import { useQuery } from '@tanstack/react-query';
import API from '@/services/API';
import { StandardsUnitSearchParams } from '@/types/ApiType';
import useRefWithSearchParam from '@/hooks/useRefWithSearchParam';
import { enforceNumberInputOnKeyDown, enforceNumberInputOnPaste } from '@/utils/InputUtils';
import { codeDescriptionToDisplayText } from '@/utils/multiSelectUtils';
import { getMultiSelectPlaceholderHelper, handleMultiSelectChangeHelper } from '@/utils/SearchUtils';
import { SSID_MAX_LENGTH } from '@/constants';
import CustomMultiSelect from '@/components/CustomMultiSelect';
import ForestClientMultiSelect from '@/components/ForestClientMultiSelect';
import { BgcSearchInputs, OrgUnitMultiSelect, SearchDateRange } from '@/components/common/SearchInput';

import './styles.scss';

type props = {
  searchParams?: StandardsUnitSearchParams;
  handleSearchFieldChange: (field: keyof StandardsUnitSearchParams, value: unknown) => void;
};

const StandardsUnitSearchInput = ({ searchParams, handleSearchFieldChange }: props) => {
  const ssidInputRef = useRef<HTMLInputElement>(null);
  useRefWithSearchParam(ssidInputRef, searchParams?.standardsRegimeId);

  const preferredSpeciesQuery = useQuery({
    queryKey: ["codes", "silv-tree-species"],
    queryFn: API.CodesEndpointService.getSilvTreeSpeciesCodes,
  });

  const handleMultiSelectChange = handleMultiSelectChangeHelper<StandardsUnitSearchParams>(
    'preferredSpecies',
    handleSearchFieldChange
  );

  return (
    <Grid className="default-search-input-grid">
      {/* SSID */}
      <Column sm={4} md={4} lg={6} max={4}>
        <TextInput
          ref={ssidInputRef}
          id="ssid-input"
          name="ssid"
          labelText="SSID"
          placeholder="Enter Stocking standards ID"
          onBlur={(e) => handleSearchFieldChange('standardsRegimeId', e.target.value ? Number(e.target.value) : undefined)}
          onKeyDown={(e) => enforceNumberInputOnKeyDown(e, SSID_MAX_LENGTH)}
          onPaste={(e) => enforceNumberInputOnPaste(ssidInputRef.current, e, SSID_MAX_LENGTH)}
        />
      </Column>

      {/* Preferred Species */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          placeholder={getMultiSelectPlaceholderHelper(searchParams?.preferredSpecies)}
          titleText="Preferred species"
          id="preferred-species-multi-select"
          className="default-search-multi-select"
          items={preferredSpeciesQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange}
          selectedItems={preferredSpeciesQuery.data?.filter(data => searchParams?.preferredSpecies?.includes(data.code ?? '')) ?? []}
        />
      </Column>

      {/* Org Unit */}
      <OrgUnitMultiSelect
        type="district"
        selectedOrgUnits={searchParams?.orgUnits}
        onChange={(orgUnits) => handleSearchFieldChange('orgUnits', orgUnits)}
      />

      {/* Client */}
      <Column sm={4} md={4} lg={6} max={4}>
        <ForestClientMultiSelect
          selectedClientNumbers={searchParams?.clientNumbers}
          onChange={(clientNumbers) => handleSearchFieldChange('clientNumbers', clientNumbers)}
        />
      </Column>

      {/* BGC Inputs (6 fields) */}
      <BgcSearchInputs
        values={searchParams}
        onFieldChange={(field, val) => handleSearchFieldChange(field, val)}
      />

      {/* Updated on date range */}
      <SearchDateRange
        startDate={searchParams?.updateDateStart}
        endDate={searchParams?.updateDateEnd}
        onStartDateChange={(date) => handleSearchFieldChange('updateDateStart', date)}
        onEndDateChange={(date) => handleSearchFieldChange('updateDateEnd', date)}
      />
    </Grid>
  );
};

export default StandardsUnitSearchInput;
