/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type CommentSearchResponseDto = {
    openingId: number;
    commentLocation: CommentSearchResponseDto.commentLocation;
    activityKind?: CommentSearchResponseDto.activityKind;
    commentText: string | null;
    updateTimestamp?: string | null;
};
export namespace CommentSearchResponseDto {
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

