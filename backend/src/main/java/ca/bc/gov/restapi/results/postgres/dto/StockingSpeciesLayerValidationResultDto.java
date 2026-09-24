package ca.bc.gov.restapi.results.postgres.dto;

import ca.bc.gov.restapi.results.postgres.enums.SpeciesValidationErrorCode;
import java.util.List;

/** Validation result for one submitted stocking-standard layer. */
public record StockingSpeciesLayerValidationResultDto(
    int layerIndex,
    String layerCode,
    boolean isValid,
    SpeciesValidationErrorCode layerErrorCode,
    String layerErrorMessage,
    List<StockingSpeciesValidationResultDto> speciesValidationResults) {}
