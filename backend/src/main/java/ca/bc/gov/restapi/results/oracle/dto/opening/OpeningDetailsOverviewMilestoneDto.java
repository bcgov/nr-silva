package ca.bc.gov.restapi.results.oracle.dto.opening;

import java.time.LocalDate;
import lombok.With;

@With
public record OpeningDetailsOverviewMilestoneDto(
    String standardsUnitId,
    LocalDate postHarvestDeclaredDate,
    LocalDate regenDeclaredDate,
    Integer regenOffsetYears,
    LocalDate regenDueDate,
    LocalDate freeGrowingDeclaredDate,
    Integer freeGrowingOffsetYears,
    LocalDate freeGrowingDueDate
) {

}
