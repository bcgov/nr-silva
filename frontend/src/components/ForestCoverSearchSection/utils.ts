import { ForestCoverSearchParams } from "@/types/ApiType";
import {
  getArrayParam,
  getNumericParam,
  getStringParam,
  hasActiveSearchFilters,
  replaceWindowUrl,
} from "@/utils/SearchUtils";

/**
 * Check if there are any active filters in the search params
 */
export const hasForestCoverSearchFilters = (params: Partial<ForestCoverSearchParams> | undefined): boolean => {
  return hasActiveSearchFilters(params);
};

/**
 * Read forest cover search params from URL query string
 */
export const readForestCoverSearchUrlParams = (): Partial<ForestCoverSearchParams> => {
  const searchParams = new URLSearchParams(window.location.search);
  const params: Partial<ForestCoverSearchParams> = {};

  const stockingStatuses = getArrayParam(searchParams, 'stockingStatuses');
  if (stockingStatuses) params.stockingStatuses = stockingStatuses;

  const stockingTypes = getArrayParam(searchParams, 'stockingTypes');
  if (stockingTypes) params.stockingTypes = stockingTypes;

  const damageAgents = getArrayParam(searchParams, 'damageAgents');
  if (damageAgents) params.damageAgents = damageAgents;

  const openingStatuses = getArrayParam(searchParams, 'openingStatuses');
  if (openingStatuses) params.openingStatuses = openingStatuses;

  const fileId = getStringParam(searchParams, 'fileId');
  if (fileId) params.fileId = fileId;

  const openingId = getNumericParam(searchParams, 'openingId');
  if (openingId !== undefined) params.openingId = openingId;

  const orgUnits = getArrayParam(searchParams, 'orgUnits');
  if (orgUnits) params.orgUnits = orgUnits;

  const openingCategories = getArrayParam(searchParams, 'openingCategories');
  if (openingCategories) params.openingCategories = openingCategories;

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

/**
 * Update forest cover search params in the URL query string
 */
export const updateForestCoverSearchUrlParams = (params?: Partial<ForestCoverSearchParams>): void => {
  if (!params) {
    replaceWindowUrl();
    return;
  }

  const searchParams = new URLSearchParams();

  params.stockingStatuses?.forEach((v: string) => searchParams.append('stockingStatuses', v));
  params.stockingTypes?.forEach((v: string) => searchParams.append('stockingTypes', v));
  params.damageAgents?.forEach((v: string) => searchParams.append('damageAgents', v));
  params.openingStatuses?.forEach((v: string) => searchParams.append('openingStatuses', v));

  if (params.fileId) searchParams.append('fileId', params.fileId);
  if (params.openingId !== undefined) searchParams.append('openingId', String(params.openingId));

  params.orgUnits?.forEach((v: string) => searchParams.append('orgUnits', v));
  params.openingCategories?.forEach((v: string) => searchParams.append('openingCategories', v));

  if (params.updateDateStart) searchParams.append('updateDateStart', params.updateDateStart);
  if (params.updateDateEnd) searchParams.append('updateDateEnd', params.updateDateEnd);

  if (params.page !== undefined) searchParams.append('page', String(params.page));
  if (params.size !== undefined) searchParams.append('size', String(params.size));

  replaceWindowUrl(searchParams);
};
