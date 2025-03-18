package ca.bc.gov.restapi.results.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * The type Invalid opening id exception.
 */
@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
public class InvalidOpeningIdException extends ResponseStatusException {

  /**
   * Instantiates a new Invalid opening id exception.
   */
  public InvalidOpeningIdException() {
    super(HttpStatus.EXPECTATION_FAILED, "Opening ID must contain numbers only!");
  }
}
