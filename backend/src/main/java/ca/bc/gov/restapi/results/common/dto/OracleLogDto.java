package ca.bc.gov.restapi.results.common.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.With;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

/**
 * This record holds messages and its local date time when it happened.
 */
@RegisterReflectionForBinding
@Builder
@With
public record OracleLogDto(String message, LocalDateTime eventTime) {

}
