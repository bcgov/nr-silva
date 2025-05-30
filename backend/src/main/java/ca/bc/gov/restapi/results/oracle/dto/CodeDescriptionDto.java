package ca.bc.gov.restapi.results.oracle.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

/**
 * The type Code description dto.
 */
@With
public record CodeDescriptionDto(
    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String code,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String description
) {

}
