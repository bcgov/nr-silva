/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BecDataDto } from '../models/BecDataDto';
import type { BecValidationResponseDto } from '../models/BecValidationResponseDto';
import type { StockingSpeciesLayerValidationRequestDto } from '../models/StockingSpeciesLayerValidationRequestDto';
import type { StockingSpeciesValidationResponseDto } from '../models/StockingSpeciesValidationResponseDto';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class StockingStandardValidationEndpointService {
    /**
     * Validate stocking-standard species entries
     * @param requestBody
     * @returns StockingSpeciesValidationResponseDto Layer-aware species validation results and duplicate conflicts
     * @throws ApiError
     */
    public static validateSpecies(
        requestBody: Array<StockingSpeciesLayerValidationRequestDto>,
    ): CancelablePromise<StockingSpeciesValidationResponseDto> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/stocking-standards/validate/species',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * Validate stocking-standard BEC combinations
     * @param requestBody
     * @returns BecValidationResponseDto Per-entry BEC validation results and duplicate conflicts
     * @throws ApiError
     */
    public static validateBec(
        requestBody: Array<BecDataDto>,
    ): CancelablePromise<BecValidationResponseDto> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/stocking-standards/validate/bec',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
}
