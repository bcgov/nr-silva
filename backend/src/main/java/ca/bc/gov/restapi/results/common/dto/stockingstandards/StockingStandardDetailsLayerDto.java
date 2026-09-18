package ca.bc.gov.restapi.results.common.dto.stockingstandards;

import java.math.BigDecimal;
import java.util.List;

/** Tree stocking criteria for one layer of a stocking standard. */
public record StockingStandardDetailsLayerDto(
    String layerCode,
    Integer minWellSpacedTrees,
    Integer minPreferredWellSpacedTrees,
    BigDecimal minHorizontalDistanceWellSpacedTrees,
    Integer targetWellSpacedTrees,
    Integer minResidualBasalArea,
    Integer minPostSpacingDensity,
    Integer maxPostSpacingDensity,
    Integer maxConiferous,
    Integer heightRelativeToComp,
    String heightRelativeToCompUnitCode,
    List<StockingStandardDetailsSpeciesDto> species) {}
