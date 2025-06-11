package ca.bc.gov.restapi.results.oracle.dto.cover;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import lombok.With;

@With
public record OpeningForestCoverPolygonDto(
    Long forestCoverId,
    CodeDescriptionDto reserve,
    CodeDescriptionDto objective,
    CodeDescriptionDto siteClass,
    Long siteIndex,
    CodeDescriptionDto siteIndexSource,
    CodeDescriptionDto treeCoverPattern,
    Long reentryYear
) {

}
