package ca.bc.gov.restapi.results.common.security;

/**
 * Enum representing the two types of roles in the system:
 * - CONCRETE: Role names without a client ID suffix (e.g., "VIEWER")
 * - ABSTRACT: Role names that must be combined with a client ID (e.g., "PLANNER_00001012")
 */
public enum RoleType {
    CONCRETE,
    ABSTRACT
}