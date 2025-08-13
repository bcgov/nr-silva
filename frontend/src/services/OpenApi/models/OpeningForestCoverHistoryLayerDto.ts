/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CodeDescriptionDto } from './CodeDescriptionDto';
import type { OpeningForestCoverHistoryDamageDto } from './OpeningForestCoverHistoryDamageDto';
import type { OpeningForestCoverHistoryDetailedSpeciesDto } from './OpeningForestCoverHistoryDetailedSpeciesDto';
export type OpeningForestCoverHistoryLayerDto = {
    layerId: number;
    layer: CodeDescriptionDto;
    crownClosure: number | number | null;
    basalAreaSt: number | number | null;
    totalStems: number | number | null;
    totalWellSpaced: number | number | null;
    wellSpaced: number | number | null;
    freeGrowing: number | number | null;
    species: Array<OpeningForestCoverHistoryDetailedSpeciesDto>;
    damage: Array<OpeningForestCoverHistoryDamageDto>;
};

