/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { TenureRequestDto } from '../models/TenureRequestDto';
import type { TenureValidationResponseDto } from '../models/TenureValidationResponseDto';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class TenureEndpointService {
    /**
     * @param clientNumber
     * @param requestBody
     * @returns TenureValidationResponseDto OK
     * @throws ApiError
     */
    public static validateTenures(
        clientNumber: string,
        requestBody: Array<TenureRequestDto>,
    ): CancelablePromise<TenureValidationResponseDto> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/tenures/validate',
            query: {
                'clientNumber': clientNumber,
            },
            body: requestBody,
            mediaType: 'application/json',
        });
    }
}
