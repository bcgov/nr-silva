import { ActivitySearchParams } from "@/types/ApiType";
import {
  getArrayParam,
  getBooleanParam,
  getNumericParam,
  getStringParam,
  hasActiveSearchFilters,
  replaceWindowUrl,
} from "@/utils/SearchUtils";

/**
 * Check if there are any active filters in the search params
 */
export const hasActivitySearchFilters = (params: ActivitySearchParams | undefined): boolean => {
  return hasActiveSearchFilters(params, ['page', 'size']);
};

/**
 * Read activity search params from URL query string
 */
export const readActivitySearchUrlParams = (): Partial<ActivitySearchParams> => {
  const searchParams = new URLSearchParams(window.location.search);
  const params: Partial<ActivitySearchParams> = {};

  const bases = getArrayParam(searchParams, 'bases');
  if (bases) params.bases = bases;

  const techniques = getArrayParam(searchParams, 'techniques');
  if (techniques) params.techniques = techniques;

  const methods = getArrayParam(searchParams, 'methods');
  if (methods) params.methods = methods;

  const isComplete = getBooleanParam(searchParams, 'isComplete');
  if (isComplete !== undefined) params.isComplete = isComplete;

  const objectives = getArrayParam(searchParams, 'objectives');
  if (objectives) params.objectives = objectives;

  const fundingSources = getArrayParam(searchParams, 'fundingSources');
  if (fundingSources) params.fundingSources = fundingSources;

  const orgUnits = getArrayParam(searchParams, 'orgUnits');
  if (orgUnits) params.orgUnits = orgUnits;

  const openingCategories = getArrayParam(searchParams, 'openingCategories');
  if (openingCategories) params.openingCategories = openingCategories;

  const fileId = getStringParam(searchParams, 'fileId');
  if (fileId) params.fileId = fileId;

  const intraAgencyNumber = getStringParam(searchParams, 'intraAgencyNumber');
  if (intraAgencyNumber) params.intraAgencyNumber = intraAgencyNumber;

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
 * Update activity search params in the URL query string
 */
export const updateActivitySearchUrlParams = (params?: Partial<ActivitySearchParams>): void => {
  if (!params) {
    replaceWindowUrl();
    return;
  }

  const searchParams = new URLSearchParams();

  params.bases?.forEach((v: string) => searchParams.append('bases', v));
  params.techniques?.forEach((v: string) => searchParams.append('techniques', v));
  params.methods?.forEach((v: string) => searchParams.append('methods', v));

  if (params.isComplete !== undefined) {
    searchParams.append('isComplete', String(params.isComplete));
  }

  params.objectives?.forEach((v: string) => searchParams.append('objectives', v));
  params.fundingSources?.forEach((v: string) => searchParams.append('fundingSources', v));
  params.orgUnits?.forEach((v: string) => searchParams.append('orgUnits', v));
  params.openingCategories?.forEach((v: string) => searchParams.append('openingCategories', v));

  if (params.fileId) searchParams.append('fileId', params.fileId);
  if (params.intraAgencyNumber) searchParams.append('intraAgencyNumber', params.intraAgencyNumber);

  params.clientNumbers?.forEach((v: string) => searchParams.append('clientNumbers', v));
  params.openingStatuses?.forEach((v: string) => searchParams.append('openingStatuses', v));

  if (params.updateDateStart) searchParams.append('updateDateStart', params.updateDateStart);
  if (params.updateDateEnd) searchParams.append('updateDateEnd', params.updateDateEnd);

  if (params.page !== undefined) searchParams.append('page', String(params.page));
  if (params.size !== undefined) searchParams.append('size', String(params.size));

  replaceWindowUrl(searchParams);
};
