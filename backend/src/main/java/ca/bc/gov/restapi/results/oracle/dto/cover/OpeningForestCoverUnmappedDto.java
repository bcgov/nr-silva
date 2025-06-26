package ca.bc.gov.restapi.results.oracle.dto.cover;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

@With
public record OpeningForestCoverUnmappedDto(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String unmappedAreaId,
    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Float area,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto stockingStatus,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto stockingType
) {

}
