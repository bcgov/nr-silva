import { useRef } from 'react';
import { Column, Grid, TextInput } from '@carbon/react';
import { useQuery } from '@tanstack/react-query';
import API from '@/services/API';
import { ForestCoverSearchParams } from '@/types/ApiType';
import useRefWithSearchParam from '@/hooks/useRefWithSearchParam';
import { enforceNumberInputOnKeyDown, enforceNumberInputOnPaste, getMultiSelectedCodes } from '@/utils/InputUtils';
import { codeDescriptionToDisplayText } from '@/utils/multiSelectUtils';
import { getMultiSelectPlaceholderHelper, handleMultiSelectChangeHelper } from '@/utils/SearchUtils';
import { OPENING_ID_MAX_LENGTH } from '@/constants';
import CustomMultiSelect from '../CustomMultiSelect';
import {
  FileIdSearchInput,
  OpeningCategoriesMultiSelect,
  OpeningStatusMultiSelect,
  OrgUnitMultiSelect,
  SearchDateRange,
} from '@/components/common/SearchInput';

import './styles.scss';

type props = {
  searchParams?: ForestCoverSearchParams;
  handleSearchFieldChange: (field: keyof ForestCoverSearchParams, value: unknown) => void;
};

const ForestCoverSearchInput = ({ searchParams, handleSearchFieldChange }: props) => {
  const openingIdInputRef = useRef<HTMLInputElement>(null);

  const stockingTypeQuery = useQuery({
    queryKey: ["codes", "stocking-type"],
    queryFn: () => API.CodesEndpointService.getStockingTypeCodes(),
  });

  const stockingStatusQuery = useQuery({
    queryKey: ["codes", "stocking-status"],
    queryFn: () => API.CodesEndpointService.getStockingStatusCodes(),
  });

  const silvDamageAgentQuery = useQuery({
    queryKey: ["codes", "silv-damage-agent"],
    queryFn: () => API.CodesEndpointService.getSilvDamageAgentCodes(),
  });

  const handleMultiSelectChange = (field: keyof ForestCoverSearchParams) =>
    handleMultiSelectChangeHelper<ForestCoverSearchParams>(field, handleSearchFieldChange);

  const getStockingTypePlaceholder = () => {
    const values = searchParams?.stockingTypes;
    const mapped = values?.map((v) => (v.trim() === '' ? 'N/A' : v));
    return getMultiSelectPlaceholderHelper(mapped);
  };

  return (
    <Grid className="default-search-input-grid">
      {/* Stocking Statuses */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="stocking-status-multiselect"
          className="default-search-multi-select"
          titleText="Stocking status"
          placeholder={getMultiSelectPlaceholderHelper(searchParams?.stockingStatuses)}
          items={stockingStatusQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('stockingStatuses')}
          selectedItems={(stockingStatusQuery.data ?? []).filter((data) =>
            searchParams?.stockingStatuses?.includes(data.code ?? '')
          )}
        />
      </Column>

      {/* Stocking type */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="stocking-type-multiselect"
          className="default-search-multi-select"
          titleText="Stocking type"
          placeholder={getStockingTypePlaceholder()}
          items={(stockingTypeQuery.data ?? []).map((data) =>
            data.code?.trim() === '' ? { ...data, code: 'N/A' } : data
          )}
          itemToString={codeDescriptionToDisplayText}
          onChange={(selected) => {
            const selectedCodes = getMultiSelectedCodes(selected).map((code) =>
              code === 'N/A' ? ' ' : code
            );
            handleSearchFieldChange('stockingTypes', selectedCodes.length > 0 ? selectedCodes : undefined);
          }}
          selectedItems={(stockingTypeQuery.data ?? [])
            .filter((data) => searchParams?.stockingTypes?.includes(data.code ?? ''))
            .map((data) => (data.code?.trim() === '' ? { ...data, code: 'N/A' } : data))}
        />
      </Column>

      {/* Damaging agent type */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="damage-agent-type-multiselect"
          className="default-search-multi-select"
          titleText="Damaging agent type"
          placeholder={getMultiSelectPlaceholderHelper(searchParams?.damageAgents)}
          items={silvDamageAgentQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('damageAgents')}
          selectedItems={(silvDamageAgentQuery.data ?? []).filter((data) =>
            searchParams?.damageAgents?.includes(data.code ?? '')
          )}
        />
      </Column>

      {/* Opening Statuses */}
      <OpeningStatusMultiSelect
        id="opening-status-multiselect"
        selectedStatuses={searchParams?.openingStatuses}
        onChange={(statuses) => handleSearchFieldChange('openingStatuses', statuses)}
      />

      {/* Opening ID */}
      <Column sm={4} md={4} lg={6} max={4}>
        <TextInput
          ref={openingIdInputRef}
          id="opening-id-input"
          name="opening-id"
          labelText="Opening ID"
          placeholder="Enter opening ID"
          onBlur={(e) => handleSearchFieldChange('openingId', e.target.value ? Number(e.target.value) : undefined)}
          onKeyDown={(e) => enforceNumberInputOnKeyDown(e, OPENING_ID_MAX_LENGTH)}
          onPaste={(e) => enforceNumberInputOnPaste(openingIdInputRef.current, e, OPENING_ID_MAX_LENGTH)}
        />
      </Column>

      {/* File ID */}
      <FileIdSearchInput
        value={searchParams?.fileId}
        onChange={(fileId) => handleSearchFieldChange('fileId', fileId)}
      />

      {/* Org Unit */}
      <OrgUnitMultiSelect
        selectedOrgUnits={searchParams?.orgUnits}
        onChange={(orgUnits) => handleSearchFieldChange('orgUnits', orgUnits)}
      />

      {/* Opening Categories */}
      <OpeningCategoriesMultiSelect
        selectedCategories={searchParams?.openingCategories}
        onChange={(cats) => handleSearchFieldChange('openingCategories', cats)}
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

export default ForestCoverSearchInput;
