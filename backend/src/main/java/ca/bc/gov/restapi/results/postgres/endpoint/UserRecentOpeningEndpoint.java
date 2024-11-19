package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.postgres.service.UserRecentOpeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/openings/recent")
public class UserRecentOpeningEndpoint {

  private final UserRecentOpeningService userRecentOpeningService;

  /**
   * Retrieves a list of recent openings viewed by the user, limited by the number of results.
   *
   * @param limit The maximum number of results to return.
   * @return A list of opening IDs viewed by the user.
   */
  @GetMapping
  public ResponseEntity<PaginatedResult<OpeningSearchResponseDto>> getUserRecentOpenings(
      @RequestParam(defaultValue = "10") int limit) {
    // Fetch recent openings for the logged-in user with the specified limit
    return ResponseEntity.ok(userRecentOpeningService.getAllRecentOpeningsForUser(limit));
  }

  /**
   * Records the opening viewed by the user based on the provided opening ID.
   *
   * @param openingId The ID of the opening viewed by the user.
   * @return A simple confirmation message or the HTTP code 204-No Content.
   */
  @PutMapping("/{openingId}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void recordUserViewedOpening(
      @PathVariable Long openingId) {
    // Store the opening and return the DTO
    userRecentOpeningService.storeViewedOpening(openingId);
  }

}