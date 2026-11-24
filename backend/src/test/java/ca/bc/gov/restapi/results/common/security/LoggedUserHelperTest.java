package ca.bc.gov.restapi.results.common.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.bc.gov.restapi.results.common.enums.Role;
import ca.bc.gov.restapi.results.common.exception.UserNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@DisplayName("Unit Test | LoggedUserHelper")
class LoggedUserHelperTest {

  private final LoggedUserHelper helper = new LoggedUserHelper();

  @AfterEach
  void clearContext() {
    SecurityContextHolder.clearContext();
  }

  @Test
  @DisplayName("Recognizes concrete roles in unscoped and app-prefixed CSS formats")
  void shouldMatchConcreteUnscopedAndAppPrefixedRoles() {
    setAuth(
        jwt("azureidir", "rhowell", "bilbo@gov.bc.ca"),
        "SILVA_VIEWER",
        "FAM:EXPIRES:2026-12-31");
    assertTrue(helper.hasConcreteRole(Role.VIEWER));
    assertFalse(helper.hasConcreteRole(Role.ADMIN));

    setAuth(jwt("idir", "rhowell", "bilbo@gov.bc.ca"), "Viewer");
    assertTrue(helper.hasConcreteRole(Role.VIEWER));

    setAuth(jwt("idir", "rhowell", "bilbo@gov.bc.ca"), "VIEWER_FOREST_CLIENT-00012797");
    assertFalse(helper.hasConcreteRole(Role.VIEWER));
  }

  @Test
  @DisplayName("Recognizes abstract roles in CSS FOREST_CLIENT and legacy formats, rejecting multi-scope grants")
  void shouldMatchAbstractRoles() {
    setAuth(
        jwt("azureidir", "rhowell", "rhowell@gov.bc.ca"),
        "SUBMITTER_FOREST_CLIENT-00012797",
        "SILVA_PLANNER_FOREST_CLIENT-00001012",
        "SILVA_PLANNER_DISTRICT-DCC_FOREST_CLIENT-00002176",
        "SILVA_PLANNER_FOREST_CLIENT-00003333_REGION-KOOTENAY_BOUNDARY",
        "Approver_00099999",
        "PLANNER_DISTRICT-DRM",
        "FAM:LABEL:Planner");

    assertTrue(helper.hasAbstractRole(Role.SUBMITTER, "00012797"));
    assertTrue(helper.hasAbstractRole(Role.PLANNER, "00001012"));
    assertTrue(helper.hasAbstractRole(Role.APPROVER, "00099999"));
    assertFalse(helper.hasAbstractRole(Role.PLANNER, "00002176"));
    assertFalse(helper.hasAbstractRole(Role.PLANNER, "00003333"));
    assertFalse(helper.hasAbstractRole(Role.PLANNER, "00012797"));
    assertFalse(helper.hasAbstractRole(Role.VIEWER, "00012797"));
    assertFalse(helper.hasAbstractRole(Role.PLANNER, ""));
  }

  @Test
  @DisplayName("Filters FAM sidecar entries from hasRoleMatching")
  void shouldIgnoreSidecarsInHasRoleMatching() {
    setAuth(
        jwt("azureidir", "rhowell", "rhowell@gov.bc.ca"),
        "FAM:EXPIRES:00001012",
        "SILVA_PLANNER_FOREST_CLIENT-00001012");

    assertFalse(helper.hasRoleMatching(role -> role.startsWith("FAM:")));
    assertTrue(helper.hasRoleMatching(role -> role.endsWith("00001012")));
  }

  @Test
  @DisplayName("Normalizes azureidir to IDIR prefix for audit and logged user IDs")
  void shouldNormalizeAzureIdirForAuditAndLoggedIds() {
    setAuth(jwt("azureidir", "RHOWELL", "rhowell@gov.bc.ca"), "SILVA_VIEWER");

    assertTrue(helper.isIdirUser());
    assertEquals("IDIR\\RHOWELL", helper.getAuditUserId());
    assertEquals("IDIR\\RHOWELL", helper.getLoggedUserId());
  }

  @Test
  @DisplayName("Preserves BCEID audit prefix and BCEIDBUSINESS logged user ID for Business BCeID")
  void shouldPreserveBceidAuditAndLoggedIds() {
    Jwt bceidJwt =
        Jwt.withTokenValue("tok")
            .header("alg", "RS256")
            .claim("identity_provider", "bceidbusiness")
            .claim("bceid_username", "BIZUSER")
            .claim("email", "biz@example.com")
            .build();
    setAuth(bceidJwt, "SUBMITTER_FOREST_CLIENT-00012797");

    assertFalse(helper.isIdirUser());
    assertEquals("BCEID\\BIZUSER", helper.getAuditUserId());
    assertEquals("BCEIDBUSINESS\\BIZUSER", helper.getLoggedUserId());
  }

  @Test
  @DisplayName(
      "Throws NoSuchElementException for unsupported provider and UserNotFoundException"
          + " without Jwt")
  void shouldThrowWhenProviderUnsupportedOrPrincipalMissing() {
    Jwt invalidJwt =
        Jwt.withTokenValue("tok")
            .header("alg", "RS256")
            .claim("identity_provider", "bcsc")
            .claim("email", "user@example.com")
            .build();
    setAuth(invalidJwt, "VIEWER");

    assertThrows(NoSuchElementException.class, helper::isIdirUser);
    assertThrows(NoSuchElementException.class, helper::getAuditUserId);

    TestingAuthenticationToken nonJwtAuth = new TestingAuthenticationToken("user", "creds");
    nonJwtAuth.setAuthenticated(true);
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(nonJwtAuth);
    SecurityContextHolder.setContext(context);

    assertThrows(UserNotFoundException.class, helper::getLoggedUserEmail);
  }

  private Jwt jwt(String provider, String username, String email) {
    return Jwt.withTokenValue("tok")
        .header("alg", "RS256")
        .claim("identity_provider", provider)
        .claim("idir_username", username)
        .claim("email", email)
        .build();
  }

  private void setAuth(Jwt jwt, String... roles) {
    List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(roles);
    JwtAuthenticationToken auth = new JwtAuthenticationToken(jwt, authorities);
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(auth);
    SecurityContextHolder.setContext(context);
  }
}
