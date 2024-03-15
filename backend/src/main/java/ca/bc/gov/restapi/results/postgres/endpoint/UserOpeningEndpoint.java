package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.postgres.entity.UserOpeningEntity;
import ca.bc.gov.restapi.results.postgres.repository.UserOpeningRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** This class holds resources for exposing user openings saved as favourites. */
@RestController
@RequestMapping(path = "/api/user-openings", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(
    name = "User Opennings (SILVA)",
    description = "Endpoints to handle user favourite Openings in the `SILVA` schema.")
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
