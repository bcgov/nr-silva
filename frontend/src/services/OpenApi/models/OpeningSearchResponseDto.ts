/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CodeDescriptionDto } from './CodeDescriptionDto';
export type OpeningSearchResponseDto = {
    openingId: number;
    mapsheetKey: string | null;
    category: CodeDescriptionDto;
    status: CodeDescriptionDto;
    licenseeOpeningId: string | null;
    cuttingPermitId: string | null;
    timberMark: string | null;
    cutBlockId: string | null;
    openingGrossAreaHa: number | null;
    disturbanceGrossArea: number | null;
    disturbanceStartDate: string | null;
    orgUnitCode: string;
    orgUnitName: string;
    clientNumber: string | null;
    clientLocation: string | null;
    clientAcronym: string | null;
    clientName: string | null;
    regenDelayDate: string;
    earlyFreeGrowingDate: string | null;
    lateFreeGrowingDate: string | null;
    updateTimestamp: string;
    entryUserId: string;
    entryTimestamp: string | null;
    submittedToFrpa: boolean;
    forestFileId: string | null;
    silvaReliefAppId: number | null;
    lastViewDate: string | null;
    valid?: boolean;
};

