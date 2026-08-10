package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.common.clamav.VirusScanService;
import ca.bc.gov.restapi.results.postgres.SilvaPostgresConstants;
import ca.bc.gov.restapi.results.postgres.dto.CreateOpeningRequestDto;
import ca.bc.gov.restapi.results.postgres.dto.CreateOpeningResponseDto;
import ca.bc.gov.restapi.results.postgres.dto.ExtractedGeoDataDto;
import ca.bc.gov.restapi.results.postgres.service.CreateOpeningService;
import ca.bc.gov.restapi.results.postgres.service.OpeningSpatialFileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/** Create-opening and spatial-upload endpoints — postgres-only mode. */
@RestController
@RequestMapping(path = "/api/openings", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class OpeningCreateEndpoint {

  private final OpeningSpatialFileService openingSpatialFileService;
  private final VirusScanService virusScanService;
  private final CreateOpeningService createOpeningService;
  private final ObjectMapper objectMapper;
  private final Validator validator;

  @PostMapping(value = "/create/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  public ExtractedGeoDataDto uploadOpeningSpatialFile(@RequestPart("file") MultipartFile file) {
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
    return openingSpatialFileService.processOpeningSpatialFile(
        file.getOriginalFilename(), fileBytes);
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public CreateOpeningResponseDto createOpening(
      @RequestPart("data")
          @Parameter(
              description = "Opening creation request (JSON)",
              schema = @Schema(implementation = CreateOpeningRequestDto.class))
          String rawData,
      @RequestPart("file")
          @Parameter(
              description = "Spatial file (GeoJSON or GML)",
              schema = @Schema(type = "string", format = "binary"))
          MultipartFile file) {
    CreateOpeningRequestDto dto;
    try {
      dto = objectMapper.readValue(rawData, CreateOpeningRequestDto.class);
    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Invalid JSON in 'data': " + e.getMessage());
    }
    Set<ConstraintViolation<CreateOpeningRequestDto>> violations = validator.validate(dto);
    if (!violations.isEmpty()) {
      String msg =
          violations.stream()
              .map(v -> v.getPropertyPath() + ": " + v.getMessage())
              .collect(Collectors.joining(", "));
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, msg);
    }
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
