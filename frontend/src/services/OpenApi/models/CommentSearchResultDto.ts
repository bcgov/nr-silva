/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type CommentSearchResultDto = {
    openingId: number;
    commentLocation: CommentSearchResultDto.commentLocation;
    activityKind?: CommentSearchResultDto.activityKind;
    commentText: string | null;
    updateTimestamp?: string | null;
};
export namespace CommentSearchResultDto {
    export enum commentLocation {
        STANDARDS_UNIT = 'STANDARDS_UNIT',
        OPENING = 'OPENING',
        MILESTONE = 'MILESTONE',
        ACTIVITIES = 'ACTIVITIES',
        FOREST_COVER = 'FOREST_COVER',
    }
    export enum activityKind {
        ACTIVITY = 'ACTIVITY',
        DISTURBANCE = 'DISTURBANCE',
    }
}

