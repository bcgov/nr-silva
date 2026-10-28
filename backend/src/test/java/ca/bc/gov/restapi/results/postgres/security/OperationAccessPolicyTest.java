package ca.bc.gov.restapi.results.postgres.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.enums.Role;
import ca.bc.gov.restapi.results.common.security.LoggedUserHelper;
import java.util.Arrays;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@DisplayName("Unit Test | OperationAccessPolicy")
class OperationAccessPolicyTest {

  private static final String CLIENT = "00012797";
  private static final Long OPENING_ID = 1001L;

  private final OpeningClientResolver openingClientResolver = mock(OpeningClientResolver.class);
  private final OperationAccessPolicy policy =
      new OperationAccessPolicy(new LoggedUserHelper(), openingClientResolver);

  @AfterEach
  void clearSecurityContext() {
    SecurityContextHolder.clearContext();
  }

  private void loginWithGroups(String... groups) {
    TestingAuthenticationToken authentication =
        new TestingAuthenticationToken("user", "n/a", groups);
    authentication.setAuthenticated(true);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  @Test
  @DisplayName("Every protected operation has roles registered, all of them client roles")
  void everyOperation_hasClientRolesRegistered() {
    for (ProtectedOperation operation : ProtectedOperation.values()) {
      assertThat(policy.allowedRoles(operation))
          .as(operation.name())
          .isNotEmpty()
          .allMatch(Role::isAbstract);
    }
  }

  @ParameterizedTest
  @EnumSource(ProtectedOperation.class)
  @DisplayName("Allows every client role for the client, whatever the case of the group")
  void requireClientRole_clientRole_allowed(ProtectedOperation operation) {
    for (Role role : policy.allowedRoles(operation)) {
      String group = role.name().charAt(0) + role.name().substring(1).toLowerCase() + "_" + CLIENT;
      loginWithGroups("Viewer", group);

      assertThatCode(() -> policy.requireClientRole(operation, CLIENT))
          .as(group + " for " + operation)
          .doesNotThrowAnyException();
    }
  }

  @ParameterizedTest
  @EnumSource(ProtectedOperation.class)
  @DisplayName("Rejects a role of another client with 403 and the client number")
  void requireClientRole_otherClient_forbidden(ProtectedOperation operation) {
    loginWithGroups("Submitter_00000001", "Planner_00000002");

    assertThatThrownBy(() -> policy.requireClientRole(operation, CLIENT))
        .isInstanceOfSatisfying(
            ResponseStatusException.class,
            exception -> {
              assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
              assertThat(exception.getReason())
                  .isEqualTo("Not authorised for client number " + CLIENT);
            });
  }

  @ParameterizedTest
  @EnumSource(ProtectedOperation.class)
  @DisplayName("Rejects a user with only the viewer role")
  void requireClientRole_viewerOnly_forbidden(ProtectedOperation operation) {
    loginWithGroups("Viewer");

    assertThatThrownBy(() -> policy.requireClientRole(operation, CLIENT))
        .isInstanceOf(ResponseStatusException.class);
  }

  @ParameterizedTest
  @EnumSource(ProtectedOperation.class)
  @DisplayName("Rejects an unauthenticated user")
  void requireClientRole_noAuthentication_forbidden(ProtectedOperation operation) {
    assertThatThrownBy(() -> policy.requireClientRole(operation, CLIENT))
        .isInstanceOf(ResponseStatusException.class);
  }

  @ParameterizedTest
  @ValueSource(strings = {"12797", "0012797", "000127971", "2797"})
  @DisplayName("Does not match a client number that only shares an ending or start")
  void requireClientRole_partialClientNumber_forbidden(String client) {
    loginWithGroups("Submitter_" + CLIENT);

    assertThatThrownBy(() -> policy.requireClientRole(ProtectedOperation.CREATE_ACTIVITY, client))
        .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  @DisplayName("Roles of the two operations are the same today")
  void createOpeningAndCreateActivity_sameRoles() {
    assertThat(policy.allowedRoles(ProtectedOperation.CREATE_ACTIVITY))
        .containsExactlyInAnyOrderElementsOf(
            Arrays.asList(Role.SUBMITTER, Role.APPROVER, Role.PLANNER, Role.ADMIN));
    assertThat(policy.allowedRoles(ProtectedOperation.CREATE_OPENING))
        .isEqualTo(policy.allowedRoles(ProtectedOperation.CREATE_ACTIVITY));
  }

  @Test
  @DisplayName("Allows a role for any one client of the opening")
  void requireOpeningRole_roleForOneOfTheClients_allowed() {
    when(openingClientResolver.clientNumbersOf(OPENING_ID))
        .thenReturn(Set.of("00000001", CLIENT, "00000003"));
    loginWithGroups("Viewer", "Planner_" + CLIENT);

    assertThatCode(
            () -> policy.requireOpeningRole(ProtectedOperation.CREATE_ACTIVITY, OPENING_ID))
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("Rejects a user with roles only for other clients")
  void requireOpeningRole_roleForAnotherClient_forbidden() {
    when(openingClientResolver.clientNumbersOf(OPENING_ID)).thenReturn(Set.of("00000001"));
    loginWithGroups("Submitter_" + CLIENT);

    assertThatThrownBy(
            () -> policy.requireOpeningRole(ProtectedOperation.CREATE_ACTIVITY, OPENING_ID))
        .isInstanceOfSatisfying(
            ResponseStatusException.class,
            exception -> {
              assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
              assertThat(exception.getReason()).isEqualTo("Not authorised for this opening");
            });
  }

  @Test
  @DisplayName("Rejects everyone when the opening has no clients")
  void requireOpeningRole_openingWithoutClients_forbidden() {
    when(openingClientResolver.clientNumbersOf(OPENING_ID)).thenReturn(Set.of());
    loginWithGroups("Admin_" + CLIENT);

    assertThatThrownBy(
            () -> policy.requireOpeningRole(ProtectedOperation.CREATE_ACTIVITY, OPENING_ID))
        .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  @DisplayName("Rejects an unauthenticated user for an opening with clients")
  void requireOpeningRole_noAuthentication_forbidden() {
    when(openingClientResolver.clientNumbersOf(OPENING_ID)).thenReturn(Set.of(CLIENT));

    assertThatThrownBy(
            () -> policy.requireOpeningRole(ProtectedOperation.CREATE_ACTIVITY, OPENING_ID))
        .isInstanceOf(ResponseStatusException.class);
  }
}
