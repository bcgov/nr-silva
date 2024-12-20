package ca.bc.gov.restapi.results.postgres.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.With;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

@RegisterReflectionForBinding
@With
@Builder
public record UserRecentOpeningDto(
    String userId,
    Long openingId,
    LocalDateTime lastViewed
) {

}
