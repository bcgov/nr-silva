/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { OpeningForestCoverLayerDto } from './OpeningForestCoverLayerDto';
import type { OpeningForestCoverPolygonDto } from './OpeningForestCoverPolygonDto';
import type { OpeningForestCoverUnmappedDto } from './OpeningForestCoverUnmappedDto';
export type OpeningForestCoverDetailsDto = {
    polygon: OpeningForestCoverPolygonDto;
    isSingleLayer: boolean;
    unmapped: Array<OpeningForestCoverUnmappedDto>;
    layers: Array<OpeningForestCoverLayerDto>;
};

