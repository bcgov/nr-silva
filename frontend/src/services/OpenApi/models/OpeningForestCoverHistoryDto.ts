/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CodeDescriptionDto } from './CodeDescriptionDto';
import type { OpeningForestCoverHistoryLayerListDescriptionDto } from './OpeningForestCoverHistoryLayerListDescriptionDto';
export type OpeningForestCoverHistoryDto = {
    coverId: number;
    archiveDate: string;
    polygonId: string;
    standardUnitId: string | null;
    unmappedArea: CodeDescriptionDto;
    grossArea: number;
    netArea: number;
    status: CodeDescriptionDto;
    coverType: CodeDescriptionDto;
    inventoryLayer: OpeningForestCoverHistoryLayerListDescriptionDto;
    silvicultureLayer: OpeningForestCoverHistoryLayerListDescriptionDto;
    referenceYear: number;
    isSingleLayer: boolean;
    hasReserve: boolean;
};

