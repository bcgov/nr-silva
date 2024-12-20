package ca.bc.gov.restapi.results.postgres.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.With;

/**
 * This record represents an opening activity in the requests tab.
 */
@Builder
@With
public record MyRecentActionsRequestsDto(
    String activityType,
    Long openingId,
    String statusCode,
    String statusDescription,
    LocalDateTime lastUpdated
) {

}
