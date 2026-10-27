/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CodeDescriptionDto } from './CodeDescriptionDto';
export type OpeningDetailsActivitiesActivitiesDto = {
    atuId: number;
    status: CodeDescriptionDto;
    base: CodeDescriptionDto;
    tech: CodeDescriptionDto;
    method: CodeDescriptionDto;
    objective1: CodeDescriptionDto;
    objective2: CodeDescriptionDto;
    objective3: CodeDescriptionDto;
    area: number | null;
    funding: CodeDescriptionDto;
    projectId: string | null;
    lastUpdate: string | null;
    plannedDate: string | null;
    endDate: string | null;
    /**
     * All objectives of the activity. objective1..3 repeat the first three.
     */
    objectives: Array<CodeDescriptionDto>;
    /**
     * All funding sources of the activity. funding repeats the first one.
     */
    fundingSources: Array<CodeDescriptionDto>;
};

