/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { OpeningsPerYearDto } from '../models/OpeningsPerYearDto';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class UserActionsEndpointService {
    /**
     * @param orgUnitCode
     * @param statusCode
     * @param entryDateStart
     * @param entryDateEnd
     * @returns OpeningsPerYearDto OK
     * @throws ApiError
     */
    public static getOpeningsSubmissionTrends(
        orgUnitCode?: Array<string>,
        statusCode?: Array<string>,
        entryDateStart?: string,
        entryDateEnd?: string,
    ): CancelablePromise<Array<OpeningsPerYearDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/users/submission-trends',
            query: {
                'orgUnitCode': orgUnitCode,
                'statusCode': statusCode,
                'entryDateStart': entryDateStart,
                'entryDateEnd': entryDateEnd,
            },
        });
    }
}
