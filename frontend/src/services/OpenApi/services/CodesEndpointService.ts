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
    public static getSilvTechniqueCodes(): CancelablePromise<Array<CodeDescriptionDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/codes/silv-technique',
        });
    }
    /**
     * @returns CodeDescriptionDto OK
     * @throws ApiError
     */
    public static getSilvObjectiveCodes(): CancelablePromise<Array<CodeDescriptionDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/codes/silv-objective',
        });
    }
    /**
     * @returns CodeDescriptionDto OK
     * @throws ApiError
     */
    public static getSilvMethodCodes(): CancelablePromise<Array<CodeDescriptionDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/codes/silv-method',
        });
    }
    /**
     * @returns CodeDescriptionDto OK
     * @throws ApiError
     */
    public static getSilvFundSourceCodes(): CancelablePromise<Array<CodeDescriptionDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/codes/silv-fund-source',
        });
    }
    /**
     * @returns CodeDescriptionDto OK
     * @throws ApiError
     */
    public static getSilvBaseCodes(): CancelablePromise<Array<CodeDescriptionDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/codes/silv-base',
        });
    }
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
