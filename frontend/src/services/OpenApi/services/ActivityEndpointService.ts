/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { ActivitySpatialUploadResponseDto } from '../models/ActivitySpatialUploadResponseDto';
import type { CreateActivityRequestDto } from '../models/CreateActivityRequestDto';
import type { CreateActivityResponseDto } from '../models/CreateActivityResponseDto';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class ActivityEndpointService {
    /**
     * Create an activity
     * Creates a planned or completed activity with a basic base code under an opening. Checks run in this order: the opening exists (404), the user has access to it (403), the optional spatial file is read and scanned for viruses (400/422), and then the request and the file are validated (400). Nothing is saved when the request or the file is invalid.
     * @param openingId Opening the activity is created under
     * @param formData
     * @returns CreateActivityResponseDto Activity created
     * @throws ApiError
     */
    public static createActivity(
        openingId: number,
        formData?: {
            data: CreateActivityRequestDto;
            /**
             * Optional spatial file (GeoJSON or GML), up to 25MB; an empty file is ignored
             */
            file?: Blob;
        },
    ): CancelablePromise<CreateActivityResponseDto> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/openings/{openingId}/activities',
            path: {
                'openingId': openingId,
            },
            formData: formData,
            mediaType: 'multipart/form-data',
            errors: {
                400: `Invalid request or spatial file; every violation is listed as 'field: message'`,
                403: `The user has no role for the licensee or obligation holder clients of the opening`,
                404: `Opening not found`,
                422: `Upload rejected by the virus scanner`,
            },
        });
    }
    /**
     * Upload an activity spatial file
     * Validates and processes one Polygon or MultiPolygon feature from a GeoJSON or GML file. Returns the geometry in EPSG:4326 and its area in hectares, with the gross area and the geometry of the opening, without persisting data.
     * @param openingId Opening the activity is created under
     * @param formData
     * @returns ActivitySpatialUploadResponseDto Processed spatial geometry and opening data
     * @throws ApiError
     */
    public static uploadActivitySpatial(
        openingId: number,
        formData?: {
            /**
             * Spatial file (GeoJSON or GML), up to 25MB
             */
            file: Blob;
        },
    ): CancelablePromise<ActivitySpatialUploadResponseDto> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/openings/{openingId}/activities/spatial/upload',
            path: {
                'openingId': openingId,
            },
            formData: formData,
            mediaType: 'multipart/form-data',
            errors: {
                400: `Invalid spatial file`,
                404: `Opening not found`,
                422: `Upload rejected by the virus scanner`,
            },
        });
    }
}
