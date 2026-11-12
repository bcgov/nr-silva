/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type StockingSpeciesDto = {
    speciesCode: string;
    speciesType: StockingSpeciesDto.speciesType;
    minHeight?: number;
};
export namespace StockingSpeciesDto {
    export enum speciesType {
        PREFERRED = 'PREFERRED',
        ACCEPTABLE = 'ACCEPTABLE',
        ECOLOGICALLY_SUITABLE = 'ECOLOGICALLY_SUITABLE',
    }
}

