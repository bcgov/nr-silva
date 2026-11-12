/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type BecDuplicateConflictDto = {
    duplicateIndices?: Array<number>;
    reason?: string;
    conflictCode?: BecDuplicateConflictDto.conflictCode;
};
export namespace BecDuplicateConflictDto {
    export enum conflictCode {
        FIELD_INVALID = 'FIELD_INVALID',
        BEC_COMBINATION_NOT_FOUND = 'BEC_COMBINATION_NOT_FOUND',
        DUPLICATE_BEC_COMBINATION = 'DUPLICATE_BEC_COMBINATION',
    }
}

