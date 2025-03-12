package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.postgres.service.UserOpeningService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Opening Favorites endpoint. It's responsible for handling user's favorite openings.
 */
@RestController
@RequestMapping(path = "/api/openings/favourites", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class OpeningFavoriteEndpoint {

  private final UserOpeningService userOpeningService;

  /**
   * Get user's favorite openings.
   *
   * @return List of favorite openings.
   */
  @GetMapping
  public List<Long> getFavorites() {
    return userOpeningService.listUserFavoriteOpenings();
  }

  /**
   * Check if an opening is a favorite.
   *
   * @param id Opening ID.
   * @return True if it's a favorite, false otherwise.
   */
  @GetMapping("/{id}")
  public boolean checkFavorite(@PathVariable Long id) {
    return !userOpeningService.checkForFavorites(List.of(id)).isEmpty();
  }

  /**
   * Add an opening to favorites.
   *
   * @param id Opening ID.
   */
  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void addToFavorites(@PathVariable Long id) {
    userOpeningService.addUserFavoriteOpening(id);
  }

  /**
   * Remove an opening from favorites.
   *
   * @param id Opening ID.
   */
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void removeFromFavorites(@PathVariable Long id) {
    userOpeningService.removeUserFavoriteOpening(id);
  }

}
