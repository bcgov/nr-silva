/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CodeDescriptionDto } from './CodeDescriptionDto';
import type { CommentDto } from './CommentDto';
import type { ForestClientDto } from './ForestClientDto';
import type { ForestClientLocationDto } from './ForestClientLocationDto';
export type OpeningDetailsActivitiesDisturbanceDto = {
    atuId: number;
    disturbance: CodeDescriptionDto;
    system: CodeDescriptionDto;
    variant: CodeDescriptionDto;
    cutPhase: CodeDescriptionDto;
    disturbanceArea: number | null;
    lastUpdate: string | null;
    startDate: string | null;
    endDate: string | null;
    licenseeActivityId: string | null;
    forestClient: ForestClientDto;
    forestClientLocation: ForestClientLocationDto;
    licenceNumber: string | null;
    cuttingPermitId: string | null;
    cutBlock: string | null;
    comments: Array<CommentDto>;
};

