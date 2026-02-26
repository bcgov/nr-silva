/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { PagedModelActivitySearchResponseDto } from '../models/PagedModelActivitySearchResponseDto';
import type { PagedModelDisturbanceSearchResponseDto } from '../models/PagedModelDisturbanceSearchResponseDto';
import type { PagedModelOpeningSearchResponseDto } from '../models/PagedModelOpeningSearchResponseDto';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class SearchEndpointService {
    /**
     * @param openingId
     * @param categories
     * @param openingStatuses
     * @param licenseNumber
     * @param licenseeOpeningId
     * @param updateDateStart
     * @param updateDateEnd
     * @param cutBlockId
     * @param cuttingPermitId
     * @param timberMark
     * @param orgUnits
     * @param clientNumbers
     * @param isCreatedByUser
     * @param submittedToFrpa
     * @param mapsheetGrid
     * @param mapsheetLetter
     * @param mapsheetSquare
     * @param mapsheetQuad
     * @param mapsheetSubQuad
     * @param openingNumber
     * @param page Zero-based page index (0..N)
     * @param size The size of the page to be returned
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
     * @returns PagedModelOpeningSearchResponseDto OK
     * @throws ApiError
     */
    public static openingSearchExact(
        openingId?: number,
        categories?: Array<string>,
        openingStatuses?: Array<string>,
        licenseNumber?: string,
        licenseeOpeningId?: string,
        updateDateStart?: string,
        updateDateEnd?: string,
        cutBlockId?: string,
        cuttingPermitId?: string,
        timberMark?: string,
        orgUnits?: Array<string>,
        clientNumbers?: Array<string>,
        isCreatedByUser?: boolean,
        submittedToFrpa?: boolean,
        mapsheetGrid?: string,
        mapsheetLetter?: string,
        mapsheetSquare?: string,
        mapsheetQuad?: string,
        mapsheetSubQuad?: string,
        openingNumber?: string,
        page?: number,
        size: number = 20,
        sort?: Array<string>,
    ): CancelablePromise<PagedModelOpeningSearchResponseDto> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/search/openings',
            query: {
                'openingId': openingId,
                'categories': categories,
                'openingStatuses': openingStatuses,
                'licenseNumber': licenseNumber,
                'licenseeOpeningId': licenseeOpeningId,
                'updateDateStart': updateDateStart,
                'updateDateEnd': updateDateEnd,
                'cutBlockId': cutBlockId,
                'cuttingPermitId': cuttingPermitId,
                'timberMark': timberMark,
                'orgUnits': orgUnits,
                'clientNumbers': clientNumbers,
                'isCreatedByUser': isCreatedByUser,
                'submittedToFrpa': submittedToFrpa,
                'mapsheetGrid': mapsheetGrid,
                'mapsheetLetter': mapsheetLetter,
                'mapsheetSquare': mapsheetSquare,
                'mapsheetQuad': mapsheetQuad,
                'mapsheetSubQuad': mapsheetSubQuad,
                'openingNumber': openingNumber,
                'page': page,
                'size': size,
                'sort': sort,
            },
        });
    }
    /**
     * @param disturbances
     * @param silvSystems
     * @param variants
     * @param cutPhases
     * @param orgUnits
     * @param openingCategories
     * @param fileId
     * @param clientNumbers
     * @param openingStatuses
     * @param updateDateStart
     * @param updateDateEnd
     * @param page Zero-based page index (0..N)
     * @param size The size of the page to be returned
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
     * @returns PagedModelDisturbanceSearchResponseDto OK
     * @throws ApiError
     */
    public static disturbanceSearch(
        disturbances?: Array<string>,
        silvSystems?: Array<string>,
        variants?: Array<string>,
        cutPhases?: Array<string>,
        orgUnits?: Array<string>,
        openingCategories?: Array<string>,
        fileId?: string,
        clientNumbers?: Array<string>,
        openingStatuses?: Array<string>,
        updateDateStart?: string,
        updateDateEnd?: string,
        page?: number,
        size: number = 20,
        sort?: Array<string>,
    ): CancelablePromise<PagedModelDisturbanceSearchResponseDto> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/search/disturbances',
            query: {
                'disturbances': disturbances,
                'silvSystems': silvSystems,
                'variants': variants,
                'cutPhases': cutPhases,
                'orgUnits': orgUnits,
                'openingCategories': openingCategories,
                'fileId': fileId,
                'clientNumbers': clientNumbers,
                'openingStatuses': openingStatuses,
                'updateDateStart': updateDateStart,
                'updateDateEnd': updateDateEnd,
                'page': page,
                'size': size,
                'sort': sort,
            },
        });
    }
    /**
     * @param bases
     * @param techniques
     * @param methods
     * @param isComplete
     * @param objectives
     * @param fundingSources
     * @param orgUnits
     * @param openingCategories
     * @param fileId
     * @param clientNumbers
     * @param openingStatuses
     * @param intraAgencyNumber
     * @param updateDateStart
     * @param updateDateEnd
     * @param page Zero-based page index (0..N)
     * @param size The size of the page to be returned
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
     * @returns PagedModelActivitySearchResponseDto OK
     * @throws ApiError
     */
    public static activitySearch(
        bases?: Array<string>,
        techniques?: Array<string>,
        methods?: Array<string>,
        isComplete?: boolean,
        objectives?: Array<string>,
        fundingSources?: Array<string>,
        orgUnits?: Array<string>,
        openingCategories?: Array<string>,
        fileId?: string,
        clientNumbers?: Array<string>,
        openingStatuses?: Array<string>,
        intraAgencyNumber?: string,
        updateDateStart?: string,
        updateDateEnd?: string,
        page?: number,
        size: number = 20,
        sort?: Array<string>,
    ): CancelablePromise<PagedModelActivitySearchResponseDto> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/search/activities',
            query: {
                'bases': bases,
                'techniques': techniques,
                'methods': methods,
                'isComplete': isComplete,
                'objectives': objectives,
                'fundingSources': fundingSources,
                'orgUnits': orgUnits,
                'openingCategories': openingCategories,
                'fileId': fileId,
                'clientNumbers': clientNumbers,
                'openingStatuses': openingStatuses,
                'intraAgencyNumber': intraAgencyNumber,
                'updateDateStart': updateDateStart,
                'updateDateEnd': updateDateEnd,
                'page': page,
                'size': size,
                'sort': sort,
            },
        });
    }
}
