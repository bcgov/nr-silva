/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CodeDescriptionDto } from './CodeDescriptionDto';
import type { ForestClientDto } from './ForestClientDto';
export type ActivitySearchResponseDto = {
    activityId?: number;
    base?: CodeDescriptionDto;
    technique?: CodeDescriptionDto;
    method?: CodeDescriptionDto;
    isComplete?: boolean;
    fundingSource?: CodeDescriptionDto;
    fileId?: string;
    cutBlock?: string;
    openingId?: number;
    cuttingPermit?: string;
    treatmentAmountArea?: number;
    intraAgencyNumber?: string;
    openingCategory?: CodeDescriptionDto;
    orgUnit?: CodeDescriptionDto;
    openingClient?: ForestClientDto;
    updateTimestamp?: string;
};

