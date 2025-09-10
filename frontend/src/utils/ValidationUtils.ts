/**
 * Checks whether a given string represents a finite numeric value.
 * Returns false if the input is undefined, an empty string, or not a valid number.
 *
 * @param val - The string to check.
 * @returns True if the string represents a finite number, false otherwise.
 *
 * @example
 * isRealNumber("123"); // true
 * isRealNumber("  45.67 "); // true
 * isRealNumber(""); // false
 * isRealNumber(undefined); // false
 * isRealNumber("abc"); // false
 */
export function isRealNumber(val: string | undefined): val is string {
  if (val === undefined) return false;
  if (val.trim() === "") return false;

  const num = Number(val);
  return !Number.isNaN(num) && Number.isFinite(num);
}
