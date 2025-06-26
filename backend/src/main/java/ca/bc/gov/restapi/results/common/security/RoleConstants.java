package ca.bc.gov.restapi.results.common.security;

import ca.bc.gov.restapi.results.common.enums.Role;
import org.springframework.stereotype.Component;


/**
 * Exposes role constants as Spring beans for use in SpEL expressions within @PreAuthorize.
 * For example: {@code @PreAuthorize("@auth.hasAbstractRole(@roles.PLANNER, '00001012')")}
 * or {@code @PreAuthorize("@auth.hasConcreteRole(@roles.VIEWER)")}
 */
@Component("roles")
public class RoleConstants {
    public final Role VIEWER = Role.VIEWER;
    public final Role SUBMITTER = Role.SUBMITTER;
    public final Role APPROVER = Role.APPROVER;
    public final Role PLANNER = Role.PLANNER;
    public final Role ADMIN = Role.ADMIN;
}
