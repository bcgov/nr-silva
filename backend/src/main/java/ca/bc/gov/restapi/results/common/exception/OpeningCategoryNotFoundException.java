package ca.bc.gov.restapi.results.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Exception thrown when a requested opening category code is not found. */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class OpeningCategoryNotFoundException extends NotFoundGenericException {

  /** Instantiates a new OpeningCategoryNotFoundException. */
  public OpeningCategoryNotFoundException() {
    super("Opening category");
  }
}
