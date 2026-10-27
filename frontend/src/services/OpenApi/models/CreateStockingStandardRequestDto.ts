/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BecDataDto } from './BecDataDto';
import type { StockingLayerDto } from './StockingLayerDto';
export type CreateStockingStandardRequestDto = {
    objective: string;
    name?: string;
    location?: string;
    authorityType: CreateStockingStandardRequestDto.authorityType;
    orgUnitCodes?: Array<string>;
    clientNumbers?: Array<string>;
    becInfoSelected: boolean;
    alternativeMethodSelected: boolean;
    becData?: Array<BecDataDto>;
    stockingType: CreateStockingStandardRequestDto.stockingType;
    regenDelayYears?: number;
    freeGrowingYears?: number;
    earlyYears?: number;
    lateYears?: number;
    layerType: CreateStockingStandardRequestDto.layerType;
    singleLayer?: StockingLayerDto;
    multiLayers?: Array<StockingLayerDto>;
    additionalStandards?: string;
};
export namespace CreateStockingStandardRequestDto {
    export enum authorityType {
        OPERATIONAL_PLAN = 'OPERATIONAL_PLAN',
        MINISTRY_DEFAULT_PROVINCIAL = 'MINISTRY_DEFAULT_PROVINCIAL',
        MINISTRY_DEFAULT_OTHERS = 'MINISTRY_DEFAULT_OTHERS',
    }
    export enum stockingType {
        REGEN_OBLIGATION = 'REGEN_OBLIGATION',
        STOCKING_REQUIREMENT = 'STOCKING_REQUIREMENT',
    }
    export enum layerType {
        SINGLE = 'SINGLE',
        MULTI = 'MULTI',
    }
}

