import { DateTime } from "luxon";

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
 * If no date is provided, returns an empty string.
 *
 * @param {string} [localDate] - The local date string (e.g., "2011-10-21T00:00:00").
 * @returns {string} The formatted date (e.g., "Oct 21, 2011") or an empty string if no date is provided.
 */
export const formatLocalDate = (localDate?: string | null): string => {
  if (!localDate) return "";

  return DateTime.fromISO(localDate, { zone: "local" }).toFormat("MMM dd, yyyy");
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

