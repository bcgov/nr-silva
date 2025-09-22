/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { ExtractedGeoDataDto } from '../models/ExtractedGeoDataDto';
import type { OpeningActivityBaseDto } from '../models/OpeningActivityBaseDto';
import type { OpeningDetailsAttachmentMetaDto } from '../models/OpeningDetailsAttachmentMetaDto';
import type { OpeningDetailsStockingDto } from '../models/OpeningDetailsStockingDto';
import type { OpeningDetailsTenuresDto } from '../models/OpeningDetailsTenuresDto';
import type { OpeningDetailsTombstoneOverviewDto } from '../models/OpeningDetailsTombstoneOverviewDto';
import type { OpeningForestCoverDetailsDto } from '../models/OpeningForestCoverDetailsDto';
import type { OpeningForestCoverDto } from '../models/OpeningForestCoverDto';
import type { OpeningForestCoverHistoryDetailsDto } from '../models/OpeningForestCoverHistoryDetailsDto';
import type { OpeningForestCoverHistoryDto } from '../models/OpeningForestCoverHistoryDto';
import type { OpeningForestCoverHistoryOverviewDto } from '../models/OpeningForestCoverHistoryOverviewDto';
import type { OpeningStockingHistoryDto } from '../models/OpeningStockingHistoryDto';
import type { OpeningStockingHistoryOverviewDto } from '../models/OpeningStockingHistoryOverviewDto';
import type { PagedModelOpeningDetailsActivitiesActivitiesDto } from '../models/PagedModelOpeningDetailsActivitiesActivitiesDto';
import type { PagedModelOpeningDetailsActivitiesDisturbanceDto } from '../models/PagedModelOpeningDetailsActivitiesDisturbanceDto';
import type { PagedModelOpeningSearchResponseDto } from '../models/PagedModelOpeningSearchResponseDto';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class OpeningEndpointService {
    /**
     * @param id
     * @returns boolean OK
     * @throws ApiError
     */
    public static checkFavorite(
        id: number,
    ): CancelablePromise<boolean> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/openings/favourites/{id}',
            path: {
                'id': id,
            },
        });
    }
    /**
     * @param id
     * @returns any Accepted
     * @throws ApiError
     */
    public static addToFavorites(
        id: number,
    ): CancelablePromise<any> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/api/openings/favourites/{id}',
            path: {
                'id': id,
            },
        });
    }
    /**
     * @param id
     * @returns void
     * @throws ApiError
     */
    public static removeFromFavorites(
        id: number,
    ): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/api/openings/favourites/{id}',
            path: {
                'id': id,
            },
        });
    }
    /**
     * @param formData
     * @returns ExtractedGeoDataDto Accepted
     * @throws ApiError
     */
    public static uploadOpeningSpatialFile(
        formData?: {
            file: Blob;
        },
    ): CancelablePromise<ExtractedGeoDataDto> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/openings/create/upload',
            formData: formData,
            mediaType: 'multipart/form-data',
        });
    }
    /**
     * @param openingId
     * @returns OpeningDetailsTombstoneOverviewDto OK
     * @throws ApiError
     */
    public static getOpeningTombstone(
        openingId: number,
    ): CancelablePromise<OpeningDetailsTombstoneOverviewDto> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/openings/{openingId}/tombstone',
            path: {
                'openingId': openingId,
            },
        });
    }
    /**
     * @param openingId
     * @param filter
     * @param page Zero-based page index (0..N)
     * @param size The size of the page to be returned
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
     * @returns OpeningDetailsTenuresDto OK
     * @throws ApiError
     */
    public static getTenures(
        openingId: number,
        filter?: string,
        page?: number,
        size: number = 20,
        sort?: Array<string>,
    ): CancelablePromise<OpeningDetailsTenuresDto> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/openings/{openingId}/tenures',
            path: {
                'openingId': openingId,
            },
            query: {
                'filter': filter,
                'page': page,
                'size': size,
                'sort': sort,
            },
        });
    }
    /**
     * @param openingId
     * @returns OpeningDetailsStockingDto OK
     * @throws ApiError
     */
    public static getOpeningSsu(
        openingId: number,
    ): CancelablePromise<Array<OpeningDetailsStockingDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/openings/{openingId}/ssu',
            path: {
                'openingId': openingId,
            },
        });
    }
    /**
     * @param openingId
     * @returns OpeningStockingHistoryOverviewDto OK
     * @throws ApiError
     */
    public static getOpeningSsuHistory(
        openingId: number,
    ): CancelablePromise<Array<OpeningStockingHistoryOverviewDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/openings/{openingId}/ssu/history',
            path: {
                'openingId': openingId,
            },
        });
    }
    /**
     * @param openingId
     * @param eventHistoryId
     * @returns OpeningStockingHistoryDto OK
     * @throws ApiError
     */
    public static getOpeningSsuHistoryDetails(
        openingId: number,
        eventHistoryId: number,
    ): CancelablePromise<Array<OpeningStockingHistoryDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/openings/{openingId}/ssu/history/{eventHistoryId}',
            path: {
                'openingId': openingId,
                'eventHistoryId': eventHistoryId,
            },
        });
    }
    /**
     * @param openingId
     * @param page Zero-based page index (0..N)
     * @param size The size of the page to be returned
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
     * @returns PagedModelOpeningDetailsActivitiesDisturbanceDto OK
     * @throws ApiError
     */
    public static getOpeningDisturbances(
        openingId: number,
        page?: number,
        size: number = 20,
        sort?: Array<string>,
    ): CancelablePromise<PagedModelOpeningDetailsActivitiesDisturbanceDto> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/openings/{openingId}/disturbances',
            path: {
                'openingId': openingId,
            },
            query: {
                'page': page,
                'size': size,
                'sort': sort,
            },
        });
    }
    /**
     * @param openingId
     * @param mainSearchTerm
     * @returns OpeningForestCoverDto OK
     * @throws ApiError
     */
    public static getCover(
        openingId: number,
        mainSearchTerm?: string,
    ): CancelablePromise<Array<OpeningForestCoverDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/openings/{openingId}/cover',
            path: {
                'openingId': openingId,
            },
            query: {
                'mainSearchTerm': mainSearchTerm,
            },
        });
    }
    /**
     * @param openingId
     * @param forestCoverId
     * @returns OpeningForestCoverDetailsDto OK
     * @throws ApiError
     */
    public static getCoverDetails(
        openingId: number,
        forestCoverId: number,
    ): CancelablePromise<OpeningForestCoverDetailsDto> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/openings/{openingId}/cover/{forestCoverId}',
            path: {
                'openingId': openingId,
                'forestCoverId': forestCoverId,
            },
        });
    }
    /**
     * @param openingId
     * @param updateDate
     * @returns OpeningForestCoverHistoryDto OK
     * @throws ApiError
     */
    public static getCoverHistory(
        openingId: number,
        updateDate: string,
    ): CancelablePromise<Array<OpeningForestCoverHistoryDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/openings/{openingId}/cover/history',
            path: {
                'openingId': openingId,
            },
            query: {
                'updateDate': updateDate,
            },
        });
    }
    /**
     * @param openingId
     * @param forestCoverId
     * @param archiveDate
     * @returns OpeningForestCoverHistoryDetailsDto OK
     * @throws ApiError
     */
    public static getForestCoverHistoryDetails(
        openingId: number,
        forestCoverId: number,
        archiveDate: string,
    ): CancelablePromise<OpeningForestCoverHistoryDetailsDto> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/openings/{openingId}/cover/history/{forestCoverId}',
            path: {
                'openingId': openingId,
                'forestCoverId': forestCoverId,
            },
            query: {
                'archiveDate': archiveDate,
            },
        });
    }
    /**
     * @param openingId
     * @returns OpeningForestCoverHistoryOverviewDto OK
     * @throws ApiError
     */
    public static getCoverHistoryOverview(
        openingId: number,
    ): CancelablePromise<Array<OpeningForestCoverHistoryOverviewDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/openings/{openingId}/cover/history/overview',
            path: {
                'openingId': openingId,
            },
        });
    }
    /**
     * @param openingId
     * @returns OpeningDetailsAttachmentMetaDto OK
     * @throws ApiError
     */
    public static getAttachments(
        openingId: number,
    ): CancelablePromise<Array<OpeningDetailsAttachmentMetaDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/openings/{openingId}/attachments',
            path: {
                'openingId': openingId,
            },
        });
    }
    /**
     * @param openingId
     * @param guid
     * @returns string OK
     * @throws ApiError
     */
    public static getAttachmentByGuid(
        openingId: number,
        guid: string,
    ): CancelablePromise<string> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/openings/{openingId}/attachments/{guid}',
            path: {
                'openingId': openingId,
                'guid': guid,
            },
        });
    }
    /**
     * @param openingId
     * @param filter
     * @param page Zero-based page index (0..N)
     * @param size The size of the page to be returned
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
     * @returns PagedModelOpeningDetailsActivitiesActivitiesDto OK
     * @throws ApiError
     */
    public static getOpeningActivities(
        openingId: number,
        filter?: string,
        page?: number,
        size: number = 20,
        sort?: Array<string>,
    ): CancelablePromise<PagedModelOpeningDetailsActivitiesActivitiesDto> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/openings/{openingId}/activities',
            path: {
                'openingId': openingId,
            },
            query: {
                'filter': filter,
                'page': page,
                'size': size,
                'sort': sort,
            },
        });
    }
    /**
     * @param openingId
     * @param atuId
     * @returns OpeningActivityBaseDto OK
     * @throws ApiError
     */
    public static getOpeningActivity(
        openingId: number,
        atuId: number,
    ): CancelablePromise<OpeningActivityBaseDto> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/openings/{openingId}/activities/{atuId}',
            path: {
                'openingId': openingId,
                'atuId': atuId,
            },
        });
    }
    /**
     * @param mainSearchTerm
     * @param orgUnit
     * @param category
     * @param statusList
     * @param myOpenings
     * @param submittedToFrpa
     * @param disturbanceDateStart
     * @param disturbanceDateEnd
     * @param regenDelayDateStart
     * @param regenDelayDateEnd
     * @param freeGrowingDateStart
     * @param freeGrowingDateEnd
     * @param updateDateStart
     * @param updateDateEnd
     * @param cuttingPermitId
     * @param cutBlockId
     * @param clientLocationCode
     * @param clientNumber
     * @param timberMark
     * @param page Zero-based page index (0..N)
     * @param size The size of the page to be returned
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
     * @returns PagedModelOpeningSearchResponseDto OK
     * @throws ApiError
     */
    public static openingSearch(
        mainSearchTerm?: string,
        orgUnit?: Array<string>,
        category?: Array<string>,
        statusList?: Array<string>,
        myOpenings?: boolean,
        submittedToFrpa?: boolean,
        disturbanceDateStart?: string,
        disturbanceDateEnd?: string,
        regenDelayDateStart?: string,
        regenDelayDateEnd?: string,
        freeGrowingDateStart?: string,
        freeGrowingDateEnd?: string,
        updateDateStart?: string,
        updateDateEnd?: string,
        cuttingPermitId?: string,
        cutBlockId?: string,
        clientLocationCode?: string,
        clientNumber?: string,
        timberMark?: string,
        page?: number,
        size: number = 20,
        sort?: Array<string>,
    ): CancelablePromise<PagedModelOpeningSearchResponseDto> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/openings/search',
            query: {
                'mainSearchTerm': mainSearchTerm,
                'orgUnit': orgUnit,
                'category': category,
                'statusList': statusList,
                'myOpenings': myOpenings,
                'submittedToFrpa': submittedToFrpa,
                'disturbanceDateStart': disturbanceDateStart,
                'disturbanceDateEnd': disturbanceDateEnd,
                'regenDelayDateStart': regenDelayDateStart,
                'regenDelayDateEnd': regenDelayDateEnd,
                'freeGrowingDateStart': freeGrowingDateStart,
                'freeGrowingDateEnd': freeGrowingDateEnd,
                'updateDateStart': updateDateStart,
                'updateDateEnd': updateDateEnd,
                'cuttingPermitId': cuttingPermitId,
                'cutBlockId': cutBlockId,
                'clientLocationCode': clientLocationCode,
                'clientNumber': clientNumber,
                'timberMark': timberMark,
                'page': page,
                'size': size,
                'sort': sort,
            },
        });
    }
    /**
     * @returns number OK
     * @throws ApiError
     */
    public static getFavorites(): CancelablePromise<Array<number>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/openings/favourites',
        });
    }
}
