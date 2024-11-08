package ca.bc.gov.restapi.results.postgres.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.With;

@With
@Builder
public record UserRecentOpeningDto(
    String userId,
    String openingId,
    LocalDateTime lastViewed
) {

}
