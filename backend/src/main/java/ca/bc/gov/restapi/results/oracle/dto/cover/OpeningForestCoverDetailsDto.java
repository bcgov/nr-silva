package ca.bc.gov.restapi.results.oracle.dto.cover;

import java.util.List;
import lombok.With;

@With
public record OpeningForestCoverDetailsDto(
    OpeningForestCoverPolygonDto polygon,
    OpeningForestCoverUnmappedDto unmapped,
    List<OpeningForestCoverLayerDto> layers
) {

}
