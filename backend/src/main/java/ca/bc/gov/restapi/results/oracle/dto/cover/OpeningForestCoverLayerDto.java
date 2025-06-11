package ca.bc.gov.restapi.results.oracle.dto.cover;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.With;

@With
public record OpeningForestCoverLayerDto(
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
    List<OpeningForestCoverDetailedSpeciesDto> species,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    OpeningForestCoverDamageDto damage
) {

}
