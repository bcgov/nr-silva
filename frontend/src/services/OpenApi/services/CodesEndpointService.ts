/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CodeDescriptionDto } from '../models/CodeDescriptionDto';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class CodesEndpointService {
    /**
     * @returns CodeDescriptionDto OK
     * @throws ApiError
     */
    public static getOpeningOrgUnits(): CancelablePromise<Array<CodeDescriptionDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/codes/org-units',
        });
    }
    /**
     * @param includeExpired
     * @returns CodeDescriptionDto OK
     * @throws ApiError
     */
    public static getOpeningCategories(
        includeExpired: boolean = true,
    ): CancelablePromise<Array<CodeDescriptionDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/codes/opening-categories',
            query: {
                'includeExpired': includeExpired,
            },
        });
    }
}
