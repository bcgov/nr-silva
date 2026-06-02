/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CodeDescriptionDto } from './CodeDescriptionDto';
import type { ForestClientDto } from './ForestClientDto';
export type StockingStandardsCommentSearchResponseDto = {
    standardsRegimeId?: number;
    commentLocation?: StockingStandardsCommentSearchResponseDto.commentLocation;
    isExpired?: boolean;
    commentText?: string;
    updateTimestamp?: string;
    approvedTimestamp?: string;
    clients?: Array<ForestClientDto>;
    orgUnits?: Array<CodeDescriptionDto>;
    fspIds?: Array<string>;
};
export namespace StockingStandardsCommentSearchResponseDto {
    export enum commentLocation {
        STANDARDS_NAME = 'STANDARDS_NAME',
        ADDITIONAL_STANDARDS = 'ADDITIONAL_STANDARDS',
        STANDARDS_OBJECTIVE = 'STANDARDS_OBJECTIVE',
    }
}

