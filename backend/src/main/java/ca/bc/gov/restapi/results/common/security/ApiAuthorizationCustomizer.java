package ca.bc.gov.restapi.results.common.security;

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

  @Override
  public void customize(
      AuthorizeHttpRequestsConfigurer<HttpSecurity>
          .AuthorizationManagerRequestMatcherRegistry authorize
  ) {

    authorize
        // Allow actuator endpoints to be accessed without authentication
        // This is useful for monitoring and health checks
        .requestMatchers(HttpMethod.GET, "/actuator/**")
        .permitAll()
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
