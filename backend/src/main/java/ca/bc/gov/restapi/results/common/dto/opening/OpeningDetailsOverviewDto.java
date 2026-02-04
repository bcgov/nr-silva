package ca.bc.gov.restapi.results.common.dto.opening;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

@With
public record OpeningDetailsOverviewDto(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  OpeningDetailsOverviewOpeningDto opening,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  OpeningDetailsOverviewMilestoneDto milestones
) {

}
