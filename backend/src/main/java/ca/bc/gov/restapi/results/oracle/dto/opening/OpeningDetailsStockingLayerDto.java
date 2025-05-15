package ca.bc.gov.restapi.results.oracle.dto.opening;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;

public record OpeningDetailsStockingLayerDto(
    CodeDescriptionDto layer,
    Long minWellspacedTrees,
    Long minPreferredWellspacedTrees,
    Long minHorizontalDistanceWellspacedTrees,
    Long targetWellspacedTrees,
    Long minResidualBasalArea,
    Long minPostspacingDensity,
    Long maxPostspacingDensity,
    Long maxConiferous,
    Long heightRelativeToComp
) {

}
