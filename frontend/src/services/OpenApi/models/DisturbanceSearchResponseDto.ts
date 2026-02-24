/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CodeDescriptionDto } from './CodeDescriptionDto';
import type { ForestClientDto } from './ForestClientDto';
export type DisturbanceSearchResponseDto = {
    activityId?: number;
    disturbance?: CodeDescriptionDto;
    silvSystem?: CodeDescriptionDto;
    variant?: CodeDescriptionDto;
    cutPhase?: CodeDescriptionDto;
    fileId?: string;
    cutBlock?: string;
    openingId?: number;
    openingCategory?: CodeDescriptionDto;
    orgUnit?: CodeDescriptionDto;
    openingClient?: ForestClientDto;
    updateTimestamp?: string;
};

