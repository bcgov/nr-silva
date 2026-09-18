import { ForestCoverSearchParams } from "@/types/ApiType";
import {
  hasActiveSearchFilters,
  ParamConfig,
  readUrlParamsWithConfig,
  updateUrlParamsWithConfig,
} from "@/utils/SearchUtils";

const CONFIG: ParamConfig<ForestCoverSearchParams> = {
  numbers: ['openingId', 'page', 'size'],
  strings: ['fileId', 'updateDateStart', 'updateDateEnd'],
  arrays: [
    'stockingStatuses',
    'stockingTypes',
    'damageAgents',
    'openingStatuses',
    'orgUnits',
    'openingCategories',
  ],
};

/**
 * Check if there are any active filters in the search params
 */
export const hasForestCoverSearchFilters = (
  params: Partial<ForestCoverSearchParams> | undefined
): boolean => hasActiveSearchFilters(params);

/**
 * Read forest cover search params from URL query string
 */
export const readForestCoverSearchUrlParams = (
  search: string = window.location.search
): Partial<ForestCoverSearchParams> =>
  readUrlParamsWithConfig<ForestCoverSearchParams>(search, CONFIG);

/**
 * Update forest cover search params in the URL query string
 */
export const updateForestCoverSearchUrlParams = (
  params?: Partial<ForestCoverSearchParams>
): void => updateUrlParamsWithConfig<ForestCoverSearchParams>(params, CONFIG);
