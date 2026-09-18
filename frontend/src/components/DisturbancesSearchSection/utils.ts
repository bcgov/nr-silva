import { DisturbanceSearchParams } from "@/types/ApiType";
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
export const hasDisturbanceSearchFilters = (params: DisturbanceSearchParams | undefined): boolean => {
  return hasActiveSearchFilters(params);
};

/**
 * Read disturbance search params from URL query string
 */
export const readDisturbanceSearchUrlParams = (): Partial<DisturbanceSearchParams> => {
  const searchParams = new URLSearchParams(window.location.search);
  const params: Partial<DisturbanceSearchParams> = {};

  const disturbances = getArrayParam(searchParams, 'disturbances');
  if (disturbances) params.disturbances = disturbances;

  const silvSystems = getArrayParam(searchParams, 'silvSystems');
  if (silvSystems) params.silvSystems = silvSystems;

  const variants = getArrayParam(searchParams, 'variants');
  if (variants) params.variants = variants;

  const cutPhases = getArrayParam(searchParams, 'cutPhases');
  if (cutPhases) params.cutPhases = cutPhases;

  const orgUnits = getArrayParam(searchParams, 'orgUnits');
  if (orgUnits) params.orgUnits = orgUnits;

  const openingCategories = getArrayParam(searchParams, 'openingCategories');
  if (openingCategories) params.openingCategories = openingCategories;

  const fileId = getStringParam(searchParams, 'fileId');
  if (fileId) params.fileId = fileId;

  const clientNumbers = getArrayParam(searchParams, 'clientNumbers');
  if (clientNumbers) params.clientNumbers = clientNumbers;

  const openingStatuses = getArrayParam(searchParams, 'openingStatuses');
  if (openingStatuses) params.openingStatuses = openingStatuses;

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
 * Update disturbance search params in the URL query string
 */
export const updateDisturbanceSearchUrlParams = (params?: Partial<DisturbanceSearchParams>): void => {
  if (!params) {
    replaceWindowUrl();
    return;
  }

  const searchParams = new URLSearchParams();

  params.disturbances?.forEach((v: string) => searchParams.append('disturbances', v));
  params.silvSystems?.forEach((v: string) => searchParams.append('silvSystems', v));
  params.variants?.forEach((v: string) => searchParams.append('variants', v));
  params.cutPhases?.forEach((v: string) => searchParams.append('cutPhases', v));
  params.orgUnits?.forEach((v: string) => searchParams.append('orgUnits', v));
  params.openingCategories?.forEach((v: string) => searchParams.append('openingCategories', v));

  if (params.fileId) searchParams.append('fileId', params.fileId);

  params.clientNumbers?.forEach((v: string) => searchParams.append('clientNumbers', v));
  params.openingStatuses?.forEach((v: string) => searchParams.append('openingStatuses', v));

  if (params.updateDateStart) searchParams.append('updateDateStart', params.updateDateStart);
  if (params.updateDateEnd) searchParams.append('updateDateEnd', params.updateDateEnd);

  if (params.page !== undefined) searchParams.append('page', String(params.page));
  if (params.size !== undefined) searchParams.append('size', String(params.size));

  replaceWindowUrl(searchParams);
};
