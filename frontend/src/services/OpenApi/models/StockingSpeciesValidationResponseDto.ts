/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { SpeciesDuplicateConflictDto } from './SpeciesDuplicateConflictDto';
import type { StockingSpeciesLayerValidationResultDto } from './StockingSpeciesLayerValidationResultDto';
export type StockingSpeciesValidationResponseDto = {
    validationResults?: Array<StockingSpeciesLayerValidationResultDto>;
    duplicateConflicts?: Array<SpeciesDuplicateConflictDto>;
    isValid?: boolean;
};

