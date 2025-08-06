import { DateTime } from "luxon";
import { PLACE_HOLDER } from "../constants";

export const formatDate = (date: string) => {
  if (date) {
    const options: Intl.DateTimeFormatOptions = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(`${date}T00:00:00-08:00`).toLocaleDateString([], options);
  }
  return '--';
};

export const dateStringToISO = (date: string): string => {
  if (date) {
    return new Date(date).toISOString();
  }
  return '';
};

export const formatDateForDatePicker = (date: any) => {
  let year, month, day;
  if (date) {
    [year, month, day] = date.split("-");
    return `${month}/${day}/${year}`;
  } else {
    return "";
  }
};

/**
 * Formats a local date string into a "MMM dd, yyyy" format.
 * If no date is provided, returns an empty string or a placeholder if specified.
 *
 * @param {string} [localDate] - The local date string (e.g., "2011-10-21T00:00:00").
 * @param {boolean} [usePlaceholder] - Whether to return a placeholder if no date is provided.
 * @returns {string} The formatted date (e.g., "Oct 21, 2011"), an empty string, or a placeholder if specified.
 */
export const formatLocalDate = (localDate?: string | null, usePlaceholder?: boolean): string => {
  if (!localDate) {
    return usePlaceholder ? PLACE_HOLDER : ""
  }

  const stringDate = String(localDate);

  return DateTime.fromISO(stringDate, { zone: "local" }).toFormat("MMM dd, yyyy");
};

/**
 * Converts a Date object to a formatted string (`YYYY-MM-DD`) for backend use.
 *
 * @param {Date} dateToFormat - The Date object to format.
 * @returns {string | null} The formatted date string (`YYYY-MM-DD`) or `null` if the date is invalid.
 */
export const formatDateObjToString = (dateToFormat: Date | null): string | null => {
  if (!dateToFormat || isNaN(dateToFormat.getTime())) return null;

  return DateTime.fromJSDate(dateToFormat).toFormat("yyyy-MM-dd");
};

/**
 * Gets the abbreviated month name (e.g., "Jan", "Feb") from a given month number.
 *
 * @param {number} monthNumber - The month number (1-12).
 * @returns {string} The abbreviated month name (e.g., "Jan" for 1, "Feb" for 2).
 */
export const getMonthAbbreviation = (monthNumber: number): string => (
  DateTime.fromObject({ month: monthNumber }).toFormat("MMM")
);

/**
 * Extracts the year from a date string in the format "YYYY-MM-DDZ".
 *
 * @param {string} dateStr - The date string (e.g., "2005-05-02Z").
 * @returns {number | null} The year as a number, or null if the input is invalid.
 */
export const extractYearFromDateString = (dateStr: string): number | null => {
  if (!dateStr) return null;
  const match = /^(\d{4})-\d{2}-\d{2}Z$/.exec(dateStr);
  return match ? Number(match[1]) : null;
};
