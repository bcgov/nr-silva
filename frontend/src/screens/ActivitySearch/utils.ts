import { ActivitySearchParams } from "@/types/ApiType";

/**
 * Check if there are any active filters in the search params
 */
export const hasActivitySearchFilters = (params: ActivitySearchParams | undefined): boolean => {
  if (!params) return false;

  const excludeKeys = ['page', 'size'];

  return Object.entries(params).some(([key, value]) => {
    if (excludeKeys.includes(key)) return false;

    if (Array.isArray(value)) {
      return value.length > 0;
    }
    return value !== undefined && value !== null && value !== '';
  });
};

/**
 * Read activity search params from URL query string
 */
export const readActivitySearchUrlParams = (): Partial<ActivitySearchParams> => {
  const searchParams = new URLSearchParams(window.location.search);
  const params: any = {};

  const bases = searchParams.getAll('bases');
  if (bases.length > 0) params.bases = bases;

  const techniques = searchParams.getAll('techniques');
  if (techniques.length > 0) params.techniques = techniques;

  const methods = searchParams.getAll('methods');
  if (methods.length > 0) params.methods = methods;

  const isComplete = searchParams.get('isComplete');
  if (isComplete) params.isComplete = isComplete === 'true';

  const objectives = searchParams.getAll('objectives');
  if (objectives.length > 0) params.objectives = objectives;

  const fundingSources = searchParams.getAll('fundingSources');
  if (fundingSources.length > 0) params.fundingSources = fundingSources;

  const orgUnits = searchParams.getAll('orgUnits');
  if (orgUnits.length > 0) params.orgUnits = orgUnits;

  const openingCategories = searchParams.getAll('openingCategories');
  if (openingCategories.length > 0) params.openingCategories = openingCategories;

  const fileId = searchParams.get('fileId');
  if (fileId) params.fileId = fileId;

  const clientNumbers = searchParams.getAll('clientNumbers');
  if (clientNumbers.length > 0) params.clientNumbers = clientNumbers;

  const openingStatuses = searchParams.getAll('openingStatuses');
  if (openingStatuses.length > 0) params.openingStatuses = openingStatuses;

  const updateDateStart = searchParams.get('updateDateStart');
  if (updateDateStart) params.updateDateStart = updateDateStart;

  const updateDateEnd = searchParams.get('updateDateEnd');
  if (updateDateEnd) params.updateDateEnd = updateDateEnd;

  const page = searchParams.get('page');
  if (page) params.page = parseInt(page, 10);

  const size = searchParams.get('size');
  if (size) params.size = parseInt(size, 10);

  return params;
};

/**
 * Update activity search params in the URL query string
 */
export const updateActivitySearchUrlParams = (params?: Partial<ActivitySearchParams>): void => {
  const searchParams = new URLSearchParams();

  if (!params) {
    window.history.replaceState({}, '', window.location.pathname);
    return;
  }

  if (params.bases && Array.isArray(params.bases)) {
    params.bases.forEach((v: string) => searchParams.append('bases', v));
  }

  if (params.techniques && Array.isArray(params.techniques)) {
    params.techniques.forEach((v: string) => searchParams.append('techniques', v));
  }

  if (params.methods && Array.isArray(params.methods)) {
    params.methods.forEach((v: string) => searchParams.append('methods', v));
  }

  if (params.isComplete !== undefined) {
    searchParams.append('isComplete', String(params.isComplete));
  }

  if (params.objectives && Array.isArray(params.objectives)) {
    params.objectives.forEach((v: string) => searchParams.append('objectives', v));
  }

  if (params.fundingSources && Array.isArray(params.fundingSources)) {
    params.fundingSources.forEach((v: string) => searchParams.append('fundingSources', v));
  }

  if (params.orgUnits && Array.isArray(params.orgUnits)) {
    params.orgUnits.forEach((v: string) => searchParams.append('orgUnits', v));
  }

  if (params.openingCategories && Array.isArray(params.openingCategories)) {
    params.openingCategories.forEach((v: string) => searchParams.append('openingCategories', v));
  }

  if (params.fileId) {
    searchParams.append('fileId', params.fileId);
  }

  if (params.clientNumbers && Array.isArray(params.clientNumbers)) {
    params.clientNumbers.forEach((v: string) => searchParams.append('clientNumbers', v));
  }

  if (params.openingStatuses && Array.isArray(params.openingStatuses)) {
    params.openingStatuses.forEach((v: string) => searchParams.append('openingStatuses', v));
  }

  if (params.updateDateStart) {
    searchParams.append('updateDateStart', params.updateDateStart);
  }

  if (params.updateDateEnd) {
    searchParams.append('updateDateEnd', params.updateDateEnd);
  }

  const queryString = searchParams.toString();
  const newUrl = queryString ? `${window.location.pathname}?${queryString}` : window.location.pathname;
  window.history.replaceState({}, '', newUrl);
};
