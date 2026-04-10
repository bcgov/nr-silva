import { StandardsUnitSearchParams } from "@/types/ApiType";

/**
 * Check if there are any active filters in the search params
 */
export const hasStandardsUnitSearchFilters = (params: Partial<StandardsUnitSearchParams> | undefined): boolean => {
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
 * Read standards unit search params from URL query string
 */
export const readStandardsUnitSearchUrlParams = (): Partial<StandardsUnitSearchParams> => {
  const searchParams = new URLSearchParams(window.location.search);
  const params: Partial<StandardsUnitSearchParams> = {};

  const standardsRegimeId = searchParams.get('standardsRegimeId');
  if (standardsRegimeId) {
    const num = Number.parseInt(standardsRegimeId, 10);
    if (Number.isFinite(num)) {
      params.standardsRegimeId = num;
    }
  }

  const preferredSpecies = searchParams.getAll('preferredSpecies');
  if (preferredSpecies.length > 0) params.preferredSpecies = preferredSpecies;

  const orgUnits = searchParams.getAll('orgUnits');
  if (orgUnits.length > 0) params.orgUnits = orgUnits;

  const clientNumbers = searchParams.getAll('clientNumbers');
  if (clientNumbers.length > 0) params.clientNumbers = clientNumbers;

  const bgcZone = searchParams.get('bgcZone');
  if (bgcZone) params.bgcZone = bgcZone;

  const bgcSubZone = searchParams.get('bgcSubZone');
  if (bgcSubZone) params.bgcSubZone = bgcSubZone;

  const bgcVariant = searchParams.get('bgcVariant');
  if (bgcVariant) params.bgcVariant = bgcVariant;

  const bgcPhase = searchParams.get('bgcPhase');
  if (bgcPhase) params.bgcPhase = bgcPhase;

  const becSiteSeries = searchParams.get('becSiteSeries');
  if (becSiteSeries) params.becSiteSeries = becSiteSeries;

  const becSiteType = searchParams.get('becSiteType');
  if (becSiteType) params.becSiteType = becSiteType;

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
 * Update standards unit search params in the URL query string
 */
export const updateStandardsUnitSearchUrlParams = (params?: Partial<StandardsUnitSearchParams>): void => {
  const searchParams = new URLSearchParams();

  if (!params) {
    window.history.replaceState({}, '', window.location.pathname);
    return;
  }

  if (params.standardsRegimeId !== undefined) {
    searchParams.append('standardsRegimeId', String(params.standardsRegimeId));
  }

  if (params.preferredSpecies && Array.isArray(params.preferredSpecies)) {
    params.preferredSpecies.forEach((v: string) => searchParams.append('preferredSpecies', v));
  }

  if (params.orgUnits && Array.isArray(params.orgUnits)) {
    params.orgUnits.forEach((v: string) => searchParams.append('orgUnits', v));
  }

  if (params.clientNumbers && Array.isArray(params.clientNumbers)) {
    params.clientNumbers.forEach((v: string) => searchParams.append('clientNumbers', v));
  }

  if (params.bgcZone) {
    searchParams.append('bgcZone', params.bgcZone);
  }

  if (params.bgcSubZone) {
    searchParams.append('bgcSubZone', params.bgcSubZone);
  }

  if (params.bgcVariant) {
    searchParams.append('bgcVariant', params.bgcVariant);
  }

  if (params.bgcPhase) {
    searchParams.append('bgcPhase', params.bgcPhase);
  }

  if (params.becSiteSeries) {
    searchParams.append('becSiteSeries', params.becSiteSeries);
  }

  if (params.becSiteType) {
    searchParams.append('becSiteType', params.becSiteType);
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
