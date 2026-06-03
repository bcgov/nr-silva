import { useRef } from 'react';
import { DateTime } from 'luxon';
import { Column, DatePicker, DatePickerInput, Grid, TextInput } from '@carbon/react';
import API from '@/services/API';
import { StockingStandardsCommentSearchParams } from '@/types/ApiType';
import useRefWithSearchParam from '@/hooks/useRefWithSearchParam';
import { getMultiSelectedCodes } from '@/utils/InputUtils';
import { CodeDescriptionDto } from '@/services/OpenApi';
import { codeDescriptionToDisplayText } from '@/utils/multiSelectUtils';
import { useQuery } from '@tanstack/react-query';
import { API_DATE_FORMAT, DATE_PICKER_FORMAT } from '@/constants';
import { getDatePickerValue, getEndMinDate, getStartMaxDate } from '@/utils/DateUtils';
import CustomMultiSelect from '@/components/CustomMultiSelect';
import ForestClientMultiSelect from '@/components/ForestClientMultiSelect';
import { STOCKING_COMMENT_KEYWORD_MAX_LENGTH, STOCKING_COMMENT_KEYWORD_MIN_LENGTH, STOCKING_COMMENT_LOCATION_LIST } from './constants';

import './styles.scss';

type Props = {
  searchParams?: StockingStandardsCommentSearchParams;
  handleSearchFieldChange: (field: keyof StockingStandardsCommentSearchParams, value: unknown) => void;
  showValidation?: boolean;
  onSearch?: () => void;
};

const StockingStandardsCommentSearchInput = ({ searchParams, handleSearchFieldChange, showValidation, onSearch }: Props) => {
  const searchTermInputRef = useRef<HTMLInputElement>(null);
  useRefWithSearchParam(searchTermInputRef, searchParams?.searchTerm);

  const orgUnitQuery = useQuery({
    queryKey: ['codes', 'org-units'],
    queryFn: API.CodesEndpointService.getOpeningOrgUnits,
  });

  const searchTermError = showValidation
    ? !searchParams?.searchTerm || searchParams.searchTerm.length < STOCKING_COMMENT_KEYWORD_MIN_LENGTH
      ? `Minimum ${STOCKING_COMMENT_KEYWORD_MIN_LENGTH} characters required`
      : searchParams.searchTerm.length > STOCKING_COMMENT_KEYWORD_MAX_LENGTH
        ? `Maximum ${STOCKING_COMMENT_KEYWORD_MAX_LENGTH} characters allowed`
        : null
    : null;

  const handleMultiSelectChange = (field: keyof StockingStandardsCommentSearchParams) => (selected: { selectedItems: CodeDescriptionDto[] }) => {
    const codes = getMultiSelectedCodes(selected);
    handleSearchFieldChange(field, codes.length > 0 ? codes : undefined);
  };

  const handleDateChange = (isStartDate: boolean) => (dates?: Date[]) => {
    if (!dates) return;
    const formattedDate =
      dates.length && dates[0]
        ? DateTime.fromJSDate(dates[0]).toFormat(API_DATE_FORMAT)
        : undefined;
    handleSearchFieldChange(isStartDate ? 'updateDateStart' : 'updateDateEnd', formattedDate);
  };

  return (
    <Grid className="default-search-input-grid">
      <Column sm={4} md={4} lg={6} max={4}>
        <TextInput
          ref={searchTermInputRef}
          id="stocking-comment-search-term-input"
          name="searchTerm"
          labelText="Keyword"
          placeholder="Enter a full or partial keyword"
          invalid={!!searchTermError}
          invalidText={searchTermError ?? ''}
          onChange={(e) =>
            handleSearchFieldChange('searchTerm', e.target.value || undefined)
          }
          onKeyDown={(e) => {
            if (e.key === 'Enter') {
              onSearch?.();
            }
          }}
        />
      </Column>

      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="stocking-comment-location-multiselect"
          className="default-search-multi-select"
          titleText="Keywords type"
          placeholder={
            searchParams?.commentLocations?.length
              ? STOCKING_COMMENT_LOCATION_LIST
                .filter((item) => searchParams.commentLocations!.includes(item.code ?? ''))
                .map((item) => item.description ?? item.code ?? '')
                .join(', ')
              : 'Choose one or more options'
          }
          items={STOCKING_COMMENT_LOCATION_LIST}
          itemToString={(item) => item?.description ?? item?.code ?? ''}
          onChange={handleMultiSelectChange('commentLocations')}
          selectedItems={STOCKING_COMMENT_LOCATION_LIST.filter((item) =>
            searchParams?.commentLocations?.includes(item.code ?? '')
          )}
        />
      </Column>

      <Column sm={4} md={4} lg={6} max={4}>
        <ForestClientMultiSelect
          selectedClientNumbers={searchParams?.clientNumbers}
          onChange={(clientNumbers) => handleSearchFieldChange('clientNumbers', clientNumbers)}
        />
      </Column>

      <Column sm={4} md={4} lg={6} max={4}>
        <CustomMultiSelect
          id="stocking-comment-org-unit-multiselect"
          className="default-search-multi-select"
          titleText="Org unit"
          placeholder="Choose one or more options"
          items={orgUnitQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleMultiSelectChange('orgUnits')}
          selectedItems={(orgUnitQuery.data ?? []).filter((item) =>
            searchParams?.orgUnits?.includes(item.code ?? '')
          )}
        />
      </Column>

      <Column sm={4} md={8} lg={16} className="default-search-date-col">
        <label className="date-label" htmlFor="stocking-comment-last-updated-date-range">
          Last update date range
        </label>
        <Grid className="date-sub-grid">
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
                id="stocking-comment-start-date-picker-input-id"
                size="md"
                labelText="Start Date"
                placeholder="yyyy/mm/dd"
              />
            </DatePicker>
          </Column>
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
                id="stocking-comment-end-date-picker-input-id"
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

export default StockingStandardsCommentSearchInput;
