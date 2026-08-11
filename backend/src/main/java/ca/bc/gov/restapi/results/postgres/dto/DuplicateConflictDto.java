package ca.bc.gov.restapi.results.postgres.dto;

import ca.bc.gov.restapi.results.postgres.enums.TenureValidationErrorCode;
import java.util.List;

/**
 * Represents a duplicate conflict detected among submitted tenures.
 *
 * @param duplicateIndices 0-based indices of the conflicting tenures in the submitted list
 * @param reason description of why these tenures are considered duplicates
 * @param conflictCode machine-readable conflict code
 */
public record DuplicateConflictDto(
    List<Integer> duplicateIndices, String reason, TenureValidationErrorCode conflictCode) {}
