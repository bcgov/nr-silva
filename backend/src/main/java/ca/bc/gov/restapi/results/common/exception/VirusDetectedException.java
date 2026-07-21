package ca.bc.gov.restapi.results.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Thrown when an uploaded file is rejected by the virus scanner — either because a virus signature
 * was detected, or because the scanner was unreachable under a fail-closed policy.
 *
 * <p>Results in HTTP 422 Unprocessable Entity via {@code GlobalErrorResponseEndpoint}.
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class VirusDetectedException extends ResponseStatusException {

  /**
   * @param reason human-readable rejection description included in the ProblemDetail response
   */
  public VirusDetectedException(String reason) {
    super(HttpStatus.UNPROCESSABLE_ENTITY, reason);
  }
}
