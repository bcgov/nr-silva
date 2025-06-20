/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CodeDescriptionDto } from './CodeDescriptionDto';
import type { OpeningForestCoverDamageDto } from './OpeningForestCoverDamageDto';
import type { OpeningForestCoverDetailedSpeciesDto } from './OpeningForestCoverDetailedSpeciesDto';
export type OpeningForestCoverLayerDto = {
    layerId: number;
    layer: CodeDescriptionDto;
    crownClosure: number | number | null;
    basalAreaSt: number | number | null;
    totalStems: number | number | null;
    totalWellSpaced: number | number | null;
    wellSpaced: number | number | null;
    freeGrowing: number | number | null;
    species: Array<OpeningForestCoverDetailedSpeciesDto>;
    damage: Array<OpeningForestCoverDamageDto>;
};

