package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.postgres.dto.UserRecentOpeningDto;
import ca.bc.gov.restapi.results.postgres.service.UserRecentOpeningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @PostMapping("/viewed/{openingId}")
    @Operation(
        summary = "Records the opening viewed by the user",
        description = "Stores the opening ID that the user has viewed. This will be used to populate the Recent Openings Table in Dashboard.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully recorded the opening viewed by the user.",
                content = @Content(mediaType = "application/json")),
            @ApiResponse(
                responseCode = "204",
                description = "No content to store. No response body."),
            @ApiResponse(
                responseCode = "401",
                description = "Access token is missing or invalid",
                content = @Content(schema = @Schema(implementation = Void.class)))
        }
    )
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
    @Operation(
        summary = "Gets the list of recent openings viewed by the user",
        description = "Returns the list of openings that the user has recently viewed, sorted by the last viewed time in descending order.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved the list of recent openings.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(
                responseCode = "401",
                description = "Access token is missing or invalid",
                content = @Content(schema = @Schema(implementation = Void.class)))
        }
    )
    public ResponseEntity<PaginatedResult<OpeningSearchResponseDto>> getUserRecentOpenings(@RequestParam(defaultValue = "10") int limit) {
        // Fetch recent openings for the logged-in user with the specified limit
        PaginatedResult<OpeningSearchResponseDto> recentOpenings = userRecentOpeningService.getAllRecentOpeningsForUser(limit);
        return ResponseEntity.ok(recentOpenings);
    }
}
