/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { StockingSpeciesDto } from './StockingSpeciesDto';
export type StockingLayerDto = {
    layerCode: string;
    minWellSpacedTrees?: number;
    minPreferredWellSpacedTrees?: number;
    minHorizontalDistance?: number;
    targetWellSpacedTrees?: number;
    minResidualBasalArea?: number;
    minPostSpacingDensity?: number;
    maxPostSpacingDensity?: number;
    maxConiferous?: number;
    heightRelativeToComp?: number;
    heightRelativeToCompUnitCode?: string;
    species?: Array<StockingSpeciesDto>;
};

