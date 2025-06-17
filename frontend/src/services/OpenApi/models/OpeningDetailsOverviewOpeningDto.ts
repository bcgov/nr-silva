/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CodeDescriptionDto } from './CodeDescriptionDto';
import type { CommentDto } from './CommentDto';
export type OpeningDetailsOverviewOpeningDto = {
    licenseeId: string | null;
    tenureType: CodeDescriptionDto;
    managementUnitType: CodeDescriptionDto;
    managementUnitId: string | null;
    timberSaleOffice: CodeDescriptionDto;
    comments: Array<CommentDto>;
};

