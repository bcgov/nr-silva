package ca.bc.gov.restapi.results.postgres.security;

import ca.bc.gov.restapi.results.common.enums.Role;
import ca.bc.gov.restapi.results.common.security.LoggedUserHelper;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/**
 * The single place that says which roles can perform each {@link ProtectedOperation}. Roles are
 * granted per client number (for example {@code SUBMITTER_00012797}). To protect a new operation,
 * add it to the enum and register its roles in {@link #ALLOWED_ROLES}.
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class OperationAccessPolicy {

  private static final Set<Role> ANY_CLIENT_ROLE =
      EnumSet.of(Role.SUBMITTER, Role.APPROVER, Role.PLANNER, Role.ADMIN);

  /** Roles allowed per operation. Every {@link ProtectedOperation} must be listed. */
  static final Map<ProtectedOperation, Set<Role>> ALLOWED_ROLES =
      Map.of(
          ProtectedOperation.CREATE_OPENING, ANY_CLIENT_ROLE,
          ProtectedOperation.CREATE_ACTIVITY, ANY_CLIENT_ROLE);

  private final LoggedUserHelper loggedUserHelper;

  /**
   * Returns the roles allowed to perform the operation.
   *
   * @param operation the operation
   * @return the allowed roles
   */
  public Set<Role> allowedRoles(ProtectedOperation operation) {
    return ALLOWED_ROLES.get(operation);
  }

  /**
   * Requires that the user holds an allowed role for the client number.
   *
   * @param operation the operation being performed
   * @param clientNumber the client number the user acts for
   * @throws ResponseStatusException with status 403 when the user has no allowed role
   */
  public void requireClientRole(ProtectedOperation operation, String clientNumber) {
    boolean allowed =
        allowedRoles(operation).stream()
            .anyMatch(role -> loggedUserHelper.hasAbstractRole(role, clientNumber));
    if (!allowed) {
      throw new ResponseStatusException(
          HttpStatus.FORBIDDEN, "Not authorised for client number " + clientNumber);
    }
  }
}
