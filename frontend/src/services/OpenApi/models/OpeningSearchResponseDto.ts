/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CodeDescriptionDto } from './CodeDescriptionDto';
export type OpeningSearchResponseDto = {
    openingId: number;
    openingNumber: string | null;
    category: CodeDescriptionDto;
    status: CodeDescriptionDto;
    cuttingPermitId: string | null;
    timberMark: string | null;
    cutBlockId: string | null;
    openingGrossAreaHa: number | null;
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
    submittedToFrpa: boolean;
    forestFileId: string | null;
    silvaReliefAppId: number | null;
    lastViewDate: string | null;
    favourite: boolean;
    valid?: boolean;
};

