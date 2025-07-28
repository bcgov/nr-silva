package ca.bc.gov.restapi.results.oracle.dto.opening.history;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

import java.util.List;

@With
public record OpeningStandardUnitHistoryLayerDetailsDto(
    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Long oldLayerId,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Long newLayerId,

    @Schema(types = {"object"}, requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto oldStockingLayer,

    @Schema(types = {"object"}, requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto newStockingLayer,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Double oldMinHorizontalDistance,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Double newMinHorizontalDistance,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer oldMinPerfStockingStandard,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer newMinPerfStockingStandard,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer oldMinStockingStandard,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer newMinStockingStandard,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer oldMinPostSpacing,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer newMinPostSpacing,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Double oldResidualBasalArea,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Double newResidualBasalArea,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer oldTargetWellSpacedTrees,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer newTargetWellSpacedTrees,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer oldHeightRelativeToComp,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer newHeightRelativeToComp,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer oldMaxConifer,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer newMaxConifer,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer oldMaxPostSpacing,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer newMaxPostSpacing,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    List<OpeningStandardUnitHistorySpeciesDetailsDto> preferredSpecies,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    List<OpeningStandardUnitHistorySpeciesDetailsDto> acceptableSpecies
) {}