package ca.bc.gov.restapi.results.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("Unit Test | ExternalServiceException")
class ExternalServiceExceptionTest {

  @Test
  @DisplayName("Should create exception with default 503 status for EXTERNAL_SERVICE_UNAVAILABLE")
  void testDefaultStatusUnavailable() {
    var ex =
        new ExternalServiceException(
            ExternalServiceErrorCode.EXTERNAL_SERVICE_UNAVAILABLE, "FTA service down");

    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    assertThat(ex.getReason()).isEqualTo("FTA service down");
    assertThat(ex.getErrorCode()).isEqualTo(ExternalServiceErrorCode.EXTERNAL_SERVICE_UNAVAILABLE);
    assertThat(ex.getServiceName()).isNull();
  }

  @Test
  @DisplayName("Should create exception with default 503 status for EXTERNAL_SERVICE_TIMEOUT")
  void testDefaultStatusTimeout() {
    var ex =
        new ExternalServiceException(
            ExternalServiceErrorCode.EXTERNAL_SERVICE_TIMEOUT, "Oracle query timed out");

    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    assertThat(ex.getReason()).isEqualTo("Oracle query timed out");
    assertThat(ex.getErrorCode()).isEqualTo(ExternalServiceErrorCode.EXTERNAL_SERVICE_TIMEOUT);
    assertThat(ex.getServiceName()).isNull();
  }

  @Test
  @DisplayName("Should support constructors with serviceName and cause")
  void testConstructorWithServiceNameAndCause() {
    var cause = new RuntimeException("connection timeout");
    var ex =
        new ExternalServiceException(
            ExternalServiceErrorCode.EXTERNAL_SERVICE_TIMEOUT,
            "FTA",
            "Timeout connecting to FTA",
            cause);

    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    assertThat(ex.getReason()).isEqualTo("Timeout connecting to FTA");
    assertThat(ex.getErrorCode()).isEqualTo(ExternalServiceErrorCode.EXTERNAL_SERVICE_TIMEOUT);
    assertThat(ex.getServiceName()).isEqualTo("FTA");
    assertThat(ex.getCause()).isSameAs(cause);
  }

  @Test
  @DisplayName("Should support explicit HTTP status such as 422 UNPROCESSABLE_ENTITY")
  void testExplicitStatus422() {
    var ex =
        new ExternalServiceException(
            HttpStatus.UNPROCESSABLE_ENTITY,
            ExternalServiceErrorCode.EXTERNAL_SERVICE_UNAVAILABLE,
            "FTA",
            "Cut block cannot be verified due to external outage");

    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    assertThat(ex.getReason()).isEqualTo("Cut block cannot be verified due to external outage");
    assertThat(ex.getErrorCode()).isEqualTo(ExternalServiceErrorCode.EXTERNAL_SERVICE_UNAVAILABLE);
    assertThat(ex.getServiceName()).isEqualTo("FTA");
  }

  @Test
  @DisplayName("Should support explicit HTTP status with message and cause")
  void testExplicitStatusWithMessageAndCause() {
    var cause = new IllegalArgumentException("invalid response");
    var ex =
        new ExternalServiceException(
            HttpStatus.BAD_GATEWAY,
            ExternalServiceErrorCode.EXTERNAL_SERVICE_UNAVAILABLE,
            "Bad response from gateway",
            cause);

    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY);
    assertThat(ex.getReason()).isEqualTo("Bad response from gateway");
    assertThat(ex.getCause()).isSameAs(cause);
  }

  @Test
  @DisplayName("Should default errorCode to EXTERNAL_SERVICE_UNAVAILABLE if null passed")
  void testNullErrorCodeFallback() {
    var ex = new ExternalServiceException(null, "some error");
    assertThat(ex.getErrorCode()).isEqualTo(ExternalServiceErrorCode.EXTERNAL_SERVICE_UNAVAILABLE);
    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
  }

  @Test
  @DisplayName("Should expose static constant aliases")
  void testStaticConstants() {
    assertThat(ExternalServiceException.EXTERNAL_SERVICE_UNAVAILABLE)
        .isEqualTo(ExternalServiceErrorCode.EXTERNAL_SERVICE_UNAVAILABLE);
    assertThat(ExternalServiceException.EXTERNAL_SERVICE_TIMEOUT)
        .isEqualTo(ExternalServiceErrorCode.EXTERNAL_SERVICE_TIMEOUT);
  }
}

