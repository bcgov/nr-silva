package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.postgres.dto.UserRecentOpeningDto;
import ca.bc.gov.restapi.results.postgres.service.UserRecentOpeningService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserRecentOpeningEndpoint {

    private final UserRecentOpeningService userRecentOpeningService;

    /**
     * Records the opening viewed by the user based on the provided opening ID.
     *
     * @param openingId The ID of the opening viewed by the user.
     * @return A simple confirmation message or the HTTP code 204-No Content.
     */
    @PostMapping("api/users/recent/{openingId}")
    public ResponseEntity<UserRecentOpeningDto> recordUserViewedOpening(@PathVariable String openingId) {
        // Store the opening and return the DTO
        UserRecentOpeningDto recentOpeningDto = userRecentOpeningService.storeViewedOpening(openingId);
        return ResponseEntity.ok(recentOpeningDto);
    }

    /**
     * Retrieves a list of recent openings viewed by the user, limited by the number of results.
     *
     * @param limit The maximum number of results to return.
     * @return A list of opening IDs viewed by the user.
     */
    @GetMapping("api/user/recent-openings")
    public ResponseEntity<PaginatedResult<OpeningSearchResponseDto>> getUserRecentOpenings(@RequestParam(defaultValue = "10") int limit) {
        // Fetch recent openings for the logged-in user with the specified limit
        PaginatedResult<OpeningSearchResponseDto> recentOpenings = userRecentOpeningService.getAllRecentOpeningsForUser(limit);
        return ResponseEntity.ok(recentOpenings);
    }
}