import API from '@/services/API';
import { OpeningSearchParamsType } from '@/types/OpeningTypes';
import { CancelablePromise, PagedModelOpeningSearchResponseDto } from './OpenApi';

/**
 * Typed wrapper for opening search that eliminates the need for `as any`
 */
export const openingSearch = (params: OpeningSearchParamsType | undefined): CancelablePromise<PagedModelOpeningSearchResponseDto> => {
  if (!params) {
    throw new Error('Search params required');
  }

  return API.SearchEndpointService.openingSearchExact(
    params.openingId,
    params.categories,
    params.openingStatuses,
    params.licenseNumber,
    params.licenseeOpeningId,
    params.entryDateStart,
    params.entryDateEnd,
    params.cutBlockId,
    params.cuttingPermitId,
    params.timberMark,
    params.orgUnits,
    params.clientNumbers,
    params.isCreatedByUser,
    params.submittedToFrpa,
    params.mapsheetGrid,
    params.mapsheetLetter,
    params.mapsheetSquare,
    params.mapsheetQuad,
    params.mapsheetSubQuad,
    params.openingNumber,
    params.page,
    params.size,
    params.sort
  );
};
