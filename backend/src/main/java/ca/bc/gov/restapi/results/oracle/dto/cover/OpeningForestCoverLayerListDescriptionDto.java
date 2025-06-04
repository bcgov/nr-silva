package ca.bc.gov.restapi.results.oracle.dto.cover;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.With;

@With
public record OpeningForestCoverLayerListDescriptionDto(
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
