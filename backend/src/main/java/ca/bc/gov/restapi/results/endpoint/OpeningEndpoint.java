package ca.bc.gov.restapi.results.endpoint;

import ca.bc.gov.restapi.results.dto.RecentOpeningDto;
import ca.bc.gov.restapi.results.endpoint.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.endpoint.pagination.PaginatedViaQuery;
import ca.bc.gov.restapi.results.endpoint.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.service.OpeningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** This class holds endpoints for the Home Screen. */
@RestController
@RequestMapping(path = "/api/opening", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Opening", description = "Endpoints for the fetching and saving Openings")
public class OpeningEndpoint {

  private final OpeningService openingService;

  /**
   * Fetches all recent openings for the home screen.
   *
   * @param paginationParameters {@link PaginationParameters} parameters
   * @return List of {@link RecentOpeningDto} or empty list.
   */
  @GetMapping(path = "/recent-openings", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Fetches all recent openings for the home screen.",
      description =
          "List all recent openings using a paginated search with a page size 5 by default.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "An array with all recent openings, ordered by the most recent ones.")
      })
  @PaginatedViaQuery
  public PaginatedResult<RecentOpeningDto> getRecentOpenings(
      @Valid PaginationParameters paginationParameters) {
    return openingService.getRecentOpenings(paginationParameters);
  }
}
