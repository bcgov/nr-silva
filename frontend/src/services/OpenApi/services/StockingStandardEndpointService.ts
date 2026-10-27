/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CreateStockingStandardRequestDto } from '../models/CreateStockingStandardRequestDto';
import type { CreateStockingStandardResponseDto } from '../models/CreateStockingStandardResponseDto';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class StockingStandardEndpointService {
    /**
     * @param requestBody
     * @returns CreateStockingStandardResponseDto Created
     * @throws ApiError
     */
    public static createStockingStandard(
        requestBody: CreateStockingStandardRequestDto,
    ): CancelablePromise<CreateStockingStandardResponseDto> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/stocking-standards',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
}
