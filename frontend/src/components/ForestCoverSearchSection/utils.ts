import { ForestCoverSearchParams } from "@/types/ApiType";

/**
 * Check if there are any active filters in the search params
 */
export const hasForestCoverSearchFilters = (params: Partial<ForestCoverSearchParams> | undefined): boolean => {
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
 * Read forest cover search params from URL query string
 */
export const readForestCoverSearchUrlParams = (): Partial<ForestCoverSearchParams> => {
  const searchParams = new URLSearchParams(window.location.search);
  const params: Partial<ForestCoverSearchParams> = {};

  const stockingStatuses = searchParams.getAll('stockingStatuses');
  if (stockingStatuses.length > 0) params.stockingStatuses = stockingStatuses;

  const stockingTypes = searchParams.getAll('stockingTypes');
  if (stockingTypes.length > 0) params.stockingTypes = stockingTypes;

  const damageAgents = searchParams.getAll('damageAgents');
  if (damageAgents.length > 0) params.damageAgents = damageAgents;

  const openingStatuses = searchParams.getAll('openingStatuses');
  if (openingStatuses.length > 0) params.openingStatuses = openingStatuses;

  const fileId = searchParams.get('fileId');
  if (fileId) params.fileId = fileId;

  const openingId = searchParams.get('openingId');
  if (openingId) {
    const openingIdNum = Number.parseInt(openingId, 10);
    if (Number.isFinite(openingIdNum)) {
      params.openingId = openingIdNum;
    }
  }

  const orgUnits = searchParams.getAll('orgUnits');
  if (orgUnits.length > 0) params.orgUnits = orgUnits;

  const openingCategories = searchParams.getAll('openingCategories');
  if (openingCategories.length > 0) params.openingCategories = openingCategories;

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
 * Update forest cover search params in the URL query string
 */
export const updateForestCoverSearchUrlParams = (params?: Partial<ForestCoverSearchParams>): void => {
  const searchParams = new URLSearchParams();

  if (!params) {
    window.history.replaceState({}, '', window.location.pathname);
    return;
  }

  if (params.stockingStatuses && Array.isArray(params.stockingStatuses)) {
    params.stockingStatuses.forEach((v: string) => searchParams.append('stockingStatuses', v));
  }

  if (params.stockingTypes && Array.isArray(params.stockingTypes)) {
    params.stockingTypes.forEach((v: string) => searchParams.append('stockingTypes', v));
  }

  if (params.damageAgents && Array.isArray(params.damageAgents)) {
    params.damageAgents.forEach((v: string) => searchParams.append('damageAgents', v));
  }

  if (params.openingStatuses && Array.isArray(params.openingStatuses)) {
    params.openingStatuses.forEach((v: string) => searchParams.append('openingStatuses', v));
  }

  if (params.fileId) {
    searchParams.append('fileId', params.fileId);
  }

  if (params.openingId !== undefined) {
    searchParams.append('openingId', String(params.openingId));
  }

  if (params.orgUnits && Array.isArray(params.orgUnits)) {
    params.orgUnits.forEach((v: string) => searchParams.append('orgUnits', v));
  }

  if (params.openingCategories && Array.isArray(params.openingCategories)) {
    params.openingCategories.forEach((v: string) => searchParams.append('openingCategories', v));
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
