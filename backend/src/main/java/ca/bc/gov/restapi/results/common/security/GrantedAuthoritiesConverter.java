package ca.bc.gov.restapi.results.common.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class GrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

  @Override
  public Collection<GrantedAuthority> convert(Jwt jwt) {
    final List<String> realmAccess = new ArrayList<>();
    Object clientRolesObj =
        jwt
        .getClaims()
        .getOrDefault("client_roles",List.of());

    if (clientRolesObj instanceof List<?> list) {
      list.forEach(item -> realmAccess.add(String.valueOf(item)));
    }
    return realmAccess
        .stream()
        .map(roleName -> "ROLE_" + roleName)
        .map(roleName -> (GrantedAuthority) new SimpleGrantedAuthority(roleName))
        .toList();
  }
}
