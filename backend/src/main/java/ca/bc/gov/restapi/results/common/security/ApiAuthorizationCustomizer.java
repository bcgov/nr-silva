package ca.bc.gov.restapi.results.common.security;

import ca.bc.gov.restapi.results.common.util.SecurityEnvironmentUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

/**
 * This class contains the configuration for API authorization. This is where our security rules are
 * defined.
 */
@Component
public class ApiAuthorizationCustomizer {

  private final String environment;

  /**
   * The environment of the application, which is injected from the application properties. The
   * default value is "PROD".
   */
  public ApiAuthorizationCustomizer(@Value("${ca.bc.gov.nrs.environment:PROD}") String environment) {
    this.environment = environment;
  }

  public void configure(
      AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry
          authorize) {

    authorize
        // Allow actuator endpoints to be accessed without authentication
        .requestMatchers(HttpMethod.GET, "/actuator/**")
        .permitAll();

    // Only allow OpenAPI and Swagger UI in the local environment
    if (SecurityEnvironmentUtil.isLocalEnvironment(environment)) {
      authorize
          .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
          .permitAll();
    } else {
      authorize.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").denyAll();
    }

    // Protect everything under /api with authentication
    authorize.requestMatchers("/api/**").authenticated();

    // Allow OPTIONS requests to be accessed with authentication
    authorize.requestMatchers(HttpMethod.OPTIONS, "/**").authenticated();

    // Deny all other requests last so additional matchers cannot be added afterwards
    authorize.anyRequest().denyAll();
  }
}
