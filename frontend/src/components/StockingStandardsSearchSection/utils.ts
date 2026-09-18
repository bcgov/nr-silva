import { StockingStandardsSearchParams } from "@/types/ApiType";
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
export const hasStockingStandardsSearchFilters = (params: Partial<StockingStandardsSearchParams> | undefined): boolean => {
  return hasActiveSearchFilters(params);
};

/**
 * Read stocking standards search params from URL query string
 */
export const readStockingStandardsSearchUrlParams = (): Partial<StockingStandardsSearchParams> => {
  const searchParams = new URLSearchParams(window.location.search);
  const params: Partial<StockingStandardsSearchParams> = {};

  const standardsRegimeId = getNumericParam(searchParams, 'standardsRegimeId');
  if (standardsRegimeId !== undefined) params.standardsRegimeId = standardsRegimeId;

  const preferredSpecies = getArrayParam(searchParams, 'preferredSpecies');
  if (preferredSpecies) params.preferredSpecies = preferredSpecies;

  const orgUnits = getArrayParam(searchParams, 'orgUnits');
  if (orgUnits) params.orgUnits = orgUnits;

  const clientNumbers = getArrayParam(searchParams, 'clientNumbers');
  if (clientNumbers) params.clientNumbers = clientNumbers;

  const fspId = getStringParam(searchParams, 'fspId');
  if (fspId) params.fspId = fspId;

  const bgcZone = getStringParam(searchParams, 'bgcZone');
  if (bgcZone) params.bgcZone = bgcZone;

  const bgcSubZone = getStringParam(searchParams, 'bgcSubZone');
  if (bgcSubZone) params.bgcSubZone = bgcSubZone;

  const bgcVariant = getStringParam(searchParams, 'bgcVariant');
  if (bgcVariant) params.bgcVariant = bgcVariant;

  const bgcPhase = getStringParam(searchParams, 'bgcPhase');
  if (bgcPhase) params.bgcPhase = bgcPhase;

  const becSiteSeries = getStringParam(searchParams, 'becSiteSeries');
  if (becSiteSeries) params.becSiteSeries = becSiteSeries;

  const becSiteType = getStringParam(searchParams, 'becSiteType');
  if (becSiteType) params.becSiteType = becSiteType;

  const approvedDateStart = getStringParam(searchParams, 'approvedDateStart');
  if (approvedDateStart) params.approvedDateStart = approvedDateStart;

  const approvedDateEnd = getStringParam(searchParams, 'approvedDateEnd');
  if (approvedDateEnd) params.approvedDateEnd = approvedDateEnd;

  const defaultStandardsInd = getBooleanParam(searchParams, 'defaultStandardsInd');
  if (defaultStandardsInd !== undefined) params.defaultStandardsInd = defaultStandardsInd;

  const page = getNumericParam(searchParams, 'page');
  if (page !== undefined) params.page = page;

  const size = getNumericParam(searchParams, 'size');
  if (size !== undefined) params.size = size;

  return params;
};

/**
 * Update stocking standards search params in the URL query string
 */
export const updateStockingStandardsSearchUrlParams = (params?: Partial<StockingStandardsSearchParams>): void => {
  if (!params) {
    replaceWindowUrl();
    return;
  }

  const searchParams = new URLSearchParams();

  if (params.standardsRegimeId !== undefined) {
    searchParams.append('standardsRegimeId', String(params.standardsRegimeId));
  }

  params.preferredSpecies?.forEach((v: string) => searchParams.append('preferredSpecies', v));
  params.orgUnits?.forEach((v: string) => searchParams.append('orgUnits', v));
  params.clientNumbers?.forEach((v: string) => searchParams.append('clientNumbers', v));

  if (params.fspId) searchParams.append('fspId', params.fspId);
  if (params.bgcZone) searchParams.append('bgcZone', params.bgcZone);
  if (params.bgcSubZone) searchParams.append('bgcSubZone', params.bgcSubZone);
  if (params.bgcVariant) searchParams.append('bgcVariant', params.bgcVariant);
  if (params.bgcPhase) searchParams.append('bgcPhase', params.bgcPhase);
  if (params.becSiteSeries) searchParams.append('becSiteSeries', params.becSiteSeries);
  if (params.becSiteType) searchParams.append('becSiteType', params.becSiteType);
  if (params.approvedDateStart) searchParams.append('approvedDateStart', params.approvedDateStart);
  if (params.approvedDateEnd) searchParams.append('approvedDateEnd', params.approvedDateEnd);

  if (params.defaultStandardsInd !== undefined) {
    searchParams.append('defaultStandardsInd', String(params.defaultStandardsInd));
  }

  if (params.page !== undefined) searchParams.append('page', String(params.page));
  if (params.size !== undefined) searchParams.append('size', String(params.size));

  replaceWindowUrl(searchParams);
};
