
import { useRef, useEffect } from 'react';
import { DateTime } from 'luxon';
import { Column, DatePicker, DatePickerInput, Dropdown, Grid, Tag } from '@carbon/react';
import API from '@/services/API';
import { ActivitySearchParams } from '@/types/ApiType';
import useRefWithSearchParam from '@/hooks/useRefWithSearchParam';
import { getMultiSelectedCodes } from '@/utils/InputUtils';
import { CodeDescriptionDto } from '@/services/OpenApi';
import CustomMultiSelect from '../CustomMultiSelect';
import { codeDescriptionToDisplayText } from '@/utils/multiSelectUtils';
import { useQuery } from '@tanstack/react-query';

import { API_DATE_FORMAT, DATE_PICKER_FORMAT } from '@/constants';
import { getDatePickerValue, getEndMinDate, getStartMaxDate } from '@/utils/DateUtils';

import './styles.scss';

type props = {
  searchParams?: ActivitySearchParams;
  handleSearchFieldChange: (field: keyof ActivitySearchParams, value: unknown) => void;
}

const ActivitySearchInput = ({ searchParams, handleSearchFieldChange }: props) => {

  const silvBaseCodeQuery = useQuery({
    queryKey: ['codes', 'silv-base'],
    queryFn: API.CodesEndpointService.getSilvBaseCodes
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

  const renderActivityStatusTag = (item: { id: string; label: string }) => (
    <Tag className="default-dropdown-tag" title={item.label} type={item.id === 'complete' ? 'purple' : 'blue'}>{item.label}</Tag>
  );

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
            { id: 'complete', label: 'Complete' },
            { id: 'planned', label: 'Planned' },
          ]}
          itemToElement={renderActivityStatusTag}
          renderSelectedItem={renderActivityStatusTag}
          onChange={({ selectedItem }) => handleSearchFieldChange('isComplete', selectedItem?.id === 'complete')}
          selectedItem={searchParams?.isComplete === true ? { id: 'complete', label: 'Complete' } : searchParams?.isComplete === false ? { id: 'planned', label: 'Planned' } : undefined}
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
        <label className="date-label">Last updated date range</label>

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
    </Grid>
  );
};

export default ActivitySearchInput;
