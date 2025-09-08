/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CommentDto } from './CommentDto';
import type { OpeningStockingHistoryDetailsDto } from './OpeningStockingHistoryDetailsDto';
import type { OpeningStockingHistoryLayerDto } from './OpeningStockingHistoryLayerDto';
import type { OpeningStockingHistorySpeciesDto } from './OpeningStockingHistorySpeciesDto';
export type OpeningStockingHistoryDto = {
    stocking: OpeningStockingHistoryDetailsDto;
    preferredSpecies: Array<OpeningStockingHistorySpeciesDto>;
    acceptableSpecies: Array<OpeningStockingHistorySpeciesDto>;
    layers: Array<OpeningStockingHistoryLayerDto>;
    comments: Array<CommentDto>;
};

