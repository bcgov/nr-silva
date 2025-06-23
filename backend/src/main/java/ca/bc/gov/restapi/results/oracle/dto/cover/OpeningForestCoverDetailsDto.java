package ca.bc.gov.restapi.results.oracle.dto.cover;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.With;

@With
public record OpeningForestCoverDetailsDto(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    OpeningForestCoverPolygonDto polygon,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    boolean isSingleLayer,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    List<OpeningForestCoverUnmappedDto> unmapped,
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    List<OpeningForestCoverLayerDto> layers
) {

}
