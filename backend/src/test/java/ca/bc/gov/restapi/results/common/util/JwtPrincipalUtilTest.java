package ca.bc.gov.restapi.results.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.bc.gov.restapi.results.common.enums.IdentityProvider;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@DisplayName("Unit Test | JwtPrincipalUtil")
class JwtPrincipalUtilTest {

  private static final Map<String, Object> JWT_HEADERS = Map.of("alg", "HS256", "typ", "JWT");
  private static final Instant ISSUED_AT = Instant.parse("2024-05-12T10:30:00Z");
  private static final Instant EXPIRES_AT = Instant.parse("2024-05-12T11:30:00Z");

  private Jwt createJwt(Map<String, Object> claims) {
    return new Jwt("mock-token-value", ISSUED_AT, EXPIRES_AT, JWT_HEADERS, claims);
  }

  private JwtAuthenticationToken createJwtAuthenticationToken(Map<String, Object> claims) {
    return new JwtAuthenticationToken(createJwt(claims), Collections.emptyList());
  }

  @Nested
  @DisplayName("getProvider Tests")
  class GetProviderTests {

    @Test
    @DisplayName("Should return IDIR provider for JwtAuthenticationToken")
    void shouldReturnIdirProviderForJwtAuthenticationToken() {
      Map<String, Object> claims =
          Map.of("custom:idp_name", "idir", "custom:idp_username", "TESTUSER");

      JwtAuthenticationToken token = createJwtAuthenticationToken(claims);
      String provider = JwtPrincipalUtil.getProvider(token);

      assertEquals("IDIR", provider);
    }

    @Test
    @DisplayName("Should return IDIR provider for Jwt")
    void shouldReturnIdirProviderForJwt() {
      Map<String, Object> claims =
          Map.of("custom:idp_name", "idir", "custom:idp_username", "TESTUSER");

      Jwt jwt = createJwt(claims);
      String provider = JwtPrincipalUtil.getProvider(jwt);

      assertEquals("IDIR", provider);
    }

    @Test
    @DisplayName("Should return BCEIDBUSINESS provider for JwtAuthenticationToken")
    void shouldReturnBceidBusinessProviderForJwtAuthenticationToken() {
      Map<String, Object> claims =
          Map.of("custom:idp_name", "bceidbusiness", "custom:idp_username", "BUSINESSUSER");

      JwtAuthenticationToken token = createJwtAuthenticationToken(claims);
      String provider = JwtPrincipalUtil.getProvider(token);

      assertEquals("BCEIDBUSINESS", provider);
    }

    @Test
    @DisplayName("Should return BCEIDBUSINESS provider for Jwt")
    void shouldReturnBceidBusinessProviderForJwt() {
      Map<String, Object> claims =
          Map.of("custom:idp_name", "bceidbusiness", "custom:idp_username", "BUSINESSUSER");

      Jwt jwt = createJwt(claims);
      String provider = JwtPrincipalUtil.getProvider(jwt);

      assertEquals("BCEIDBUSINESS", provider);
    }

    @Test
    @DisplayName("Should return empty string when provider is missing")
    void shouldReturnEmptyStringWhenProviderIsMissing() {
      Map<String, Object> claims = Map.of("custom:idp_username", "TESTUSER");

      Jwt jwt = createJwt(claims);
      String provider = JwtPrincipalUtil.getProvider(jwt);

      assertEquals("", provider);
    }
  }

  @Nested
  @DisplayName("getUserId Tests")
  class GetUserIdTests {

    @Test
    @DisplayName("Should return userId with provider prefix for JwtAuthenticationToken")
    void shouldReturnUserIdWithProviderPrefixForJwtAuthenticationToken() {
      Map<String, Object> claims =
          Map.of("custom:idp_name", "idir", "custom:idp_username", "TESTUSER");

      JwtAuthenticationToken token = createJwtAuthenticationToken(claims);
      String userId = JwtPrincipalUtil.getUserId(token);

      assertEquals("IDIR\\TESTUSER", userId);
    }

    @Test
    @DisplayName("Should return userId with provider prefix for Jwt")
    void shouldReturnUserIdWithProviderPrefixForJwt() {
      Map<String, Object> claims =
          Map.of("custom:idp_name", "idir", "custom:idp_username", "TESTUSER");

      Jwt jwt = createJwt(claims);
      String userId = JwtPrincipalUtil.getUserId(jwt);

      assertEquals("IDIR\\TESTUSER", userId);
    }

    @Test
    @DisplayName("Should return userId with BCEIDBUSINESS provider")
    void shouldReturnUserIdWithBceidBusinessProvider() {
      Map<String, Object> claims =
          Map.of("custom:idp_name", "bceidbusiness", "custom:idp_username", "BIZUSER123");

      Jwt jwt = createJwt(claims);
      String userId = JwtPrincipalUtil.getUserId(jwt);

      assertEquals("BCEIDBUSINESS\\BIZUSER123", userId);
    }

    @Test
    @DisplayName("Should return empty backslash when username is missing")
    void shouldReturnEmptyBackslashWhenUsernameIsMissing() {
      Map<String, Object> claims = Map.of("custom:idp_name", "idir");

      Jwt jwt = createJwt(claims);
      String userId = JwtPrincipalUtil.getUserId(jwt);

      assertEquals("IDIR\\", userId);
    }
  }

  @Nested
  @DisplayName("getEmail Tests")
  class GetEmailTests {

    @Test
    @DisplayName("Should return email for JwtAuthenticationToken")
    void shouldReturnEmailForJwtAuthenticationToken() {
      Map<String, Object> claims =
          Map.of("email", "test.user@gov.bc.ca", "custom:idp_name", "idir");

      JwtAuthenticationToken token = createJwtAuthenticationToken(claims);
      String email = JwtPrincipalUtil.getEmail(token);

      assertEquals("test.user@gov.bc.ca", email);
    }

    @Test
    @DisplayName("Should return email for Jwt")
    void shouldReturnEmailForJwt() {
      Map<String, Object> claims =
          Map.of("email", "test.user@gov.bc.ca", "custom:idp_name", "idir");

      Jwt jwt = createJwt(claims);
      String email = JwtPrincipalUtil.getEmail(jwt);

      assertEquals("test.user@gov.bc.ca", email);
    }

    @Test
    @DisplayName("Should return empty string when email is missing")
    void shouldReturnEmptyStringWhenEmailIsMissing() {
      Map<String, Object> claims = Map.of("custom:idp_name", "idir");

      Jwt jwt = createJwt(claims);
      String email = JwtPrincipalUtil.getEmail(jwt);

      assertEquals("", email);
    }
  }

  @Nested
  @DisplayName("getName Tests")
  class GetNameTests {

    @Test
    @DisplayName("Should return full name from given and family names for IDIR")
    void shouldReturnFullNameFromGivenAndFamilyNamesForIdir() {
      Map<String, Object> claims =
          Map.of(
              "given_name", "John",
              "family_name", "Doe",
              "custom:idp_name", "idir",
              "custom:idp_display_name", "");

      Jwt jwt = createJwt(claims);
      String name = JwtPrincipalUtil.getName(jwt);

      assertEquals("John Doe", name);
    }

    @Test
    @DisplayName("Should parse display name for BCEIDBUSINESS with space separator")
    void shouldParseDisplayNameForBceidBusinessWithSpace() {
      Map<String, Object> claims =
          Map.of(
              "given_name", "",
              "family_name", "",
              "custom:idp_name", "bceidbusiness",
              "custom:idp_display_name", "Alice Smith");

      Jwt jwt = createJwt(claims);
      String name = JwtPrincipalUtil.getName(jwt);

      assertEquals("Alice Smith", name);
    }

    @Test
    @DisplayName("Should parse IDIR display name with comma format")
    void shouldParseIdirDisplayNameWithCommaFormat() {
      Map<String, Object> claims =
          Map.of(
              "given_name", "",
              "family_name", "",
              "custom:idp_name", "idir",
              "custom:idp_display_name", "Smith, Bob WLRS:EX");

      Jwt jwt = createJwt(claims);
      String name = JwtPrincipalUtil.getName(jwt);

      assertEquals("Bob Smith", name);
    }

    @Test
    @DisplayName("Should parse IDIR display name with middle initial")
    void shouldParseIdirDisplayNameWithMiddleInitial() {
      Map<String, Object> claims =
          Map.of(
              "given_name", "",
              "family_name", "",
              "custom:idp_name", "idir",
              "custom:idp_display_name", "Doe, John A WLRS:EX");

      Jwt jwt = createJwt(claims);
      String name = JwtPrincipalUtil.getName(jwt);

      assertEquals("John Doe", name);
    }

    @Test
    @DisplayName("Should parse display name with prefix like 'da' or 'de'")
    void shouldParseDisplayNameWithPrefix() {
      Map<String, Object> claims =
          Map.of(
              "given_name", "",
              "family_name", "",
              "custom:idp_name", "idir",
              "custom:idp_display_name", "da Silva, Maria WLRS:EX");

      Jwt jwt = createJwt(claims);
      String name = JwtPrincipalUtil.getName(jwt);

      assertEquals("Maria Silva", name);
    }

    @Test
    @DisplayName("Should return empty string when all name fields are missing")
    void shouldReturnEmptyStringWhenAllNameFieldsAreMissing() {
      Map<String, Object> claims =
          Map.of(
              "given_name", "",
              "family_name", "",
              "custom:idp_name", "idir",
              "custom:idp_display_name", "");

      Jwt jwt = createJwt(claims);
      String name = JwtPrincipalUtil.getName(jwt);

      assertEquals("", name);
    }

    @Test
    @DisplayName("Should work with JwtAuthenticationToken")
    void shouldWorkWithJwtAuthenticationToken() {
      Map<String, Object> claims =
          Map.of(
              "given_name", "Jane",
              "family_name", "Smith",
              "custom:idp_name", "idir",
              "custom:idp_display_name", "");

      JwtAuthenticationToken token = createJwtAuthenticationToken(claims);
      String name = JwtPrincipalUtil.getName(token);

      assertEquals("Jane Smith", name);
    }
  }

  @Nested
  @DisplayName("getFirstName Tests")
  class GetFirstNameTests {

    @Test
    @DisplayName("Should return first name from given_name for IDIR")
    void shouldReturnFirstNameFromGivenNameForIdir() {
      Map<String, Object> claims =
          Map.of(
              "given_name", "John",
              "family_name", "Doe",
              "custom:idp_name", "idir",
              "custom:idp_display_name", "");

      Jwt jwt = createJwt(claims);
      String firstName = JwtPrincipalUtil.getFirstName(jwt);

      assertEquals("John", firstName);
    }

    @Test
    @DisplayName("Should parse first name from display name for BCEIDBUSINESS")
    void shouldParseFirstNameFromDisplayNameForBceidBusiness() {
      Map<String, Object> claims =
          Map.of(
              "given_name", "",
              "family_name", "",
              "custom:idp_name", "bceidbusiness",
              "custom:idp_display_name", "Alice Smith");

      Jwt jwt = createJwt(claims);
      String firstName = JwtPrincipalUtil.getFirstName(jwt);

      assertEquals("Alice", firstName);
    }

    @Test
    @DisplayName("Should parse first name from comma-separated IDIR display name")
    void shouldParseFirstNameFromCommaSeparatedIdirDisplayName() {
      Map<String, Object> claims =
          Map.of(
              "given_name", "",
              "family_name", "",
              "custom:idp_name", "idir",
              "custom:idp_display_name", "Smith, Bob WLRS:EX");

      Jwt jwt = createJwt(claims);
      String firstName = JwtPrincipalUtil.getFirstName(jwt);

      assertEquals("Bob", firstName);
    }

    @Test
    @DisplayName("Should return empty string when first name is missing")
    void shouldReturnEmptyStringWhenFirstNameIsMissing() {
      Map<String, Object> claims =
          Map.of(
              "given_name", "",
              "family_name", "",
              "custom:idp_name", "idir",
              "custom:idp_display_name", "");

      Jwt jwt = createJwt(claims);
      String firstName = JwtPrincipalUtil.getFirstName(jwt);

      assertEquals("", firstName);
    }
  }

  @Nested
  @DisplayName("getLastName Tests")
  class GetLastNameTests {

    @Test
    @DisplayName("Should return last name from family_name for IDIR")
    void shouldReturnLastNameFromFamilyNameForIdir() {
      Map<String, Object> claims =
          Map.of(
              "given_name", "John",
              "family_name", "Doe",
              "custom:idp_name", "idir",
              "custom:idp_display_name", "");

      Jwt jwt = createJwt(claims);
      String lastName = JwtPrincipalUtil.getLastName(jwt);

      assertEquals("Doe", lastName);
    }

    @Test
    @DisplayName("Should parse last name from display name for BCEIDBUSINESS")
    void shouldParseLastNameFromDisplayNameForBceidBusiness() {
      Map<String, Object> claims =
          Map.of(
              "given_name", "",
              "family_name", "",
              "custom:idp_name", "bceidbusiness",
              "custom:idp_display_name", "Alice Smith");

      Jwt jwt = createJwt(claims);
      String lastName = JwtPrincipalUtil.getLastName(jwt);

      assertEquals("Smith", lastName);
    }

    @Test
    @DisplayName("Should parse last name from comma-separated IDIR display name")
    void shouldParseLastNameFromCommaSeparatedIdirDisplayName() {
      Map<String, Object> claims =
          Map.of(
              "given_name", "",
              "family_name", "",
              "custom:idp_name", "idir",
              "custom:idp_display_name", "Smith, Bob WLRS:EX");

      Jwt jwt = createJwt(claims);
      String lastName = JwtPrincipalUtil.getLastName(jwt);

      assertEquals("Smith", lastName);
    }

    @Test
    @DisplayName("Should handle last name with prefix like 'da' or 'de'")
    void shouldHandleLastNameWithPrefix() {
      Map<String, Object> claims =
          Map.of(
              "given_name", "",
              "family_name", "",
              "custom:idp_name", "idir",
              "custom:idp_display_name", "da Silva, Maria WLRS:EX");

      Jwt jwt = createJwt(claims);
      String lastName = JwtPrincipalUtil.getLastName(jwt);

      assertEquals("Silva", lastName);
    }

    @Test
    @DisplayName("Should return empty string when last name is missing")
    void shouldReturnEmptyStringWhenLastNameIsMissing() {
      Map<String, Object> claims =
          Map.of(
              "given_name", "",
              "family_name", "",
              "custom:idp_name", "idir",
              "custom:idp_display_name", "");

      Jwt jwt = createJwt(claims);
      String lastName = JwtPrincipalUtil.getLastName(jwt);

      assertEquals("", lastName);
    }
  }

  @Nested
  @DisplayName("getDisplayName Tests")
  class GetDisplayNameTests {

    @Test
    @DisplayName("Should return display name for JwtAuthenticationToken")
    void shouldReturnDisplayNameForJwtAuthenticationToken() {
      Map<String, Object> claims =
          Map.of("custom:idp_display_name", "Smith, John WLRS:EX", "custom:idp_name", "idir");

      JwtAuthenticationToken token = createJwtAuthenticationToken(claims);
      String displayName = JwtPrincipalUtil.getDisplayName(token);

      assertEquals("Smith, John WLRS:EX", displayName);
    }

    @Test
    @DisplayName("Should return display name for Jwt")
    void shouldReturnDisplayNameForJwt() {
      Map<String, Object> claims =
          Map.of("custom:idp_display_name", "Alice Johnson", "custom:idp_name", "bceidbusiness");

      Jwt jwt = createJwt(claims);
      String displayName = JwtPrincipalUtil.getDisplayName(jwt);

      assertEquals("Alice Johnson", displayName);
    }

    @Test
    @DisplayName("Should return empty string when display name is missing")
    void shouldReturnEmptyStringWhenDisplayNameIsMissing() {
      Map<String, Object> claims = Map.of("custom:idp_name", "idir");

      Jwt jwt = createJwt(claims);
      String displayName = JwtPrincipalUtil.getDisplayName(jwt);

      assertEquals("", displayName);
    }
  }

  @Nested
  @DisplayName("getIdpUsername Tests")
  class GetIdpUsernameTests {

    @Test
    @DisplayName("Should return IDP username for JwtAuthenticationToken")
    void shouldReturnIdpUsernameForJwtAuthenticationToken() {
      Map<String, Object> claims =
          Map.of("custom:idp_username", "TESTUSER", "custom:idp_name", "idir");

      JwtAuthenticationToken token = createJwtAuthenticationToken(claims);
      String username = JwtPrincipalUtil.getIdpUsername(token);

      assertEquals("TESTUSER", username);
    }

    @Test
    @DisplayName("Should return IDP username for Jwt")
    void shouldReturnIdpUsernameForJwt() {
      Map<String, Object> claims =
          Map.of("custom:idp_username", "TESTUSER", "custom:idp_name", "idir");

      Jwt jwt = createJwt(claims);
      String username = JwtPrincipalUtil.getIdpUsername(jwt);

      assertEquals("TESTUSER", username);
    }

    @Test
    @DisplayName("Should return empty string when IDP username is missing")
    void shouldReturnEmptyStringWhenIdpUsernameIsMissing() {
      Map<String, Object> claims = Map.of("custom:idp_name", "idir");

      Jwt jwt = createJwt(claims);
      String username = JwtPrincipalUtil.getIdpUsername(jwt);

      assertEquals("", username);
    }
  }

  @Nested
  @DisplayName("getGroups Tests")
  class GetGroupsTests {

    @Test
    @DisplayName("Should return groups from JwtAuthenticationToken")
    void shouldReturnGroupsFromJwtAuthenticationToken() {
      Map<String, Object> claims =
          Map.of(
              "cognito:groups", List.of("CLIENT_ADMIN", "USER_ADMIN"), "custom:idp_name", "idir");

      JwtAuthenticationToken token = createJwtAuthenticationToken(claims);
      Set<String> groups = JwtPrincipalUtil.getGroups(token);

      assertEquals(2, groups.size());
      assertTrue(groups.contains("CLIENT_ADMIN"));
      assertTrue(groups.contains("USER_ADMIN"));
    }

    @Test
    @DisplayName("Should return groups from Jwt")
    void shouldReturnGroupsFromJwt() {
      Map<String, Object> claims =
          Map.of("cognito:groups", List.of("CLIENT_ADMIN"), "custom:idp_name", "idir");

      Jwt jwt = createJwt(claims);
      Set<String> groups = JwtPrincipalUtil.getGroups(jwt);

      assertEquals(1, groups.size());
      assertTrue(groups.contains("CLIENT_ADMIN"));
    }

    @Test
    @DisplayName("Should return empty set when groups are missing")
    void shouldReturnEmptySetWhenGroupsAreMissing() {
      Map<String, Object> claims = Map.of("custom:idp_name", "idir");

      Jwt jwt = createJwt(claims);
      Set<String> groups = JwtPrincipalUtil.getGroups(jwt);

      assertTrue(groups.isEmpty());
    }

    @Test
    @DisplayName("Should return empty set when groups list is empty")
    void shouldReturnEmptySetWhenGroupsListIsEmpty() {
      Map<String, Object> claims =
          Map.of("cognito:groups", Collections.emptyList(), "custom:idp_name", "idir");

      Jwt jwt = createJwt(claims);
      Set<String> groups = JwtPrincipalUtil.getGroups(jwt);

      assertTrue(groups.isEmpty());
    }

    @Test
    @DisplayName("Should return empty set when JwtAuthenticationToken is null")
    void shouldReturnEmptySetWhenJwtAuthenticationTokenIsNull() {
      Set<String> groups = JwtPrincipalUtil.getGroups((JwtAuthenticationToken) null);

      assertTrue(groups.isEmpty());
    }

    @Test
    @DisplayName("Should return empty set when Jwt is null")
    void shouldReturnEmptySetWhenJwtIsNull() {
      Set<String> groups = JwtPrincipalUtil.getGroups((Jwt) null);

      assertTrue(groups.isEmpty());
    }

    @Test
    @DisplayName("Should filter out null values from groups list")
    void shouldFilterOutNullValuesFromGroupsList() {
      List<String> groupsWithNull = new java.util.ArrayList<>();
      groupsWithNull.add("CLIENT_ADMIN");
      groupsWithNull.add(null);
      groupsWithNull.add("USER_ADMIN");

      Map<String, Object> claims = new java.util.HashMap<>();
      claims.put("cognito:groups", groupsWithNull);

      Jwt jwt = createJwt(claims);
      Set<String> groups = JwtPrincipalUtil.getGroups(jwt);

      assertEquals(2, groups.size());
      assertTrue(groups.contains("CLIENT_ADMIN"));
      assertTrue(groups.contains("USER_ADMIN"));
    }
  }

  @Nested
  @DisplayName("getIdentityProvider Tests")
  class GetIdentityProviderTests {

    @Test
    @DisplayName("Should return IDIR identity provider for JwtAuthenticationToken")
    void shouldReturnIdirIdentityProviderForJwtAuthenticationToken() {
      Map<String, Object> claims =
          Map.of("custom:idp_name", "idir", "custom:idp_username", "TESTUSER");

      JwtAuthenticationToken token = createJwtAuthenticationToken(claims);
      IdentityProvider identityProvider = JwtPrincipalUtil.getIdentityProvider(token);

      assertEquals(IdentityProvider.IDIR, identityProvider);
    }

    @Test
    @DisplayName("Should return IDIR identity provider for Jwt")
    void shouldReturnIdirIdentityProviderForJwt() {
      Map<String, Object> claims =
          Map.of("custom:idp_name", "idir", "custom:idp_username", "TESTUSER");

      Jwt jwt = createJwt(claims);
      IdentityProvider identityProvider = JwtPrincipalUtil.getIdentityProvider(jwt);

      assertEquals(IdentityProvider.IDIR, identityProvider);
    }

    @Test
    @DisplayName("Should return BUSINESS_BCEID identity provider for JwtAuthenticationToken")
    void shouldReturnBusinessBceidIdentityProviderForJwtAuthenticationToken() {
      Map<String, Object> claims =
          Map.of("custom:idp_name", "bceidbusiness", "custom:idp_username", "BIZUSER");

      JwtAuthenticationToken token = createJwtAuthenticationToken(claims);
      IdentityProvider identityProvider = JwtPrincipalUtil.getIdentityProvider(token);

      assertEquals(IdentityProvider.BUSINESS_BCEID, identityProvider);
    }

    @Test
    @DisplayName("Should return BUSINESS_BCEID identity provider for Jwt")
    void shouldReturnBusinessBceidIdentityProviderForJwt() {
      Map<String, Object> claims =
          Map.of("custom:idp_name", "bceidbusiness", "custom:idp_username", "BIZUSER");

      Jwt jwt = createJwt(claims);
      IdentityProvider identityProvider = JwtPrincipalUtil.getIdentityProvider(jwt);

      assertEquals(IdentityProvider.BUSINESS_BCEID, identityProvider);
    }

    @Test
    @DisplayName("Should throw NoSuchElementException when provider is invalid")
    void shouldThrowNoSuchElementExceptionWhenProviderIsInvalid() {
      Map<String, Object> claims =
          Map.of("custom:idp_name", "invalid_provider", "custom:idp_username", "USER");

      Jwt jwt = createJwt(claims);

      assertThrows(NoSuchElementException.class, () -> JwtPrincipalUtil.getIdentityProvider(jwt));
    }

    @Test
    @DisplayName("Should throw NoSuchElementException when provider is missing")
    void shouldThrowNoSuchElementExceptionWhenProviderIsMissing() {
      Map<String, Object> claims = Map.of("custom:idp_username", "USER");

      Jwt jwt = createJwt(claims);

      assertThrows(NoSuchElementException.class, () -> JwtPrincipalUtil.getIdentityProvider(jwt));
    }
  }
}
