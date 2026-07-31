package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.common.clamav.VirusScanService;
import ca.bc.gov.restapi.results.postgres.SilvaPostgresConstants;
import ca.bc.gov.restapi.results.postgres.dto.CreateOpeningRequestDto;
import ca.bc.gov.restapi.results.postgres.dto.CreateOpeningResponseDto;
import ca.bc.gov.restapi.results.postgres.dto.ExtractedGeoDataDto;
import ca.bc.gov.restapi.results.postgres.service.CreateOpeningService;
import ca.bc.gov.restapi.results.postgres.service.OpeningSpatialFileService;
import ca.bc.gov.restapi.results.postgres.service.UserOpeningService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.io.IOException;
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
import org.springframework.web.server.ResponseStatusException;

/** Opening endpoint. */
@RestController("postgresOpeningEndpoint")
@RequestMapping(path = "/api/openings", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class OpeningEndpoint {

  private final UserOpeningService userOpeningService;
  private final OpeningSpatialFileService openingSpatialFileService;
  private final VirusScanService virusScanService;
  private final CreateOpeningService createOpeningService;

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
    // Early validation before loading file into memory
    if (file == null || file.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uploaded file is null or empty");
    }
    if (file.getSize() > SilvaPostgresConstants.MAX_OPENING_FILE_SIZE_BYTES) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File exceeds 25MB size limit");
    }

    // Get bytes with proper exception handling
    byte[] fileBytes;
    try {
      fileBytes = file.getBytes();
    } catch (IOException e) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Failed to read file: " + e.getMessage(), e);
    }

    // Virus scan
    virusScanService.scanOrThrow(fileBytes, file.getOriginalFilename());

    // Process with pre-read bytes to avoid double-reading file content
    return openingSpatialFileService.processOpeningSpatialFile(file.getOriginalFilename(), fileBytes);
  }

  /**
   * Create a new opening from a JSON request part and a spatial file.
   *
   * @param dto the opening creation request containing business fields and tenure details
   * @param file the spatial file (GeoJSON or GML) describing the opening geometry
   * @return the ID of the newly created opening
   */
  @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public CreateOpeningResponseDto createOpening(
      @RequestPart("data") @Valid
          @Parameter(
              description = "Opening creation request",
              schema = @Schema(implementation = CreateOpeningRequestDto.class))
          CreateOpeningRequestDto dto,
      @RequestPart("file")
          @Parameter(
              description = "Spatial file (GeoJSON or GML)",
              schema = @Schema(type = "string", format = "binary"))
          MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uploaded file is null or empty");
    }
    if (file.getSize() > SilvaPostgresConstants.MAX_OPENING_FILE_SIZE_BYTES) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File exceeds 25MB size limit");
    }

    byte[] fileBytes;
    try {
      fileBytes = file.getBytes();
    } catch (IOException e) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Failed to read file: " + e.getMessage(), e);
    }

    virusScanService.scanOrThrow(fileBytes, file.getOriginalFilename());

    return createOpeningService.createOpening(dto, file.getOriginalFilename(), fileBytes);
  }
}
