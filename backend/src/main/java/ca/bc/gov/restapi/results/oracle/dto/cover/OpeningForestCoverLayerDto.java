package ca.bc.gov.restapi.results.oracle.dto.cover;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import java.util.List;
import lombok.With;

@With
public record OpeningForestCoverLayerDto(
    Long layerId,
    CodeDescriptionDto layer,
    Long crownClosure,
    Long basalAreaSt,
    Long totalStems,
    Long totalWellSpaced,
    Long wellSpaced,
    Long freeGrowing,
    List<OpeningForestCoverDetailedSpeciesDto> species,
    OpeningForestCoverDamageDto damage
) {

}
