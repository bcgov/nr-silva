package ca.bc.gov.restapi.results.common.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.bc.gov.restapi.results.common.security.CssRoleNaming.Scope;
import ca.bc.gov.restapi.results.common.security.CssRoleNaming.ScopedRoleName;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Unit Test | CssRoleNaming")
class CssRoleNamingTest {

  @ParameterizedTest
  @ValueSource(strings = {"VIEWER", "SILVA_VIEWER", "DATA-STEWARD", "FOREST_CLIENT-0001"})
  @DisplayName("Parses unscoped role names cleanly including hyphenated base names")
  void shouldParseUnscopedRoleNames(String roleName) {
    ScopedRoleName parsed = CssRoleNaming.parse(roleName);

    assertEquals(roleName, parsed.baseRoleName());
    assertFalse(parsed.isScoped());
    assertTrue(parsed.scopes().isEmpty());
    assertNull(parsed.scopeType());
    assertNull(parsed.scopeValue());
    assertEquals(Optional.empty(), parsed.scopeValue(CssRoleNaming.SCOPE_FOREST_CLIENT));
  }

  @ParameterizedTest
  @CsvSource({
    "SUBMITTER_FOREST_CLIENT-00012797, SUBMITTER, FOREST_CLIENT, 00012797",
    "SILVA_APPROVER_FOREST_CLIENT-00001012, SILVA_APPROVER, FOREST_CLIENT, 00001012",
    "DATA-STEWARD_FOREST_CLIENT-00012797, DATA-STEWARD, FOREST_CLIENT, 00012797",
    "PLANNER_DISTRICT-DRM, PLANNER, DISTRICT, DRM",
    "ADMIN_REGION-RKO, ADMIN, REGION, RKO",
    "PLANNER_REGION-KOOTENAY-BOUNDARY, PLANNER, REGION, KOOTENAY-BOUNDARY"
  })
  @DisplayName("Parses single-scope CSS role names")
  void shouldParseSingleScopeRoleNames(
      String roleName, String expectedBase, String expectedType, String expectedValue) {
    ScopedRoleName parsed = CssRoleNaming.parse(roleName);

    assertEquals(expectedBase, parsed.baseRoleName());
    assertTrue(parsed.isScoped());
    assertEquals(1, parsed.scopes().size());
    assertEquals(expectedType, parsed.scopeType());
    assertEquals(expectedValue, parsed.scopeValue());
    assertEquals(Optional.of(expectedValue), parsed.scopeValue(expectedType));
  }

  @Test
  @DisplayName("Parses compound multi-scope CSS roles left-to-right")
  void shouldParseCompoundMultiScopeRole() {
    ScopedRoleName parsed =
        CssRoleNaming.parse("PLANNER_FOREST_CLIENT-00012797_REGION-KOOTENAY_BOUNDARY");

    assertEquals("PLANNER", parsed.baseRoleName());
    assertTrue(parsed.isScoped());
    assertEquals(
        List.of(
            new Scope(CssRoleNaming.SCOPE_FOREST_CLIENT, "00012797"),
            new Scope(CssRoleNaming.SCOPE_REGION, "KOOTENAY_BOUNDARY")),
        parsed.scopes());
    assertEquals(CssRoleNaming.SCOPE_FOREST_CLIENT, parsed.scopeType());
    assertEquals("00012797", parsed.scopeValue());
    assertEquals(Optional.of("00012797"), parsed.scopeValue(CssRoleNaming.SCOPE_FOREST_CLIENT));
    assertEquals(
        Optional.of("KOOTENAY_BOUNDARY"), parsed.scopeValue(CssRoleNaming.SCOPE_REGION));
  }

  @Test
  @DisplayName("Does not strip scope marker when scope value is empty")
  void shouldNotStripEmptyScopeValue() {
    ScopedRoleName parsed = CssRoleNaming.parse("VIEWER_FOREST_CLIENT-");

    assertEquals("VIEWER_FOREST_CLIENT-", parsed.baseRoleName());
    assertFalse(parsed.isScoped());
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "FAM:LABEL:Submitter",
        "FAM:DESC:Allows submissions",
        "FAM:EXPIRES:2026-12-31T00:00:00Z",
        "FAM:OTHER:meta"
      })
  @DisplayName("Identifies FAM metadata sidecar roles")
  void shouldIdentifySidecarRoles(String roleName) {
    assertTrue(CssRoleNaming.isSidecarRole(roleName));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"VIEWER", "SILVA_SUBMITTER_FOREST_CLIENT-00012797"})
  @DisplayName("Returns false for non-sidecar, null, and empty roles")
  void shouldRejectNonSidecarRoles(String roleName) {
    assertFalse(CssRoleNaming.isSidecarRole(roleName));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  @DisplayName("Throws IllegalArgumentException when parsing null or blank role name")
  void shouldThrowOnNullOrBlankParse(String roleName) {
    assertThrows(IllegalArgumentException.class, () -> CssRoleNaming.parse(roleName));
  }
}
