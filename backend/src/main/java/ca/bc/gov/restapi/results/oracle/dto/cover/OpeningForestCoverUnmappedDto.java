package ca.bc.gov.restapi.results.oracle.dto.cover;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import lombok.With;

@With
public record OpeningForestCoverUnmappedDto(
    Long unmappedAreaId,
    Float area,
    CodeDescriptionDto stockingStatus,
    CodeDescriptionDto stockingType
) {

}
