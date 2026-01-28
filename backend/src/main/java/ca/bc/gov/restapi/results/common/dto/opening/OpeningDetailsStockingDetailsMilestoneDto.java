package ca.bc.gov.restapi.results.common.dto.opening;

import ca.bc.gov.restapi.results.common.dto.comment.CommentDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

import java.time.LocalDate;
import java.util.List;

@With
public record OpeningDetailsStockingDetailsMilestoneDto(
    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate postHarvestDeclaredDate,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate regenDeclaredDate,

    @Schema(types = {"integer", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer regenOffsetYears,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate regenDueDate,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate noRegenDeclaredDate,

    @Schema(types = {"integer", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer noRegenOffsetYears,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate noRegenDueDate,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate freeGrowingDeclaredDate,

    @Schema(types = {"integer", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer freeGrowingOffsetYears,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate freeGrowingDueDate,

    @Schema(types = {"boolean"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Boolean noRegenIndicated,

    @Schema(types = {"boolean"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Boolean extentDeclared,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    List<CommentDto> comments
) {}
