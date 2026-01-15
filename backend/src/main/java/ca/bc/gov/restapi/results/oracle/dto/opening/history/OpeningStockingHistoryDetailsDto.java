package ca.bc.gov.restapi.results.oracle.dto.opening.history;

import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsBecDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

@With
public record OpeningStockingHistoryDetailsDto(
    @Schema(
            types = {"string", "null"},
            requiredMode = Schema.RequiredMode.REQUIRED)
        String stockingStandardUnit,
    @Schema(
            types = {"integer", "null"},
            requiredMode = Schema.RequiredMode.REQUIRED)
        Long ssuId,
    @Schema(
            types = {"integer", "null"},
            requiredMode = Schema.RequiredMode.REQUIRED)
        Long srid,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED) boolean defaultMof,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED) boolean manualEntry,
    @Schema(
            types = {"integer", "null"},
            requiredMode = Schema.RequiredMode.REQUIRED)
        Long fspId,
    @Schema(
            types = {"number", "null"},
            requiredMode = Schema.RequiredMode.REQUIRED)
        Float netArea,
    @Schema(
            types = {"number", "null"},
            requiredMode = Schema.RequiredMode.REQUIRED)
        Float soilDisturbancePercent,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED) OpeningDetailsBecDto bec,
    @Schema(
            types = {"integer", "null"},
            requiredMode = Schema.RequiredMode.REQUIRED)
        Long regenDelay,
    @Schema(
            types = {"integer", "null"},
            requiredMode = Schema.RequiredMode.REQUIRED)
        Long freeGrowingLate,
    @Schema(
            types = {"integer", "null"},
            requiredMode = Schema.RequiredMode.REQUIRED)
        Long freeGrowingEarly,
    @Schema(
            types = {"string", "null"},
            requiredMode = Schema.RequiredMode.REQUIRED)
        String additionalStandards,
    @Schema(
            types = {"string", "null"},
            requiredMode = Schema.RequiredMode.REQUIRED)
        String amendmentComment) {}
