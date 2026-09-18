import { DisturbanceSearchParams } from "@/types/ApiType";
import {
  hasActiveSearchFilters,
  ParamConfig,
  readUrlParamsWithConfig,
  updateUrlParamsWithConfig,
} from "@/utils/SearchUtils";

const CONFIG: ParamConfig<DisturbanceSearchParams> = {
  numbers: ['page', 'size'],
  strings: ['fileId', 'updateDateStart', 'updateDateEnd'],
  arrays: [
    'disturbances',
    'silvSystems',
    'variants',
    'cutPhases',
    'orgUnits',
    'openingCategories',
    'clientNumbers',
    'openingStatuses',
  ],
};

/**
 * Check if there are any active filters in the search params
 */
export const hasDisturbanceSearchFilters = (
  params: DisturbanceSearchParams | undefined
): boolean => hasActiveSearchFilters(params);

/**
 * Read disturbance search params from URL query string
 */
export const readDisturbanceSearchUrlParams = (
  search: string = window.location.search
): Partial<DisturbanceSearchParams> =>
  readUrlParamsWithConfig<DisturbanceSearchParams>(search, CONFIG);

/**
 * Update disturbance search params in the URL query string
 */
export const updateDisturbanceSearchUrlParams = (
  params?: Partial<DisturbanceSearchParams>
): void => updateUrlParamsWithConfig<DisturbanceSearchParams>(params, CONFIG);
