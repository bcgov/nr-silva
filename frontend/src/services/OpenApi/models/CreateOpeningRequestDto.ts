/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { TenureRequestDto } from './TenureRequestDto';
/**
 * Opening creation request (JSON)
 */
export type CreateOpeningRequestDto = {
    openingGrossArea: number;
    maxAllowablePermAccessPerc: number;
    clientNumber: string;
    orgUnitCode: string;
    openingCategoryCode: string;
    licenseeOpeningId?: string;
    tenures: Array<TenureRequestDto>;
};

