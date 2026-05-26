import { useRef, useEffect, useState } from 'react';
import { DateTime } from 'luxon';
import { getDatePickerValue, getEndMinDate, getStartMaxDate } from '@/utils/DateUtils';
import { ChevronDown, ChevronUp } from '@carbon/icons-react';
import { Button, Column, DatePicker, DatePickerInput, Grid, InlineNotification, TextInput } from '@carbon/react';
import API from '@/services/API';
import { StockingStandardsSearchParams } from '@/types/ApiType';
import useRefWithSearchParam from '@/hooks/useRefWithSearchParam';
import { enforceNumberInputOnKeyDown, enforceNumberInputOnPaste, getMultiSelectedCodes, handleAutoUpperInput, handleAutoUpperPaste } from '@/utils/InputUtils';
import { CodeDescriptionDto } from '@/services/OpenApi';
import { codeDescriptionToDisplayText } from '@/utils/multiSelectUtils';
import { useQuery } from '@tanstack/react-query';
import {
  API_DATE_FORMAT, BEC_SITE_PHASE_MAX_LENGTH, BEC_SITE_SERIES_MAX_LENGTH,
  BGC_PHASE_MAX_LENGTH, BGC_SUBZONE_MAX_LENGTH, BGC_VARIANT_MAX_LENGTH, BGC_ZONE_MAX_LENGTH,
  DATE_PICKER_FORMAT, FSP_ID_MAX_LENGTH, SSID_MAX_LENGTH
} from '@/constants';
import CustomMultiSelect from '@/components/CustomMultiSelect';
import ForestClientMultiSelect from '@/components/ForestClientMultiSelect';

import './styles.scss';

type props = {
  searchParams?: StockingStandardsSearchParams;
  queryParams?: StockingStandardsSearchParams;
  handleSearchFieldChange: (field: keyof StockingStandardsSearchParams, value: unknown) => void;
}

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

  const bgcZoneInputRef = useRef<HTMLInputElement>(null);
  useRefWithSearchParam(bgcZoneInputRef, searchParams?.bgcZone);

  const bgcSubZoneInputRef = useRef<HTMLInputElement>(null);
  useRefWithSearchParam(bgcSubZoneInputRef, searchParams?.bgcSubZone);

  const bgcVariantInputRef = useRef<HTMLInputElement>(null);
  useRefWithSearchParam(bgcVariantInputRef, searchParams?.bgcVariant);

  const bgcPhaseInputRef = useRef<HTMLInputElement>(null);
  useRefWithSearchParam(bgcPhaseInputRef, searchParams?.bgcPhase);

  const becSiteSeriesInputRef = useRef<HTMLInputElement>(null);
  useRefWithSearchParam(becSiteSeriesInputRef, searchParams?.becSiteSeries);

  // Site Type == Site Phase
  const becSitePhaseInputRef = useRef<HTMLInputElement>(null);
  useRefWithSearchParam(becSitePhaseInputRef, searchParams?.becSiteType);

  const fspIdInputRef = useRef<HTMLInputElement>(null);
  useRefWithSearchParam(fspIdInputRef, searchParams?.fspId);

  const preferredSpeciesQuery = useQuery({
    queryKey: ["codes", "silv-tree-species"],
    queryFn: API.CodesEndpointService.getSilvTreeSpeciesCodes,
  });

  const orgUnitQuery = useQuery({
    queryKey: ["codes", "org-units"],
    queryFn: API.CodesEndpointService.getOpeningOrgUnits,
  });

  const handleMultiSelectChange = (field: keyof StockingStandardsSearchParams) => (selected: { selectedItems: CodeDescriptionDto[] }) => {
    const selectedCodes = getMultiSelectedCodes(selected);
    handleSearchFieldChange(field, selectedCodes.length > 0 ? selectedCodes : undefined);
  };

  const getMultiSelectPlaceholder = (field: keyof StockingStandardsSearchParams, defaultText = 'Choose one or more options') => {
    const values = searchParams?.[field] as unknown as string[] | undefined;
    return values && values.length > 0 ? values.join(', ') : defaultText;
  };

  const handleDateChange = (isStartDate: boolean) => (dates?: Date[]) => {
    if (!dates) return;

    const formattedDate =
      dates.length && dates[0]
        ? DateTime.fromJSDate(dates[0]).toFormat(API_DATE_FORMAT)
        : undefined;

    handleSearchFieldChange(
      isStartDate ? "updateDateStart" : "updateDateEnd",
      formattedDate
    );
  };

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
    queryParams?.updateDateStart ||
    queryParams?.updateDateEnd
  ));

  return (
    <Grid className="default-search-input-grid">
      {
        showSsidOverrideWarning
          ? (
            <Column sm={4} md={8} lg={16}>
              <InlineNotification
                title="Standards ID takes priority"
                subtitle="Other filters are ignored when a Standards ID is provided."
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

      {/* Row 1: Standards ID + Preferred Species */}
      <Column sm={4} md={4} lg={6} max={4}>
        <TextInput
          ref={ssidInputRef}
          id="standards-id-input"
          name="standards-id"
          labelText="Standards ID"
          placeholder="Enter standards ID"
          onBlur={(e) => handleSearchFieldChange('standardsRegimeId', e.target.value ? Number(e.target.value) : undefined)}
          onKeyDown={(e) => enforceNumberInputOnKeyDown(e, SSID_MAX_LENGTH)}
          onPaste={(e) => enforceNumberInputOnPaste(ssidInputRef.current, e, SSID_MAX_LENGTH)}
        />
      </Column>

      {/* Preferred Species */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          placeholder={getMultiSelectPlaceholder('preferredSpecies')}
          titleText="Preferred species"
          id="preferred-species-multi-select"
          className="default-search-multi-select"
          items={preferredSpeciesQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('preferredSpecies')}
          selectedItems={preferredSpeciesQuery.data?.filter(data => searchParams?.preferredSpecies?.includes(data.code ?? '')) ?? []}
        />
      </Column>

      {/* Row 2: BGC fields — wrapped in full-width column to always start on own row */}
      <Column sm={4} md={8} lg={16}>
        <Grid className="default-search-input-grid">
          {/* BGC Zone */}
          <Column sm={4} md={4} lg={6} max={3}>
            <TextInput
              ref={bgcZoneInputRef}
              id="bgc-zone-input"
              name="bgc-zone"
              labelText="BGC zone"
              placeholder="Enter BGC zone"
              onInput={(e) => handleAutoUpperInput(e, BGC_ZONE_MAX_LENGTH)}
              onPaste={(e) => handleAutoUpperPaste(e, BGC_ZONE_MAX_LENGTH)}
              onBlur={(e) => handleSearchFieldChange('bgcZone', e.target.value ? e.target.value : undefined)}
            />
          </Column>

          {/* BGC Sub Zone */}
          <Column sm={4} md={4} lg={6} max={3}>
            <TextInput
              ref={bgcSubZoneInputRef}
              id="bgc-sub-zone-input"
              name="bgc-sub-zone"
              labelText="BGC sub zone"
              placeholder="Enter BGC sub zone"
              onBlur={(e) => handleSearchFieldChange('bgcSubZone', e.target.value ? e.target.value : undefined)}
              maxLength={BGC_SUBZONE_MAX_LENGTH}
            />
          </Column>

          {/* Variant */}
          <Column sm={4} md={4} lg={6} max={2}>
            <TextInput
              ref={bgcVariantInputRef}
              id="bgc-variant-input"
              name="bgc-variant"
              labelText="Variant"
              placeholder="Enter variant"
              onBlur={(e) => handleSearchFieldChange('bgcVariant', e.target.value ? e.target.value : undefined)}
              maxLength={BGC_VARIANT_MAX_LENGTH}
            />
          </Column>

          {/* Phase */}
          <Column sm={4} md={4} lg={6} max={2}>
            <TextInput
              ref={bgcPhaseInputRef}
              id="bgc-phase-input"
              name="bgc-phase"
              labelText="Phase"
              placeholder="Enter phase"
              onBlur={(e) => handleSearchFieldChange('bgcPhase', e.target.value ? e.target.value : undefined)}
              maxLength={BGC_PHASE_MAX_LENGTH}
            />
          </Column>

          {/* Site Series */}
          <Column sm={4} md={4} lg={6} max={3}>
            <TextInput
              ref={becSiteSeriesInputRef}
              id="bec-site-series-input"
              name="bec-site-series"
              labelText="Site series"
              placeholder="Enter site series"
              onBlur={(e) => handleSearchFieldChange('becSiteSeries', e.target.value ? e.target.value : undefined)}
              maxLength={BEC_SITE_SERIES_MAX_LENGTH}
            />
          </Column>

          {/* Site Phase */}
          <Column sm={4} md={4} lg={6} max={3}>
            <TextInput
              ref={becSitePhaseInputRef}
              id="bec-site-phase-input"
              name="bec-site-phase"
              labelText="Site phase"
              placeholder="Enter site phase"
              onBlur={(e) => handleSearchFieldChange('becSiteType', e.target.value ? e.target.value : undefined)}
              maxLength={BEC_SITE_PHASE_MAX_LENGTH}
            />
          </Column>
        </Grid>
      </Column>

      {/* Row 3: Last updated date range */}
      <Column sm={4} md={8} lg={16} className="default-search-date-col">
        <label className="date-label" htmlFor="last-updated-date-range">Last updated date range</label>

        <Grid className="date-sub-grid">
          {/* Start date */}
          <Column sm={4} md={4} lg={6} max={4}>
            <DatePicker
              className="advanced-date-picker"
              datePickerType="single"
              dateFormat="Y/m/d"
              allowInput
              maxDate={getStartMaxDate(searchParams?.updateDateEnd)}
              onChange={handleDateChange(true)}
              value={getDatePickerValue(searchParams?.updateDateStart)}
            >
              <DatePickerInput
                id="start-date-picker-input-id"
                size="md"
                labelText="Start Date"
                placeholder="yyyy/mm/dd"
              />
            </DatePicker>
          </Column>

          {/* End date */}
          <Column sm={4} md={4} lg={6} max={4}>
            <DatePicker
              className="advanced-date-picker"
              datePickerType="single"
              dateFormat="Y/m/d"
              allowInput
              minDate={getEndMinDate(searchParams?.updateDateStart)}
              maxDate={DateTime.now().toFormat(DATE_PICKER_FORMAT)}
              onChange={handleDateChange(false)}
              value={getDatePickerValue(searchParams?.updateDateEnd)}
            >
              <DatePickerInput
                id="end-date-picker-input-id"
                size="md"
                labelText="End Date"
                placeholder="yyyy/mm/dd"
              />
            </DatePicker>
          </Column>
        </Grid>
      </Column>

      {/* Row 4: More/Fewer filters toggle */}
      <Column sm={4} md={8} lg={16}>
        <Button
          type="button"
          kind="tertiary"
          renderIcon={showMoreFilters ? ChevronUp : ChevronDown}
          title={`${showMoreFilters ? 'Fewer' : 'More'} filters`}
          onClick={() => setShowMoreFilters((prev) => !prev)}
        >
          {showMoreFilters ? 'Fewer filters' : 'More filters'}
        </Button>
      </Column>

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
          <Column sm={4} md={4} lg={6} max={4}>
            <CustomMultiSelect
              id="org-unit-multiselect"
              className="default-search-multi-select"
              titleText="Org unit"
              placeholder={getMultiSelectPlaceholder('orgUnits')}
              items={orgUnitQuery.data ?? []}
              itemToString={codeDescriptionToDisplayText}
              onChange={handleMultiSelectChange('orgUnits')}
              selectedItems={(orgUnitQuery.data ?? []).filter(data => searchParams?.orgUnits?.includes(data.code ?? ''))}
            />
          </Column>

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
