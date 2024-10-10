package ca.bc.gov.restapi.results.common.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.With;

/**
 * This record holds messages and its local date time when it happened.
 */
@Builder
@With
public record OracleLogDto(String message, LocalDateTime eventTime) {

}
