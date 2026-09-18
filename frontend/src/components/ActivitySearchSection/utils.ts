import { ActivitySearchParams } from "@/types/ApiType";
import {
  hasActiveSearchFilters,
  ParamConfig,
  readUrlParamsWithConfig,
  updateUrlParamsWithConfig,
} from "@/utils/SearchUtils";

const CONFIG: ParamConfig<ActivitySearchParams> = {
  numbers: ['page', 'size'],
  booleans: ['isComplete'],
  strings: ['fileId', 'intraAgencyNumber', 'updateDateStart', 'updateDateEnd'],
  arrays: [
    'bases',
    'techniques',
    'methods',
    'objectives',
    'fundingSources',
    'orgUnits',
    'openingCategories',
    'clientNumbers',
    'openingStatuses',
  ],
};

/**
 * Check if there are any active filters in the search params
 */
export const hasActivitySearchFilters = (
  params: ActivitySearchParams | undefined
): boolean => hasActiveSearchFilters(params, ['page', 'size']);

/**
 * Read activity search params from URL query string
 */
export const readActivitySearchUrlParams = (
  search: string = window.location.search
): Partial<ActivitySearchParams> =>
  readUrlParamsWithConfig<ActivitySearchParams>(search, CONFIG);

/**
 * Update activity search params in the URL query string
 */
export const updateActivitySearchUrlParams = (
  params?: Partial<ActivitySearchParams>
): void => updateUrlParamsWithConfig<ActivitySearchParams>(params, CONFIG);
