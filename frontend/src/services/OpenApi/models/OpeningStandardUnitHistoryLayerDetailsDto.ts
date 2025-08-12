/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CodeDescriptionDto } from './CodeDescriptionDto';
import type { OpeningStandardUnitHistorySpeciesDetailsDto } from './OpeningStandardUnitHistorySpeciesDetailsDto';
export type OpeningStandardUnitHistoryLayerDetailsDto = {
    oldLayerId: number | number | null;
    newLayerId: number | number | null;
    oldStockingLayer: CodeDescriptionDto;
    newStockingLayer: CodeDescriptionDto;
    oldMinHorizontalDistance: number | null;
    newMinHorizontalDistance: number | null;
    oldMinPerfStockingStandard: number | number | null;
    newMinPerfStockingStandard: number | number | null;
    oldMinStockingStandard: number | number | null;
    newMinStockingStandard: number | number | null;
    oldMinPostSpacing: number | number | null;
    newMinPostSpacing: number | number | null;
    oldResidualBasalArea: number | null;
    newResidualBasalArea: number | null;
    oldTargetWellSpacedTrees: number | number | null;
    newTargetWellSpacedTrees: number | number | null;
    oldHeightRelativeToComp: number | number | null;
    newHeightRelativeToComp: number | number | null;
    oldMaxConifer: number | number | null;
    newMaxConifer: number | number | null;
    oldMaxPostSpacing: number | number | null;
    newMaxPostSpacing: number | number | null;
    preferredSpecies: Array<OpeningStandardUnitHistorySpeciesDetailsDto>;
    acceptableSpecies: Array<OpeningStandardUnitHistorySpeciesDetailsDto>;
};

