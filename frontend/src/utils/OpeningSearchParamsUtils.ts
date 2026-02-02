import { OpeningSearchParamsType } from '@/types/OpeningTypes';

/**
 * Check if searchParams has any actual filter values (excluding pagination)
 */
export const hasActiveFilters = (params: OpeningSearchParamsType | undefined): boolean => {
  if (!params) return false;
  // Exclude page and size from active filters check
  return Object.entries(params)
    .filter(([key]) => key !== 'page' && key !== 'size')
    .some(([, v]) => v !== undefined && v !== null && v !== '');
};

/**
 * Serialize opening search params object to URL query string
 * Omits undefined/null/empty values
 */
export const serializeOpeningSearchParams = (params: OpeningSearchParamsType | undefined): URLSearchParams => {
  const urlParams = new URLSearchParams();

  if (!params) return urlParams;

  const entries = Object.entries(params) as [keyof OpeningSearchParamsType, unknown][];

  for (const [key, value] of entries) {
    if (value === undefined || value === null || value === '') {
      continue;
    }

    if (Array.isArray(value)) {
      // For arrays, add multiple values with same key or join with comma
      value.forEach((v) => {
        if (v !== undefined && v !== null && v !== '') {
          urlParams.append(key, String(v));
        }
      });
    } else if (typeof value === 'boolean') {
      urlParams.set(key, String(value));
    } else {
      urlParams.set(key, String(value));
    }
  }

  return urlParams;
};

/**
 * Deserialize URL query params back to opening search params object
 * Handles arrays (multiple values with same key) and type conversions
 */
export const deserializeOpeningSearchParams = (urlParams: URLSearchParams): OpeningSearchParamsType => {
  const params: Record<string, any> = {};

  const arrayFields = new Set([
    'categories',
    'openingStatuses',
    'orgUnits',
    'clientNumbers',
    'sort',
  ]);

  const booleanFields = new Set(['isCreatedByUser', 'submittedToFrpa']);

  const numberFields = new Set(['openingId', 'page', 'size']);

  for (const [key, value] of urlParams.entries()) {
    if (arrayFields.has(key)) {
      // For array fields, accumulate all values with the same key
      if (!Array.isArray(params[key])) {
        params[key] = [];
      }
      params[key].push(value);
    } else if (booleanFields.has(key)) {
      params[key] = value === 'true';
    } else if (numberFields.has(key)) {
      params[key] = Number.parseInt(value, 10);
    } else {
      params[key] = value;
    }
  }

  return params as OpeningSearchParamsType;
};

/**
 * Push opening search params to browser URL without triggering navigation
 */
export const updateOpeningSearchUrlParams = (params: OpeningSearchParamsType | undefined) => {
  const urlParams = serializeOpeningSearchParams(params);
  const queryString = urlParams.toString();
  const url = queryString ? `${window.location.pathname}?${queryString}` : window.location.pathname;
  window.history.replaceState({}, '', url);
};

/**
 * Read opening search params from current URL
 */
export const readOpeningSearchUrlParams = (): OpeningSearchParamsType => {
  return deserializeOpeningSearchParams(new URLSearchParams(window.location.search));
};
