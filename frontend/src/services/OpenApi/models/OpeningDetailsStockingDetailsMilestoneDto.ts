/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CommentDto } from './CommentDto';
export type OpeningDetailsStockingDetailsMilestoneDto = {
    postHarvestDeclaredDate: string | null;
    regenDeclaredDate: string | null;
    regenOffsetYears: number | null;
    regenDueDate: string | null;
    noRegenDeclaredDate: string | null;
    noRegenOffsetYears: number | null;
    noRegenDueDate: string | null;
    freeGrowingDeclaredDate: string | null;
    freeGrowingOffsetYears: number | null;
    freeGrowingDueDate: string | null;
    noRegenIndicated: boolean;
    extentDeclared: boolean;
    comments: Array<CommentDto>;
};

