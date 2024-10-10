package ca.bc.gov.restapi.results.postgres.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.With;

/** This record represents a record for the "Opening submission trends" chart. */
@Builder
@With
@Schema(
    description = "This record represents a record for the \"Opening submission trends\" chart.")
public record OpeningsPerYearDto(
    @Schema(description = "The `x` value with the month number.", example = "3") Integer month,
    @Schema(description = "The `x` value with the month name.", example = "Mar") String monthName,
    @Schema(description = "The `y` value with the month value.", example = "70") Integer amount) {}
