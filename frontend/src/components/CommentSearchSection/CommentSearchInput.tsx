import { useRef } from 'react';
import { DateTime } from 'luxon';
import { Column, DatePicker, DatePickerInput, Grid, Select, SelectItem, TextInput } from '@carbon/react';
import API from '@/services/API';
import { CommentSearchParams } from '@/types/ApiType';
import useRefWithSearchParam from '@/hooks/useRefWithSearchParam';
import { getMultiSelectedCodes } from '@/utils/InputUtils';
import { CodeDescriptionDto } from '@/services/OpenApi';
import { codeDescriptionToDisplayText } from '@/utils/multiSelectUtils';
import { useQuery } from '@tanstack/react-query';
import { API_DATE_FORMAT, DATE_PICKER_FORMAT } from '@/constants';
import { getDatePickerValue, getEndMinDate, getStartMaxDate } from '@/utils/DateUtils';
import CustomMultiSelect from '@/components/CustomMultiSelect';
import ForestClientMultiSelect from '@/components/ForestClientMultiSelect';
import { COMMENT_KEYWORD_MAX_LENGTH, COMMENT_KEYWORD_MIN_LENGTH, COMMENT_LOCATION_LIST } from './constants';

import './styles.scss';

type Props = {
  searchParams?: CommentSearchParams;
  handleSearchFieldChange: (field: keyof CommentSearchParams, value: unknown) => void;
  showValidation?: boolean;
};

const CommentSearchInput = ({ searchParams, handleSearchFieldChange, showValidation }: Props) => {
  const searchTermInputRef = useRef<HTMLInputElement>(null);
  useRefWithSearchParam(searchTermInputRef, searchParams?.searchTerm);

  const orgUnitQuery = useQuery({
    queryKey: ['codes', 'org-units'],
    queryFn: API.CodesEndpointService.getOpeningOrgUnits,
  });

  const searchTermError = showValidation
    ? !searchParams?.searchTerm || searchParams.searchTerm.length < COMMENT_KEYWORD_MIN_LENGTH
      ? `Minimum ${COMMENT_KEYWORD_MIN_LENGTH} characters required`
      : searchParams.searchTerm.length > COMMENT_KEYWORD_MAX_LENGTH
        ? `Maximum ${COMMENT_KEYWORD_MAX_LENGTH} characters allowed`
        : null
    : null;

  const handleOrgUnitChange = (selected: { selectedItems: CodeDescriptionDto[] }) => {
    const codes = getMultiSelectedCodes(selected);
    handleSearchFieldChange('orgUnits', codes.length > 0 ? codes : undefined);
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
      {/* Search term */}
      <Column sm={4} md={4} lg={6} max={4}>
        <TextInput
          ref={searchTermInputRef}
          id="comment-search-term-input"
          name="searchTerm"
          labelText="Keyword"
          placeholder="Enter a full or partial keyword"
          invalid={!!searchTermError}
          invalidText={searchTermError ?? ''}
          onChange={(e) =>
            handleSearchFieldChange('searchTerm', e.target.value || undefined)
          }
        />
      </Column>

      {/* Comment Location */}
      <Column sm={4} md={4} lg={6} max={4}>
        <Select
          id="comment-location-select"
          labelText="Comment location"
          value={searchParams?.commentLocation ?? ''}
          onChange={(e) =>
            handleSearchFieldChange('commentLocation', e.target.value || undefined)
          }
        >
          <SelectItem value="" text="All locations" />
          {COMMENT_LOCATION_LIST.map((item) => (
            <SelectItem key={item.code} value={item.code ?? ''} text={item.description ?? ''} />
          ))}
        </Select>
      </Column>

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
          id="comment-org-unit-multiselect"
          className="default-search-multi-select"
          titleText="Org unit"
          placeholder="Choose one or more options"
          items={orgUnitQuery.data ?? []}
          itemToString={codeDescriptionToDisplayText}
          onChange={handleOrgUnitChange}
          selectedItems={(orgUnitQuery.data ?? []).filter((item) =>
            searchParams?.orgUnits?.includes(item.code ?? '')
          )}
        />
      </Column>

      {/* Date range */}
      <Column sm={4} md={8} lg={16} className="default-search-date-col">
        <label className="date-label" htmlFor="comment-last-updated-date-range">
          Last updated date range
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
                id="start-date-picker-input-id"
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

export default CommentSearchInput;

