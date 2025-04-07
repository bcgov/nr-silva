package ca.bc.gov.restapi.results.oracle.dto.opening;

import java.time.LocalDate;
import lombok.With;

@With
public record OpeningDetailsOverviewMilestoneDto(
    String standardsUnitId,
    LocalDate postHarvestDeclaredDate,
    LocalDate regenDeclaredDate,
    int regenOffsetYears,
    LocalDate regenDueDate,
    LocalDate freeGrowingDeclaredDate,
    int freeGrowingOffsetYears,
    LocalDate freeGrowingDueDate
) {

}
