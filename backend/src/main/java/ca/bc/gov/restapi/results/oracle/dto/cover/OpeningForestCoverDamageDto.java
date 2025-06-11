package ca.bc.gov.restapi.results.oracle.dto.cover;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import lombok.With;

@With
public record OpeningForestCoverDamageDto(
    CodeDescriptionDto damageAgent,
    Integer healthIncidencePercentage,
    Float incidenceArea
) {

}
