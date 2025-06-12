/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CommentDto } from './CommentDto';
import type { ForestClientDto } from './ForestClientDto';
import type { ForestClientLocationDto } from './ForestClientLocationDto';
export type OpeningActivityBaseDto = {
    licenseeActivityId: string | null;
    intraAgencyNumber: string | null;
    activityClient: ForestClientDto;
    activityLocation: ForestClientLocationDto;
    plannedAmount: number | null;
    treatedAmount: number | null;
    plannedCost: number | null;
    actualCost: number | null;
    totalPlanting: number | null;
    comments: Array<CommentDto>;
};

