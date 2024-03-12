package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.postgres.entity.UserOpeningEntity;
import ca.bc.gov.restapi.results.postgres.repository.UserOpeningRepository;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/user-openings", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserOpeningEndpoint {

  private final UserOpeningRepository userOpeningRepository;

  UserOpeningEndpoint(UserOpeningRepository userOpeningRepository) {
    this.userOpeningRepository = userOpeningRepository;
  }

  @GetMapping
  public List<UserOpeningEntity> getAll() {
    return userOpeningRepository.findAll();
  }
}
