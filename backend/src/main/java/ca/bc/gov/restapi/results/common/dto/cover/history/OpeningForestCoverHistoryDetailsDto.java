package ca.bc.gov.restapi.results.common.dto.cover.history;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

import java.util.List;

@With
public record OpeningForestCoverHistoryDetailsDto(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    OpeningForestCoverHistoryPolygonDto polygon,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    boolean isSingleLayer,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    List<OpeningForestCoverHistoryUnmappedDto> unmapped,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    List<OpeningForestCoverHistoryLayerDto> layers
) {

}
