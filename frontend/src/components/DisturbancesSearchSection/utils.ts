import { DisturbanceSearchParams } from "@/types/ApiType";

/**
 * Check if there are any active filters in the search params
 */
export const hasDisturbanceSearchFilters = (params: DisturbanceSearchParams | undefined): boolean => {
  if (!params) return false;

  const excludeKeys = new Set(['page', 'size', 'sort']);

  return Object.entries(params).some(([key, value]) => {
    if (excludeKeys.has(key)) return false;

    if (Array.isArray(value)) {
      return value.length > 0;
    }
    return value !== undefined && value !== null && value !== '';
  });
};

/**
 * Read disturbance search params from URL query string
 */
export const readDisturbanceSearchUrlParams = (): Partial<DisturbanceSearchParams> => {
  const searchParams = new URLSearchParams(window.location.search);
  const params: Partial<DisturbanceSearchParams> = {};

  const disturbances = searchParams.getAll('disturbances');
  if (disturbances.length > 0) params.disturbances = disturbances;

  const silvSystems = searchParams.getAll('silvSystems');
  if (silvSystems.length > 0) params.silvSystems = silvSystems;

  const variants = searchParams.getAll('variants');
  if (variants.length > 0) params.variants = variants;

  const cutPhases = searchParams.getAll('cutPhases');
  if (cutPhases.length > 0) params.cutPhases = cutPhases;

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
  if (page) {
    const pageNum = Number.parseInt(page, 10);
    if (Number.isFinite(pageNum)) {
      params.page = pageNum;
    }
  }

  const size = searchParams.get('size');
  if (size) {
    const sizeNum = Number.parseInt(size, 10);
    if (Number.isFinite(sizeNum)) {
      params.size = sizeNum;
    }
  }

  return params;
};

/**
 * Update disturbance search params in the URL query string
 */
export const updateDisturbanceSearchUrlParams = (params?: Partial<DisturbanceSearchParams>): void => {
  const searchParams = new URLSearchParams();

  if (!params) {
    window.history.replaceState({}, '', window.location.pathname);
    return;
  }

  if (params.disturbances && Array.isArray(params.disturbances)) {
    params.disturbances.forEach((v: string) => searchParams.append('disturbances', v));
  }

  if (params.silvSystems && Array.isArray(params.silvSystems)) {
    params.silvSystems.forEach((v: string) => searchParams.append('silvSystems', v));
  }

  if (params.variants && Array.isArray(params.variants)) {
    params.variants.forEach((v: string) => searchParams.append('variants', v));
  }

  if (params.cutPhases && Array.isArray(params.cutPhases)) {
    params.cutPhases.forEach((v: string) => searchParams.append('cutPhases', v));
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

  if (params.page !== undefined) {
    searchParams.append('page', String(params.page));
  }

  if (params.size !== undefined) {
    searchParams.append('size', String(params.size));
  }

  const queryString = searchParams.toString();
  const newUrl = queryString ? `${window.location.pathname}?${queryString}` : window.location.pathname;
  window.history.replaceState({}, '', newUrl);
};
