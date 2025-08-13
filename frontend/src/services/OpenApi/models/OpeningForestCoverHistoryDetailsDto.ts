/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { OpeningForestCoverHistoryLayerDto } from './OpeningForestCoverHistoryLayerDto';
import type { OpeningForestCoverHistoryPolygonDto } from './OpeningForestCoverHistoryPolygonDto';
import type { OpeningForestCoverHistoryUnmappedDto } from './OpeningForestCoverHistoryUnmappedDto';
export type OpeningForestCoverHistoryDetailsDto = {
    polygon: OpeningForestCoverHistoryPolygonDto;
    isSingleLayer: boolean;
    unmapped: Array<OpeningForestCoverHistoryUnmappedDto>;
    layers: Array<OpeningForestCoverHistoryLayerDto>;
};

