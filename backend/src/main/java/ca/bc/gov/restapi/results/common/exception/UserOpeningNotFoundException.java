package ca.bc.gov.restapi.results.common.exception;

/** This class represents a UserOpeningEntity not found in the database. */
public class UserOpeningNotFoundException extends NotFoundGenericException {

  public UserOpeningNotFoundException() {
    super("UserOpeningEntity");
  }
}
