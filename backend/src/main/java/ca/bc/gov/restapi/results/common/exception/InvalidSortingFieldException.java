package ca.bc.gov.restapi.results.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.PRECONDITION_REQUIRED)
public class InvalidSortingFieldException extends ResponseStatusException {

  public InvalidSortingFieldException(String message) {
    super(
        HttpStatus.PRECONDITION_REQUIRED,
        "Field " + message + " is not a valid sorting field. Please check the documentation for valid sorting fields."
    );
  }
}
