import React from 'react';
import { Column, DatePicker, DatePickerInput, Grid } from '@carbon/react';
import { DateTime } from 'luxon';
import { API_DATE_FORMAT, DATE_PICKER_FORMAT } from '@/constants';
import { getDatePickerValue, getEndMinDate, getStartMaxDate } from '@/utils/DateUtils';

export type SearchDateRangeProps = {
  label?: string;
  startDate?: string;
  endDate?: string;
  onStartDateChange?: (date?: string) => void;
  onEndDateChange?: (date?: string) => void;
  handleDateChange?: (isStartDate: boolean) => (dates?: Date[]) => void;
  startInputId?: string;
  endInputId?: string;
  labelHtmlFor?: string;
  children?: React.ReactNode;
};

const parseDate = (dates?: Date[]): string | undefined => {
  if (!dates || !dates.length || !dates[0]) return undefined;
  return DateTime.fromJSDate(dates[0]).toFormat(API_DATE_FORMAT);
};

export const SearchDateRange = ({
  label = 'Last updated date range',
  startDate,
  endDate,
  onStartDateChange,
  onEndDateChange,
  handleDateChange,
  startInputId = 'start-date-picker-input-id',
  endInputId = 'end-date-picker-input-id',
  labelHtmlFor = 'last-updated-date-range',
  children,
}: SearchDateRangeProps) => {
  const onStartChange = handleDateChange
    ? handleDateChange(true)
    : (dates?: Date[]) => {
      onStartDateChange?.(parseDate(dates));
    };

  const onEndChange = handleDateChange
    ? handleDateChange(false)
    : (dates?: Date[]) => {
      onEndDateChange?.(parseDate(dates));
    };

  return (
    <Column sm={4} md={8} lg={16} className="default-search-date-col">
      <label className="date-label" htmlFor={labelHtmlFor}>
        {label}
      </label>

      <Grid className="date-sub-grid">
        {/* Start date */}
        <Column sm={4} md={4} lg={6} max={4}>
          <DatePicker
            className="advanced-date-picker"
            datePickerType="single"
            dateFormat="Y/m/d"
            allowInput
            maxDate={getStartMaxDate(endDate)}
            onChange={onStartChange}
            value={getDatePickerValue(startDate)}
          >
            <DatePickerInput
              id={startInputId}
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
            minDate={getEndMinDate(startDate)}
            maxDate={DateTime.now().toFormat(DATE_PICKER_FORMAT)}
            onChange={onEndChange}
            value={getDatePickerValue(endDate)}
          >
            <DatePickerInput
              id={endInputId}
              size="md"
              labelText="End Date"
              placeholder="yyyy/mm/dd"
            />
          </DatePicker>
        </Column>

        {children}
      </Grid>
    </Column>
  );
};

export default SearchDateRange;
