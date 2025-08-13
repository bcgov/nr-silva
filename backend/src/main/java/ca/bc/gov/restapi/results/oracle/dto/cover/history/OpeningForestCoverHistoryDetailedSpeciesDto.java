package ca.bc.gov.restapi.results.oracle.dto.cover.history;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

@With
public record OpeningForestCoverHistoryDetailedSpeciesDto(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto species,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Long percentage,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Long averageAge,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Float averageHeight
) {

}
