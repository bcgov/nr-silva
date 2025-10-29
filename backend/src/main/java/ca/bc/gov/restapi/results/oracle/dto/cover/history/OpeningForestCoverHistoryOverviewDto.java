package ca.bc.gov.restapi.results.oracle.dto.cover.history;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record OpeningForestCoverHistoryOverviewDto(
        @Schema(types = {"string"}, requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime updateTimestamp,

        @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
        Double np,

        @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
        Double nsr,

        @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
        Double imm,

        @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
        Double other,

        @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
        Double total,

        @Schema(types = {"boolean"}, requiredMode = Schema.RequiredMode.REQUIRED)
        boolean hasDetails,

        @Schema(types = {"boolean"}, requiredMode = Schema.RequiredMode.REQUIRED)
        boolean isCurrent,

        @Schema(types = {"boolean"}, requiredMode = Schema.RequiredMode.REQUIRED)
        boolean isOldest)
{}
