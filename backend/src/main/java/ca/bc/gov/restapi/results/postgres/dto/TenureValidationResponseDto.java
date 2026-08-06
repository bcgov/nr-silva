package ca.bc.gov.restapi.results.postgres.dto;

import java.util.List;

/**
 * Response for {@code POST /api/tenures/validate}.
 *
 * @param validationResults per-tenure validation status and first error (if any)
 * @param duplicateConflicts list of duplicate tenure groups detected
 * @param isValid true only if all tenures are valid AND no duplicates exist
 */
public record TenureValidationResponseDto(
    List<TenureValidationResultDto> validationResults,
    List<DuplicateConflictDto> duplicateConflicts,
    boolean isValid) {}
