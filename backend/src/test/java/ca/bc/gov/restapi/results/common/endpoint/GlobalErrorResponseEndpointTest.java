package ca.bc.gov.restapi.results.common.endpoint;

import static org.assertj.core.api.Assertions.assertThat;

import ca.bc.gov.restapi.results.common.exception.ExternalServiceErrorCode;
import ca.bc.gov.restapi.results.common.exception.ExternalServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@DisplayName("Unit Test | GlobalErrorResponseEndpoint")
class GlobalErrorResponseEndpointTest {

  private final GlobalErrorResponseEndpoint handler = new GlobalErrorResponseEndpoint();
  private final WebRequest mockRequest = new ServletWebRequest(new MockHttpServletRequest());

  @Test
  @DisplayName("Should return 409 for IllegalStateException")
  void testIllegalStateException() {
    var ex = new IllegalStateException("Invalid state");
    ProblemDetail response = handler.handleDupKey(ex, mockRequest);
    assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    assertThat(response.getDetail()).isEqualTo("Invalid state");
  }

  @Test
  @DisplayName("Should return status from ResponseStatusException")
  void testResponseStatusException() {
    var ex = new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource missing");
    ProblemDetail response = handler.handleDupKey(ex, mockRequest);
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(response.getDetail()).isEqualTo("Resource missing");
  }

  @Test
  @DisplayName("Should return 500 for general RuntimeException")
  void testRuntimeException() {
    var ex = new RuntimeException("Something broke");
    ProblemDetail response = handler.handleDupKey(ex, mockRequest);
    assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    assertThat(response.getDetail()).isEqualTo("Something broke");
  }

  @Test
  @DisplayName("Should handle blank message safely")
  void testBlankMessage() {
    var ex = new RuntimeException("");
    ProblemDetail response = handler.handleDupKey(ex, mockRequest);
    assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    assertThat(response.getDetail()).isEqualTo("");
  }

  @Test
  @DisplayName("Should return 403 for AccessDeniedException")
  void testAccessDeniedException() {
    var ex = new AccessDeniedException("Access is forbidden");
    ProblemDetail response = handler.handleAccessDeniedException(ex, mockRequest);
    assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    assertThat(response.getDetail()).isEqualTo("Access denied");
  }

  @Test
  @DisplayName("Should return 401 for AuthenticationException")
  void testAuthenticationException() {
    var ex = new AuthenticationException("Invalid credentials") {};
    ProblemDetail response = handler.handleAuthenticationException(ex, mockRequest);
    assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    assertThat(response.getDetail()).isEqualTo("Authentication failed");
  }

  @Test
  @DisplayName("Should return 503 ProblemDetail for ExternalServiceException with unavailable code")
  void testExternalServiceUnavailable() {
    var ex =
        new ExternalServiceException(
            ExternalServiceErrorCode.EXTERNAL_SERVICE_UNAVAILABLE,
            "FTA",
            "FTA service is currently unavailable");

    ProblemDetail response = handler.handleExternalServiceException(ex, mockRequest);

    assertThat(response.getStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE.value());
    assertThat(response.getDetail()).isEqualTo("FTA service is currently unavailable");
    assertThat(response.getTitle()).isEqualTo("External Service Error");
    assertThat(response.getProperties()).isNotNull();
    assertThat(response.getProperties().get("code"))
        .isEqualTo(ExternalServiceErrorCode.EXTERNAL_SERVICE_UNAVAILABLE.name());
    assertThat(response.getProperties().get("serviceName")).isEqualTo("FTA");
  }

  @Test
  @DisplayName("Should return 503 ProblemDetail for ExternalServiceException with timeout code")
  void testExternalServiceTimeout() {
    var ex =
        new ExternalServiceException(
            ExternalServiceErrorCode.EXTERNAL_SERVICE_TIMEOUT,
            "Oracle",
            "Oracle query timed out after 5000ms");

    ProblemDetail response = handler.handleExternalServiceException(ex, mockRequest);

    assertThat(response.getStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE.value());
    assertThat(response.getDetail()).isEqualTo("Oracle query timed out after 5000ms");
    assertThat(response.getTitle()).isEqualTo("External Service Error");
    assertThat(response.getProperties().get("code"))
        .isEqualTo(ExternalServiceErrorCode.EXTERNAL_SERVICE_TIMEOUT.name());
    assertThat(response.getProperties().get("serviceName")).isEqualTo("Oracle");
  }

  @Test
  @DisplayName("Should return 422 ProblemDetail for ExternalServiceException with 422 status")
  void testExternalServiceUnprocessableEntity() {
    var ex =
        new ExternalServiceException(
            HttpStatus.UNPROCESSABLE_ENTITY,
            ExternalServiceErrorCode.EXTERNAL_SERVICE_UNAVAILABLE,
            "FTA",
            "Cut block validation cannot be processed due to downstream dependency");

    ProblemDetail response = handler.handleExternalServiceException(ex, mockRequest);

    assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    assertThat(response.getDetail())
        .isEqualTo("Cut block validation cannot be processed due to downstream dependency");
    assertThat(response.getTitle()).isEqualTo("External Service Error");
    assertThat(response.getProperties().get("code"))
        .isEqualTo(ExternalServiceErrorCode.EXTERNAL_SERVICE_UNAVAILABLE.name());
    assertThat(response.getProperties().get("serviceName")).isEqualTo("FTA");
  }

  @Test
  @DisplayName("Should handle ExternalServiceException without serviceName safely")
  void testExternalServiceWithoutServiceName() {
    var ex =
        new ExternalServiceException(
            ExternalServiceErrorCode.EXTERNAL_SERVICE_UNAVAILABLE,
            "Downstream error");

    ProblemDetail response = handler.handleExternalServiceException(ex, mockRequest);

    assertThat(response.getStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE.value());
    assertThat(response.getDetail()).isEqualTo("Downstream error");
    assertThat(response.getProperties().get("code"))
        .isEqualTo(ExternalServiceErrorCode.EXTERNAL_SERVICE_UNAVAILABLE.name());
    assertThat(response.getProperties().get("serviceName")).isNull();
  }
}
