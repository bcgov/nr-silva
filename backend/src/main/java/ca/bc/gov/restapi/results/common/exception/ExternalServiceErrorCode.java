package ca.bc.gov.restapi.results.common.exception;

/**
 * Standard error codes representing external system integration failures, downstream service
 * outages, and external execution timeouts.
 */
public enum ExternalServiceErrorCode {
  EXTERNAL_SERVICE_UNAVAILABLE,
  EXTERNAL_SERVICE_TIMEOUT;
}

