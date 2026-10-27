/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { TenureRequestDto } from '../models/TenureRequestDto';
import type { TenureUpdateItemDto } from '../models/TenureUpdateItemDto';
import type { TenureValidationResponseDto } from '../models/TenureValidationResponseDto';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class TenureEndpointService {
    /**
     * @param openingId
     * @param clientNumber
     * @param requestBody
     * @returns void
     * @throws ApiError
     */
    public static updateTenures(
        openingId: number,
        clientNumber: string,
        requestBody: Array<TenureUpdateItemDto>,
    ): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/api/openings/{openingId}/tenures',
            path: {
                'openingId': openingId,
            },
            query: {
                'clientNumber': clientNumber,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                422: `Validation failed`,
            },
        });
    }
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
