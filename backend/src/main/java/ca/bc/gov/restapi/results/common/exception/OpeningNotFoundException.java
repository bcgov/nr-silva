package ca.bc.gov.restapi.results.common.exception;

/** This class represents an error, when a Opening was not found. */
public class OpeningNotFoundException extends NotFoundGenericException {

  public OpeningNotFoundException() {
    super("UserOpening");
  }
}
