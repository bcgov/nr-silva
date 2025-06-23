/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { OpeningDetailsNotificationDto } from './OpeningDetailsNotificationDto';
import type { OpeningDetailsOverviewDto } from './OpeningDetailsOverviewDto';
import type { OpeningDetailsTombstoneDto } from './OpeningDetailsTombstoneDto';
export type OpeningDetailsTombstoneOverviewDto = {
    openingId: number;
    tombstone: OpeningDetailsTombstoneDto;
    overview: OpeningDetailsOverviewDto;
    notifications: Array<OpeningDetailsNotificationDto>;
};

