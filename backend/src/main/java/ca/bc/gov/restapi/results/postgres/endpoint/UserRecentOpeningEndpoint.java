package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.common.dto.opening.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.postgres.service.UserRecentOpeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/openings/recent")
public class UserRecentOpeningEndpoint {

  private final UserRecentOpeningService userRecentOpeningService;

  @GetMapping
  public Page<OpeningSearchResponseDto> getUserRecentOpenings(Pageable pageable) {
    // Fetch recent openings for the logged-in user with the specified limit
    return userRecentOpeningService.getAllRecentOpeningsForUser(pageable);
  }

  /**
   * Records the opening viewed by the user based on the provided opening ID.
   *
   * @param openingId The ID of the opening viewed by the user.
   */
  @PutMapping("/{openingId}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void recordUserViewedOpening(@PathVariable Long openingId) {
    // Store the opening and return the DTO
    userRecentOpeningService.storeViewedOpening(openingId);
  }
}
