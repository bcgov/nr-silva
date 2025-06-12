/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CodeDescriptionDto } from '../models/CodeDescriptionDto';
import type { ForestClientAutocompleteResultDto } from '../models/ForestClientAutocompleteResultDto';
import type { ForestClientDto } from '../models/ForestClientDto';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class ForestClientEndpointService {
    /**
     * @param clientNumber
     * @returns ForestClientDto OK
     * @throws ApiError
     */
    public static getForestClient(
        clientNumber: string,
    ): CancelablePromise<ForestClientDto> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/forest-clients/{clientNumber}',
            path: {
                'clientNumber': clientNumber,
            },
        });
    }
    /**
     * @param clientNumber
     * @returns CodeDescriptionDto OK
     * @throws ApiError
     */
    public static getForestClientLocations(
        clientNumber: string,
    ): CancelablePromise<Array<CodeDescriptionDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/forest-clients/{clientNumber}/locations',
            path: {
                'clientNumber': clientNumber,
            },
        });
    }
    /**
     * @param value
     * @param page
     * @param size
     * @returns ForestClientAutocompleteResultDto OK
     * @throws ApiError
     */
    public static searchForestClients(
        value: string,
        page?: number,
        size: number = 10,
    ): CancelablePromise<Array<ForestClientAutocompleteResultDto>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/forest-clients/byNameAcronymNumber',
            query: {
                'page': page,
                'size': size,
                'value': value,
            },
        });
    }
}
