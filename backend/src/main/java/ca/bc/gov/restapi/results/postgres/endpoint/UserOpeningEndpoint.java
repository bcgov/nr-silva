package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.postgres.dto.MyRecentActionsRequestsDto;
import ca.bc.gov.restapi.results.postgres.service.UserOpeningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** This class holds resources for exposing user openings saved as favourites. */
@RestController
@RequestMapping("/api/user-openings")
@Tag(
    name = "User Openings (Postgres)",
    description = "Endpoints to handle user favourite Openings on `SILVA` schema.")
@RequiredArgsConstructor
public class UserOpeningEndpoint {

  private final UserOpeningService userOpeningService;

  /**
   * Gets up to three tracked Openings to a user.
   *
   * @return A list of openings or the http code 204-No Content.
   */
  @GetMapping("/dashboard-track-openings")
  @Operation(
      summary = "Gets up to three updated Openings to a user",
      description = "Gets up to three updated openings that got saved by the user.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "An array containing up to three Openings for the user.",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(
            responseCode = "204",
            description = "No data found for this user. No response body."),
        @ApiResponse(
            responseCode = "401",
            description = "Access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = Void.class)))
      })
  public ResponseEntity<List<MyRecentActionsRequestsDto>> getUserTrackedOpenings() {
    List<MyRecentActionsRequestsDto> userOpenings = userOpeningService.getUserTrackedOpenings();
    if (userOpenings.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(userOpenings);
  }

  /**
   * Saves one Opening ID as favourite to an user.
   *
   * @param id The opening ID.
   * @return HTTP status code 201 if success, no response body.
   */
  @PostMapping("/{id}")
  @Operation(
      summary = "Saves one Opening ID as favourite to an user",
      description = "Allow users to save Opening IDs to track them easily in the dashboard.",
      responses = {
        @ApiResponse(responseCode = "201", description = "Opening successfully saved to the user."),
        @ApiResponse(
            responseCode = "401",
            description = "Access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = Void.class)))
      })
  public ResponseEntity<Void> saveUserOpening(
      @Parameter(
              name = "id",
              in = ParameterIn.PATH,
              description = "Opening ID",
              required = true,
              schema = @Schema(type = "integer", format = "int64"))
          @PathVariable
          Long id) {
    userOpeningService.saveOpeningToUser(id);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Deletes one or more user openings from their favourite list.
   *
   * @param id The opening ID.
   * @return HTTP status code 204 if success, no response body.
   */
  @DeleteMapping("/{id}")
  @Operation(
      summary = "Deleted an Opening ID from the user's favourite",
      description = "Allow users to remove saved Opening IDs from their favourite list.",
      responses = {
        @ApiResponse(
            responseCode = "204",
            description = "Opening successfully removed. No content body."),
        @ApiResponse(
            responseCode = "401",
            description = "Access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = Void.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Opening not found in the user's favourite.")
      })
  public ResponseEntity<Void> deleteUserOpening(
      @Parameter(
              name = "id",
              in = ParameterIn.PATH,
              description = "Opening ID",
              required = true,
              schema = @Schema(type = "integer", format = "int64"))
          @PathVariable
          Long id) {
    userOpeningService.deleteOpeningFromUserFavourite(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
