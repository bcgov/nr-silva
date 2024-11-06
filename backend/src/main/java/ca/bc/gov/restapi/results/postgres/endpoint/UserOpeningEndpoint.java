package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.postgres.dto.MyRecentActionsRequestsDto;
import ca.bc.gov.restapi.results.postgres.service.UserOpeningService;
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

/**
 * This class holds resources for exposing user openings saved as favourites.
 */
@RestController
@RequestMapping("/api/user-openings")
@RequiredArgsConstructor
public class UserOpeningEndpoint {

  private final UserOpeningService userOpeningService;

  /**
   * Gets up to three tracked Openings to a user.
   *
   * @return A list of openings or the http code 204-No Content.
   */
  @GetMapping("/dashboard-track-openings")
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
  public ResponseEntity<Void> saveUserOpening(@PathVariable Long id) {
    userOpeningService.addUserFavoriteOpening(id);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Deletes one or more user openings from their favourite list.
   *
   * @param id The opening ID.
   * @return HTTP status code 204 if success, no response body.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUserOpening(
      @PathVariable
      Long id) {
    userOpeningService.removeUserFavoriteOpening(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
