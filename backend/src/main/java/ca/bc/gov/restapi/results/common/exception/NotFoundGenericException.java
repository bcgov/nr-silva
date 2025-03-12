package ca.bc.gov.restapi.results.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

/** This class represents a generic not found request. */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundGenericException extends ResponseStatusException {

  /**
   * Instantiates a new Not found generic exception.
   *
   * @param entityName the entity name
   */
  public NotFoundGenericException(String entityName) {
    super(HttpStatus.NOT_FOUND, String.format("%s record(s) not found!", entityName));
  }
}
