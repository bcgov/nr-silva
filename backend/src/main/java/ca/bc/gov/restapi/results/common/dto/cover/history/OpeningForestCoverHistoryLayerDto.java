package ca.bc.gov.restapi.results.common.dto.cover.history;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

import java.util.List;

@With
public record OpeningForestCoverHistoryLayerDto(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Long layerId,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto layer,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Long crownClosure,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Long basalAreaSt,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Long totalStems,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Long totalWellSpaced,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Long wellSpaced,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Long freeGrowing,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    List<OpeningForestCoverHistoryDetailedSpeciesDto> species,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    List<OpeningForestCoverHistoryDamageDto> damage
) {

}
