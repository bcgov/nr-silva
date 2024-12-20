package ca.bc.gov.restapi.results.postgres.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.With;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

/**
 * This record represent a slice of the free growing milestone chart.
 */
@RegisterReflectionForBinding
@Builder
@With
public record FreeGrowingMilestonesDto(
    Integer index,
    String label,
    Integer amount,
    BigDecimal percentage) {

}
