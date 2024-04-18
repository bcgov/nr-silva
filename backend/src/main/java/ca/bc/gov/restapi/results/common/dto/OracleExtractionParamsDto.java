package ca.bc.gov.restapi.results.common.dto;

/** This record represents the filters for the oracle extraction process. */
public record OracleExtractionParamsDto(Integer months, Boolean debug, Boolean manuallyTriggered) {}
