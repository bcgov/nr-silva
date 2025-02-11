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


