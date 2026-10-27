package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.common.clamav.VirusScanService;
import ca.bc.gov.restapi.results.postgres.SilvaPostgresConstants;
import ca.bc.gov.restapi.results.postgres.dto.CreateActivityRequestDto;
import ca.bc.gov.restapi.results.postgres.dto.CreateActivityResponseDto;
import ca.bc.gov.restapi.results.postgres.dto.ExtractedGeoDataDto;
import ca.bc.gov.restapi.results.postgres.service.CreateActivityService;
import ca.bc.gov.restapi.results.postgres.service.SpatialFileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/**
 * Activity endpoints available in PostgreSQL mode. Paths are set on each method because activities
 * are created under an opening while the spatial upload does not depend on one.
 */
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class ActivityEndpoint {

  private final SpatialFileService spatialFileService;
  private final VirusScanService virusScanService;
  private final CreateActivityService createActivityService;
  private final ObjectMapper objectMapper;

  /** Creates a basic activity under an opening, with an optional spatial file. */
  @PostMapping(
      value = "/api/openings/{openingId}/activities",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(
      summary = "Create an activity",
      description =
          "Creates a planned or completed activity with a basic base code under an opening. "
              + "The optional spatial file is scanned for viruses first, then validated and "
              + "processed like the activity spatial upload. Nothing is saved when the request "
              + "or the file is invalid.")
  @ApiResponse(responseCode = "201", description = "Activity created")
  @ApiResponse(
      responseCode = "400",
      description =
          "Invalid request or spatial file; every violation is listed as 'field: message'",
      content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
  @ApiResponse(
      responseCode = "404",
      description = "Opening not found",
      content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
  @ApiResponse(
      responseCode = "422",
      description = "Upload rejected by the virus scanner",
      content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
  public CreateActivityResponseDto createActivity(
      @PathVariable("openingId") @Parameter(description = "Opening the activity is created under")
          Long openingId,
      @RequestPart("data")
          @Parameter(
              description = "Activity creation request (JSON)",
              schema = @Schema(implementation = CreateActivityRequestDto.class))
          String rawData,
      @RequestPart(value = "file", required = false)
          @Parameter(
              description = "Optional spatial file (GeoJSON or GML), up to 25MB",
              schema = @Schema(type = "string", format = "binary"))
          MultipartFile file) {
    CreateActivityRequestDto dto;
    try {
      dto = objectMapper.readValue(rawData, CreateActivityRequestDto.class);
    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Invalid JSON in 'data': " + e.getOriginalMessage());
    }
    if (file == null || (file.isEmpty() && isBlank(file.getOriginalFilename()))) {
      return createActivityService.createActivity(openingId, dto);
    }
    byte[] fileBytes = readAndScanFile(file);
    return createActivityService.createActivity(
        openingId, dto, file.getOriginalFilename(), fileBytes);
  }

  private boolean isBlank(String value) {
    return value == null || value.isBlank();
  }

  /** Processes an activity spatial file without creating an activity or saving geometry. */
  @PostMapping(
      value = "/api/activities/spatial/upload",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  @Operation(
      summary = "Upload an activity spatial file",
      description =
          "Validates and processes one Polygon or MultiPolygon feature from a GeoJSON or GML file. "
              + "Returns geometry in EPSG:4326 and area in hectares without persisting data.")
  @ApiResponse(responseCode = "202", description = "Processed spatial geometry and area")
  @ApiResponse(
      responseCode = "400",
      description = "Invalid spatial file",
      content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
  @ApiResponse(
      responseCode = "422",
      description = "Upload rejected by the virus scanner",
      content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
  public ExtractedGeoDataDto uploadActivitySpatial(
      @RequestPart("file")
          @Parameter(
              description = "Spatial file (GeoJSON or GML), up to 25MB",
              schema = @Schema(type = "string", format = "binary"))
          MultipartFile file) {
    byte[] fileBytes = readAndScanFile(file);
    return spatialFileService.processSpatialFile(file.getOriginalFilename(), fileBytes);
  }

  /**
   * Checks that the uploaded file is present and within the size limit, reads it and scans it for
   * viruses before anything else is done with it.
   */
  private byte[] readAndScanFile(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uploaded file is null or empty");
    }
    if (file.getSize() > SilvaPostgresConstants.MAX_SPATIAL_FILE_SIZE_BYTES) {
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
    return fileBytes;
  }
}
