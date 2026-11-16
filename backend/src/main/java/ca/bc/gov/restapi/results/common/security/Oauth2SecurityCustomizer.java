package ca.bc.gov.restapi.results.common.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.JWKSourceBuilder;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

/**
 * Customizes OAuth2 resource server JWT validation and authority extraction for BC Gov SSO
 * Keycloak tokens.
 */
@Component
public class Oauth2SecurityCustomizer
    implements Customizer<OAuth2ResourceServerConfigurer<HttpSecurity>> {

  private static final int CONNECT_TIMEOUT_MS = 10_000;
  private static final int READ_TIMEOUT_MS = 15_000;
  private static final String CLAIM_AZP = "azp";
  private static final String CLAIM_CLIENT_ROLES = "client_roles";
  private static final String CLAIM_RESOURCE_ACCESS = "resource_access";
  private static final String CLAIM_ROLES = "roles";
  private static final String INVALID_TOKEN_ERROR = "invalid_token";

  @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
  String issuerUri;

  @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
  String jwkSetUri;

  @Value("${spring.security.oauth2.resourceserver.jwt.client-id}")
  String expectedClientId;

  @Override
  public void customize(OAuth2ResourceServerConfigurer<HttpSecurity> customize) {
    customize.jwt(jwt -> jwt.decoder(jwtDecoder()).jwtAuthenticationConverter(converter()));
  }

  JwtDecoder jwtDecoder() {
    return jwtDecoder(new DefaultResourceRetriever(CONNECT_TIMEOUT_MS, READ_TIMEOUT_MS));
  }

  JwtDecoder jwtDecoder(ResourceRetriever resourceRetriever) {
    try {
      JWKSource<SecurityContext> jwkSource =
          JWKSourceBuilder.create(URI.create(jwkSetUri).toURL(), resourceRetriever)
              .refreshAheadCache(true)
              .retrying(true)
              .build();

      ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
      JWSKeySelector<SecurityContext> keySelector =
          new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, jwkSource);
      jwtProcessor.setJWSKeySelector(keySelector);

      NimbusJwtDecoder decoder = new NimbusJwtDecoder(jwtProcessor);
      OAuth2TokenValidator<Jwt> validator =
          new DelegatingOAuth2TokenValidator<>(
              JwtValidators.createDefaultWithIssuer(issuerUri), this::validateAuthorizedParty);
      decoder.setJwtValidator(validator);
      return decoder;
    } catch (MalformedURLException | IllegalArgumentException ex) {
      throw new IllegalStateException("Invalid Keycloak JWK Set URI: " + jwkSetUri, ex);
    }
  }

  OAuth2TokenValidatorResult validateAuthorizedParty(Jwt jwt) {
    String azp = jwt.getClaimAsString(CLAIM_AZP);
    if (expectedClientId == null
        || expectedClientId.isBlank()
        || !Objects.equals(expectedClientId, azp)) {
      return OAuth2TokenValidatorResult.failure(
          new OAuth2Error(
              INVALID_TOKEN_ERROR,
              "The azp claim does not match the expected Keycloak client ID",
              null));
    }
    return OAuth2TokenValidatorResult.success();
  }

  Converter<Jwt, AbstractAuthenticationToken> converter() {
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(this::extractAuthorities);
    return converter;
  }

  private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
    return extractRawRoles(jwt).stream()
        .<GrantedAuthority>map(SimpleGrantedAuthority::new)
        .toList();
  }

  private List<String> extractRawRoles(Jwt jwt) {
    List<String> clientRoles = filterRoles(toStringList(jwt.getClaim(CLAIM_CLIENT_ROLES)));
    if (!clientRoles.isEmpty()) {
      return clientRoles;
    }

    Map<String, Object> resourceAccess = jwt.getClaimAsMap(CLAIM_RESOURCE_ACCESS);
    if (resourceAccess == null || resourceAccess.isEmpty()) {
      return List.of();
    }

    String azp = jwt.getClaimAsString(CLAIM_AZP);
    String clientKey = (azp != null && !azp.isBlank()) ? azp : expectedClientId;
    if (clientKey == null || clientKey.isBlank()) {
      return List.of();
    }

    Object clientEntry = resourceAccess.get(clientKey);
    if (clientEntry instanceof Map<?, ?> clientMap) {
      return filterRoles(toStringList(clientMap.get(CLAIM_ROLES)));
    }
    return List.of();
  }

  private List<String> filterRoles(List<String> roles) {
    return roles.stream()
        .filter(role -> !role.isBlank() && !CssRoleNaming.isSidecarRole(role))
        .toList();
  }

  private List<String> toStringList(Object rawClaim) {
    if (rawClaim instanceof Collection<?> collection) {
      return collection.stream().filter(Objects::nonNull).map(Object::toString).toList();
    }
    return Collections.emptyList();
  }
}
