/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CodeDescriptionDto } from './CodeDescriptionDto';
import type { ForestClientDto } from './ForestClientDto';
export type StandardUnitSearchResponseDto = {
    stockingStandardUnitId?: number;
    openingId?: number;
    fileId?: string;
    cutBlock?: string;
    cuttingPermit?: string;
    standardsUnitId?: string;
    standardsRegimeId?: number;
    isStandardsRegimeExpired?: boolean;
    netArea?: number;
    regenDueDate?: string;
    freeGrowingDueDate?: string;
    totalLayer?: number;
    preferredSpecies?: Array<CodeDescriptionDto>;
    bgcZone?: string;
    bgcSubZone?: string;
    bgcVariant?: string;
    bgcPhase?: string;
    becSiteSeries?: string;
    becSiteType?: string;
    becSeral?: string;
    orgUnit?: CodeDescriptionDto;
    openingClient?: ForestClientDto;
    updateTimestamp?: string;
};

