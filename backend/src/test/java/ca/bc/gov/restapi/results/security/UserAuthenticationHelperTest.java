package ca.bc.gov.restapi.results.security;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.enums.IdentityProvider;
import ca.bc.gov.restapi.results.common.enums.Role;
import ca.bc.gov.restapi.results.common.security.UserAuthenticationHelper;
import ca.bc.gov.restapi.results.common.security.UserInfo;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit Test | UserAuthenticationHelper")
class UserAuthenticationHelperTest {

  private UserAuthenticationHelper userAuthenticationHelper;

  @BeforeEach
  void setup() {
    userAuthenticationHelper = new UserAuthenticationHelper();
  }

  @Test
  @DisplayName("getUserInfoIdirTest")
  void getUserInfoIdirTest() {
    Authentication authentication = mock(Authentication.class);
    SecurityContext securityContext = mock(SecurityContext.class);
    SecurityContextHolder.setContext(securityContext);

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.isAuthenticated()).thenReturn(true);

    Jwt.Builder builder = Jwt.withTokenValue("myTokenValue");
    builder.subject("BAGGINGS");
    builder.header("alg", "HS256");
    builder.header("typ", "JWT");
    builder.claim("email", "bilbo.baggings@gov.bc.ca");
    builder.claim("custom:idp_display_name", "from Baggings, Bilbo LWRS:EX");
    builder.claim("custom:idp_username", "BAGGINGS");
    builder.claim("custom:idp_name", "idir");
    builder.claim("cognito:username", "IDIR\\BAGGINGS");
    builder.claim("cognito:groups", List.of("Viewer", "Planner_00001012"));

    when(authentication.getPrincipal()).thenReturn(builder.build());

    Optional<UserInfo> userInfoOptional = userAuthenticationHelper.getUserInfo();
    Assertions.assertTrue(userInfoOptional.isPresent());

    UserInfo userInfo = userInfoOptional.get();
    Assertions.assertEquals("IDIR\\BAGGINGS", userInfo.id());
    Assertions.assertEquals("Bilbo", userInfo.firstName());
    Assertions.assertEquals("Baggings", userInfo.lastName());
    Assertions.assertEquals("bilbo.baggings@gov.bc.ca", userInfo.email());
    Assertions.assertEquals("from Baggings, Bilbo LWRS:EX", userInfo.displayName());
    Assertions.assertEquals("BAGGINGS", userInfo.idirUsername());
    Assertions.assertEquals(IdentityProvider.IDIR, userInfo.identityProvider());
    Assertions.assertEquals(2, userInfo.roles().size());
  }

  @Test
  @DisplayName("getUserInfoBusinessBceidTest")
  void getUserInfoBusinessBceidTest() {
    Authentication authentication = mock(Authentication.class);
    SecurityContext securityContext = mock(SecurityContext.class);
    SecurityContextHolder.setContext(securityContext);

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.isAuthenticated()).thenReturn(true);

    Jwt.Builder builder = Jwt.withTokenValue("myTokenValue");
    builder.subject("MORDOR-BCEID");
    builder.header("alg", "HS256");
    builder.header("typ", "JWT");
    builder.claim("email", "lord.sauron@mordor.middleearth");
    builder.claim("custom:idp_display_name", "Lord Sauron of Mordor");
    builder.claim("custom:idp_username", "MORDOR-BCEID");
    builder.claim("custom:idp_name", "bceidbusiness");
    builder.claim("cognito:username", "BCEIDBUSINESS\\MORDOR-BCEID");

    when(authentication.getPrincipal()).thenReturn(builder.build());

    Optional<UserInfo> userInfoOptional = userAuthenticationHelper.getUserInfo();
    Assertions.assertTrue(userInfoOptional.isPresent());

    UserInfo userInfo = userInfoOptional.get();
    Assertions.assertEquals("BCEIDBUSINESS\\MORDOR-BCEID", userInfo.id());
    Assertions.assertEquals("Lord", userInfo.firstName());
    Assertions.assertEquals("Sauron of Mordor", userInfo.lastName());
    Assertions.assertEquals("lord.sauron@mordor.middleearth", userInfo.email());
    Assertions.assertEquals("Lord Sauron of Mordor", userInfo.displayName());
    Assertions.assertEquals("MORDOR-BCEID", userInfo.businessName());
    Assertions.assertEquals(IdentityProvider.BUSINESS_BCEID, userInfo.identityProvider());
  }

  @Test
  @DisplayName("getUserInfoTestNotAuthenticated")
  void getUserInfoTestNotAuthenticated() {
    Authentication authentication = mock(Authentication.class);
    SecurityContext securityContext = mock(SecurityContext.class);
    SecurityContextHolder.setContext(securityContext);

    when(securityContext.getAuthentication()).thenReturn(authentication);

    Optional<UserInfo> userInfoOptional = userAuthenticationHelper.getUserInfo();
    Assertions.assertFalse(userInfoOptional.isPresent());
  }

  @Test
  @DisplayName("hasConcreteRole returns true for matching concrete role")
  void testHasConcreteRole() {
    Authentication authentication = mock(Authentication.class);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    Jwt.Builder builder = Jwt.withTokenValue("myTokenValue");
    builder.header("alg", "HS256");
    builder.header("typ", "JWT");
    builder.claim("custom:idp_display_name", "Test User");
    builder.claim("custom:idp_username", "TESTUSER");
    builder.claim("custom:idp_name", "idir");
    builder.claim("email", "test@gov.bc.ca");
    builder.claim("cognito:groups", List.of("VIEWER"));

    Jwt jwt = builder.build();
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getPrincipal()).thenReturn(jwt);

    Assertions.assertTrue(userAuthenticationHelper.hasConcreteRole(Role.VIEWER));
  }

  @Test
  @DisplayName("hasAbstractRole returns true for matching abstract role with clientId")
  void testHasAbstractRole() {
    Authentication authentication = mock(Authentication.class);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    Jwt.Builder builder = Jwt.withTokenValue("myTokenValue");
    builder.header("alg", "HS256");
    builder.header("typ", "JWT");
    builder.claim("custom:idp_display_name", "Test User");
    builder.claim("custom:idp_username", "TESTUSER");
    builder.claim("custom:idp_name", "idir");
    builder.claim("email", "test@gov.bc.ca");
    builder.claim("cognito:groups", List.of("PLANNER_00001012"));

    Jwt jwt = builder.build();
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getPrincipal()).thenReturn(jwt);

    Assertions.assertTrue(userAuthenticationHelper.hasAbstractRole(Role.PLANNER, "00001012"));
  }
}
