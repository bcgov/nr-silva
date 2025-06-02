package ca.bc.gov.restapi.results.oracle.dto.opening;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import io.swagger.v3.oas.annotations.media.Schema;

public record OpeningDetailsStockingLayerDto(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto layer,

    @Schema(types = {"integer", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Long minWellspacedTrees,

    @Schema(types = {"integer", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Long minPreferredWellspacedTrees,

    @Schema(types = {"integer", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Long minHorizontalDistanceWellspacedTrees,

    @Schema(types = {"integer", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Long targetWellspacedTrees,

    @Schema(types = {"integer", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Long minResidualBasalArea,

    @Schema(types = {"integer", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Long minPostspacingDensity,

    @Schema(types = {"integer", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Long maxPostspacingDensity,

    @Schema(types = {"integer", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Long maxConiferous,

    @Schema(types = {"integer", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Long heightRelativeToComp
) {

}
