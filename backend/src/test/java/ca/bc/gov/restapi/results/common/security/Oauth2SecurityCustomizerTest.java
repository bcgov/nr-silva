package ca.bc.gov.restapi.results.common.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jose.util.Resource;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidationException;

@DisplayName("Unit Test | Oauth2SecurityCustomizer")
class Oauth2SecurityCustomizerTest {

  private static final String ISSUER_URI = "https://dev.loginproxy.gov.bc.ca/auth/realms/standard";
  private static final String JWK_SET_URI =
      "https://dev.loginproxy.gov.bc.ca/auth/realms/standard/protocol/openid-connect/certs";
  private static final String CLIENT_ID = "nr-silva";

  private RSAKey rsaKey;
  private ResourceRetriever jwksRetriever;
  private Oauth2SecurityCustomizer customizer;

  @BeforeEach
  void setUp() throws Exception {
    rsaKey = new RSAKeyGenerator(2048).keyID("test-kid").generate();
    String jwksJson = new JWKSet(rsaKey.toPublicJWK()).toString();
    jwksRetriever = url -> new Resource(jwksJson, "application/json");

    customizer = new Oauth2SecurityCustomizer();
    customizer.issuerUri = ISSUER_URI;
    customizer.jwkSetUri = JWK_SET_URI;
    customizer.expectedClientId = CLIENT_ID;
  }

  @Test
  @DisplayName("Builds default JwtDecoder lazily without network I/O on creation")
  void shouldBuildDefaultJwtDecoderLazily() {
    assertNotNull(customizer.jwtDecoder());
  }

  @Test
  @DisplayName("Decodes valid signed Keycloak JWT with matching issuer and azp")
  void shouldDecodeValidKeycloakJwt() throws Exception {
    JwtDecoder decoder = customizer.jwtDecoder(jwksRetriever);
    String token = signToken(ISSUER_URI, CLIENT_ID, List.of("SILVA_VIEWER"));

    Jwt decoded = decoder.decode(token);

    assertNotNull(decoded);
    assertEquals(CLIENT_ID, decoded.getClaimAsString("azp"));
    assertEquals(List.of("SILVA_VIEWER"), decoded.getClaimAsStringList("client_roles"));
  }

  @Test
  @DisplayName("Rejects JWT when azp claim does not match expected client ID")
  void shouldRejectTokenWithMismatchedAzp() throws Exception {
    JwtDecoder decoder = customizer.jwtDecoder(jwksRetriever);
    String wrongAzpToken = signToken(ISSUER_URI, "wrong-client", List.of("SILVA_VIEWER"));

    assertThrows(JwtValidationException.class, () -> decoder.decode(wrongAzpToken));
  }

  @Test
  @DisplayName("Rejects JWT when azp claim is missing")
  void shouldRejectTokenWithMissingAzp() throws Exception {
    JwtDecoder decoder = customizer.jwtDecoder(jwksRetriever);
    String missingAzpToken = signToken(ISSUER_URI, null, List.of("SILVA_VIEWER"));

    assertThrows(JwtValidationException.class, () -> decoder.decode(missingAzpToken));
  }

  @Test
  @DisplayName("Rejects JWT when issuer does not match expected issuer URI")
  void shouldRejectTokenWithMismatchedIssuer() throws Exception {
    JwtDecoder decoder = customizer.jwtDecoder(jwksRetriever);
    String wrongIssuerToken =
        signToken("https://rogue.example.com/realms/standard", CLIENT_ID, List.of("SILVA_VIEWER"));

    assertThrows(JwtValidationException.class, () -> decoder.decode(wrongIssuerToken));
  }

  @Test
  @DisplayName("Fails azp validation closed when expectedClientId is blank")
  void shouldFailClosedWhenExpectedClientIdBlank() {
    customizer.expectedClientId = " ";
    Jwt jwt =
        Jwt.withTokenValue("tok")
            .header("alg", "RS256")
            .claim("azp", CLIENT_ID)
            .build();

    OAuth2TokenValidatorResult result = customizer.validateAuthorizedParty(jwt);

    assertTrue(result.hasErrors());
  }

  @Test
  @DisplayName("Throws IllegalStateException when JWK Set URI is malformed")
  void shouldThrowOnMalformedJwkSetUri() {
    customizer.jwkSetUri = "http://[invalid-host";
    assertThrows(IllegalStateException.class, () -> customizer.jwtDecoder());
  }

  @Test
  @DisplayName("Extracts authorities from client_roles without prefix and filters FAM sidecars")
  void shouldExtractAuthoritiesFromClientRolesAndFilterSidecars() {
    Jwt jwt =
        Jwt.withTokenValue("tok")
            .header("alg", "RS256")
            .claim("sub", "user1")
            .claim("azp", CLIENT_ID)
            .claim(
                "client_roles",
                List.of(
                    "SILVA_VIEWER",
                    "SUBMITTER_FOREST_CLIENT-00012797",
                    "FAM:LABEL:Submitter",
                    "FAM:DESC:Description",
                    "FAM:EXPIRES:2026-12-31",
                    " "))
            .build();

    AbstractAuthenticationToken authentication = customizer.converter().convert(jwt);

    assertNotNull(authentication);
    Set<String> authorities =
        authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());
    assertEquals(Set.of("SILVA_VIEWER", "SUBMITTER_FOREST_CLIENT-00012797"), authorities);
  }

  @Test
  @DisplayName("Falls back to resource_access.<azp>.roles when client_roles is absent or empty")
  void shouldFallbackToResourceAccessRolesAndFilterSidecars() {
    Jwt jwt =
        Jwt.withTokenValue("tok")
            .header("alg", "RS256")
            .claim("sub", "user1")
            .claim("azp", CLIENT_ID)
            .claim("client_roles", List.of())
            .claim(
                "resource_access",
                Map.of(
                    CLIENT_ID,
                    Map.of(
                        "roles",
                        List.of(
                            "SILVA_PLANNER_FOREST_CLIENT-00001012",
                            "FAM:EXPIRES:2026-06-01")),
                    "other-client",
                    Map.of("roles", List.of("ADMIN"))))
            .build();

    AbstractAuthenticationToken authentication = customizer.converter().convert(jwt);

    assertNotNull(authentication);
    Set<String> authorities =
        authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());
    assertEquals(Set.of("SILVA_PLANNER_FOREST_CLIENT-00001012"), authorities);
    assertFalse(authorities.contains("ADMIN"));
  }

  @Test
  @DisplayName(
      "Falls back to resource_access when client_roles contains only FAM sidecars or blanks")
  void shouldFallbackToResourceAccessWhenClientRolesOnlyContainsSidecarsOrBlanks() {
    Jwt jwt =
        Jwt.withTokenValue("tok")
            .header("alg", "RS256")
            .claim("sub", "user1")
            .claim("azp", CLIENT_ID)
            .claim("client_roles", List.of("FAM:EXPIRES:2026-06-01", " "))
            .claim(
                "resource_access",
                Map.of(CLIENT_ID, Map.of("roles", List.of("SILVA_VIEWER"))))
            .build();

    AbstractAuthenticationToken authentication = customizer.converter().convert(jwt);

    assertNotNull(authentication);
    Set<String> authorities =
        authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());
    assertEquals(Set.of("SILVA_VIEWER"), authorities);
  }

  private String signToken(String issuer, String azp, List<String> clientRoles) throws Exception {
    Instant now = Instant.now();
    JWTClaimsSet.Builder claimsBuilder =
        new JWTClaimsSet.Builder()
            .subject("user-123")
            .issuer(issuer)
            .issueTime(Date.from(now))
            .expirationTime(Date.from(now.plusSeconds(300)))
            .claim("client_roles", clientRoles);

    if (azp != null) {
      claimsBuilder.claim("azp", azp);
    }

    SignedJWT signedJwt =
        new SignedJWT(
            new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaKey.getKeyID()).build(),
            claimsBuilder.build());
    signedJwt.sign(new RSASSASigner(rsaKey));
    return signedJwt.serialize();
  }
}
