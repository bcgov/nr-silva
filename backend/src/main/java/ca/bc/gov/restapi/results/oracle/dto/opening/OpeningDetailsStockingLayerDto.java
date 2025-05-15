package ca.bc.gov.restapi.results.oracle.dto.opening;

public record OpeningDetailsStockingLayerDto(
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
