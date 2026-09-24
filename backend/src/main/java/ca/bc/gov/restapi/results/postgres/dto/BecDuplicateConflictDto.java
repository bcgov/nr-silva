package ca.bc.gov.restapi.results.postgres.dto;

import ca.bc.gov.restapi.results.postgres.enums.BecValidationErrorCode;
import java.util.List;

/** A normalized duplicate BEC combination in one validation request. */
public record BecDuplicateConflictDto(
    List<Integer> duplicateIndices, String reason, BecValidationErrorCode conflictCode) {}
