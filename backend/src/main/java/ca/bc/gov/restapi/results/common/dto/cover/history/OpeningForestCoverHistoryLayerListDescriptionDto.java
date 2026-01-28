package ca.bc.gov.restapi.results.common.dto.cover.history;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

import java.util.List;

@With
public record OpeningForestCoverHistoryLayerListDescriptionDto(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    List<CodeDescriptionDto> species,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Float total,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Float totalWellSpaced,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Float wellSpaced,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Float freeGrowing
) {

}
