package ca.bc.gov.restapi.results.common.exception;

/** This class represents an error, when a Opening was not found. */
public class OpeningNotFoundException extends NotFoundGenericException {

  /**
   * Instantiates a new Opening not found exception.
   */
  public OpeningNotFoundException() {
    super("UserOpening");
  }
  public OpeningNotFoundException(Long openingId) {
    super(String.format("UserOpening with ID %d", openingId));
  }
}
