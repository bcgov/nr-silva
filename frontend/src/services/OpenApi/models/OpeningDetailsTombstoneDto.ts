/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CodeDescriptionDto } from './CodeDescriptionDto';
import type { ForestClientDto } from './ForestClientDto';
export type OpeningDetailsTombstoneDto = {
    openingNumber: string | null;
    openingStatus: CodeDescriptionDto;
    orgUnitCode: string | null;
    orgUnitName: string | null;
    openCategory: CodeDescriptionDto;
    client: ForestClientDto;
    fileId: string | null;
    cutBlockID: string | null;
    cuttingPermitId: string | null;
    timberMark: string | null;
    maxAllowedAccess: string | null;
    openingGrossArea: number | null;
    createdBy: string | null;
    createdOn: string | null;
    lastUpdatedOn: string | null;
    disturbanceStartDate: string | null;
};

