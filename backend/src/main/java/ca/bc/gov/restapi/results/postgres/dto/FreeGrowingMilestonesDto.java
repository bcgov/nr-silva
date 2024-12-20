package ca.bc.gov.restapi.results.postgres.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.With;

/**
 * This record represent a slice of the free growing milestone chart.
 */
@Builder
@With
public record FreeGrowingMilestonesDto(
    Integer index,
    String label,
    Integer amount,
    BigDecimal percentage) {

}
