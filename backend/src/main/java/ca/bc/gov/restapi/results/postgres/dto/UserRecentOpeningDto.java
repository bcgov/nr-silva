package ca.bc.gov.restapi.results.postgres.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserRecentOpeningDto {
    private final String userId;
    private final String openingId;
    private final LocalDateTime lastViewed;
}
