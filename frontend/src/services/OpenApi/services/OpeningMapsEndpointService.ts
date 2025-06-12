/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { FeatureCollection } from '../models/FeatureCollection';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class OpeningMapsEndpointService {
    /**
     * @param openingId
     * @param kind
     * @returns FeatureCollection OK
     * @throws ApiError
     */
    public static getOpeningPolygonAndProperties(
        openingId: string,
        kind: string = 'WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW',
    ): CancelablePromise<FeatureCollection> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/openings/map/{openingId}',
            path: {
                'openingId': openingId,
            },
            query: {
                'kind': kind,
            },
        });
    }
}
