/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { OpeningDetailsBecDto } from './OpeningDetailsBecDto';
export type OpeningDetailsStockingDetailsDto = {
    stockingStandardUnit: string | null;
    ssid: number | null;
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
};

