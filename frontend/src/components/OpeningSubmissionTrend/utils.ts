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
