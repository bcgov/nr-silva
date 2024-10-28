package ca.bc.gov.restapi.results.postgres.dto;

import lombok.Builder;
import lombok.With;

/**
 * This record represents a record for the "Opening submission trends" chart.
 */
@Builder
@With
public record OpeningsPerYearDto(
    Integer month,
    String monthName,
    Integer amount
) {

}
