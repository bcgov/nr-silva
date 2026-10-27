/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
/**
 * Activity creation request (JSON)
 */
export type CreateActivityRequestDto = {
    status: CreateActivityRequestDto.status;
    baseCode: string;
    techniqueCode?: string;
    methodCode?: string;
    objectiveCodes?: Array<string>;
    fundingSourceCodes: Array<string>;
    projectNumber?: string;
    licenseeActivityId?: string;
    comment?: string;
    plannedDate?: string;
    plannedArea?: number;
    plannedCost?: number;
    completionDate?: string;
    treatedArea?: number;
    actualCost?: number;
};
export namespace CreateActivityRequestDto {
    export enum status {
        PLANNED = 'PLANNED',
        COMPLETED = 'COMPLETED',
    }
}

