package ca.bc.gov.restapi.results.oracle.endpoint;

import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.common.pagination.PaginatedViaQuery;
import ca.bc.gov.restapi.results.common.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.oracle.dto.RecentOpeningDto;
import ca.bc.gov.restapi.results.oracle.service.OpeningService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** This class holds endpoints for the Home Screen. */
@RestController
@RequestMapping(path = "/api/openings", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class OpeningEndpoint {

  private final OpeningService openingService;

  /**
   * Fetches all recent openings for the home screen.
   *
   * @param paginationParameters {@link PaginationParameters} parameters
   * @return List of {@link RecentOpeningDto} or empty list.
   */
  @GetMapping(path = "/recent-openings", produces = MediaType.APPLICATION_JSON_VALUE)
  @PaginatedViaQuery
  @CrossOrigin(exposedHeaders = "x-opening-source")
  public ResponseEntity<PaginatedResult<RecentOpeningDto>> getRecentOpenings(
      @Valid PaginationParameters paginationParameters) {
    PaginatedResult<RecentOpeningDto> userOpenings =
        openingService.getRecentOpeningsCurrentUser(paginationParameters);

    HttpHeaders headers = new HttpHeaders();
    headers.set("x-opening-source", "user-based");
    return ResponseEntity.ok().headers(headers).body(userOpenings);
  }
}
