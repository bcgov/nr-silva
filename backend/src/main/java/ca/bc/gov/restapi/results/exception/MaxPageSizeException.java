package ca.bc.gov.restapi.results.exception;

import ca.bc.gov.restapi.results.config.ConstantsConfig;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

/** This class represents an exception of maximum records per page. */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MaxPageSizeException extends ResponseStatusException {

  /** Creates an MaxPageSizeException exception with http status and message. */
  public MaxPageSizeException() {
    super(
        HttpStatus.BAD_REQUEST,
        "Max page size limit exceeded! Choose a number below " + ConstantsConfig.MAX_PAGE_SIZE);
  }
}
