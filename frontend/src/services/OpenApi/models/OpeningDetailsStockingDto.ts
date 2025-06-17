/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CommentDto } from './CommentDto';
import type { OpeningDetailsStockingDetailsDto } from './OpeningDetailsStockingDetailsDto';
import type { OpeningDetailsStockingLayerDto } from './OpeningDetailsStockingLayerDto';
import type { OpeningDetailsStockingSpeciesDto } from './OpeningDetailsStockingSpeciesDto';
export type OpeningDetailsStockingDto = {
    stocking: OpeningDetailsStockingDetailsDto;
    preferredSpecies: Array<OpeningDetailsStockingSpeciesDto>;
    acceptableSpecies: Array<OpeningDetailsStockingSpeciesDto>;
    layers: Array<OpeningDetailsStockingLayerDto>;
    comments: Array<CommentDto>;
};

