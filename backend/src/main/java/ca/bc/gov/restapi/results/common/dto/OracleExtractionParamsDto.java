package ca.bc.gov.restapi.results.common.dto;

import lombok.Builder;
import lombok.With;

/**
 * This record represents the filters for the oracle extraction process.
 */
@Builder
@With
public record OracleExtractionParamsDto(Integer months, Boolean debug, Boolean manuallyTriggered) {

}
