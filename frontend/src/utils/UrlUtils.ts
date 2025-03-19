/**
 * Builds a query string from an object of parameters.
 *
 * - Converts all values to strings and removes properties with `undefined` or `null` values.
 * - Supports arrays by joining them into a comma-separated string (`key=value1,value2`),
 *   with commas percent-encoded as `%2C` in the final output.
 *
 * @template T - The type of the parameters object.
 * @param {T} params - The object containing key-value pairs to be converted into a query string.
 * @returns {string} The formatted query string.
 *
 * @example
 * const query = buildQueryString({ page: 1, search: "tree", active: true });
 * console.log(query); // "page=1&search=tree&active=true"
 *
 * @example
 * const query = buildQueryString({ dateType: "Update", updateDateStart: "2024-03-01", updateDateEnd: "2024-03-10" });
 * console.log(query); // "dateType=Update&updateDateStart=2024-03-01&updateDateEnd=2024-03-10"
 *
 * @example
 * const query = buildQueryString({ orgUnit: ["A", "B"], status: ["Open", "Closed"] });
 * console.log(query); // "orgUnit=A%2CB&status=Open%2CClosed"
 */
export const buildQueryString = <T extends Record<string, any>>(params: T): string => {
  const query = new URLSearchParams();

  Object.entries(params).forEach(([key, value]) => {
    if (value !== undefined && value !== null) {
      if (Array.isArray(value) && value.length > 0) {
        query.append(key, value.join(","));
      } else if (!Array.isArray(value)) {
        query.append(key, String(value));
      }
    }
  });

  return query.toString();
};
