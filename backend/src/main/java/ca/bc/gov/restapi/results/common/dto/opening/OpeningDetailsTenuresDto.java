package ca.bc.gov.restapi.results.common.dto.opening;

import java.util.List;

import ca.bc.gov.restapi.results.common.dto.SimplePageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

@With
public record OpeningDetailsTenuresDto(

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    OpeningDetailsTenureDto primary,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    List<OpeningDetailsTenureDto> content,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    SimplePageDto page,

    @Schema(types = {"integer", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    long totalUnfiltered

) {}
