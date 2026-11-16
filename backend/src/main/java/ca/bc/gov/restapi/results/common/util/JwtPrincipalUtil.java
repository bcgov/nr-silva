package ca.bc.gov.restapi.results.common.util;

import ca.bc.gov.restapi.results.common.enums.IdentityProvider;
import ca.bc.gov.restapi.results.common.security.CssRoleNaming;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * This is a utility class for handling JWT principals. It provides methods to extract various
 * attributes from a JwtAuthenticationToken or Jwt object for BC Gov SSO Keycloak tokens, with
 * backwards-compatible fallbacks for legacy Cognito test claims.
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class JwtPrincipalUtil {

  private static final String FIRST_NAME = "firstName";
  private static final String LAST_NAME = "lastName";
  private static final String FULL_NAME = "fullName";
  private static final String DISPLAY_NAME_KEY = "displayName";

  private static final String CLAIM_IDENTITY_PROVIDER = "identity_provider";
  private static final String CLAIM_PREFERRED_USERNAME = "preferred_username";
  private static final String CLAIM_IDIR_USERNAME = "idir_username";
  private static final String CLAIM_BCEID_USERNAME = "bceid_username";
  private static final String CLAIM_IDIR_GUID = "idir_user_guid";
  private static final String CLAIM_BCEID_GUID = "bceid_user_guid";
  private static final String CLAIM_DISPLAY_NAME = "display_name";
  private static final String CLAIM_GIVEN_NAME = "given_name";
  private static final String CLAIM_FAMILY_NAME = "family_name";
  private static final String CLAIM_EMAIL = "email";
  private static final String CLAIM_AZP = "azp";
  private static final String CLAIM_CLIENT_ROLES = "client_roles";
  private static final String CLAIM_RESOURCE_ACCESS = "resource_access";
  private static final String CLAIM_ROLES = "roles";

  private static final String LEGACY_IDP_NAME = "custom:idp_name";
  private static final String LEGACY_IDP_USERNAME = "custom:idp_username";
  private static final String LEGACY_IDP_DISPLAY_NAME = "custom:idp_display_name";
  private static final String LEGACY_COGNITO_GROUPS = "cognito:groups";

  private static final String PROVIDER_AZURE_IDIR = "AZUREIDIR";
  private static final String PROVIDER_IDIR = "IDIR";
  private static final String PROVIDER_BCEID_BUSINESS = "BCEIDBUSINESS";

  /**
   * Retrieves the provider of the JWT token from the given JwtAuthenticationToken principal. The
   * provider is extracted from the token attributes under the key "custom:idp_name" and converted
   * to uppercase. If the provider is not specified, an empty string is returned.
   *
   * @param principal JwtAuthenticationToken object from which the provider is to be extracted.
   * @return The provider of the JWT token in uppercase, or an empty string if the provider is not
   *     present.
   */
  public static String getProvider(JwtAuthenticationToken principal) {
    return getProviderValue(principal.getTokenAttributes());
  }

  /**
   * Retrieves the identity provider from the given Jwt token. It extracts the value under the
   * "custom:idp_name" claim.
   *
   * @param principal the Jwt token containing the claims
   * @return the identity provider as a string, or an empty string if the claim is missing or not a
   *     string
   */
  public static String getProvider(Jwt principal) {
    return getProviderValue(principal.getClaims());
  }

  /**
   * Retrieves the user ID from the given JwtAuthenticationToken principal. The user ID is extracted
   * from the token attributes under the key "custom:idp_username". The user ID is prefixed with the
   * provider in uppercase and a backslash. If the username is not present or blank, the result will
   * have an empty string after the backslash.
   *
   * @param principal JwtAuthenticationToken object from which the user ID is to be extracted.
   * @return The user ID prefixed with the provider in uppercase and a backslash (e.g.,
   *     "IDIR\\TESTUSER"), or "PROVIDER\\" if the username is blank.
   */
  public static String getUserId(JwtAuthenticationToken principal) {
    return getUserIdWithProviderValue(principal.getTokenAttributes());
  }

  /**
   * Retrieves the user ID from the given Jwt principal. The user ID is extracted from the token
   * claims under the key "custom:idp_username". The user ID is prefixed with the provider in
   * uppercase and a backslash. If the username is not present or blank, the result will have an
   * empty string after the backslash.
   *
   * @param principal Jwt object from which the user ID is to be extracted.
   * @return The user ID prefixed with the provider in uppercase and a backslash (e.g.,
   *     "IDIR\\TESTUSER"), or "PROVIDER\\" if the username is blank.
   */
  public static String getUserId(Jwt principal) {
    return getUserIdWithProviderValue(principal.getClaims());
  }

  /**
   * Retrieves the email from the given JwtAuthenticationToken principal. The email is extracted
   * from the token attributes under the key "email". If the email is not present, an empty string
   * is returned.
   *
   * @param principal JwtAuthenticationToken object from which the email is to be extracted.
   * @return The email value, or an empty string if the email is not present.
   */
  public static String getEmail(JwtAuthenticationToken principal) {
    return getEmailValue(principal.getTokenAttributes());
  }

  /**
   * Retrieves the email from the given Jwt principal. The email is extracted from the token claims
   * under the key "email". If the email is not present, an empty string is returned.
   *
   * @param principal Jwt object from which the email is to be extracted.
   * @return The email value, or an empty string if the email is not present.
   */
  public static String getEmail(Jwt principal) {
    return getEmailValue(principal.getClaims());
  }

  /**
   * Retrieves the display name from the given JwtAuthenticationToken principal. The display name is
   * extracted from the token attributes under the key "custom:idp_display_name". If the display
   * name is blank, the first and last names are extracted and concatenated. If both the first and
   * last names are blank, an empty string is returned.
   *
   * @param principal JwtAuthenticationToken object from which the display name is to be extracted.
   * @return The display name, or the concatenated first and last names, or an empty string if both
   *     the display name and the first and last names are blank.
   */
  public static String getName(JwtAuthenticationToken principal) {
    return getNameValue(principal.getTokenAttributes());
  }

  /**
   * Retrieves the display name from the given Jwt principal. The display name is extracted from the
   * token attributes under the key "custom:idp_display_name". If the display name is blank, the
   * first and last names are extracted and concatenated. If both the first and last names are
   * blank, an empty string is returned.
   *
   * @param principal Jwt object from which the display name is to be extracted.
   * @return The display name, or the concatenated first and last names, or an empty string if both
   *     the display name and the first and last names are blank.
   */
  public static String getName(Jwt principal) {
    return getNameValue(principal.getClaims());
  }

  /**
   * Retrieves the first name from the given JwtAuthenticationToken principal. For IDIR providers,
   * the first name is extracted from the "given_name" claim. For BCEIDBUSINESS providers or when
   * given_name is blank, the display name is parsed to extract the first name. If no first name can
   * be determined, an empty string is returned.
   *
   * @param principal JwtAuthenticationToken object from which the first name is to be extracted.
   * @return The first name, or an empty string if not present or cannot be determined.
   */
  public static String getFirstName(JwtAuthenticationToken principal) {
    return getFirstNameValue(principal.getTokenAttributes());
  }

  /**
   * Retrieves the first name from the given Jwt principal. For IDIR providers, the first name is
   * extracted from the "given_name" claim. For BCEIDBUSINESS providers or when given_name is blank,
   * the display name is parsed to extract the first name. If no first name can be determined, an
   * empty string is returned.
   *
   * @param principal Jwt object from which the first name is to be extracted.
   * @return The first name, or an empty string if not present or cannot be determined.
   */
  public static String getFirstName(Jwt principal) {
    return getFirstNameValue(principal.getClaims());
  }

  /**
   * Retrieves the last name from the given JwtAuthenticationToken principal. For IDIR providers,
   * the last name is extracted from the "family_name" claim. For BCEIDBUSINESS providers or when
   * family_name is blank, the display name is parsed to extract the last name. If no last name can
   * be determined, an empty string is returned.
   *
   * @param principal JwtAuthenticationToken object from which the last name is to be extracted.
   * @return The last name, or an empty string if not present or cannot be determined.
   */
  public static String getLastName(JwtAuthenticationToken principal) {
    return getLastNameValue(principal.getTokenAttributes());
  }

  /**
   * Retrieves the last name from the given Jwt principal. For IDIR providers, the last name is
   * extracted from the "family_name" claim. For BCEIDBUSINESS providers or when family_name is
   * blank, the display name is parsed to extract the last name. If no last name can be determined,
   * an empty string is returned.
   *
   * @param principal Jwt object from which the last name is to be extracted.
   * @return The last name, or an empty string if not present or cannot be determined.
   */
  public static String getLastName(Jwt principal) {
    return getLastNameValue(principal.getClaims());
  }

  /**
   * Retrieves the display name from the given JwtAuthenticationToken principal. The display name is
   * extracted from the token attributes under the key "custom:idp_display_name". If the display
   * name is blank, an empty string is returned.
   *
   * @param principal JwtAuthenticationToken object from which the display name is to be extracted.
   * @return The display name or an empty string if the display name is blank.
   */
  public static String getDisplayName(JwtAuthenticationToken principal) {
    return getDisplayNameValue(principal.getTokenAttributes());
  }

  /**
   * Retrieves the display name from the given Jwt principal. The display name is extracted from the
   * token attributes under the key "custom:idp_display_name". If the display name is blank, an
   * empty string is returned.
   *
   * @param principal Jwt object from which the display name is to be extracted.
   * @return The display name or an empty string if the display name is blank.
   */
  public static String getDisplayName(Jwt principal) {
    return getDisplayNameValue(principal.getClaims());
  }

  /**
   * Retrieves the IDP username from the given JwtAuthenticationToken principal. The IDP username is
   * extracted from the token attributes under the key "custom:idp_username". If the IDP username is
   * not present or blank, an empty string is returned.
   *
   * @param principal JwtAuthenticationToken object from which the IDP username is to be extracted.
   * @return The IDP username, or an empty string if not present or blank.
   */
  public static String getIdpUsername(JwtAuthenticationToken principal) {
    return getIdpUsernameValue(principal.getTokenAttributes());
  }

  /**
   * Retrieves the IDP username from the given Jwt principal. The IDP username is extracted from the
   * token claims under the key "custom:idp_username". If the IDP username is not present or blank,
   * an empty string is returned.
   *
   * @param principal Jwt object from which the IDP username is to be extracted.
   * @return The IDP username, or an empty string if not present or blank.
   */
  public static String getIdpUsername(Jwt principal) {
    return getIdpUsernameValue(principal.getClaims());
  }

  /**
   * Retrieves a set of groups from the given JwtAuthenticationToken. This method extracts the token
   * attributes, then looks for the key "cognito:groups" in the token attributes. If the value
   * associated with this key is a {@link List}, the method filters the elements to only include
   * non-null values of type {@link String}. The resulting set of strings is returned.
   *
   * @param jwtPrincipal The {@link JwtAuthenticationToken} containing the token attributes. If
   *     jwtPrincipal or its attributes are null, an empty set is returned. If the "cognito:groups"
   *     key does not exist or the value is not a list of strings, an empty set is returned.
   * @return A set of group names, or an empty set if the key is missing, the value is not a list of
   *     strings, or the principal is null.
   */
  public static Set<String> getGroups(JwtAuthenticationToken jwtPrincipal) {
    if (jwtPrincipal == null || jwtPrincipal.getTokenAttributes() == null) {
      return Collections.emptySet();
    }
    return getClaimGroups(jwtPrincipal.getTokenAttributes());
  }

  /**
   * Retrieves a set of groups from the given Jwt. This method extracts the token claims, then looks
   * for the key "cognito:groups" in the claims. If the value associated with this key is a {@link
   * List}, the method filters the elements to only include non-null values of type {@link String}.
   * The resulting set of strings is returned.
   *
   * @param jwtPrincipal The {@link Jwt} containing the token claims. If jwtPrincipal or its claims
   *     are null, an empty set is returned. If the "cognito:groups" key does not exist or the value
   *     is not a list of strings, an empty set is returned.
   * @return A set of group names, or an empty set if the key is missing, the value is not a list of
   *     strings, or the principal is null.
   */
  public static Set<String> getGroups(Jwt jwtPrincipal) {
    if (jwtPrincipal == null || jwtPrincipal.getClaims() == null) {
      return Collections.emptySet();
    }
    return getClaimGroups(jwtPrincipal.getClaims());
  }

  public static IdentityProvider getIdentityProvider(JwtAuthenticationToken jwtPrincipal) {
    return IdentityProvider.fromClaim(getProvider(jwtPrincipal)).orElseThrow();
  }

  public static IdentityProvider getIdentityProvider(Jwt jwtPrincipal) {
    return IdentityProvider.fromClaim(getProvider(jwtPrincipal)).orElseThrow();
  }

  private static Set<String> getClaimGroups(Map<String, Object> tokenAttributes) {
    Set<String> clientRoles = extractFilteredStringSet(tokenAttributes.get(CLAIM_CLIENT_ROLES));
    if (!clientRoles.isEmpty()) {
      return clientRoles;
    }

    Set<String> resourceRoles = extractResourceAccessRoles(tokenAttributes);
    if (!resourceRoles.isEmpty()) {
      return resourceRoles;
    }

    return extractFilteredStringSet(tokenAttributes.get(LEGACY_COGNITO_GROUPS));
  }

  private static Set<String> extractResourceAccessRoles(Map<String, Object> tokenAttributes) {
    String azp = getClaimValue(tokenAttributes, CLAIM_AZP);
    if (azp.isBlank()) {
      return Collections.emptySet();
    }
    Object resourceAccessObj = tokenAttributes.get(CLAIM_RESOURCE_ACCESS);
    if (resourceAccessObj instanceof Map<?, ?> resourceAccess
        && resourceAccess.get(azp) instanceof Map<?, ?> clientAccess) {
      return extractFilteredStringSet(clientAccess.get(CLAIM_ROLES));
    }
    return Collections.emptySet();
  }

  private static Set<String> extractFilteredStringSet(Object rawClaim) {
    if (rawClaim instanceof Collection<?> collection) {
      return collection.stream()
          .filter(Objects::nonNull)
          .filter(String.class::isInstance)
          .map(String.class::cast)
          .filter(role -> !role.isBlank() && !CssRoleNaming.isSidecarRole(role))
          .collect(Collectors.toSet());
    }
    return Collections.emptySet();
  }

  private static String getClaimValue(Map<String, Object> claims, String claimName) {
    Object value = claims.get(claimName);
    return value == null ? StringUtils.EMPTY : value.toString();
  }

  private static String getProviderValue(Map<String, Object> claims) {
    String provider = getClaimValue(claims, CLAIM_IDENTITY_PROVIDER);
    if (provider.isBlank()) {
      provider = extractProviderFromPreferredUsername(claims);
    }
    if (provider.isBlank()) {
      provider = getClaimValue(claims, LEGACY_IDP_NAME);
    }
    if (provider.isBlank()) {
      return StringUtils.EMPTY;
    }

    String normalized = provider.trim().toUpperCase(Locale.ROOT);
    if (PROVIDER_AZURE_IDIR.equals(normalized) || PROVIDER_IDIR.equals(normalized)) {
      return PROVIDER_IDIR;
    }
    if (PROVIDER_BCEID_BUSINESS.equals(normalized)) {
      return PROVIDER_BCEID_BUSINESS;
    }
    return normalized;
  }

  private static String extractProviderFromPreferredUsername(Map<String, Object> claims) {
    String preferredUsername = getClaimValue(claims, CLAIM_PREFERRED_USERNAME);
    int atIndex = preferredUsername.lastIndexOf('@');
    if (atIndex >= 0 && atIndex < preferredUsername.length() - 1) {
      return preferredUsername.substring(atIndex + 1);
    }
    return StringUtils.EMPTY;
  }

  private static String getUserIdWithProviderValue(Map<String, Object> claims) {
    return getProviderValue(claims) + "\\" + getIdpUsernameValue(claims);
  }

  private static String getIdpUsernameValue(Map<String, Object> claims) {
    String idirUsername = getClaimValue(claims, CLAIM_IDIR_USERNAME);
    if (!idirUsername.isBlank()) {
      return idirUsername;
    }
    String bceidUsername = getClaimValue(claims, CLAIM_BCEID_USERNAME);
    if (!bceidUsername.isBlank()) {
      return bceidUsername;
    }
    String legacyUsername = getClaimValue(claims, LEGACY_IDP_USERNAME);
    if (!legacyUsername.isBlank()) {
      return legacyUsername;
    }
    String idirGuid = getClaimValue(claims, CLAIM_IDIR_GUID);
    if (!idirGuid.isBlank()) {
      return idirGuid.toUpperCase(Locale.ROOT);
    }
    String bceidGuid = getClaimValue(claims, CLAIM_BCEID_GUID);
    if (!bceidGuid.isBlank()) {
      return bceidGuid.toUpperCase(Locale.ROOT);
    }
    String preferredUsername = getClaimValue(claims, CLAIM_PREFERRED_USERNAME);
    int atIndex = preferredUsername.indexOf('@');
    if (atIndex > 0) {
      return preferredUsername.substring(0, atIndex).toUpperCase(Locale.ROOT);
    }
    return StringUtils.EMPTY;
  }

  private static String getEmailValue(Map<String, Object> claims) {
    return getClaimValue(claims, CLAIM_EMAIL);
  }

  private static String getDisplayNameValue(Map<String, Object> claims) {
    String displayName = getClaimValue(claims, CLAIM_DISPLAY_NAME);
    if (!displayName.isBlank()) {
      return displayName;
    }
    return getClaimValue(claims, LEGACY_IDP_DISPLAY_NAME);
  }

  private static Map<String, String> processName(Map<String, Object> claims) {
    Map<String, String> additionalInfo = new HashMap<>();

    String firstName = getClaimValue(claims, CLAIM_GIVEN_NAME);
    String lastName = getClaimValue(claims, CLAIM_FAMILY_NAME);

    boolean isBceidWithDisplayName =
        PROVIDER_BCEID_BUSINESS.equalsIgnoreCase(getProviderValue(claims))
            && !getDisplayNameValue(claims).isBlank();
    boolean useDisplayName = isBceidWithDisplayName || (firstName.isEmpty() && lastName.isEmpty());
    if (useDisplayName) {
      Triple<String, String, String> names = extractNameClaim(claims);
      firstName = names.getMiddle();
      lastName = names.getRight();
    }

    additionalInfo.put(DISPLAY_NAME_KEY, getDisplayNameValue(claims));
    additionalInfo.put(FIRST_NAME, firstName.trim());
    additionalInfo.put(LAST_NAME, lastName.trim());
    additionalInfo.put(FULL_NAME, String.join(" ", firstName, lastName).trim());

    return additionalInfo;
  }

  private static String getLastNameValue(Map<String, Object> claims) {
    return processName(claims).get(LAST_NAME);
  }

  private static String getNameValue(Map<String, Object> claims) {
    return processName(claims).get(FULL_NAME);
  }

  private static String getFirstNameValue(Map<String, Object> claims) {
    return processName(claims).get(FIRST_NAME);
  }

  private static Triple<String, String, String> extractNameClaim(Map<String, Object> claims) {
    String displayName = getDisplayNameValue(claims);
    String firstName = StringUtils.EMPTY;
    String lastName = StringUtils.EMPTY;

    if (displayName.contains(",")) {
      String[] parts = displayName.split(",", 2);
      if (parts.length > 1) {
        firstName = parts[1].trim();
        if (firstName.contains(" ")) {
          firstName = firstName.split(" ")[0].trim();
        }
      }

      lastName = parts[0].trim();
      if (lastName.contains(" ")) {
        String[] lastParts = lastName.split(" ", 2);
        lastName = lastParts.length > 1 ? lastParts[1].trim() : lastName;
      }
    } else if (displayName.contains(" ")) {
      int indexFirstSpace = displayName.indexOf(' ');
      firstName = displayName.substring(0, indexFirstSpace);
      lastName = displayName.substring(indexFirstSpace).trim();
    }

    return Triple.of(displayName, firstName, lastName);
  }
}
