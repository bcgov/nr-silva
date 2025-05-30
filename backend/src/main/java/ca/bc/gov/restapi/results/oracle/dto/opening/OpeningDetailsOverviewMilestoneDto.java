package ca.bc.gov.restapi.results.oracle.dto.opening;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

@With
public record OpeningDetailsOverviewMilestoneDto(
  @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  String standardsUnitId,

  @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  LocalDate postHarvestDeclaredDate,

  @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  LocalDate regenDeclaredDate,

  @Schema(types = {"integer", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  Integer regenOffsetYears,

  @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  LocalDate regenDueDate,

  @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  LocalDate freeGrowingDeclaredDate,

  @Schema(types = {"integer", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  Integer freeGrowingOffsetYears,

  @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  LocalDate freeGrowingDueDate
) {

}
