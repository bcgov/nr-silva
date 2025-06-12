/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { Pageable } from '../models/Pageable';
import type { PagedModelOpeningSearchResponseDto } from '../models/PagedModelOpeningSearchResponseDto';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class UserRecentOpeningEndpointService {
    /**
     * @param openingId
     * @returns any Accepted
     * @throws ApiError
     */
    public static recordUserViewedOpening(
        openingId: number,
    ): CancelablePromise<any> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/api/openings/recent/{openingId}',
            path: {
                'openingId': openingId,
            },
        });
    }
    /**
     * @param pageable
     * @returns PagedModelOpeningSearchResponseDto OK
     * @throws ApiError
     */
    public static getUserRecentOpenings(
        pageable: Pageable,
    ): CancelablePromise<PagedModelOpeningSearchResponseDto> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/openings/recent',
            query: {
                'pageable': pageable,
            },
        });
    }
}
