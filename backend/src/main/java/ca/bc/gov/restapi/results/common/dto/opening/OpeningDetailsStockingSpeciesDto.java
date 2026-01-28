package ca.bc.gov.restapi.results.common.dto.opening;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

@With
public record OpeningDetailsStockingSpeciesDto(
    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String layer,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto species,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Float minHeight
) {

}
