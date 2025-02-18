/**
 * Generates a list of years from the current year back to 1912 when BC Forest Service is established.
 *
 * @returns {number[]} An array of years in descending order from the current year to 1912.
 */
export const generateYearList = (): number[] => {
  const currentYear = new Date().getFullYear();
  const years: number[] = [];

  for (let year = currentYear; year >= 1912; year--) {
    years.push(year);
  }

  return years;
};

/**
 * Generates the start or end date of a given year in `YYYY-MM-DD` format.
 * If the provided year is `null`, the function returns `null`.
 *
 * @param {number | null} year - The year for which the date should be generated, or `null` to return `null`.
 * @param {boolean} isStartDate - If `true`, returns the start date (`YYYY-01-01`);
 *                                if `false`, returns the end date (`YYYY-12-31`).
 * @returns {string | null} The formatted date string in `YYYY-MM-DD` format, or `null` if `year` is `null`.
 */
export const getYearBoundaryDate = (year: number | null, isStartDate: boolean): string | null => {
  if(!year){
    return null;
  }
  const month = isStartDate ? "01" : "12";
  const day = isStartDate ? "01" : "31";
  return `${year}-${month}-${day}`;
};
