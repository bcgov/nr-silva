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
    crownClosure: number | null;
    basalAreaSt: number | null;
    totalStems: number | null;
    totalWellSpaced: number | null;
    wellSpaced: number | null;
    freeGrowing: number | null;
    species: Array<OpeningForestCoverHistoryDetailedSpeciesDto>;
    damage: Array<OpeningForestCoverHistoryDamageDto>;
};

