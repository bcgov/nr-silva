package ca.bc.gov.restapi.results.common.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * This class handles global error responses. It should capture any error that is thrown in the
 * application before it goes out to the client who requested the data and return a ProblemDetail
 * object.
 */
@ControllerAdvice
@RestControllerAdvice
@Slf4j
public class GlobalErrorResponseEndpoint extends ResponseEntityExceptionHandler {

  /**
   * Handle any runtime exception that is thrown in the application.
   *
   * @param exception The exception that was thrown
   * @param request The request that was made
   * @return A ProblemDetail object with the error status and message
   */
  @ExceptionHandler({RuntimeException.class, ResponseStatusException.class})
  public ProblemDetail handleDupKey(Exception exception, WebRequest request) {

    // Get the error message
    String errorMessage = exception.getMessage();
    // Set the default error status to INTERNAL_SERVER_ERROR
    HttpStatusCode errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    if (exception instanceof IllegalStateException) {
      log.error("Request encountered an illegal state", exception);
      errorStatus = HttpStatus.CONFLICT;
    }

    // If the error is a ResponseStatusException, get the reason and status code from the exception
    if (exception instanceof ResponseStatusException responseStatusException) {
      errorMessage = responseStatusException.getReason();
      errorStatus = responseStatusException.getStatusCode();
    }

    // If the error message is blank, set it to an empty string
    errorMessage =
        BooleanUtils.toString(StringUtils.isBlank(errorMessage), StringUtils.EMPTY, errorMessage);

    // Log the error status and message
    log.error("{} - {}", errorStatus, errorMessage, exception);

    return ProblemDetail.forStatusAndDetail(errorStatus, errorMessage);
  }

  /**
   * Handle access denied (403 Forbidden) exceptions thrown by Spring Security when a user is
   * authenticated but lacks the necessary permissions to access a resource.
   *
   * @param ex the AccessDeniedException thrown
   * @param request the current web request
   * @return a ProblemDetail with HTTP 403 and an "Access denied" message
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ProblemDetail handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
    log.warn("Access denied: {}", ex.getMessage());
    return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "Access denied");
  }

  /**
   * Handle authentication failure (401 Unauthorized) exceptions thrown by Spring Security when a
   * request lacks valid authentication credentials (e.g. expired token, missing header).
   *
   * @param ex the AuthenticationException thrown
   * @param request the current web request
   * @return a ProblemDetail with HTTP 401 and an "Authentication failed" message
   */
  @ExceptionHandler(AuthenticationException.class)
  public ProblemDetail handleAuthenticationException(
      AuthenticationException ex, WebRequest request) {
    log.warn("Authentication failed: {}", ex.getMessage());
    return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Authentication failed");
  }
}
