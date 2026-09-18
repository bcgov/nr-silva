import { StockingStandardsCommentSearchParams } from '@/types/ApiType';
import {
  hasActiveSearchFilters,
  ParamConfig,
  readUrlParamsWithConfig,
  updateUrlParamsWithConfig,
} from '@/utils/SearchUtils';

const CONFIG: ParamConfig<StockingStandardsCommentSearchParams> = {
  strings: ['searchTerm', 'updateDateStart', 'updateDateEnd'],
  numbers: ['page', 'size'],
  arrays: ['commentLocations', 'clientNumbers', 'orgUnits'],
};

export const hasStockingStandardsCommentSearchFilters = (
  params: StockingStandardsCommentSearchParams | undefined
): boolean => hasActiveSearchFilters(params);

export const readStockingStandardsCommentSearchUrlParams = (
  search: string = window.location.search
): Partial<StockingStandardsCommentSearchParams> =>
  readUrlParamsWithConfig<StockingStandardsCommentSearchParams>(search, CONFIG);

export const updateStockingStandardsCommentSearchUrlParams = (
  params?: Partial<StockingStandardsCommentSearchParams>
): void => updateUrlParamsWithConfig<StockingStandardsCommentSearchParams>(params, CONFIG);
