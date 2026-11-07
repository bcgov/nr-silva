package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.postgres.dto.ExtractedGeoDataDto;
import ca.bc.gov.restapi.results.postgres.entity.activity.ActivityTreatmentGeometryEntity;
import ca.bc.gov.restapi.results.postgres.repository.ActivityTreatmentGeometryPostgresRepository;
import ca.bc.gov.restapi.results.postgres.util.GeometryReprojectionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/** Processes the spatial file of an activity and saves its geometry. */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class ActivityGeometryService {

  // Feature class of the activity treatment unit layer in RESULTS
  static final long ACTIVITY_FEATURE_CLASS_SKEY = 2411L;
  private static final double SQUARE_METRES_PER_HECTARE = 10_000.0;

  private final SpatialFileService spatialFileService;
  private final ActivityTreatmentGeometryPostgresRepository geometryRepository;
  private final ObjectMapper objectMapper;

  /** Geometry in BC Albers (EPSG:3005) with its area in hectares and perimeter in metres. */
  public record ProcessedGeometry(Geometry geometry, BigDecimal area, BigDecimal perimeter) {}

  /**
   * Validates and processes the file with the same rules as the activity spatial upload.
   *
   * @param fileName the original file name
   * @param fileBytes the file content, already scanned for viruses
   * @return the geometry ready to be saved
   * @throws ResponseStatusException with status 400 when the file is not a valid spatial file
   */
  public ProcessedGeometry process(String fileName, byte[] fileBytes) {
    ExtractedGeoDataDto geoData = spatialFileService.processSpatialFile(fileName, fileBytes);
    Geometry geometry3005 = reprojectTo3005(parseFirstFeature(geoData.geoJson()));
    return new ProcessedGeometry(
        geometry3005,
        BigDecimal.valueOf(geometry3005.getArea() / SQUARE_METRES_PER_HECTARE)
            .setScale(4, RoundingMode.HALF_UP),
        BigDecimal.valueOf(geometry3005.getLength()).setScale(4, RoundingMode.HALF_UP));
  }

  /** Saves the geometry of a newly created activity. */
  public void save(
      Long activityId, ProcessedGeometry geometry, String userId, LocalDateTime now) {
    geometryRepository.save(
        ActivityTreatmentGeometryEntity.builder()
            .activityTreatmentUnitId(activityId)
            .isNew(true)
            .geometry(geometry.geometry())
            .featureArea(geometry.area())
            .featurePerimeter(geometry.perimeter())
            .featureClassSkey(ACTIVITY_FEATURE_CLASS_SKEY)
            .entryUserId(userId)
            .entryTimestamp(now)
            .updateUserId(userId)
            .updateTimestamp(now)
            .revisionCount(1)
            .build());
  }

  private Geometry parseFirstFeature(JsonNode geoJson) {
    JsonNode features = geoJson == null ? null : geoJson.get("features");
    if (features == null || features.isEmpty()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Spatial file contains no features");
    }
    try {
      return new GeoJsonReader()
          .read(objectMapper.writeValueAsString(features.get(0).get("geometry")));
    } catch (Exception e) {
      log.error("Failed to parse activity geometry from GeoJSON", e);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to parse spatial geometry");
    }
  }

  private Geometry reprojectTo3005(Geometry geometry4326) {
    try {
      return GeometryReprojectionUtils.to3005(geometry4326);
    } catch (RuntimeException e) {
      log.error("Failed to reproject activity geometry to EPSG:3005", e);
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Failed to reproject geometry");
    }
  }
}
