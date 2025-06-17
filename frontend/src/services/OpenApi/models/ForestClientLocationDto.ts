/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type ForestClientLocationDto = {
    clientNumber: string | null;
    locationCode: string | null;
    locationName: string | null;
    companyCode: string | null;
    address1: string | null;
    address2: string | null;
    address3: string | null;
    city: string | null;
    province: string | null;
    postalCode: string | null;
    country: string | null;
    businessPhone: string | null;
    homePhone: string | null;
    cellPhone: string | null;
    faxNumber: string | null;
    email: string | null;
    expired: ForestClientLocationDto.expired;
    trusted: ForestClientLocationDto.trusted;
    returnedMailDate: string | null;
    comment: string | null;
};
export namespace ForestClientLocationDto {
    export enum expired {
        Y = 'Y',
        N = 'N',
    }
    export enum trusted {
        Y = 'Y',
        N = 'N',
    }
}

