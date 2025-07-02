package ca.bc.gov.restapi.results.common.endpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@DisplayName("Unit Test | GlobalErrorResponseEndpoint")
class GlobalErrorResponseEndpointTest {

    private final GlobalErrorResponseEndpoint handler = new GlobalErrorResponseEndpoint();
    private final WebRequest mockRequest = mock(WebRequest.class);

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
}
