
import { useRef, useEffect, useState } from 'react';
import { DateTime } from 'luxon';
import { getDatePickerValue, getEndMinDate, getStartMaxDate } from '@/utils/DateUtils';
import { ChevronDown, ChevronUp } from '@carbon/icons-react';
import { Button, Column, DatePicker, DatePickerInput, Grid, TextInput } from '@carbon/react';
import API from '@/services/API';
import { DisturbanceSearchParams } from '@/types/ApiType';
import useRefWithSearchParam from '@/hooks/useRefWithSearchParam';
import { getMultiSelectedCodes, handleAutoUpperInput, handleAutoUpperPaste } from '@/utils/InputUtils';
import { CodeDescriptionDto } from '@/services/OpenApi';
import { codeDescriptionToDisplayText } from '@/utils/multiSelectUtils';
import { useQuery } from '@tanstack/react-query';
import { API_DATE_FORMAT, DATE_PICKER_FORMAT, OPENING_STATUS_LIST } from '@/constants';
import { FILE_ID_MAX_LENGTH } from '@/constants';
import CustomMultiSelect from '../CustomMultiSelect';
import ForestClientMultiSelect from '../ForestClientMultiSelect';


type props = {
  searchParams?: DisturbanceSearchParams;
  handleSearchFieldChange: (field: keyof DisturbanceSearchParams, value: unknown) => void;
}

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

  const fileIdInputRef = useRef<HTMLInputElement>(null); // VARCHAR2(10)
  useRefWithSearchParam(fileIdInputRef, searchParams?.fileId);

  const disturbanceCodeQuery = useQuery({
    queryKey: ['codes', 'disturbance'],
    queryFn: API.CodesEndpointService.getDisturbanceCodes,
  });

  const silvSystemCodeQuery = useQuery({
    queryKey: ['codes', 'silv-system'],
    queryFn: API.CodesEndpointService.getSilvSystemCodes
  });

  const silvVariantCodeQuery = useQuery({
    queryKey: ['codes', 'silv-system-variant'],
    queryFn: API.CodesEndpointService.getSilvSystemVariantCodes
  });

  const silvCutPhaseQuery = useQuery({
    queryKey: ['codes', 'silv-cut-phase'],
    queryFn: API.CodesEndpointService.getSilvCutPhaseCodes
  });

  const orgUnitQuery = useQuery({
    queryKey: ["codes", "org-units"],
    queryFn: API.CodesEndpointService.getOpeningOrgUnits
  });

  const categoryQuery = useQuery({
    queryKey: ["codes", "opening-categories"],
    queryFn: () => API.CodesEndpointService.getOpeningCategories(),
  });

  const handleMultiSelectChange = (field: keyof DisturbanceSearchParams) => (selected: { selectedItems: CodeDescriptionDto[] }) => {
    const selectedCodes = getMultiSelectedCodes(selected);
    handleSearchFieldChange(field, selectedCodes.length > 0 ? selectedCodes : undefined);
  };

  const getMultiSelectPlaceholder = (field: keyof DisturbanceSearchParams, defaultText = 'Choose one or more options') => {
    const values = searchParams?.[field] as unknown as string[] | undefined;
    return values && values.length > 0 ? values.join(', ') : defaultText;
  };

  const handleDateChange = (isStartDate: boolean) => (dates?: Date[]) => {
    if (!dates) return;

    const formattedDate =
      dates.length && dates[0]
        ? DateTime.fromJSDate(dates[0]).toFormat(API_DATE_FORMAT)
        : "";

    handleSearchFieldChange(
      isStartDate ? "updateDateStart" : "updateDateEnd",
      formattedDate ? formattedDate : undefined
    );
  };

  return (
    <Grid className="default-search-input-grid">
      {/* Disturbance */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="disturbance-multiselect"
          className="default-search-multi-select"
          titleText="Disturbance code"
          placeholder={getMultiSelectPlaceholder('disturbances')}
          items={disturbanceCodeQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('disturbances')}
          selectedItems={(disturbanceCodeQuery.data ?? []).filter(data => searchParams?.disturbances?.includes(data.code ?? ''))}
        />
      </Column>

      {/* Silviculture system */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="silv-system-multiselect"
          className="default-search-multi-select"
          titleText="Silviculture system"
          placeholder={getMultiSelectPlaceholder('silvSystems')}
          items={silvSystemCodeQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('silvSystems')}
          selectedItems={(silvSystemCodeQuery.data ?? []).filter(data => searchParams?.silvSystems?.includes(data.code ?? ''))}
        />
      </Column>

      {/* Silviculture system variant */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="silv-system-variant-multiselect"
          className="default-search-multi-select"
          titleText="Variant"
          placeholder={getMultiSelectPlaceholder('variants')}
          items={silvVariantCodeQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('variants')}
          selectedItems={(silvVariantCodeQuery.data ?? []).filter(data => searchParams?.variants?.includes(data.code ?? ''))}
        />
      </Column>

      {/* Cut phase */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="cut-phase-multiselect"
          className="default-search-multi-select"
          titleText="Cut phase"
          placeholder={getMultiSelectPlaceholder('cutPhases')}
          items={silvCutPhaseQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('cutPhases')}
          selectedItems={(silvCutPhaseQuery.data ?? []).filter(data => searchParams?.cutPhases?.includes(data.code ?? ''))}
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

      {/* Opening Categories */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          placeholder={getMultiSelectPlaceholder('openingCategories')}
          titleText="Opening category"
          id="category-multi-select"
          className="default-search-multi-select"
          items={categoryQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('openingCategories')}
          selectedItems={categoryQuery.data?.filter(data => searchParams?.openingCategories?.includes(data.code ?? '')) ?? []}
        />
      </Column>

      {/* Updated on date range */}
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

          <Column sm={4} md={8} lg={16}>
            <Button
              type="button"
              renderIcon={showMoreFilters ? ChevronUp : ChevronDown}
              title={`${showMoreFilters ? 'Fewer' : 'More'} filters`}
              kind="tertiary"
              onClick={() => setShowMoreFilters(prev => !prev)}
            >
              {showMoreFilters ? 'Fewer filters' : 'More filters'}
            </Button>
          </Column>

          {/* More filters */}
          {
            showMoreFilters
              ? (
                <>
                  {/* File ID */}
                  <Column sm={4} md={4} lg={6} max={4}>
                    <TextInput
                      ref={fileIdInputRef}
                      id="file-id-input"
                      name="file-id"
                      labelText="File ID"
                      placeholder="Enter file ID"
                      defaultValue={searchParams?.fileId ?? ''}
                      onInput={(e) => handleAutoUpperInput(e, FILE_ID_MAX_LENGTH)}
                      onPaste={(e) => handleAutoUpperPaste(e, FILE_ID_MAX_LENGTH)}
                      onBlur={(e) => handleSearchFieldChange('fileId', e.target.value ? e.target.value : undefined)}
                    />
                  </Column>

                  {/* Client */}
                  <Column sm={4} md={4} lg={6} max={4}>
                    <ForestClientMultiSelect
                      selectedClientNumbers={searchParams?.clientNumbers}
                      onChange={(clientNumbers) => handleSearchFieldChange('clientNumbers', clientNumbers)}
                    />
                  </Column>

                  {/* Opening status */}
                  <Column sm={4} md={4} lg={6} max={4}>
                    <CustomMultiSelect
                      id="status-multiselect"
                      className="default-search-multi-select"
                      titleText="Opening status"
                      placeholder={getMultiSelectPlaceholder('openingStatuses')}
                      items={OPENING_STATUS_LIST}
                      itemToString={codeDescriptionToDisplayText}
                      onChange={handleMultiSelectChange('openingStatuses')}
                      selectedItems={OPENING_STATUS_LIST.filter(data => searchParams?.openingStatuses?.includes(data.code ?? '')) ?? []}
                    />
                  </Column>

                </>
              )
              : null
          }
        </Grid>
      </Column>
    </Grid>
  );
};

export default DisturbanceSearchInput;
