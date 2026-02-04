package ca.bc.gov.restapi.results.common.dto.cover;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

@With
public record OpeningForestCoverDamageDto(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto damageAgent,
    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Integer healthIncidencePercentage,
    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Float incidenceArea
) {

}
