package ca.bc.gov.restapi.results.common.security;

import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.XXssConfig;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class HeadersSecurityCustomizer implements Customizer<HeadersConfigurer<HttpSecurity>> {

  @Value("${ca.bc.gov.nrs.self-uri}")
  String selfUri;

  /**
   * The environment of the application, which is injected from the application properties. The
   * default value is "PROD".
   */
  @Value("${ca.bc.gov.nrs.environment:PROD}")
  String environment;

  @Override
  public void customize(HeadersConfigurer<HttpSecurity> headerSpec) {
// Define the policy directives for the Content-Security-Policy header.
    String policyDirectives = String.join("; ",
        "default-src 'none'",
        "connect-src 'self' " + selfUri,
        "script-src 'strict-dynamic' 'nonce-" + UUID.randomUUID()
        + "' " + ("local".equalsIgnoreCase(environment) ? "http: " : StringUtils.EMPTY) + "https:",
        "object-src 'none'",
        "base-uri 'none'",
        "frame-ancestors 'none'",
        "require-trusted-types-for 'script'",
        "report-uri " + selfUri
    );

    // Customize the HTTP headers.
    headerSpec
        .frameOptions(FrameOptionsConfig::deny) // Set the X-Frame-Options header to "DENY".
        .contentSecurityPolicy(
            contentSecurityPolicySpec -> contentSecurityPolicySpec.policyDirectives(
                policyDirectives)) // Set the Content-Security-Policy header.
        .httpStrictTransportSecurity(hstsSpec ->
            hstsSpec.maxAgeInSeconds(Duration.ofDays(30).getSeconds())
                .includeSubDomains(true)) // Set the Strict-Transport-Security header.
        .xssProtection(XXssConfig::disable) // Disable the X-XSS-Protection header.
        .contentTypeOptions(
            Customizer.withDefaults()) // Set the X-Content-Type-Options header to its default value.
        .referrerPolicy(referrerPolicySpec -> referrerPolicySpec.policy(
            ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)) // Set the Referrer-Policy header.
        .permissionsPolicy(permissionsPolicySpec -> permissionsPolicySpec.policy(
            "geolocation=(), microphone=(), camera=(), speaker=(), usb=(), bluetooth=(), payment=(), interest-cohort=()")) // Set the Permissions-Policy header.
    ;
  }
}