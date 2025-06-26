package ca.bc.gov.restapi.results.extensions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
    Jwt jwt = Jwt
        .withTokenValue("token")
        .header("alg", "none")
        .claim("sub", annotation.value())
            .claim("cognito:groups", List.of(annotation.cognitoGroups()))
        .claim("custom:idp_name", annotation.idp())
        .claim("custom:idp_username", annotation.value())
        .claim("custom:idp_display_name", annotation.displayName())
        .claim("email", annotation.email())
        .build();

    List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(annotation.cognitoGroups());
    JwtAuthenticationToken token = new JwtAuthenticationToken(jwt, authorities);

    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(token);
    return context;
  }
}
