package ca.bc.gov.restapi.results.postgres.dto;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.With;

/**
 * This record represents a record for the "Opening submission trends" chart.
 */
@Builder
@With
public record OpeningsPerYearDto(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Integer month,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Integer year,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Long amount,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Map<String, Long> statusCounts
) {

}
