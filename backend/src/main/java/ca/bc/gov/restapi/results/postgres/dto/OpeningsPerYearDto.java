package ca.bc.gov.restapi.results.postgres.dto;

import java.util.Map;
import lombok.Builder;
import lombok.With;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

/**
 * This record represents a record for the "Opening submission trends" chart.
 */
@RegisterReflectionForBinding
@Builder
@With
public record OpeningsPerYearDto(
    Integer month,
    Integer year,
    String monthName,
    Long amount,
    Map<String,Long> statusCounts
) {

}
