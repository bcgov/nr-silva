package ca.bc.gov.restapi.results.common.dto.opening.history;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

@With
public record OpeningStockingHistorySpeciesWithComparisonDto(
    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String oldLayerCode,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String newLayerCode,

    @Schema(types = {"object"}, requiredMode = Schema.RequiredMode.REQUIRED)

    CodeDescriptionDto oldSpecies,

    @Schema(types = {"object"}, requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto newSpecies,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Double oldMinHeight,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Double newMinHeight
) {}
