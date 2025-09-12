/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { OpeningDetailsBecDto } from './OpeningDetailsBecDto';
export type OpeningStockingHistoryDetailsDto = {
    stockingStandardUnit: string | null;
    ssuId: number | null;
    srid: number | null;
    defaultMof: boolean;
    manualEntry: boolean;
    fspId: number | null;
    netArea: number | null;
    soilDisturbancePercent: number | null;
    bec: OpeningDetailsBecDto;
    regenDelay: number | null;
    freeGrowingLate: number | null;
    freeGrowingEarly: number | null;
    additionalStandards: string | null;
    amendmentComment: string | null;
};

