/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { StockingSpeciesValidationResultDto } from './StockingSpeciesValidationResultDto';
export type StockingSpeciesLayerValidationResultDto = {
    layerIndex?: number;
    layerCode?: string;
    isValid?: boolean;
    layerErrorCode?: StockingSpeciesLayerValidationResultDto.layerErrorCode;
    layerErrorMessage?: string;
    speciesValidationResults?: Array<StockingSpeciesValidationResultDto>;
};
export namespace StockingSpeciesLayerValidationResultDto {
    export enum layerErrorCode {
        FIELD_INVALID = 'FIELD_INVALID',
        LAYER_CODE_INVALID = 'LAYER_CODE_INVALID',
        SPECIES_CODE_UNKNOWN_OR_INACTIVE = 'SPECIES_CODE_UNKNOWN_OR_INACTIVE',
        SPECIES_MIN_HEIGHT_REQUIRED = 'SPECIES_MIN_HEIGHT_REQUIRED',
        SPECIES_MIN_HEIGHT_INVALID = 'SPECIES_MIN_HEIGHT_INVALID',
        DUPLICATE_SPECIES_CODE = 'DUPLICATE_SPECIES_CODE',
    }
}

