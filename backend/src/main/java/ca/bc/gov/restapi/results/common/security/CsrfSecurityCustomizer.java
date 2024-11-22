package ca.bc.gov.restapi.results.common.security;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.stereotype.Component;

@Component
public class CsrfSecurityCustomizer implements Customizer<CsrfConfigurer<HttpSecurity>> {

  @Override
  public void customize(CsrfConfigurer<HttpSecurity> csrfSpec) {
    csrfSpec
        .csrfTokenRepository(new CookieCsrfTokenRepository());
  }
}
