/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CodeDescriptionDto } from './CodeDescriptionDto';
import type { OpeningForestCoverLayerListDescriptionDto } from './OpeningForestCoverLayerListDescriptionDto';
export type OpeningForestCoverDto = {
    coverId: number;
    polygonId: string;
    standardUnitId: string | null;
    unmappedArea: CodeDescriptionDto;
    grossArea: number;
    netArea: number;
    status: CodeDescriptionDto;
    coverType: CodeDescriptionDto;
    inventoryLayer: OpeningForestCoverLayerListDescriptionDto;
    silvicultureLayer: OpeningForestCoverLayerListDescriptionDto;
    referenceYear: number;
    isSingleLayer: boolean;
    hasReserve: boolean;
};

