package ca.bc.gov.restapi.results.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.bc.gov.restapi.results.common.enums.Role;
import ca.bc.gov.restapi.results.common.enums.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test | Role Enum")
class RoleTest {

    @Test
    @DisplayName("Role types should be correctly assigned")
    void testRoleTypes() {
        assertEquals(RoleType.CONCRETE, Role.VIEWER.getType(), "VIEWER should be CONCRETE");
        assertEquals(RoleType.ABSTRACT, Role.SUBMITTER.getType(), "SUBMITTER should be ABSTRACT");
        assertEquals(RoleType.ABSTRACT, Role.APPROVER.getType(), "APPROVER should be ABSTRACT");
        assertEquals(RoleType.ABSTRACT, Role.PLANNER.getType(), "PLANNER should be ABSTRACT");
        assertEquals(RoleType.ABSTRACT, Role.ADMIN.getType(), "ADMIN should be ABSTRACT");
    }

    @Test
    @DisplayName("Concrete role should return true for isConcrete()")
    void testIsConcrete() {
        assertTrue(Role.VIEWER.isConcrete(), "VIEWER should be concrete");
        assertFalse(Role.SUBMITTER.isConcrete(), "SUBMITTER should not be concrete");
        assertFalse(Role.APPROVER.isConcrete(), "APPROVER should not be concrete");
        assertFalse(Role.PLANNER.isConcrete(), "PLANNER should not be concrete");
        assertFalse(Role.ADMIN.isConcrete(), "ADMIN should not be concrete");
    }

    @Test
    @DisplayName("Abstract roles should return true for isAbstract()")
    void testIsAbstract() {
        assertFalse(Role.VIEWER.isAbstract(), "VIEWER should not be abstract");
        assertTrue(Role.SUBMITTER.isAbstract(), "SUBMITTER should be abstract");
        assertTrue(Role.APPROVER.isAbstract(), "APPROVER should be abstract");
        assertTrue(Role.PLANNER.isAbstract(), "PLANNER should be abstract");
        assertTrue(Role.ADMIN.isAbstract(), "ADMIN should be abstract");
    }
}