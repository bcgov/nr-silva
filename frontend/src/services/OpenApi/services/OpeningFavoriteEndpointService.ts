/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class OpeningFavoriteEndpointService {
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
