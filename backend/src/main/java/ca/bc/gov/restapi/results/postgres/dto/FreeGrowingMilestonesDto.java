package ca.bc.gov.restapi.results.postgres.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.With;

/**
 * This record represent a slice of the free growing milestone chart.
 */
@Builder
@With
public record FreeGrowingMilestonesDto(
    @Schema(description = "Number representing the index, between 0 and 3.", example = "1")
    Integer index,
    @Schema(
        description = "Label of the current slice. E.g.: 0 - 5 month, 6 - 11 months",
        example = "12 - 17 months")
    String label,
    @Schema(description = "Amount of openings in the slice", example = "33") Integer amount,
    @Schema(
        description =
            "Percentage of this slice considering the total of openings on the period.",
        example = "28")
    BigDecimal percentage) {

}
