package ca.bc.gov.restapi.results.common.security;

import ca.bc.gov.restapi.results.common.enums.IdentityProvider;
import ca.bc.gov.restapi.results.common.enums.Role;
import ca.bc.gov.restapi.results.common.exception.UserNotFoundException;
import ca.bc.gov.restapi.results.common.util.JwtPrincipalUtil;
import java.util.function.Predicate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

/**
 * This class contains all user related methods and artifacts.
 */
@Component("auth")
public class LoggedUserHelper {

  /**
   * Get the email address from the logged user.
   *
   * @return a String containing the email address if logged in
   * @throws UserNotFoundException when not logged in
   */
  public String getLoggedUserEmail() {
    return JwtPrincipalUtil.getEmail(getPrincipal());
  }

  /**
   * Get the ID from the logged user.
   *
   * @return a String containing the id if logged in. Or an empty string otherwise
   * @throws UserNotFoundException when not logged in
   */
  public String getLoggedUserId() {
    return JwtPrincipalUtil.getUserId(getPrincipal());
  }

  /**
   * Checks whether the currently authenticated user is from the IDIR identity provider.
   *
   * <p>This method retrieves the identity provider from the current JWT principal
   * and returns {@code true} if the provider matches {@link IdentityProvider#IDIR}.
   *
   * @return {@code true} if the user is authenticated via IDIR, {@code false} otherwise
   */
  public boolean isIdirUser() {
    IdentityProvider provider = JwtPrincipalUtil.getIdentityProvider(getPrincipal());
    return provider == IdentityProvider.IDIR;
  }

  /**
   * Checks whether the currently authenticated user has the specified concrete role (e.g.,
   * "VIEWER").
   *
   * @param role the concrete role to check
   * @return true if the user has the role; false otherwise
   */
  public boolean hasConcreteRole(Role role) {
    if (!role.isConcrete()) {
      return false;
    }
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      return false;
    }

    return authentication
        .getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .map(String::toUpperCase)
        .anyMatch(auth -> auth.equals(role.name()));
  }

  /**
   * Checks whether the currently authenticated user has the specified abstract role for a given
   * client ID (e.g., "PLANNER_00001012").
   *
   * @param role     the abstract role to check
   * @param clientId the client ID to append to the role
   * @return true if the user has the role; false otherwise
   */
  public boolean hasAbstractRole(Role role, String clientId) {
    if (role.isConcrete()) {
      return false;
    }
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      return false;
    }

    return authentication
        .getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .map(String::toUpperCase)
        .anyMatch(auth -> auth.equals(role.name() + "_" + clientId));
  }

  /**
   * Checks whether the currently authenticated user has a role that matches the provided
   * predicate.
   *
   * @param matcher a predicate to match roles against
   * @return true if the user has a matching role; false otherwise
   */
  public boolean hasRoleMatching(Predicate<String> matcher) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      return false;
    }

    return authentication
        .getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .anyMatch(matcher);
  }

  private Jwt getPrincipal() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication.isAuthenticated()
        && authentication.getPrincipal() instanceof Jwt jwtPrincipal) {
      return jwtPrincipal;
    }
    throw new UserNotFoundException();
  }

}
