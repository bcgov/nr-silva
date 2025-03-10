import { OpeningSearchFilterType } from "./definitions";

/**
 * Checks if the provided `OpeningSearchFilterType` object contains any active filters.
 * A filter is considered active if its value is:
 * - A non-empty string
 * - A non-empty array
 * - A defined boolean set to `true`
 * - A defined number (e.g., pagination)
 *
 * The `dateType` field is intentionally ignored in this check.
 *
 * @param filters - The filters object to evaluate.
 * @returns `true` if at least one filter is active (excluding `dateType`), otherwise `false`.
 */
export function hasAnyActiveFilters(filters: OpeningSearchFilterType): boolean {
  return Object.entries(filters).some(([key, value]) => {
    if (key === "dateType") return false;

    if (Array.isArray(value)) return value.length > 0;
    if (typeof value === "string") return value.trim() !== "";
    if (typeof value === "boolean") return value === true;
    if (typeof value === "number") return true;
    if (value !== null && value !== undefined) return true;

    return false;
  });
}
