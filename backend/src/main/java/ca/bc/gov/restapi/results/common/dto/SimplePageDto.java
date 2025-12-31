package ca.bc.gov.restapi.results.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record SimplePageDto(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    long size,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    long number,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    long totalElements,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    long totalPages
) {

}
