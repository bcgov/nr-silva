package ca.bc.gov.restapi.results.common.security;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component("auditorProvider")
@RequiredArgsConstructor
public class SilvaAuditorAware implements AuditorAware<String> {

  private final LoggedUserService loggedUserService;

  @Override
  public Optional<String> getCurrentAuditor() {
    return Optional.ofNullable(loggedUserService.getLoggedUserId());
  }
}
