package ca.bc.gov.restapi.results.common.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Parser and constants for BC Gov CSS (Common Single Sign-On) Keycloak role names and FAM metadata
 * sidecar entries.
 *
 * <p>Scoped roles follow the naming convention {@code <BASE_ROLE>_<SCOPE_TYPE>-<SCOPE_VALUE>},
 * potentially chained with multiple scopes (e.g., {@code
 * PLANNER_FOREST_CLIENT-00012797_REGION-KOOTENAY_BOUNDARY}).
 */
public final class CssRoleNaming {

  public static final String SCOPE_FOREST_CLIENT = "FOREST_CLIENT";
  public static final String SCOPE_DISTRICT = "DISTRICT";
  public static final String SCOPE_REGION = "REGION";
  public static final List<String> SCOPE_TYPES =
      List.of(SCOPE_FOREST_CLIENT, SCOPE_DISTRICT, SCOPE_REGION);

  public static final String FAM_SIDECAR_PREFIX = "FAM:";
  public static final String LABEL_PREFIX = "FAM:LABEL:";
  public static final String DESCRIPTION_PREFIX = "FAM:DESC:";
  public static final String EXPIRY_PREFIX = "FAM:EXPIRES:";

  private CssRoleNaming() {}

  /**
   * Represents a single extracted role scope consisting of a type (e.g., {@code FOREST_CLIENT}) and
   * its target value (e.g., {@code 00012797}).
   *
   * @param type the scope type
   * @param value the scope identifier value
   */
  public record Scope(String type, String value) {}

  /**
   * Result of parsing a CSS role name into its unscoped base role and ordered list of scopes.
   *
   * @param baseRoleName the base role name after stripping all recognized scope suffixes
   * @param scopes the recognized scopes ordered left-to-right
   */
  public record ScopedRoleName(String baseRoleName, List<Scope> scopes) {

    /**
     * Returns whether at least one scope suffix was parsed from the role name.
     *
     * @return {@code true} when one or more scopes are present
     */
    public boolean isScoped() {
      return !scopes.isEmpty();
    }

    /**
     * Returns the first scope's type, or {@code null} when unscoped.
     *
     * @return the primary scope type, or {@code null}
     */
    public String scopeType() {
      return scopes.isEmpty() ? null : scopes.get(0).type();
    }

    /**
     * Returns the first scope's value, or {@code null} when unscoped.
     *
     * @return the primary scope value, or {@code null}
     */
    public String scopeValue() {
      return scopes.isEmpty() ? null : scopes.get(0).value();
    }

    /**
     * Returns the value of the first scope matching the given scope type.
     *
     * @param type the scope type to look up (e.g., {@link CssRoleNaming#SCOPE_FOREST_CLIENT})
     * @return an {@link Optional} containing the matching scope value if present
     */
    public Optional<String> scopeValue(String type) {
      if (type == null) {
        return Optional.empty();
      }
      return scopes.stream()
          .filter(scope -> type.equals(scope.type()))
          .map(Scope::value)
          .findFirst();
    }
  }

  /**
   * Checks whether a role entry is a FAM metadata sidecar (e.g., {@code FAM:LABEL:...}, {@code
   * FAM:DESC:...}, {@code FAM:EXPIRES:...}) that must not be treated as a granted authority.
   *
   * @param roleName raw role string from token claims
   * @return {@code true} if the entry is a {@code FAM:} metadata sidecar
   */
  public static boolean isSidecarRole(String roleName) {
    return roleName != null && roleName.startsWith(FAM_SIDECAR_PREFIX);
  }

  /**
   * Parses a CSS role name by stripping recognized {@code _<SCOPE_TYPE>-<SCOPE_VALUE>} suffixes
   * from right to left.
   *
   * @param roleName raw role name to parse
   * @return the parsed {@link ScopedRoleName}
   */
  public static ScopedRoleName parse(String roleName) {
    if (roleName == null || roleName.isBlank()) {
      throw new IllegalArgumentException("Role name must not be null or blank");
    }

    List<Scope> reversedScopes = new ArrayList<>();
    String current = roleName;

    while (true) {
      Optional<StrippedScope> stripped = stripOneScope(current);
      if (stripped.isEmpty()) {
        break;
      }
      reversedScopes.add(stripped.get().scope());
      current = stripped.get().prefix();
    }

    Collections.reverse(reversedScopes);
    return new ScopedRoleName(current, List.copyOf(reversedScopes));
  }

  private static Optional<StrippedScope> stripOneScope(String name) {
    int bestMarkerIndex = -1;
    String bestScopeType = null;
    String bestMarker = null;

    for (String scopeType : SCOPE_TYPES) {
      String marker = "_" + scopeType + "-";
      int idx = name.lastIndexOf(marker);
      if (idx > 0 && idx > bestMarkerIndex) {
        String candidateValue = name.substring(idx + marker.length());
        if (!candidateValue.isEmpty()) {
          bestMarkerIndex = idx;
          bestScopeType = scopeType;
          bestMarker = marker;
        }
      }
    }

    if (bestMarkerIndex < 0) {
      return Optional.empty();
    }

    String prefix = name.substring(0, bestMarkerIndex);
    String value = name.substring(bestMarkerIndex + bestMarker.length());
    return Optional.of(new StrippedScope(prefix, new Scope(bestScopeType, value)));
  }

  private record StrippedScope(String prefix, Scope scope) {}
}
