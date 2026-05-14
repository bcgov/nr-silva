/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { PagedModelActivitySearchResponseDto } from '../models/PagedModelActivitySearchResponseDto';
import type { PagedModelCommentSearchResponseDto } from '../models/PagedModelCommentSearchResponseDto';
import type { PagedModelDisturbanceSearchResponseDto } from '../models/PagedModelDisturbanceSearchResponseDto';
import type { PagedModelForestCoverSearchResponseDto } from '../models/PagedModelForestCoverSearchResponseDto';
import type { PagedModelOpeningSearchResponseDto } from '../models/PagedModelOpeningSearchResponseDto';
import type { PagedModelStandardUnitSearchResponseDto } from '../models/PagedModelStandardUnitSearchResponseDto';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class SearchEndpointService {
    /**
     * @param standardsRegimeId
     * @param preferredSpecies
     * @param orgUnits
     * @param clientNumbers
     * @param bgcZone
     * @param bgcSubZone
     * @param bgcVariant
     * @param bgcPhase
     * @param becSiteSeries
     * @param becSiteType
     * @param becSeral
     * @param updateDateStart
     * @param updateDateEnd
     * @param page Zero-based page index (0..N)
     * @param size The size of the page to be returned
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
     * @returns PagedModelStandardUnitSearchResponseDto OK
     * @throws ApiError
     */
    public static standardsUnitSearch(
        standardsRegimeId?: number,
        preferredSpecies?: Array<string>,
        orgUnits?: Array<string>,
        clientNumbers?: Array<string>,
        bgcZone?: string,
        bgcSubZone?: string,
        bgcVariant?: string,
        bgcPhase?: string,
        becSiteSeries?: string,
        becSiteType?: string,
        becSeral?: string,
        updateDateStart?: string,
        updateDateEnd?: string,
        page?: number,
        size: number = 20,
        sort?: Array<string>,
    ): CancelablePromise<PagedModelStandardUnitSearchResponseDto> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/search/standards-unit',
            query: {
                'standardsRegimeId': standardsRegimeId,
                'preferredSpecies': preferredSpecies,
                'orgUnits': orgUnits,
                'clientNumbers': clientNumbers,
                'bgcZone': bgcZone,
                'bgcSubZone': bgcSubZone,
                'bgcVariant': bgcVariant,
                'bgcPhase': bgcPhase,
                'becSiteSeries': becSiteSeries,
                'becSiteType': becSiteType,
                'becSeral': becSeral,
                'updateDateStart': updateDateStart,
                'updateDateEnd': updateDateEnd,
                'page': page,
                'size': size,
                'sort': sort,
            },
        });
    }
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
     * @param openingId
     * @param stockingStatuses
     * @param stockingTypes
     * @param damageAgents
     * @param openingStatuses
     * @param fileId
     * @param orgUnits
     * @param openingCategories
     * @param updateDateStart
     * @param updateDateEnd
     * @param page Zero-based page index (0..N)
     * @param size The size of the page to be returned
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
     * @returns PagedModelForestCoverSearchResponseDto OK
     * @throws ApiError
     */
    public static forestCoverSearch(
        openingId?: number,
        stockingStatuses?: Array<string>,
        stockingTypes?: Array<string>,
        damageAgents?: Array<string>,
        openingStatuses?: Array<string>,
        fileId?: string,
        orgUnits?: Array<string>,
        openingCategories?: Array<string>,
        updateDateStart?: string,
        updateDateEnd?: string,
        page?: number,
        size: number = 20,
        sort?: Array<string>,
    ): CancelablePromise<PagedModelForestCoverSearchResponseDto> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/search/forest-cover',
            query: {
                'openingId': openingId,
                'stockingStatuses': stockingStatuses,
                'stockingTypes': stockingTypes,
                'damageAgents': damageAgents,
                'openingStatuses': openingStatuses,
                'fileId': fileId,
                'orgUnits': orgUnits,
                'openingCategories': openingCategories,
                'updateDateStart': updateDateStart,
                'updateDateEnd': updateDateEnd,
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
     * @param searchTerm
     * @param commentLocation
     * @param clientNumbers
     * @param orgUnits
     * @param updateDateStart
     * @param updateDateEnd
     * @param page Zero-based page index (0..N)
     * @param size The size of the page to be returned
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
     * @returns PagedModelCommentSearchResponseDto OK
     * @throws ApiError
     */
    public static commentSearch(
        searchTerm: string,
        commentLocation?: 'STANDARDS_UNIT' | 'OPENING' | 'MILESTONE' | 'ACTIVITIES' | 'FOREST_COVER',
        clientNumbers?: Array<string>,
        orgUnits?: Array<string>,
        updateDateStart?: string,
        updateDateEnd?: string,
        page?: number,
        size: number = 20,
        sort?: Array<string>,
    ): CancelablePromise<PagedModelCommentSearchResponseDto> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/search/comments',
            query: {
                'searchTerm': searchTerm,
                'commentLocation': commentLocation,
                'clientNumbers': clientNumbers,
                'orgUnits': orgUnits,
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
