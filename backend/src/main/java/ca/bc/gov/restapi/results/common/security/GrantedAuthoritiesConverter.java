package ca.bc.gov.restapi.results.common.security;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * The type Granted authorities converter.
 */
public class GrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

  @Override
  public Collection<GrantedAuthority> convert(Jwt jwt) {
      return
          Optional
              .ofNullable(
                  jwt.getClaimAsStringList("cognito:groups")
              )
              .orElse(List.of())
              .stream()
              .filter(Objects::nonNull)
              .map(Object::toString)
              .map(roleName -> "ROLE_" + roleName)
              .map(roleName -> (GrantedAuthority) new SimpleGrantedAuthority(roleName))
              .collect(Collectors.toSet());
  }
}
