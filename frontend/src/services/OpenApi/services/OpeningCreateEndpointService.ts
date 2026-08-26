/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CreateOpeningRequestDto } from '../models/CreateOpeningRequestDto';
import type { CreateOpeningResponseDto } from '../models/CreateOpeningResponseDto';
import type { ExtractedGeoDataDto } from '../models/ExtractedGeoDataDto';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class OpeningCreateEndpointService {
    /**
     * @param formData
     * @returns CreateOpeningResponseDto Created
     * @throws ApiError
     */
    public static createOpening(
        formData?: {
            data: CreateOpeningRequestDto;
            /**
             * Spatial file (GeoJSON or GML)
             */
            file: Blob;
        },
    ): CancelablePromise<CreateOpeningResponseDto> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/openings',
            formData: formData,
            mediaType: 'multipart/form-data',
        });
    }
    /**
     * @param formData
     * @returns ExtractedGeoDataDto Accepted
     * @throws ApiError
     */
    public static uploadOpeningSpatialFile(
        formData?: {
            file: Blob;
        },
    ): CancelablePromise<ExtractedGeoDataDto> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/openings/create/upload',
            formData: formData,
            mediaType: 'multipart/form-data',
        });
    }
}
