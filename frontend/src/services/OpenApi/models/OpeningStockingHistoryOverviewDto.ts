/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CodeDescriptionDto } from './CodeDescriptionDto';
export type OpeningStockingHistoryOverviewDto = {
    stockingEventHistoryId: number;
    amendmentNumber: number | null;
    eventTimestamp: string | null;
    suCount: number | null;
    totalNar: number | null;
    auditAction: CodeDescriptionDto;
    esfSubmissionId: string | null;
    submittedByUserId: string | null;
    approvedByUserId: string | null;
    isLatest: boolean;
    isOldest: boolean;
};

