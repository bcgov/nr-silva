/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type SpeciesDuplicateConflictDto = {
    layerIndex?: number;
    layerCode?: string;
    duplicateSpeciesIndices?: Array<number>;
    reason?: string;
    conflictCode?: SpeciesDuplicateConflictDto.conflictCode;
};
export namespace SpeciesDuplicateConflictDto {
    export enum conflictCode {
        FIELD_INVALID = 'FIELD_INVALID',
        LAYER_CODE_INVALID = 'LAYER_CODE_INVALID',
        SPECIES_CODE_UNKNOWN_OR_INACTIVE = 'SPECIES_CODE_UNKNOWN_OR_INACTIVE',
        SPECIES_MIN_HEIGHT_REQUIRED = 'SPECIES_MIN_HEIGHT_REQUIRED',
        SPECIES_MIN_HEIGHT_INVALID = 'SPECIES_MIN_HEIGHT_INVALID',
        DUPLICATE_SPECIES_CODE = 'DUPLICATE_SPECIES_CODE',
    }
}

