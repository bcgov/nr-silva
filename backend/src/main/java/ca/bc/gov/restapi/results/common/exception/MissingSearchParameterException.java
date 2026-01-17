package ca.bc.gov.restapi.results.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * This class represents an exception when no search filters are provided in an exact search
 * request.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MissingSearchParameterException extends ResponseStatusException {

  /** Creates a MissingSearchParameterException with http status and message. */
  public MissingSearchParameterException() {
    super(HttpStatus.BAD_REQUEST, "At least one search parameter is required.");
  }
}
