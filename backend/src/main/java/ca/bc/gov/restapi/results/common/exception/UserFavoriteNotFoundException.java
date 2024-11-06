package ca.bc.gov.restapi.results.common.exception;

public class UserFavoriteNotFoundException extends NotFoundGenericException {

  public UserFavoriteNotFoundException() {
    super("UserFavoriteEntity");
  }

}
