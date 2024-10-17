package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.postgres.dto.UserRecentOpeningDto;
import ca.bc.gov.restapi.results.postgres.service.UserRecentOpeningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
}
