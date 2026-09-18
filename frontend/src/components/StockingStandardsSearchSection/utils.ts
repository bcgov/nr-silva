import { StockingStandardsSearchParams } from "@/types/ApiType";
import {
  hasActiveSearchFilters,
  ParamConfig,
  readUrlParamsWithConfig,
  updateUrlParamsWithConfig,
} from "@/utils/SearchUtils";

const CONFIG: ParamConfig<StockingStandardsSearchParams> = {
  numbers: ['standardsRegimeId', 'page', 'size'],
  booleans: ['defaultStandardsInd'],
  arrays: ['preferredSpecies', 'orgUnits', 'clientNumbers'],
  strings: [
    'fspId',
    'bgcZone',
    'bgcSubZone',
    'bgcVariant',
    'bgcPhase',
    'becSiteSeries',
    'becSiteType',
    'approvedDateStart',
    'approvedDateEnd',
  ],
};

/**
 * Check if there are any active filters in the search params
 */
export const hasStockingStandardsSearchFilters = (
  params: Partial<StockingStandardsSearchParams> | undefined
): boolean => hasActiveSearchFilters(params);

/**
 * Read stocking standards search params from URL query string
 */
export const readStockingStandardsSearchUrlParams = (
  search: string = window.location.search
): Partial<StockingStandardsSearchParams> =>
  readUrlParamsWithConfig<StockingStandardsSearchParams>(search, CONFIG);

/**
 * Update stocking standards search params in the URL query string
 */
export const updateStockingStandardsSearchUrlParams = (
  params?: Partial<StockingStandardsSearchParams>
): void => updateUrlParamsWithConfig<StockingStandardsSearchParams>(params, CONFIG);
