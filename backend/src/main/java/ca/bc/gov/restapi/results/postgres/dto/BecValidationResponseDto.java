package ca.bc.gov.restapi.results.postgres.dto;

import java.util.List;

/** Response for {@code POST /api/stocking-standards/validate/bec}. */
public record BecValidationResponseDto(
    List<BecValidationResultDto> validationResults,
    List<BecDuplicateConflictDto> duplicateConflicts,
    List<BecValidationErrorDto> validationErrors,
    boolean isValid) {}
