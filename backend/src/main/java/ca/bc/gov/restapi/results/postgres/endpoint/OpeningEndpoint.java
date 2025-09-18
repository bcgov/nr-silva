package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.postgres.dto.ExtractedGeoDataDto;
import ca.bc.gov.restapi.results.postgres.service.OpeningSpatialFileService;
import ca.bc.gov.restapi.results.postgres.service.UserOpeningService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/** Opening endpoint. */
@RestController("postgresOpeningEndpoint")
@RequestMapping(path = "/api/openings", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class OpeningEndpoint {

  private final UserOpeningService userOpeningService;
  private final OpeningSpatialFileService openingSpatialFileService;

  /**
   * Get user's favorite openings.
   *
   * @return List of favorite openings.
   */
  @GetMapping("/favourites")
  public List<Long> getFavorites() {
    return userOpeningService.listUserFavoriteOpenings();
  }

  /**
   * Check if an opening is a favorite.
   *
   * @param id Opening ID.
   * @return True if it's a favorite, false otherwise.
   */
  @GetMapping("/favourites/{id}")
  public boolean checkFavorite(@PathVariable Long id) {
    return !userOpeningService.checkForFavorites(List.of(id)).isEmpty();
  }

  /**
   * Add an opening to favorites.
   *
   * @param id Opening ID.
   */
  @PutMapping("/favourites/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void addToFavorites(@PathVariable Long id) {
    userOpeningService.addUserFavoriteOpening(id);
  }

  /**
   * Remove an opening from favorites.
   *
   * @param id Opening ID.
   */
  @DeleteMapping("/favourites/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void removeFromFavorites(@PathVariable Long id) {
    userOpeningService.removeUserFavoriteOpening(id);
  }

  /**
   * Upload and process an opening spatial file.
   *
   * @param file Multipart spatial file.
   */
  @PostMapping(value = "/create/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  public ExtractedGeoDataDto uploadOpeningSpatialFile(@RequestPart("file") MultipartFile file) {
    return openingSpatialFileService.processOpeningSpatialFile(file);
  }
}
