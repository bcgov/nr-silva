package ca.bc.gov.restapi.results.common.util;

import ca.bc.gov.restapi.results.common.enums.IdentityProvider;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
 * attributes from a JwtAuthenticationToken object. The class is designed with a private constructor
 * to prevent instantiation.
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class JwtPrincipalUtil {

  private static final String FIRST_NAME = "firstName";
  private static final String LAST_NAME = "lastName";

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
    Object groups = tokenAttributes.get("cognito:groups");

    if (groups instanceof List) {
      return ((List<?>) groups)
          .stream()
              .filter(Objects::nonNull)
              .filter(String.class::isInstance)
              .map(String.class::cast)
              .collect(Collectors.toSet());
    }

    return Collections.emptySet();
  }

  /**
   * Retrieves the value of a specified claim from the claims map. If the claim is not present,
   * returns an empty string.
   *
   * @param claims The map containing the JWT claims.
   * @param claimName The name of the claim to retrieve.
   * @return The value of the specified claim as a String, or an empty string if the claim is not
   *     present.
   */
  private static String getClaimValue(Map<String, Object> claims, String claimName) {
    return claims.getOrDefault(claimName, StringUtils.EMPTY).toString();
  }

  /**
   * Retrieves the provider value from the JWT claims. The provider is identified by the key
   * "custom:idp_name" within the claims. If the provider's name starts with "ca.bc.gov.flnr.fam.",
   * it is replaced with "BCSC". Otherwise, the provider's name is returned in uppercase. If the
   * provider is not specified, an empty string is returned.
   *
   * @param claims The map containing the JWT claims.
   * @return The provider's name in uppercase or "BCSC" if it starts with "ca.bc.gov.flnr.fam.", or
   *     an empty string if the provider is not specified.
   */
  private static String getProviderValue(Map<String, Object> claims) {
    return getClaimValue(claims, "custom:idp_name").toUpperCase();
  }

  /**
   * Constructs the user ID by combining the provider's name with the user's username or user ID.
   * The method first attempts to retrieve the user's username from the JWT claims using the key
   * "custom:idp_username". If the username is not present or is blank, it then attempts to retrieve
   * the user's ID using the key "custom:idp_user_id". If either value is found, it is combined with
   * the provider's name, separated by a backslash. If neither value is found, an empty string is
   * returned. This method ensures that the user ID is uniquely identified by prefixing it with the
   * provider's name.
   *
   * @param claims The map containing the JWT claims.
   * @return The constructed user ID in the format "Provider\Username" or "Provider\UserID", or an
   *     empty string if neither the username nor the user ID is present in the claims.
   */
  private static String getUserIdWithProviderValue(Map<String, Object> claims) {
    return getProviderValue(claims) + "\\" + getIdpUsernameValue(claims);
  }

  private static String getIdpUsernameValue(Map<String, Object> claims) {
    return getClaimValue(claims, "custom:idp_username");
  }

  /**
   * Retrieves the email value from the JWT claims. This method looks for the "email" key within the
   * claims map. If the email is specified, its value is returned. Otherwise, an empty string is
   * returned, indicating that the email claim is not present.
   *
   * @param claims The map containing the JWT claims.
   * @return The email value as a String, or an empty string if the "email" claim is not present.
   */
  private static String getEmailValue(Map<String, Object> claims) {
    return getClaimValue(claims, "email");
  }

  /**
   * Retrieves the display name value from the JWT claims. This method searches for the
   * "custom:idp_display_name" key within the claims map. If the display name is specified, its
   * value is returned. Otherwise, an empty string is returned, indicating that the display name
   * claim is not present.
   *
   * @param claims The map containing the JWT claims.
   * @return The display name value as a String, or an empty string if the "custom:idp_display_name"
   *     claim is not present.
   */
  private static String getDisplayNameValue(Map<String, Object> claims) {
    return getClaimValue(claims, "custom:idp_display_name");
  }

  /**
   * Processes the given JWT claims to extract and assemble user name information. This method
   * extracts the first and last names from the JWT claims. For BCEIDBUSINESS providers or when both
   * first and last names are blank, it parses the display name to extract these names. The method
   * then assembles and returns a map containing the display name, first name, last name, and full
   * name (a concatenation of first and last names with trimming).
   *
   * @param claims The map containing the JWT claims from which the name information is to be
   *     extracted.
   * @return A map with keys "displayName", "firstName", "lastName", and "fullName", containing the
   *     extracted and/or computed name information. If specific name components are not found,
   *     their values in the map will be empty strings.
   */
  private static Map<String, String> processName(Map<String, Object> claims) {
    Map<String, String> additionalInfo = new HashMap<>();

    // Extract first and last names if they exist
    String firstName = getClaimValue(claims, "given_name");
    String lastName = getClaimValue(claims, "family_name");

    // Determine if special handling for names is required
    boolean useDisplayName =
        "bceidbusiness".equals(getProviderValue(claims))
            || (firstName.isEmpty() && lastName.isEmpty());
    if (useDisplayName) {
      Triple<String, String, String> names = extractNameClaim(claims);
      firstName = names.getMiddle();
      lastName = names.getRight();
    }

    // Put extracted or computed first and last names into the map
    additionalInfo.put("displayName", getDisplayNameValue(claims));
    additionalInfo.put(FIRST_NAME, firstName.trim());
    additionalInfo.put(LAST_NAME, lastName.trim());
    additionalInfo.put("fullName", String.join(" ", firstName, lastName).trim());

    return additionalInfo;
  }

  /**
   * Retrieves the last name value from the JWT claims. This method utilizes the {@code processName}
   * method to extract and assemble user name information from the JWT claims, specifically
   * targeting the last name. If the last name is not specified, an empty string is returned.
   *
   * @param claims The map containing the JWT claims.
   * @return The last name extracted from the JWT claims, or an empty string if not specified.
   */
  private static String getLastNameValue(Map<String, Object> claims) {
    return processName(claims).get("lastName");
  }

  /**
   * Retrieves the full name value from the JWT claims. This method leverages the {@code
   * processName} method to extract and assemble user name information from the JWT claims, focusing
   * on assembling the full name (a concatenation of first and last names). If both first and last
   * names are not specified, an empty string is returned.
   *
   * @param claims The map containing the JWT claims.
   * @return The full name (concatenation of first and last names) extracted from the JWT claims, or
   *     an empty string if not specified.
   */
  private static String getNameValue(Map<String, Object> claims) {
    return processName(claims).get("fullName");
  }

  private static String getFirstNameValue(Map<String, Object> claims) {
    return processName(claims).get(FIRST_NAME);
  }

  /**
   * Parses the display name claim to extract first and last names. Supports two formats:
   *
   * <ul>
   *   <li>Comma-separated format (typically IDIR): "LastName, FirstName WLRS:EX" or "da Silva,
   *       Maria WLRS:EX"
   *   <li>Space-separated format (typically BCEIDBUSINESS): "FirstName LastName"
   * </ul>
   *
   * For comma-separated format, the first part is the last name and the second part is the first
   * name. Additional information after a space in the first name part (e.g., "WLRS:EX") is removed.
   * For space-separated format with prefixes (e.g., "da", "de"), the second word is used as the
   * last name.
   *
   * @param claims The map containing the JWT claims with "custom:idp_display_name" key.
   * @return A Triple containing (displayName, firstName, lastName). If display name cannot be
   *     parsed, lastName and firstName will be empty strings.
   */
  private static Triple<String, String, String> extractNameClaim(Map<String, Object> claims) {
    // User name
    String displayName = getDisplayNameValue(claims);
    String firstName = "";
    String lastName = "";

    // Usually only IDIR contains comma.
    if (displayName.contains(",")) {
      String[] parts = displayName.split(",");
      firstName = parts[1].trim();

      // Remove WLRS:EX or any additional info
      if (firstName.contains(" ")) {
        firstName = firstName.split(" ")[0].trim();
      }

      lastName = parts[0].trim();
      // Remove 'de' or other starting characters before space
      if (lastName.contains(" ")) {
        lastName = lastName.split(" ")[1].trim();
      }
    } else if (displayName.contains(" ")) {
      // Usually BCeID contains space. E.g.: NRS Load Test-3
      int indexFirstSpace = displayName.indexOf(' ');
      firstName = displayName.substring(0, indexFirstSpace);
      lastName = displayName.substring(indexFirstSpace).trim();
    }

    return Triple.of(displayName, firstName, lastName);
  }
}
