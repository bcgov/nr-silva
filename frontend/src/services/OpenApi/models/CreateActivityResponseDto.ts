/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type CreateActivityResponseDto = {
    activityId?: number;
    openingId?: number;
    status?: CreateActivityResponseDto.status;
    baseCode?: string;
};
export namespace CreateActivityResponseDto {
    export enum status {
        PLANNED = 'PLANNED',
        COMPLETED = 'COMPLETED',
    }
}

