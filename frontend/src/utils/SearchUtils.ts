import { CodeDescriptionDto } from "@/services/OpenApi";
import { getMultiSelectedCodes } from "@/utils/InputUtils";

/**
 * Checks if there are any active filters in the given search parameters,
 * ignoring pagination and sorting keys.
 */
export const hasActiveSearchFilters = <T extends object>(
  params: Partial<T> | undefined,
  excludeKeys: string[] = ['page', 'size', 'sort']
): boolean => {
  if (!params) return false;

  const excludeSet = new Set(excludeKeys);

  return Object.entries(params).some(([key, value]) => {
    if (excludeSet.has(key)) return false;

    if (Array.isArray(value)) {
      return value.length > 0;
    }
    return value !== undefined && value !== null && value !== '';
  });
};

/**
 * Returns a comma-separated string of selected values as placeholder, or default placeholder.
 */
export const getMultiSelectPlaceholderHelper = (
  values?: string[],
  defaultText: string = 'Choose one or more options'
): string => {
  return values && values.length > 0 ? values.join(', ') : defaultText;
};

/**
 * Creates a standard handler for multi-select changes.
 */
export const handleMultiSelectChangeHelper = <T extends object>(
  field: keyof T,
  handleSearchFieldChange: (field: keyof T, value: unknown) => void
) => (selected: { selectedItems: CodeDescriptionDto[] }) => {
  const selectedCodes = getMultiSelectedCodes(selected);
  handleSearchFieldChange(field, selectedCodes.length > 0 ? selectedCodes : undefined);
};

/**
 * Extracts a numeric value from URLSearchParams.
 */
export const getNumericParam = (params: URLSearchParams, key: string): number | undefined => {
  const val = params.get(key);
  if (!val) return undefined;
  const num = Number.parseInt(val, 10);
  return Number.isFinite(num) ? num : undefined;
};

/**
 * Extracts a boolean value from URLSearchParams.
 */
export const getBooleanParam = (params: URLSearchParams, key: string): boolean | undefined => {
  const val = params.get(key);
  if (val === 'true') return true;
  if (val === 'false') return false;
  return undefined;
};

/**
 * Extracts a string array from URLSearchParams.
 */
export const getArrayParam = (params: URLSearchParams, key: string): string[] | undefined => {
  const values = params.getAll(key);
  return values.length > 0 ? values : undefined;
};

/**
 * Extracts a string value from URLSearchParams.
 */
export const getStringParam = (params: URLSearchParams, key: string): string | undefined => {
  return params.get(key) || undefined;
};

/**
 * Replaces the browser URL query string without reloading the page.
 */
export const replaceWindowUrl = (searchParams?: URLSearchParams): void => {
  if (!searchParams) {
    window.history.replaceState({}, '', window.location.pathname);
    return;
  }
  const queryString = searchParams.toString();
  const newUrl = queryString ? `${window.location.pathname}?${queryString}` : window.location.pathname;
  window.history.replaceState({}, '', newUrl);
};
