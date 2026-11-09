package ca.bc.gov.restapi.results.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

/**
 * Base exception thrown when an external downstream system, database, or service (such as FTA,
 * Forest Client, Org Units, or external Oracle) fails, times out, or cannot process a request.
 */
@Getter
public class ExternalServiceException extends ResponseStatusException {

  public static final ExternalServiceErrorCode EXTERNAL_SERVICE_UNAVAILABLE =
      ExternalServiceErrorCode.EXTERNAL_SERVICE_UNAVAILABLE;
  public static final ExternalServiceErrorCode EXTERNAL_SERVICE_TIMEOUT =
      ExternalServiceErrorCode.EXTERNAL_SERVICE_TIMEOUT;

  private final ExternalServiceErrorCode errorCode;
  private final String serviceName;

  /**
   * Instantiates an ExternalServiceException with a specific error code and detail message.
   *
   * @param errorCode the integration error code
   * @param message the detail message describing the error
   */
  public ExternalServiceException(ExternalServiceErrorCode errorCode, String message) {
    this(defaultStatusFor(errorCode), errorCode, null, message, null);
  }

  /**
   * Instantiates an ExternalServiceException with an error code, service name, and detail message.
   *
   * @param errorCode the integration error code
   * @param serviceName the name of the external service or system
   * @param message the detail message describing the error
   */
  public ExternalServiceException(
      ExternalServiceErrorCode errorCode, String serviceName, String message) {
    this(defaultStatusFor(errorCode), errorCode, serviceName, message, null);
  }

  /**
   * Instantiates an ExternalServiceException with an error code, service name, detail message, and
   * underlying cause.
   *
   * @param errorCode the integration error code
   * @param serviceName the name of the external service or system
   * @param message the detail message describing the error
   * @param cause the root cause
   */
  public ExternalServiceException(
      ExternalServiceErrorCode errorCode,
      String serviceName,
      String message,
      Throwable cause) {
    this(defaultStatusFor(errorCode), errorCode, serviceName, message, cause);
  }

  /**
   * Instantiates an ExternalServiceException with an explicit HTTP status, error code, and message.
   *
   * @param status the HTTP status code (e.g. 503 or 422)
   * @param errorCode the integration error code
   * @param message the detail message describing the error
   */
  public ExternalServiceException(
      HttpStatusCode status, ExternalServiceErrorCode errorCode, String message) {
    this(status, errorCode, null, message, null);
  }

  /**
   * Instantiates an ExternalServiceException with an explicit HTTP status, error code, message, and
   * root cause.
   *
   * @param status the HTTP status code (e.g. 503 or 422)
   * @param errorCode the integration error code
   * @param message the detail message describing the error
   * @param cause the root cause
   */
  public ExternalServiceException(
      HttpStatusCode status,
      ExternalServiceErrorCode errorCode,
      String message,
      Throwable cause) {
    this(status, errorCode, null, message, cause);
  }

  /**
   * Instantiates an ExternalServiceException with an explicit HTTP status, error code,
   * service name, and message.
   *
   * @param status the HTTP status code (e.g. 503 or 422)
   * @param errorCode the integration error code
   * @param serviceName the name of the external service or system
   * @param message the detail message describing the error
   */
  public ExternalServiceException(
      HttpStatusCode status,
      ExternalServiceErrorCode errorCode,
      String serviceName,
      String message) {
    this(status, errorCode, serviceName, message, null);
  }

  /**
   * Instantiates an ExternalServiceException with full parameters.
   *
   * @param status the HTTP status code (e.g. 503 or 422)
   * @param errorCode the integration error code
   * @param serviceName the name of the external service or system
   * @param message the detail message describing the error
   * @param cause the root cause
   */
  public ExternalServiceException(
      HttpStatusCode status,
      ExternalServiceErrorCode errorCode,
      String serviceName,
      String message,
      Throwable cause) {
    super(status != null ? status : defaultStatusFor(errorCode), message, cause);
    this.errorCode =
        errorCode != null ? errorCode : ExternalServiceErrorCode.EXTERNAL_SERVICE_UNAVAILABLE;
    this.serviceName = serviceName;
  }

  private static HttpStatusCode defaultStatusFor(ExternalServiceErrorCode code) {
    if (code == ExternalServiceErrorCode.EXTERNAL_SERVICE_TIMEOUT) {
      return HttpStatus.SERVICE_UNAVAILABLE;
    }
    return HttpStatus.SERVICE_UNAVAILABLE;
  }
}
