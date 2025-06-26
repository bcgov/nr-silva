package ca.bc.gov.restapi.results.common.security;

import lombok.Getter;

/**
 * Enum representing all roles in the system, each associated with a {@link RoleType}.
 * Used for authorization checks in conjunction with Spring Security's @PreAuthorize.
 */
@Getter
public enum Role {
    VIEWER(RoleType.CONCRETE),
    SUBMITTER(RoleType.ABSTRACT),
    APPROVER(RoleType.ABSTRACT),
    PLANNER(RoleType.ABSTRACT),
    ADMIN(RoleType.ABSTRACT);

    private final RoleType type;

    Role(RoleType type) {
        this.type = type;
    }

    /**
     * Checks if the role is of type CONCRETE.
     *
     * @return true if the role is concrete; false otherwise
     */
    public boolean isConcrete() {
        return type == RoleType.CONCRETE;
    }

    /**
     * Checks if the role is of type ABSTRACT.
     *
     * @return true if the role is abstract; false otherwise
     */
    public boolean isAbstract() {
        return type == RoleType.ABSTRACT;
    }
}