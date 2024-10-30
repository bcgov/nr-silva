package ca.bc.gov.restapi.results.postgres.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@With
@Builder
public class UserRecentOpeningDto {
    private final String userId;
    private final String openingId;
    private final LocalDateTime lastViewed;
}
