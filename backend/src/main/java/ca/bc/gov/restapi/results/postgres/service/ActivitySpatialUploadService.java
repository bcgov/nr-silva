package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.exception.NotFoundGenericException;
import ca.bc.gov.restapi.results.common.service.OpeningGeometryService;
import ca.bc.gov.restapi.results.postgres.dto.ActivitySpatialUploadResponseDto;
import ca.bc.gov.restapi.results.postgres.dto.ExtractedGeoDataDto;
import ca.bc.gov.restapi.results.postgres.repository.OpeningPostgresRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/** Processes the spatial file of an activity and adds the opening data to compare it with. */
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class ActivitySpatialUploadService {

  private final SpatialFileService spatialFileService;
  private final ActivityGeometryService geometryService;
  private final OpeningPostgresRepository openingRepository;
  private final OpeningGeometryService openingGeometryService;
  private final ObjectMapper objectMapper;

  /**
   * Validates and processes the file without saving anything.
   *
   * @param openingId the opening the activity is created under
   * @param fileName the original file name
   * @param fileBytes the file content, already scanned for viruses
   * @return the processed activity geometry with the opening geometry to compare it with
   * @throws NotFoundGenericException when the opening does not exist
   * @throws org.springframework.web.server.ResponseStatusException with status 400 when the file is
   *     invalid or its geometry is not within the opening geometry
   */
  public ActivitySpatialUploadResponseDto process(
      Long openingId, String fileName, byte[] fileBytes) {
    if (!openingRepository.existsById(openingId)) {
      throw new NotFoundGenericException("Opening");
    }
    ExtractedGeoDataDto activity =
        spatialFileService.processSpatialFile(
            fileName,
            fileBytes,
            geometry -> geometryService.validateWithinOpening(openingId, geometry));

    return new ActivitySpatialUploadResponseDto(
        activity.geometryArea(), activity.geoJson(), openingGeoJson(openingId));
  }

  private JsonNode openingGeoJson(Long openingId) {
    FeatureCollection openingGeometry = openingGeometryService.getOpeningGeometry(openingId);
    boolean hasGeometry =
        openingGeometry.getFeatures().stream().map(Feature::getGeometry).anyMatch(Objects::nonNull);
    return hasGeometry ? objectMapper.valueToTree(openingGeometry) : null;
  }
}
