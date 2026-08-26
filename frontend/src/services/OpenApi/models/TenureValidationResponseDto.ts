/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { DuplicateConflictDto } from './DuplicateConflictDto';
import type { TenureDto } from './TenureDto';
import type { TenureValidationResultDto } from './TenureValidationResultDto';
export type TenureValidationResponseDto = {
    validationResults?: Array<TenureValidationResultDto>;
    duplicateConflicts?: Array<DuplicateConflictDto>;
    isValid?: boolean;
    tenures?: Array<TenureDto>;
};

