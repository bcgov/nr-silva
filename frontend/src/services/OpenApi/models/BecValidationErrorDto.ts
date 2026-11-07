/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type BecValidationErrorDto = {
    errorCode?: BecValidationErrorDto.errorCode;
    errorMessage?: string;
};
export namespace BecValidationErrorDto {
    export enum errorCode {
        FIELD_INVALID = 'FIELD_INVALID',
        BEC_COMBINATION_NOT_FOUND = 'BEC_COMBINATION_NOT_FOUND',
        DUPLICATE_BEC_COMBINATION = 'DUPLICATE_BEC_COMBINATION',
    }
}

