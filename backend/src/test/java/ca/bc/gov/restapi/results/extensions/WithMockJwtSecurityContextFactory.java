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
        .claim("client_roles", listToString(annotation.roles()))
        .claim("custom:idp_name", annotation.idp())
        .claim("custom:idp_username", annotation.value())
        .claim("custom:idp_display_name", annotation.displayName())
        .claim("email", annotation.email())
        .build();

    List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(annotation.roles());
    JwtAuthenticationToken token = new JwtAuthenticationToken(jwt, authorities);

    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(token);
    return context;

  }

  private String listToString(String[] list) {
    return listToString(Arrays.asList(list));
  }

  private String listToString(List<String> list) {
    return
        list
            .stream()
            .map(content -> String.format("\"%s\"", content))
            .collect(Collectors.joining(",", "[", "]"));
  }
}
