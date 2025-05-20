package ca.bc.gov.restapi.results.common.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

/**
 * This class contains the configuration for API authorization. This is where our security rules are
 * defined.
 */
@Component
public class ApiAuthorizationCustomizer implements
    Customizer<
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry
        > {

  /**
   * The environment of the application, which is injected from the application properties. The
   * default value is "PROD".
   */
  @Value("${ca.bc.gov.nrs.environment:PROD}")
  String environment;

  @Override
  public void customize(
      AuthorizeHttpRequestsConfigurer<HttpSecurity>
          .AuthorizationManagerRequestMatcherRegistry authorize
  ) {

    authorize
        // Allow actuator endpoints to be accessed without authentication
        .requestMatchers(HttpMethod.GET, "/actuator/**")
        .permitAll();

    // Only allow OpenAPI and Swagger UI in the local environment
    if ("local".equalsIgnoreCase(environment)) {
      authorize
        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
        .permitAll();
    }

    authorize
        // Protect everything under /api with authentication
        .requestMatchers("/api/**")
        .authenticated()
        // Allow OPTIONS requests to be accessed with authentication
        .requestMatchers(HttpMethod.OPTIONS, "/**")
        .authenticated()
        // Deny all other requests
        .anyRequest().denyAll();

  }
}
