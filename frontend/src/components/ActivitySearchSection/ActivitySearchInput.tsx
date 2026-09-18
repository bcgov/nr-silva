import { useEffect, useRef, useState } from 'react';
import { Column, Dropdown, Grid, TextInput } from '@carbon/react';
import { useQuery } from '@tanstack/react-query';
import API from '@/services/API';
import { ActivitySearchParams } from '@/types/ApiType';
import useRefWithSearchParam from '@/hooks/useRefWithSearchParam';
import { handleAutoUpperInput, handleAutoUpperPaste } from '@/utils/InputUtils';
import { codeDescriptionToDisplayText } from '@/utils/multiSelectUtils';
import { getMultiSelectPlaceholderHelper, handleMultiSelectChangeHelper } from '@/utils/SearchUtils';
import { INTRA_AGENCY_NUMBER_MAX_LENGTH } from '@/constants';
import { ActivityStatusTag } from '../Tags';
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

import './styles.scss';

interface ActivityStatusDropdownItem {
  id: string;
  label: string;
}

const renderActivityStatusItem = (item: ActivityStatusDropdownItem) =>
  item.id === '' ? (
    <span className="empty-dropdown-option">Clear selected</span>
  ) : (
    <ActivityStatusTag isComplete={item.id === 'complete'} />
  );

const renderActivityStatusSelectedItem = (item: ActivityStatusDropdownItem) =>
  item.id === '' ? (
    <span className="empty-dropdown-option">Choose an option</span>
  ) : (
    <ActivityStatusTag isComplete={item.id === 'complete'} />
  );

const getActivityStatusSelectedItem = (isComplete?: boolean): ActivityStatusDropdownItem => {
  if (isComplete === true) {
    return { id: 'complete', label: 'Complete' };
  }
  if (isComplete === false) {
    return { id: 'planned', label: 'Planned' };
  }
  return { id: '', label: '' };
};

type props = {
  searchParams?: ActivitySearchParams;
  handleSearchFieldChange: (field: keyof ActivitySearchParams, value: unknown) => void;
};

const ActivitySearchInput = ({ searchParams, handleSearchFieldChange }: props) => {
  const [showMoreFilters, setShowMoreFilters] = useState<boolean>(false);
  const hasInitializedMoreFilters = useRef(false);

  // Auto-expand more filters on mount if URL params contain any hidden filter values
  useEffect(() => {
    if (hasInitializedMoreFilters.current) return;
    if (
      searchParams?.fileId !== undefined ||
      searchParams?.clientNumbers !== undefined ||
      searchParams?.openingStatuses !== undefined ||
      searchParams?.intraAgencyNumber !== undefined
    ) {
      setShowMoreFilters(true);
      hasInitializedMoreFilters.current = true;
    }
  }, [searchParams]);

  const intraAgencyNumberInputRef = useRef<HTMLInputElement>(null);
  useRefWithSearchParam(intraAgencyNumberInputRef, searchParams?.intraAgencyNumber);

  const silvBaseCodeQuery = useQuery({
    queryKey: ['codes', 'silv-base'],
    queryFn: API.CodesEndpointService.getSilvBaseCodes,
    select: (data) => data.filter((code) => code.code !== 'DN'), // Code 'DN' is considered as a disturbance
  });

  const silvTechniqueCodeQuery = useQuery({
    queryKey: ['codes', 'silv-technique'],
    queryFn: API.CodesEndpointService.getSilvTechniqueCodes,
  });

  const silvMethodCodeQuery = useQuery({
    queryKey: ['codes', 'silv-method'],
    queryFn: API.CodesEndpointService.getSilvMethodCodes,
  });

  const silvObjectiveCodeQuery = useQuery({
    queryKey: ['codes', 'silv-objective'],
    queryFn: API.CodesEndpointService.getSilvObjectiveCodes,
  });

  const silvFundSourceCodeQuery = useQuery({
    queryKey: ['codes', 'silv-fund-source'],
    queryFn: API.CodesEndpointService.getSilvFundSourceCodes,
  });

  const handleMultiSelectChange = (field: keyof ActivitySearchParams) =>
    handleMultiSelectChangeHelper<ActivitySearchParams>(field, handleSearchFieldChange);

  return (
    <Grid className="default-search-input-grid">
      {/* Base */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="base-multiselect"
          className="default-search-multi-select"
          titleText="Base"
          placeholder={getMultiSelectPlaceholderHelper(searchParams?.bases)}
          items={silvBaseCodeQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('bases')}
          selectedItems={(silvBaseCodeQuery.data ?? []).filter((data) =>
            searchParams?.bases?.includes(data.code ?? '')
          )}
        />
      </Column>

      {/* Technique */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="technique-multiselect"
          className="default-search-multi-select"
          titleText="Technique"
          placeholder={getMultiSelectPlaceholderHelper(searchParams?.techniques)}
          items={silvTechniqueCodeQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('techniques')}
          selectedItems={(silvTechniqueCodeQuery.data ?? []).filter((data) =>
            searchParams?.techniques?.includes(data.code ?? '')
          )}
        />
      </Column>

      {/* Method */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="method-multiselect"
          className="default-search-multi-select"
          titleText="Method"
          placeholder={getMultiSelectPlaceholderHelper(searchParams?.methods)}
          items={silvMethodCodeQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('methods')}
          selectedItems={(silvMethodCodeQuery.data ?? []).filter((data) =>
            searchParams?.methods?.includes(data.code ?? '')
          )}
        />
      </Column>

      {/* Activity Status */}
      <Column sm={4} md={4} lg={6} max={4}>
        <Dropdown
          key={String(searchParams?.isComplete)}
          id="activity-status-dropdown"
          titleText="Activity status"
          label="Choose an option"
          items={[
            ...(searchParams?.isComplete !== undefined ? [{ id: '', label: '' }] : []),
            { id: 'complete', label: 'Complete' },
            { id: 'planned', label: 'Planned' },
          ]}
          itemToElement={renderActivityStatusItem}
          renderSelectedItem={renderActivityStatusSelectedItem}
          onChange={({ selectedItem }) =>
            handleSearchFieldChange(
              'isComplete',
              !selectedItem || selectedItem.id === '' ? undefined : selectedItem.id === 'complete'
            )
          }
          selectedItem={getActivityStatusSelectedItem(searchParams?.isComplete)}
        />
      </Column>

      {/* Objectives */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="objective-multiselect"
          className="default-search-multi-select"
          titleText="Objectives"
          placeholder={getMultiSelectPlaceholderHelper(searchParams?.objectives)}
          items={silvObjectiveCodeQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('objectives')}
          selectedItems={(silvObjectiveCodeQuery.data ?? []).filter((data) =>
            searchParams?.objectives?.includes(data.code ?? '')
          )}
        />
      </Column>

      {/* Funding Source */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="fund-source-multiselect"
          className="default-search-multi-select"
          titleText="Funding source"
          placeholder={getMultiSelectPlaceholderHelper(searchParams?.fundingSources)}
          items={silvFundSourceCodeQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('fundingSources')}
          selectedItems={(silvFundSourceCodeQuery.data ?? []).filter((data) =>
            searchParams?.fundingSources?.includes(data.code ?? '')
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

            {/* Inter-agency number */}
            <Column sm={4} md={4} lg={6} max={4}>
              <TextInput
                ref={intraAgencyNumberInputRef}
                id="inter-agency-number-input"
                name="inter-agency-number"
                labelText="Inter-agency number"
                placeholder="Enter inter-agency number"
                defaultValue={searchParams?.intraAgencyNumber ?? ''}
                onInput={(e) => handleAutoUpperInput(e, INTRA_AGENCY_NUMBER_MAX_LENGTH, true)}
                onPaste={(e) => handleAutoUpperPaste(e, INTRA_AGENCY_NUMBER_MAX_LENGTH, true)}
                onBlur={(e) => handleSearchFieldChange('intraAgencyNumber', e.target.value ? e.target.value : undefined)}
              />
            </Column>
          </>
        ) : null}
      </SearchDateRange>
    </Grid>
  );
};

export default ActivitySearchInput;
