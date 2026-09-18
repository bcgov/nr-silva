import { StockingStandardsCommentSearchParams } from '@/types/ApiType';
import {
  getArrayParam,
  getNumericParam,
  getStringParam,
  hasActiveSearchFilters,
  replaceWindowUrl,
} from '@/utils/SearchUtils';

export const hasStockingStandardsCommentSearchFilters = (params: StockingStandardsCommentSearchParams | undefined): boolean => {
  return hasActiveSearchFilters(params);
};

export const readStockingStandardsCommentSearchUrlParams = (): Partial<StockingStandardsCommentSearchParams> => {
  const searchParams = new URLSearchParams(window.location.search);
  const params: Partial<StockingStandardsCommentSearchParams> = {};

  const searchTerm = getStringParam(searchParams, 'searchTerm');
  if (searchTerm) params.searchTerm = searchTerm;

  const commentLocations = getArrayParam(searchParams, 'commentLocations');
  if (commentLocations) params.commentLocations = commentLocations;

  const clientNumbers = getArrayParam(searchParams, 'clientNumbers');
  if (clientNumbers) params.clientNumbers = clientNumbers;

  const orgUnits = getArrayParam(searchParams, 'orgUnits');
  if (orgUnits) params.orgUnits = orgUnits;

  const updateDateStart = getStringParam(searchParams, 'updateDateStart');
  if (updateDateStart) params.updateDateStart = updateDateStart;

  const updateDateEnd = getStringParam(searchParams, 'updateDateEnd');
  if (updateDateEnd) params.updateDateEnd = updateDateEnd;

  const page = getNumericParam(searchParams, 'page');
  if (page !== undefined) params.page = page;

  const size = getNumericParam(searchParams, 'size');
  if (size !== undefined) params.size = size;

  return params;
};

export const updateStockingStandardsCommentSearchUrlParams = (params?: Partial<StockingStandardsCommentSearchParams>): void => {
  if (!params) {
    replaceWindowUrl();
    return;
  }

  const searchParams = new URLSearchParams();

  if (params.searchTerm) searchParams.append('searchTerm', params.searchTerm);
  params.commentLocations?.forEach((v) => searchParams.append('commentLocations', v));
  params.clientNumbers?.forEach((v) => searchParams.append('clientNumbers', v));
  params.orgUnits?.forEach((v) => searchParams.append('orgUnits', v));

  if (params.updateDateStart) searchParams.append('updateDateStart', params.updateDateStart);
  if (params.updateDateEnd) searchParams.append('updateDateEnd', params.updateDateEnd);

  if (params.page !== undefined) searchParams.append('page', String(params.page));
  if (params.size !== undefined) searchParams.append('size', String(params.size));

  replaceWindowUrl(searchParams);
};
