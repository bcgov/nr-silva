package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.postgres.dto.UserOpeningCreateDto;
import ca.bc.gov.restapi.results.postgres.entity.UserOpeningEntity;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** This class holds resources for exposing user openings saved as favourites. */
@RestController
@RequestMapping(path = "/api/user-openings", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(
    name = "User Openings (SILVA)",
    description = "Endpoints to handle user favourite Openings in the `SILVA` schema.")
@RequiredArgsConstructor
public class UserOpeningEndpoint {

  private final UserOpeningService userOpeningService;

  /**
   * Gets all saved Openings to a user.
   *
   * @return A list of openings or the http code 204-No Content.
   */
  @GetMapping
  @Operation(
      summary = "Gets all saved Openings to a user",
      description = "Gets all openings highlighted by the user, saved as favourite.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "An array containing one or more Openings found for the user."),
        @ApiResponse(
            responseCode = "204",
            description = "No data found in the dable. No response body."),
        @ApiResponse(
            responseCode = "401",
            description = "Access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = Void.class)))
      })
  public ResponseEntity<List<UserOpeningEntity>> getAll() {
    List<UserOpeningEntity> userOpenings = userOpeningService.getAllUserOpenings();
    if (userOpenings.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(userOpenings);
  }

  /**
   * Saves one or more Opening ID as favourite to an user.
   *
   * @param createDtos List of {@link UserOpeningCreateDto} with opening ids
   * @return HTTP status code 201 if success, no response body.
   */
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Saves one or more Opening ID as favourite to an user",
      description = "Allow users to save on or more Opening IDs at the same request as favourite.",
      responses = {
        @ApiResponse(
            responseCode = "201",
            description = "All opening ids were found and successfully saved to the user."),
        @ApiResponse(
            responseCode = "401",
            description = "Access token is missing or invalid",
            content = @Content(schema = @Schema(implementation = Void.class))),
        @ApiResponse(
            responseCode = "404",
            description = "One or more openings were not found in the system.")
      })
  public ResponseEntity<Void> saveUserOpening(@RequestBody List<UserOpeningCreateDto> createDtos) {
    userOpeningService.saveOpeningsToUser(createDtos);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Deletes one or more user openings from their favourite list.
   *
   * @param openingIds List with one or more Opening IDs.
   * @return HTTP status code 204 if success, no response body.
   */
  @DeleteMapping
  public ResponseEntity<Void> deleteUserOpening(
      @Parameter(
              name = "openingIds",
              in = ParameterIn.QUERY,
              description = "Opening ID list separated by comma",
              required = true)
          @RequestParam
          List<String> openingIds) {
    userOpeningService.deleteOpeningsFromUserFavourite(openingIds);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
