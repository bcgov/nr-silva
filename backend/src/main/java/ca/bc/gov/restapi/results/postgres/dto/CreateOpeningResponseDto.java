package ca.bc.gov.restapi.results.postgres.dto;

/**
 * Response body returned after successfully creating a new opening.
 *
 * @param openingId the system-generated ID of the newly created opening
 */
public record CreateOpeningResponseDto(Long openingId) {}
