package ca.bc.gov.restapi.results.common.exception;

public class ForestClientNotFoundException extends NotFoundGenericException {

  public ForestClientNotFoundException() {
    super("ForestsClient");
  }
}
