package ca.bc.gov.restapi.results.oracle.dto.opening.history;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

@With
public record OpeningStandardUnitHistoryDetailsDto(
    @Schema(types = {"number"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Long stockingStandardUnitId,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String standardsUnitId,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Long oldRegimeId,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Long newRegimeId,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Double oldNetArea,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Double newNetArea,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Double oldMaxSoilDisturbance,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Double newMaxSoilDisturbance,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String oldVarianceIndicator,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String newVarianceIndicator,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String oldRegenObligationIndicator,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String newRegenObligationIndicator,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer oldNoRegenEarlyOffsetYears,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer newNoRegenEarlyOffsetYears,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer oldNoRegenLateOffsetYears,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer newNoRegenLateOffsetYears,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer oldRegenOffsetYears,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer newRegenOffsetYears,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer oldFreeGrowingEarlyOffsetYears,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer newFreeGrowingEarlyOffsetYears,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer oldFreeGrowingLateOffsetYears,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer newFreeGrowingLateOffsetYears,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String oldBgcZone,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String newBgcZone,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String oldBgcSubzone,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String newBgcSubzone,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String oldBgcVariant,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String newBgcVariant,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String oldBgcPhase,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String newBgcPhase,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String oldBecSiteSeries,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String newBecSiteSeries,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String oldBecSiteType,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String newBecSiteType,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String oldBecSeral,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String newBecSeral
) {
}
