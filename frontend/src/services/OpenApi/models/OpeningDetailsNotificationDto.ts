/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type OpeningDetailsNotificationDto = {
    title: string | null;
    description: string | null;
    /**
     * Represents the status of a notification in the opening details.
     */
    status: OpeningDetailsNotificationDto.status;
};
export namespace OpeningDetailsNotificationDto {
    /**
     * Represents the status of a notification in the opening details.
     */
    export enum status {
        ERROR = 'ERROR',
        WARNING = 'WARNING',
        INFO = 'INFO',
        SUCCESS = 'SUCCESS',
    }
}

