package ca.bc.gov.restapi.results.postgres.dto;

import ca.bc.gov.restapi.results.postgres.enums.ActivityStatus;

/**
 * Response of a created activity.
 *
 * @param activityId the id of the new activity
 * @param openingId the opening the activity was created under
 * @param status whether the activity is planned or completed
 * @param baseCode the activity base code
 */
public record CreateActivityResponseDto(
    Long activityId, Long openingId, ActivityStatus status, String baseCode) {}
