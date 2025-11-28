package ca.bc.gov.restapi.results.extensions;

import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockJwtSecurityContextFactory implements WithSecurityContextFactory<WithMockJwt> {

  @Override
  public SecurityContext createSecurityContext(WithMockJwt annotation) {
    // Construct a username claim that includes the idp suffix (e.g. "JAKETHEDOG@idir")
    String usernameWithIdp = annotation.value();
    if (annotation.idp() != null && !annotation.idp().isBlank()) {
      usernameWithIdp = annotation.value() + "@" + annotation.idp();
    }

    Jwt jwt =
        Jwt.withTokenValue("token")
            .header("alg", "none")
            .claim("sub", annotation.value())
            .claim("username", usernameWithIdp)
            .claim("cognito:groups", List.of(annotation.cognitoGroups()))
            .claim("custom:idp_name", annotation.idp())
            .claim("custom:idp_username", annotation.value())
            .claim("custom:idp_display_name", annotation.displayName())
            .claim("email", annotation.email())
            .build();

    List<GrantedAuthority> authorities =
        AuthorityUtils.createAuthorityList(annotation.cognitoGroups());
    JwtAuthenticationToken token = new JwtAuthenticationToken(jwt, authorities);

    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(token);
    return context;
  }
}
