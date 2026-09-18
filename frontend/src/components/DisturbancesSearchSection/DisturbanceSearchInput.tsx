import { useEffect, useRef, useState } from 'react';
import { Column, Grid } from '@carbon/react';
import { useQuery } from '@tanstack/react-query';
import API from '@/services/API';
import { DisturbanceSearchParams } from '@/types/ApiType';
import { codeDescriptionToDisplayText } from '@/utils/multiSelectUtils';
import { getMultiSelectPlaceholderHelper, handleMultiSelectChangeHelper } from '@/utils/SearchUtils';
import CustomMultiSelect from '../CustomMultiSelect';
import ForestClientMultiSelect from '../ForestClientMultiSelect';
import {
  FileIdSearchInput,
  MoreFiltersToggle,
  OpeningCategoriesMultiSelect,
  OpeningStatusMultiSelect,
  OrgUnitMultiSelect,
  SearchDateRange,
} from '@/components/common/SearchInput';

type props = {
  searchParams?: DisturbanceSearchParams;
  handleSearchFieldChange: (field: keyof DisturbanceSearchParams, value: unknown) => void;
};

const DisturbanceSearchInput = ({ searchParams, handleSearchFieldChange }: props) => {
  const [showMoreFilters, setShowMoreFilters] = useState<boolean>(false);
  const hasInitializedMoreFilters = useRef(false);

  // Auto-expand more filters on mount if URL params contain any hidden filter values
  useEffect(() => {
    if (hasInitializedMoreFilters.current) return;
    if (
      searchParams?.fileId !== undefined ||
      searchParams?.clientNumbers !== undefined ||
      searchParams?.openingStatuses !== undefined
    ) {
      setShowMoreFilters(true);
      hasInitializedMoreFilters.current = true;
    }
  }, [searchParams]);

  const disturbanceCodeQuery = useQuery({
    queryKey: ['codes', 'disturbance'],
    queryFn: API.CodesEndpointService.getDisturbanceCodes,
  });

  const silvSystemCodeQuery = useQuery({
    queryKey: ['codes', 'silv-system'],
    queryFn: API.CodesEndpointService.getSilvSystemCodes,
  });

  const silvVariantCodeQuery = useQuery({
    queryKey: ['codes', 'silv-system-variant'],
    queryFn: API.CodesEndpointService.getSilvSystemVariantCodes,
  });

  const silvCutPhaseQuery = useQuery({
    queryKey: ['codes', 'silv-cut-phase'],
    queryFn: API.CodesEndpointService.getSilvCutPhaseCodes,
  });

  const handleMultiSelectChange = (field: keyof DisturbanceSearchParams) =>
    handleMultiSelectChangeHelper<DisturbanceSearchParams>(field, handleSearchFieldChange);

  return (
    <Grid className="default-search-input-grid">
      {/* Disturbance */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="disturbance-multiselect"
          className="default-search-multi-select"
          titleText="Disturbance code"
          placeholder={getMultiSelectPlaceholderHelper(searchParams?.disturbances)}
          items={disturbanceCodeQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('disturbances')}
          selectedItems={(disturbanceCodeQuery.data ?? []).filter((data) =>
            searchParams?.disturbances?.includes(data.code ?? '')
          )}
        />
      </Column>

      {/* Silviculture system */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="silv-system-multiselect"
          className="default-search-multi-select"
          titleText="Silviculture system"
          placeholder={getMultiSelectPlaceholderHelper(searchParams?.silvSystems)}
          items={silvSystemCodeQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('silvSystems')}
          selectedItems={(silvSystemCodeQuery.data ?? []).filter((data) =>
            searchParams?.silvSystems?.includes(data.code ?? '')
          )}
        />
      </Column>

      {/* Silviculture system variant */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="silv-system-variant-multiselect"
          className="default-search-multi-select"
          titleText="Variant"
          placeholder={getMultiSelectPlaceholderHelper(searchParams?.variants)}
          items={silvVariantCodeQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('variants')}
          selectedItems={(silvVariantCodeQuery.data ?? []).filter((data) =>
            searchParams?.variants?.includes(data.code ?? '')
          )}
        />
      </Column>

      {/* Cut phase */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="cut-phase-multiselect"
          className="default-search-multi-select"
          titleText="Cut phase"
          placeholder={getMultiSelectPlaceholderHelper(searchParams?.cutPhases)}
          items={silvCutPhaseQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('cutPhases')}
          selectedItems={(silvCutPhaseQuery.data ?? []).filter((data) =>
            searchParams?.cutPhases?.includes(data.code ?? '')
          )}
        />
      </Column>

      {/* Org Unit */}
      <OrgUnitMultiSelect
        type="district"
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
      >
        <MoreFiltersToggle
          isExpanded={showMoreFilters}
          onToggle={setShowMoreFilters}
        />

        {/* More filters */}
        {showMoreFilters ? (
          <>
            {/* File ID */}
            <FileIdSearchInput
              value={searchParams?.fileId}
              onChange={(fileId) => handleSearchFieldChange('fileId', fileId)}
            />

            {/* Client */}
            <Column sm={4} md={4} lg={6} max={4}>
              <ForestClientMultiSelect
                selectedClientNumbers={searchParams?.clientNumbers}
                onChange={(clientNumbers) => handleSearchFieldChange('clientNumbers', clientNumbers)}
              />
            </Column>

            {/* Opening status */}
            <OpeningStatusMultiSelect
              selectedStatuses={searchParams?.openingStatuses}
              onChange={(statuses) => handleSearchFieldChange('openingStatuses', statuses)}
            />
          </>
        ) : null}
      </SearchDateRange>
    </Grid>
  );
};

export default DisturbanceSearchInput;
