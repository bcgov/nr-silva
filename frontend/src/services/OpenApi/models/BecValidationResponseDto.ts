/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BecDuplicateConflictDto } from './BecDuplicateConflictDto';
import type { BecValidationErrorDto } from './BecValidationErrorDto';
import type { BecValidationResultDto } from './BecValidationResultDto';
export type BecValidationResponseDto = {
    validationResults?: Array<BecValidationResultDto>;
    duplicateConflicts?: Array<BecDuplicateConflictDto>;
    validationErrors?: Array<BecValidationErrorDto>;
    isValid?: boolean;
};

