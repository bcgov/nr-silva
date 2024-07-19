package ca.bc.gov.restapi.results.common.exception;

/** This class represents a Forest Client not found, will throw 404 http error. */
public class ForestClientNotFoundException extends NotFoundGenericException {

  public ForestClientNotFoundException() {
    super("ForestsClient");
  }
}
