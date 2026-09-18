import { useEffect, useRef, useState } from 'react';
import { Column, Dropdown, Grid, InlineNotification, TextInput } from '@carbon/react';
import { useQuery } from '@tanstack/react-query';
import API from '@/services/API';
import { StockingStandardsSearchParams } from '@/types/ApiType';
import useRefWithSearchParam from '@/hooks/useRefWithSearchParam';
import { enforceNumberInputOnKeyDown, enforceNumberInputOnPaste } from '@/utils/InputUtils';
import { codeDescriptionToDisplayText } from '@/utils/multiSelectUtils';
import { getMultiSelectPlaceholderHelper, handleMultiSelectChangeHelper } from '@/utils/SearchUtils';
import { FSP_ID_MAX_LENGTH, SSID_MAX_LENGTH } from '@/constants';
import CustomMultiSelect from '@/components/CustomMultiSelect';
import ForestClientMultiSelect from '@/components/ForestClientMultiSelect';
import {
  BgcSearchInputs,
  MoreFiltersToggle,
  OrgUnitMultiSelect,
  SearchDateRange,
} from '@/components/common/SearchInput';

import './styles.scss';

type props = {
  searchParams?: StockingStandardsSearchParams;
  queryParams?: StockingStandardsSearchParams;
  handleSearchFieldChange: (field: keyof StockingStandardsSearchParams, value: unknown) => void;
};

const StockingStandardsSearchInput = ({ searchParams, queryParams, handleSearchFieldChange }: props) => {
  const [showMoreFilters, setShowMoreFilters] = useState<boolean>(false);
  const hasInitializedMoreFilters = useRef(false);

  // Auto-expand more filters on mount if URL params contain any hidden filter values
  useEffect(() => {
    if (hasInitializedMoreFilters.current) return;
    if (
      searchParams?.clientNumbers !== undefined ||
      searchParams?.orgUnits !== undefined ||
      searchParams?.fspId !== undefined
    ) {
      setShowMoreFilters(true);
      hasInitializedMoreFilters.current = true;
    }
  }, [searchParams]);

  const ssidInputRef = useRef<HTMLInputElement>(null);
  useRefWithSearchParam(ssidInputRef, searchParams?.standardsRegimeId);

  const fspIdInputRef = useRef<HTMLInputElement>(null);
  useRefWithSearchParam(fspIdInputRef, searchParams?.fspId);

  const preferredSpeciesQuery = useQuery({
    queryKey: ["codes", "silv-tree-species"],
    queryFn: API.CodesEndpointService.getSilvTreeSpeciesCodes,
  });

  const handleMultiSelectChange = (field: keyof StockingStandardsSearchParams) =>
    handleMultiSelectChangeHelper<StockingStandardsSearchParams>(field, handleSearchFieldChange);

  const showSsidOverrideWarning = !!(queryParams?.standardsRegimeId && (
    queryParams?.preferredSpecies?.length ||
    queryParams?.orgUnits?.length ||
    queryParams?.clientNumbers?.length ||
    queryParams?.fspId ||
    queryParams?.bgcZone ||
    queryParams?.bgcSubZone ||
    queryParams?.bgcVariant ||
    queryParams?.bgcPhase ||
    queryParams?.becSiteSeries ||
    queryParams?.becSiteType ||
    queryParams?.approvedDateStart ||
    queryParams?.approvedDateEnd
  ));

  return (
    <Grid className="default-search-input-grid">
      {
        showSsidOverrideWarning
          ? (
            <Column sm={4} md={8} lg={16}>
              <InlineNotification
                title="Stocking standards ID takes priority"
                subtitle="Other filters are ignored when a Stocking standards ID is provided."
                kind="warning"
                lowContrast
                className="inline-notification"
                hideCloseButton
                role="alert"
              />
            </Column>
          )
          : null
      }

      {/* Row 1: Stocking Standards ID + Preferred Species */}
      <Column sm={4} md={4} lg={6} max={4}>
        <TextInput
          ref={ssidInputRef}
          id="stocking-standards-id-input"
          name="stocking-standards-id"
          labelText="Stocking standards ID"
          placeholder="Enter SSID"
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
          onChange={handleMultiSelectChange('preferredSpecies')}
          selectedItems={preferredSpeciesQuery.data?.filter(data => searchParams?.preferredSpecies?.includes(data.code ?? '')) ?? []}
        />
      </Column>

      {/* Default indicator */}
      <Column sm={4} md={4} lg={6} max={4}>
        <Dropdown
          key={String(searchParams?.defaultStandardsInd)}
          id="default-standards-ind-dropdown"
          titleText="Default standards indicator"
          label="Choose an option"
          items={[
            ...(searchParams?.defaultStandardsInd !== undefined ? [{ id: '', label: '' }] : []),
            { id: 'yes', label: 'Yes' },
            { id: 'no', label: 'No' },
          ]}
          itemToString={(item) => item?.label ?? ''}
          itemToElement={(item) => item.id === '' ? <span className="empty-dropdown-option">Clear selected</span> : <span>{item.label}</span>}
          renderSelectedItem={(item) => item.id === '' ? <span className="empty-dropdown-option">Choose an option</span> : <span>{item.label}</span>}
          selectedItem={
            searchParams?.defaultStandardsInd === true
              ? { id: 'yes', label: 'Yes' }
              : searchParams?.defaultStandardsInd === false
                ? { id: 'no', label: 'No' }
                : { id: '', label: '' }
          }
          onChange={({ selectedItem }) =>
            handleSearchFieldChange('defaultStandardsInd', !selectedItem || selectedItem.id === '' ? undefined : selectedItem.id === 'yes')
          }
        />
      </Column>

      {/* Row 2: BGC fields */}
      <BgcSearchInputs
        values={searchParams}
        onFieldChange={(field, val) => handleSearchFieldChange(field, val)}
        wrapInRow
      />

      {/* Row 3: Approved date range */}
      <SearchDateRange
        label="Approved date range"
        labelHtmlFor="start-date-picker-input-id"
        startDate={searchParams?.approvedDateStart}
        endDate={searchParams?.approvedDateEnd}
        onStartDateChange={(date) => handleSearchFieldChange('approvedDateStart', date)}
        onEndDateChange={(date) => handleSearchFieldChange('approvedDateEnd', date)}
      />

      {/* Row 4: More/Fewer filters toggle */}
      <MoreFiltersToggle
        isExpanded={showMoreFilters}
        onToggle={setShowMoreFilters}
      />

      {/* Row 5: Extended filters */}
      {showMoreFilters ? (
        <>
          {/* Client */}
          <Column sm={4} md={4} lg={6} max={4}>
            <ForestClientMultiSelect
              selectedClientNumbers={searchParams?.clientNumbers}
              onChange={(clientNumbers) => handleSearchFieldChange('clientNumbers', clientNumbers)}
            />
          </Column>

          {/* Org Unit */}
          <OrgUnitMultiSelect
            selectedOrgUnits={searchParams?.orgUnits}
            onChange={(orgUnits) => handleSearchFieldChange('orgUnits', orgUnits)}
          />

          {/* FSP ID */}
          <Column sm={4} md={4} lg={6} max={4}>
            <TextInput
              ref={fspIdInputRef}
              id="fsp-id-input"
              name="fsp-id"
              labelText="FSP ID"
              placeholder="Enter FSP ID"
              defaultValue={searchParams?.fspId ?? ''}
              onBlur={(e) => handleSearchFieldChange('fspId', e.target.value ? e.target.value : undefined)}
              maxLength={FSP_ID_MAX_LENGTH}
            />
          </Column>
        </>
      ) : null}
    </Grid>
  );
};

export default StockingStandardsSearchInput;
