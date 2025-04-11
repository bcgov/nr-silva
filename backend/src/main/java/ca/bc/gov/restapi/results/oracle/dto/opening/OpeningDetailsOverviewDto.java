package ca.bc.gov.restapi.results.oracle.dto.opening;

import lombok.With;

@With
public record OpeningDetailsOverviewDto(
  OpeningDetailsOverviewOpeningDto opening,
  OpeningDetailsOverviewMilestoneDto milestones
) {

}
