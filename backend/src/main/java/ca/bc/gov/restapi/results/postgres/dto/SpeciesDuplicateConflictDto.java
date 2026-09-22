package ca.bc.gov.restapi.results.postgres.dto;

import ca.bc.gov.restapi.results.postgres.enums.SpeciesValidationErrorCode;
import java.util.List;

/** A normalized duplicate species code within one submitted stocking-standard layer. */
public record SpeciesDuplicateConflictDto(
    int layerIndex,
    String layerCode,
    List<Integer> duplicateSpeciesIndices,
    String reason,
    SpeciesValidationErrorCode conflictCode) {}
