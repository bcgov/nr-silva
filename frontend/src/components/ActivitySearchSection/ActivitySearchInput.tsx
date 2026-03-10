
import { useRef, useEffect, useState } from 'react';
import { DateTime } from 'luxon';
import { getDatePickerValue, getEndMinDate, getStartMaxDate } from '@/utils/DateUtils';
import { ChevronDown, ChevronUp } from '@carbon/icons-react';
import { Button, Column, DatePicker, DatePickerInput, Dropdown, Grid, TextInput } from '@carbon/react';
import API from '@/services/API';
import { ActivitySearchParams } from '@/types/ApiType';
import useRefWithSearchParam from '@/hooks/useRefWithSearchParam';
import { getMultiSelectedCodes, handleAutoUpperInput, handleAutoUpperPaste } from '@/utils/InputUtils';
import { CodeDescriptionDto } from '@/services/OpenApi';
import { codeDescriptionToDisplayText } from '@/utils/multiSelectUtils';
import { useQuery } from '@tanstack/react-query';
import { API_DATE_FORMAT, DATE_PICKER_FORMAT, INTRA_AGENCY_NUMBER_MAX_LENGTH, OPENING_STATUS_LIST } from '@/constants';
import { FILE_ID_MAX_LENGTH } from '@/constants';
import { ActivityStatusTag } from '../Tags';
import CustomMultiSelect from '../CustomMultiSelect';
import ForestClientMultiSelect from '../ForestClientMultiSelect';

import './styles.scss';

type props = {
  searchParams?: ActivitySearchParams;
  handleSearchFieldChange: (field: keyof ActivitySearchParams, value: unknown) => void;
}

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

  const fileIdInputRef = useRef<HTMLInputElement>(null); // VARCHAR2(10)
  useRefWithSearchParam(fileIdInputRef, searchParams?.fileId);

  const intraAgencyNumberInputRef = useRef<HTMLInputElement>(null); // VARCHAR2(10)
  useRefWithSearchParam(intraAgencyNumberInputRef, searchParams?.intraAgencyNumber);

  const silvBaseCodeQuery = useQuery({
    queryKey: ['codes', 'silv-base'],
    queryFn: API.CodesEndpointService.getSilvBaseCodes,
    select: (data) => data.filter((code) => code.code !== 'DN') // Code 'DN' is considered as a disturbance
  });

  const silvTechniqueCodeQuery = useQuery({
    queryKey: ['codes', 'silv-technique'],
    queryFn: API.CodesEndpointService.getSilvTechniqueCodes
  });

  const silvMethodCodeQuery = useQuery({
    queryKey: ['codes', 'silv-method'],
    queryFn: API.CodesEndpointService.getSilvMethodCodes
  });

  const silvObjectiveCodeQuery = useQuery({
    queryKey: ['codes', 'silv-objective'],
    queryFn: API.CodesEndpointService.getSilvObjectiveCodes
  });

  const silvFundSourceCodeQuery = useQuery({
    queryKey: ['codes', 'silv-fund-source'],
    queryFn: API.CodesEndpointService.getSilvFundSourceCodes
  });

  const orgUnitQuery = useQuery({
    queryKey: ["codes", "org-units"],
    queryFn: API.CodesEndpointService.getOpeningOrgUnits
  });

  const categoryQuery = useQuery({
    queryKey: ["codes", "opening-categories"],
    queryFn: () => API.CodesEndpointService.getOpeningCategories(),
  });

  const handleMultiSelectChange = (field: keyof ActivitySearchParams) => (selected: { selectedItems: CodeDescriptionDto[] }) => {
    const selectedCodes = getMultiSelectedCodes(selected);
    handleSearchFieldChange(field, selectedCodes.length > 0 ? selectedCodes : undefined);
  };

  const getMultiSelectPlaceholder = (field: keyof ActivitySearchParams, defaultText = 'Choose one or more options') => {
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

  return (
    <Grid className="default-search-input-grid">
      {/* Base */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="base-multiselect"
          className="default-search-multi-select"
          titleText="Base"
          placeholder={getMultiSelectPlaceholder('bases')}
          items={silvBaseCodeQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('bases')}
          selectedItems={(silvBaseCodeQuery.data ?? []).filter(data => searchParams?.bases?.includes(data.code ?? ''))}
        />
      </Column>

      {/* Technique */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="technique-multiselect"
          className="default-search-multi-select"
          titleText="Technique"
          placeholder={getMultiSelectPlaceholder('techniques')}
          items={silvTechniqueCodeQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('techniques')}
          selectedItems={(silvTechniqueCodeQuery.data ?? []).filter(data => searchParams?.techniques?.includes(data.code ?? ''))}
        />
      </Column>

      {/* Method */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="method-multiselect"
          className="default-search-multi-select"
          titleText="Method"
          placeholder={getMultiSelectPlaceholder('methods')}
          items={silvMethodCodeQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('methods')}
          selectedItems={(silvMethodCodeQuery.data ?? []).filter(data => searchParams?.methods?.includes(data.code ?? ''))}
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
          itemToElement={(item) => item.id === '' ? <span className='empty-dropdown-option'>Clear selected</span> : <ActivityStatusTag isComplete={item.id === 'complete'} />}
          renderSelectedItem={(item) => item.id === '' ? <span className='empty-dropdown-option'>Choose an option</span> : <ActivityStatusTag isComplete={item.id === 'complete'} />}
          onChange={({ selectedItem }) =>
            handleSearchFieldChange(
              'isComplete',
              !selectedItem || selectedItem.id === '' ? undefined : selectedItem.id === 'complete'
            )
          }
          selectedItem={searchParams?.isComplete === true ? { id: 'complete', label: 'Complete' } : searchParams?.isComplete === false ? { id: 'planned', label: 'Planned' } : { id: '', label: '' }}
        />
      </Column>

      {/* Objectives */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="objective-multiselect"
          className="default-search-multi-select"
          titleText="Objectives"
          placeholder={getMultiSelectPlaceholder('objectives')}
          items={silvObjectiveCodeQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('objectives')}
          selectedItems={(silvObjectiveCodeQuery.data ?? []).filter(data => searchParams?.objectives?.includes(data.code ?? ''))}
        />
      </Column>

      {/* Funding Source */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="fund-source-multiselect"
          className="default-search-multi-select"
          titleText="Funding source"
          placeholder={getMultiSelectPlaceholder('fundingSources')}
          items={silvFundSourceCodeQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('fundingSources')}
          selectedItems={(silvFundSourceCodeQuery.data ?? []).filter(data => searchParams?.fundingSources?.includes(data.code ?? ''))}
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

                  {/* Inter-agency number, first named intra agency number, later renamed */}
                  <Column sm={4} md={4} lg={6} max={4}>
                    <TextInput
                      ref={intraAgencyNumberInputRef}
                      id="inter-agency-number-input"
                      name="inter-agency-number"
                      labelText="Inter-agency number"
                      placeholder="Enter inter-agency number"
                      defaultValue={searchParams?.intraAgencyNumber ?? ''}
                      onInput={(e) => handleAutoUpperInput(e, INTRA_AGENCY_NUMBER_MAX_LENGTH)}
                      onPaste={(e) => handleAutoUpperPaste(e, INTRA_AGENCY_NUMBER_MAX_LENGTH)}
                      onBlur={(e) => handleSearchFieldChange('intraAgencyNumber', e.target.value ? e.target.value : undefined)}
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

export default ActivitySearchInput;
