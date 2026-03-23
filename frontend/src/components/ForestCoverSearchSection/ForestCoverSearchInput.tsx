
import { useRef } from 'react';
import { DateTime } from 'luxon';
import { getDatePickerValue, getEndMinDate, getStartMaxDate } from '@/utils/DateUtils';
import { Column, DatePicker, DatePickerInput, Grid, TextInput } from '@carbon/react';
import API from '@/services/API';
import { ForestCoverSearchParams } from '@/types/ApiType';
import useRefWithSearchParam from '@/hooks/useRefWithSearchParam';
import { getMultiSelectedCodes, handleAutoUpperInput, handleAutoUpperPaste } from '@/utils/InputUtils';
import { CodeDescriptionDto } from '@/services/OpenApi';
import { codeDescriptionToDisplayText } from '@/utils/multiSelectUtils';
import { useQuery } from '@tanstack/react-query';
import { API_DATE_FORMAT, DATE_PICKER_FORMAT, FILE_ID_MAX_LENGTH, OPENING_STATUS_LIST } from '@/constants';

import CustomMultiSelect from '../CustomMultiSelect';

import './styles.scss';

type props = {
  searchParams?: ForestCoverSearchParams;
  handleSearchFieldChange: (field: keyof ForestCoverSearchParams, value: unknown) => void;
}

const ForestCoverSearchInput = ({ searchParams, handleSearchFieldChange }: props) => {
  const fileIdInputRef = useRef<HTMLInputElement>(null); // VARCHAR2(10)
  useRefWithSearchParam(fileIdInputRef, searchParams?.fileId);

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

  const orgUnitQuery = useQuery({
    queryKey: ["codes", "org-units"],
    queryFn: API.CodesEndpointService.getOpeningOrgUnits
  });

  const categoryQuery = useQuery({
    queryKey: ["codes", "opening-categories"],
    queryFn: () => API.CodesEndpointService.getOpeningCategories(),
  });

  const handleMultiSelectChange = (field: keyof ForestCoverSearchParams) => (selected: { selectedItems: CodeDescriptionDto[] }) => {
    const selectedCodes = getMultiSelectedCodes(selected);
    handleSearchFieldChange(field, selectedCodes.length > 0 ? selectedCodes : undefined);
  };

  const getMultiSelectPlaceholder = (field: keyof ForestCoverSearchParams, defaultText = 'Choose one or more options') => {
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
    <Grid className="default-search-input-grid" >
      {/* Stocking Statuses */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="stocking-status-multiselect"
          className="default-search-multi-select"
          titleText="Stocking status"
          placeholder={getMultiSelectPlaceholder("stockingStatuses")}
          items={stockingStatusQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('stockingStatuses')}
          selectedItems={(stockingStatusQuery.data ?? []).filter(data => searchParams?.stockingStatuses?.includes(data.code ?? ''))}
        />
      </Column>

      {/* Stocking type */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="stocking-type-multiselect"
          className="default-search-multi-select"
          titleText="Stocking type"
          placeholder={getMultiSelectPlaceholder("stockingTypes")}
          items={stockingTypeQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('stockingTypes')}
          selectedItems={(stockingTypeQuery.data ?? []).filter(data => searchParams?.stockingTypes?.includes(data.code ?? ''))}
        />
      </Column>

      {/* Damaging agent type */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="damage-agent-type-multiselect"
          className="default-search-multi-select"
          titleText="Damaging agent type"
          placeholder={getMultiSelectPlaceholder("damageAgents")}
          items={silvDamageAgentQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('damageAgents')}
          selectedItems={(silvDamageAgentQuery.data ?? []).filter(data => searchParams?.damageAgents?.includes(data.code ?? ''))}
        />
      </Column>

      {/* Opening Statuses */}
      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="opening-status-multiselect"
          className="default-search-multi-select"
          titleText="Opening status"
          placeholder={getMultiSelectPlaceholder('openingStatuses')}
          items={OPENING_STATUS_LIST}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('openingStatuses')}
          selectedItems={OPENING_STATUS_LIST.filter(data => searchParams?.openingStatuses?.includes(data.code ?? '')) ?? []}
        />
      </Column>

      {/* File ID, aka License Number */}
      <Column sm={4} md={4} lg={6} max={4}>
        <TextInput
          ref={fileIdInputRef}
          id="file-id-input"
          name="file-id"
          labelText="File ID"
          placeholder="Enter file ID"
          onInput={(e) => handleAutoUpperInput(e, FILE_ID_MAX_LENGTH)}
          onPaste={(e) => handleAutoUpperPaste(e, FILE_ID_MAX_LENGTH)}
          onBlur={(e) => handleSearchFieldChange('fileId', e.target.value ? e.target.value : undefined)}
        />
      </Column>

      {/* Org Unit */}
      < Column sm={4} md={4} lg={6} max={4} >
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
      </Column >

      {/* Opening Categories */}
      < Column sm={4} md={4} lg={6} max={4} >
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
      </Column >

      {/* Updated on date range */}
      < Column sm={4} md={8} lg={16} className="default-search-date-col" >
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
      </Column >
    </Grid >
  );
};

export default ForestCoverSearchInput;
