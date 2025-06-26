package ca.bc.gov.restapi.results.common.security;

import ca.bc.gov.restapi.results.common.enums.IdentityProvider;
import ca.bc.gov.restapi.results.common.enums.Role;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

/**
 * This class contains helper methods to retrieved authenticated user.
 */
@Slf4j
@Component("auth")
@RequiredArgsConstructor
public class UserAuthenticationHelper {

  /**
   * Get the logged user information.
   *
   * @return An Optional of {@link UserInfo} with all information from JWT token, if logged in or
   *         empty Optional otherwise.
   */
  public Optional<UserInfo> getUserInfo() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication.isAuthenticated()
        && authentication.getPrincipal() instanceof Jwt jwtPrincipal) {
      Set<String> roles = getRoles(jwtPrincipal);

      // Provider IDIR or BCeID & username
      String provider = jwtPrincipal.getClaimAsString("custom:idp_name");
      boolean isIdirProvider = provider.equals("idir");
      provider = isIdirProvider ? "idir" : "bceidbusiness";
      String idpUsername = jwtPrincipal.getClaimAsString("custom:idp_username");

      Triple<String, String, String> names = getName(jwtPrincipal);

      // Email will be empty, until next FAM release
      String email = jwtPrincipal.getClaimAsString("email");

      UserInfo userInfo =
          new UserInfo(
              String.format("%s\\%s", provider.toUpperCase(), idpUsername.toUpperCase()),
              names.getMiddle(),
              names.getRight(),
              email,
              names.getLeft(),
              isIdirProvider ? idpUsername : null,
              isIdirProvider ? null : idpUsername,
              IdentityProvider.fromClaim(provider).orElseThrow(),
              roles,
              jwtPrincipal.getTokenValue());

      return Optional.of(userInfo);
    }

    log.info("User not authenticated!");
    return Optional.empty();
  }

  private static Set<String> getRoles(Jwt jwtPrincipal) {
    Object groupsObj = jwtPrincipal.getClaims().get("cognito:groups");

    if (groupsObj instanceof List<?> rawList) {
      return rawList.stream()
              .filter(Objects::nonNull)
              .map(Object::toString)
              .collect(Collectors.toSet());
    }

    return Collections.emptySet();
  }

  /**
   * Checks whether the currently authenticated user has the specified concrete role
   * (e.g., "VIEWER").
   *
   * @param role the concrete role to check
   * @return true if the user has the role; false otherwise
   */
  public boolean hasConcreteRole(Role role) {
    if (!role.isConcrete()) return false;
    return getUserInfo()
            .map(user -> user.roles().stream()
                    .anyMatch(r -> r.equalsIgnoreCase(role.name())))
            .orElse(false);
  }

  /**
   * Checks whether the currently authenticated user has the specified abstract role
   * for a given client ID (e.g., "PLANNER_00001012").
   *
   * @param role     the abstract role to check
   * @param clientId the client ID to append to the role
   * @return true if the user has the role; false otherwise
   */
  public boolean hasAbstractRole(Role role, String clientId) {
    if (!role.isAbstract()) return false;
    String expected = role.name() + "_" + clientId;
    return getUserInfo()
            .map(user -> user.roles().stream()
                    .anyMatch(r -> r.equalsIgnoreCase(expected)))
            .orElse(false);
  }

  private Triple<String, String, String> getName(Jwt jwtPrincipal) {
    // User name
    String displayName = jwtPrincipal.getClaimAsString("custom:idp_display_name");
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
