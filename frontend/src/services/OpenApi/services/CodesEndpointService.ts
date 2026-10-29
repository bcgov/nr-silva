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
     * @param effectiveOnly
     * @returns CodeDescriptionDto OK
     * @throws ApiError
     */
    public static getStockingTypeCodes(
        effectiveOnly: boolean = false,
    ): CancelablePromise<Array<CodeDescriptionDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/codes/stocking-type',
            query: {
                'effectiveOnly': effectiveOnly,
            },
        });
    }
    /**
     * @param effectiveOnly
     * @returns CodeDescriptionDto OK
     * @throws ApiError
     */
    public static getStockingStatusCodes(
        effectiveOnly: boolean = false,
    ): CancelablePromise<Array<CodeDescriptionDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/codes/stocking-status',
            query: {
                'effectiveOnly': effectiveOnly,
            },
        });
    }
    /**
     * @param effectiveOnly
     * @returns CodeDescriptionDto OK
     * @throws ApiError
     */
    public static getSilvTreeSpeciesCodes(
        effectiveOnly: boolean = false,
    ): CancelablePromise<Array<CodeDescriptionDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/codes/silv-tree-species',
            query: {
                'effectiveOnly': effectiveOnly,
            },
        });
    }
    /**
     * @param effectiveOnly
     * @returns CodeDescriptionDto OK
     * @throws ApiError
     */
    public static getSilvTechniqueCodes(
        effectiveOnly: boolean = false,
    ): CancelablePromise<Array<CodeDescriptionDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/codes/silv-technique',
            query: {
                'effectiveOnly': effectiveOnly,
            },
        });
    }
    /**
     * @param effectiveOnly
     * @returns CodeDescriptionDto OK
     * @throws ApiError
     */
    public static getSilvSystemCodes(
        effectiveOnly: boolean = false,
    ): CancelablePromise<Array<CodeDescriptionDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/codes/silv-system',
            query: {
                'effectiveOnly': effectiveOnly,
            },
        });
    }
    /**
     * @param effectiveOnly
     * @returns CodeDescriptionDto OK
     * @throws ApiError
     */
    public static getSilvSystemVariantCodes(
        effectiveOnly: boolean = false,
    ): CancelablePromise<Array<CodeDescriptionDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/codes/silv-system-variant',
            query: {
                'effectiveOnly': effectiveOnly,
            },
        });
    }
    /**
     * @param effectiveOnly
     * @returns CodeDescriptionDto OK
     * @throws ApiError
     */
    public static getSilvObjectiveCodes(
        effectiveOnly: boolean = false,
    ): CancelablePromise<Array<CodeDescriptionDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/codes/silv-objective',
            query: {
                'effectiveOnly': effectiveOnly,
            },
        });
    }
    /**
     * @param effectiveOnly
     * @returns CodeDescriptionDto OK
     * @throws ApiError
     */
    public static getSilvMethodCodes(
        effectiveOnly: boolean = false,
    ): CancelablePromise<Array<CodeDescriptionDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/codes/silv-method',
            query: {
                'effectiveOnly': effectiveOnly,
            },
        });
    }
    /**
     * @param effectiveOnly
     * @returns CodeDescriptionDto OK
     * @throws ApiError
     */
    public static getSilvFundSourceCodes(
        effectiveOnly: boolean = false,
    ): CancelablePromise<Array<CodeDescriptionDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/codes/silv-fund-source',
            query: {
                'effectiveOnly': effectiveOnly,
            },
        });
    }
    /**
     * @param effectiveOnly
     * @returns CodeDescriptionDto OK
     * @throws ApiError
     */
    public static getSilvDamageAgentCodes(
        effectiveOnly: boolean = false,
    ): CancelablePromise<Array<CodeDescriptionDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/codes/silv-damage-agent',
            query: {
                'effectiveOnly': effectiveOnly,
            },
        });
    }
    /**
     * @param effectiveOnly
     * @returns CodeDescriptionDto OK
     * @throws ApiError
     */
    public static getSilvCutPhaseCodes(
        effectiveOnly: boolean = false,
    ): CancelablePromise<Array<CodeDescriptionDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/codes/silv-cut-phase',
            query: {
                'effectiveOnly': effectiveOnly,
            },
        });
    }
    /**
     * @param effectiveOnly
     * @returns CodeDescriptionDto OK
     * @throws ApiError
     */
    public static getSilvBaseCodes(
        effectiveOnly: boolean = false,
    ): CancelablePromise<Array<CodeDescriptionDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/codes/silv-base',
            query: {
                'effectiveOnly': effectiveOnly,
            },
        });
    }
    /**
     * @param type
     * @returns CodeDescriptionDto OK
     * @throws ApiError
     */
    public static getOpeningOrgUnits(
        type: 'all' | 'district' = 'all',
    ): CancelablePromise<Array<CodeDescriptionDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/codes/org-units',
            query: {
                'type': type,
            },
        });
    }
    /**
     * @param effectiveOnly
     * @returns CodeDescriptionDto OK
     * @throws ApiError
     */
    public static getOpeningStatusCodes(
        effectiveOnly: boolean = false,
    ): CancelablePromise<Array<CodeDescriptionDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/codes/opening-status',
            query: {
                'effectiveOnly': effectiveOnly,
            },
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
    /**
     * @param effectiveOnly
     * @returns CodeDescriptionDto OK
     * @throws ApiError
     */
    public static getDisturbanceCodes(
        effectiveOnly: boolean = false,
    ): CancelablePromise<Array<CodeDescriptionDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/codes/disturbance',
            query: {
                'effectiveOnly': effectiveOnly,
            },
        });
    }
}
