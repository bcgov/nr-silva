/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CodeDescriptionDto } from './CodeDescriptionDto';
import type { ForestClientDto } from './ForestClientDto';
export type StockingStandardsSearchResponseDto = {
    standardsRegimeId?: number;
    standardsRegimeName?: string;
    isExpired?: boolean;
    standardsObjective?: string;
    preferredSpecies?: Array<CodeDescriptionDto>;
    fspIds?: Array<string>;
    bgcZone?: string;
    bgcSubZone?: string;
    bgcVariant?: string;
    bgcPhase?: string;
    becSiteSeries?: string;
    becSiteType?: string;
    becSeral?: string;
    orgUnits?: Array<CodeDescriptionDto>;
    clients?: Array<ForestClientDto>;
    updateTimestamp?: string;
};

