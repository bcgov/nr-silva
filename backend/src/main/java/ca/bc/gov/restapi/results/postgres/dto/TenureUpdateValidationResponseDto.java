package ca.bc.gov.restapi.results.postgres.dto;

import java.util.List;

/** Structured validation failures from an opening-tenure replacement request. */
public record TenureUpdateValidationResponseDto(
    TenureValidationResponseDto tenureValidation,
    List<TenureRemovalValidationResultDto> removalErrors) {}
