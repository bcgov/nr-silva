package ca.bc.gov.restapi.results.postgres.dto;

import java.util.Map;
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
    Long amount,
    Map<String,Long> statusCounts
) {

}
