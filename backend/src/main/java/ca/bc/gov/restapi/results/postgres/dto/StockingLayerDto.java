package ca.bc.gov.restapi.results.postgres.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Tree Stocking Criteria for a single layer. The same shape is used for the single-layer case
 * ({@code layerCode = "I"}) and each of the four multi-layer entries ({@code layerCode} 4/3/2/1);
 * field applicability by layer number is enforced by the service, not this DTO.
 *
 * @param layerCode "I" for single layer, or "4"/"3"/"2"/"1" for multi-layer
 * @param minWellSpacedTrees minimum well-spaced trees
 * @param minPreferredWellSpacedTrees minimum preferred well-spaced trees
 * @param minHorizontalDistance minimum horizontal distance between well-spaced trees, in metres
 * @param targetWellSpacedTrees target well-spaced trees per hectare
 * @param minResidualBasalArea minimum residual basal area (m²/ha); layers 1/2 only
 * @param minPostSpacingDensity minimum post-spacing density (st/ha); layers 1/2 only
 * @param maxPostSpacingDensity maximum post-spacing density (st/ha); layers 1/2 only
 * @param maxConiferous maximum coniferous (st/ha); layers 1/2 only
 * @param heightRelativeToComp height relative to competition; layers 3/4 only (or any layer for
 *     single layer)
 * @param heightRelativeToCompUnitCode unit for {@code heightRelativeToComp} ("CM" or "PCT"),
 *     required whenever {@code heightRelativeToComp} is supplied
 */
public record StockingLayerDto(
    @NotBlank @Size(max = 2) String layerCode,
    Integer minWellSpacedTrees,
    Integer minPreferredWellSpacedTrees,
    java.math.BigDecimal minHorizontalDistance,
    Integer targetWellSpacedTrees,
    Integer minResidualBasalArea,
    Integer minPostSpacingDensity,
    Integer maxPostSpacingDensity,
    Integer maxConiferous,
    Integer heightRelativeToComp,
    @Size(max = 3) String heightRelativeToCompUnitCode) {}
