package ca.bc.gov.restapi.results.postgres.dto;

/**
 * Response body for {@code POST /api/stocking-standards}.
 *
 * @param stockingStandardsId the generated standards_regime_id of the created Stocking Standard
 */
public record CreateStockingStandardResponseDto(Long stockingStandardsId) {}
