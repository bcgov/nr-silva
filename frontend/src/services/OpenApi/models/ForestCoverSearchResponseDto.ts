/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CodeDescriptionDto } from './CodeDescriptionDto';
export type ForestCoverSearchResponseDto = {
    forestCoverId?: number;
    polygonId?: string;
    standardUnitId?: string;
    damageAgents?: Array<CodeDescriptionDto>;
    stockingType?: CodeDescriptionDto;
    stockingStatus?: CodeDescriptionDto;
    fileId?: string;
    openingId?: number;
    openingCategory?: CodeDescriptionDto;
    orgUnit?: CodeDescriptionDto;
    updateTimestamp?: string;
    regenDueDate?: string;
    freeGrowingDueDate?: string;
};

