package ca.bc.gov.restapi.results.extensions;

import ca.bc.gov.restapi.results.common.security.CssRoleNaming;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

/** Creates a mock {@link SecurityContext} populated with a Keycloak-style {@link Jwt}. */
public class WithMockJwtSecurityContextFactory implements WithSecurityContextFactory<WithMockJwt> {

  private static final String BCEID_BUSINESS = "bceidbusiness";

  @Override
  public SecurityContext createSecurityContext(WithMockJwt annotation) {
    String usernameWithIdp = annotation.value();
    if (annotation.idp() != null && !annotation.idp().isBlank()) {
      usernameWithIdp = annotation.value() + "@" + annotation.idp();
    }

    List<String> effectiveRoles =
        Stream.concat(Arrays.stream(annotation.roles()), Arrays.stream(annotation.cognitoGroups()))
            .filter(role -> role != null && !role.isBlank())
            .distinct()
            .toList();

    Jwt.Builder jwtBuilder =
        Jwt.withTokenValue("token")
            .header("alg", "none")
            .claim("sub", annotation.value())
            .claim("preferred_username", usernameWithIdp)
            .claim("azp", annotation.azp())
            .claim("client_roles", effectiveRoles)
            .claim("identity_provider", annotation.idp())
            .claim("display_name", annotation.displayName())
            .claim("email", annotation.email());

    if (BCEID_BUSINESS.equalsIgnoreCase(annotation.idp())) {
      jwtBuilder.claim("bceid_username", annotation.value());
    } else {
      jwtBuilder.claim("idir_username", annotation.value());
    }

    Jwt jwt = jwtBuilder.build();

    List<String> authorityRoles =
        effectiveRoles.stream().filter(role -> !CssRoleNaming.isSidecarRole(role)).toList();
    List<GrantedAuthority> authorities =
        AuthorityUtils.createAuthorityList(authorityRoles.toArray(String[]::new));
    JwtAuthenticationToken token = new JwtAuthenticationToken(jwt, authorities);

    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(token);
    return context;
  }
}
