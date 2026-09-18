import { StandardsUnitSearchParams } from "@/types/ApiType";
import {
  hasActiveSearchFilters,
  ParamConfig,
  readUrlParamsWithConfig,
  updateUrlParamsWithConfig,
} from "@/utils/SearchUtils";

const CONFIG: ParamConfig<StandardsUnitSearchParams> = {
  numbers: ['standardsRegimeId', 'page', 'size'],
  arrays: ['preferredSpecies', 'orgUnits', 'clientNumbers'],
  strings: [
    'bgcZone',
    'bgcSubZone',
    'bgcVariant',
    'bgcPhase',
    'becSiteSeries',
    'becSiteType',
    'updateDateStart',
    'updateDateEnd',
  ],
};

/**
 * Check if there are any active filters in the search params
 */
export const hasStandardsUnitSearchFilters = (
  params: Partial<StandardsUnitSearchParams> | undefined
): boolean => hasActiveSearchFilters(params);

/**
 * Read standards unit search params from URL query string
 */
export const readStandardsUnitSearchUrlParams = (
  search: string = window.location.search
): Partial<StandardsUnitSearchParams> =>
  readUrlParamsWithConfig<StandardsUnitSearchParams>(search, CONFIG);

/**
 * Update standards unit search params in the URL query string
 */
export const updateStandardsUnitSearchUrlParams = (
  params?: Partial<StandardsUnitSearchParams>
): void => updateUrlParamsWithConfig<StandardsUnitSearchParams>(params, CONFIG);
