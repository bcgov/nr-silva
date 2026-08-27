/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type TenureRemovalValidationResultDto = {
    cboaId?: number;
    errorCode?: TenureRemovalValidationResultDto.errorCode;
    errorMessage?: string;
};
export namespace TenureRemovalValidationResultDto {
    export enum errorCode {
        FIELD_INVALID = 'FIELD_INVALID',
        TENURE_NOT_FOUND = 'TENURE_NOT_FOUND',
        CLIENT_NOT_LICENSEE = 'CLIENT_NOT_LICENSEE',
        TENURE_DUPLICATE_OPENING = 'TENURE_DUPLICATE_OPENING',
        DUPLICATE_IN_REQUEST = 'DUPLICATE_IN_REQUEST',
        DISTURBANCE_EXISTS = 'DISTURBANCE_EXISTS',
        STALE_TENURE = 'STALE_TENURE',
        TENURE_NOT_ASSOCIATED = 'TENURE_NOT_ASSOCIATED',
    }
}

