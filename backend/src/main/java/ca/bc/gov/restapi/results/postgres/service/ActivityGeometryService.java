package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.postgres.dto.ExtractedGeoDataDto;
import ca.bc.gov.restapi.results.postgres.SilvaPostgresConstants;
import ca.bc.gov.restapi.results.postgres.entity.OpeningGeometryEntity;
import ca.bc.gov.restapi.results.postgres.entity.activity.ActivityTreatmentGeometryEntity;
import ca.bc.gov.restapi.results.postgres.repository.ActivityTreatmentGeometryPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningGeometryPostgresRepository;
import ca.bc.gov.restapi.results.postgres.util.GeometryReprojectionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.TopologyException;
import org.locationtech.jts.operation.distance.IndexedFacetDistance;
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
  private final OpeningGeometryPostgresRepository openingGeometryRepository;
  private final ObjectMapper objectMapper;

  /** Geometry in BC Albers (EPSG:3005) with its area in hectares and perimeter in metres. */
  public record ProcessedGeometry(Geometry geometry, BigDecimal area, BigDecimal perimeter) {}

  /**
   * Validates and processes the file with the same rules as the activity spatial upload, including
   * the check that the geometry is within the opening geometry.
   *
   * @param openingId the opening the activity is created under
   * @param fileName the original file name
   * @param fileBytes the file content, already scanned for viruses
   * @return the geometry ready to be saved
   * @throws ResponseStatusException with status 400 when the file is not a valid spatial file
   */
  public ProcessedGeometry process(Long openingId, String fileName, byte[] fileBytes) {
    ExtractedGeoDataDto geoData =
        spatialFileService.processSpatialFile(
            fileName, fileBytes, geometry -> validateWithinOpening(openingId, geometry));
    Geometry geometry3005 = reprojectTo3005(parseFirstFeature(geoData.geoJson()));
    return new ProcessedGeometry(
        geometry3005,
        BigDecimal.valueOf(geometry3005.getArea() / SQUARE_METRES_PER_HECTARE)
            .setScale(4, RoundingMode.HALF_UP),
        BigDecimal.valueOf(geometry3005.getLength()).setScale(4, RoundingMode.HALF_UP));
  }

  /**
   * Business rule: the activity geometry must be within the opening geometry. Touching the opening
   * boundary is allowed, and a tolerance applies, see {@link
   * SilvaPostgresConstants#OPENING_CONTAINMENT_TOLERANCE_METERS}. A MultiPolygon opening is
   * treated as a union of its parts. The check passes when the opening has no geometry.
   *
   * @param openingId the opening the activity belongs to
   * @param geometry3005 the activity geometry in EPSG:3005
   * @throws ResponseStatusException with status 400 when the activity geometry is out of bounds
   */
  public void validateWithinOpening(Long openingId, Geometry geometry3005) {
    Geometry opening =
        openingGeometryRepository
            .findById(openingId)
            .map(OpeningGeometryEntity::getGeometry)
            .orElse(null);
    if (opening == null || opening.isEmpty() || !opening.getGeometryType().contains("Polygon")) {
      return;
    }
    Boolean covered = coversWithTolerance(openingId, opening, geometry3005);
    if (Boolean.FALSE.equals(covered)) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Activity geometry must be within the opening geometry"
              + overshootSuffix(opening, geometry3005));
    }
  }

  /** Describes how far the activity reaches outside the opening, or nothing if it can't be told. */
  private String overshootSuffix(Geometry opening, Geometry activity) {
    try {
      IndexedFacetDistance distance = new IndexedFacetDistance(opening);
      double max = 0;
      for (var coordinate : activity.difference(opening).getCoordinates()) {
        max = Math.max(max, distance.distance(activity.getFactory().createPoint(coordinate)));
      }
      return String.format(" (up to %.1f m outside)", max);
    } catch (RuntimeException e) {
      return "";
    }
  }

  /** Returns null when the check cannot be run because the stored opening geometry is invalid. */
  private Boolean coversWithTolerance(Long openingId, Geometry opening, Geometry activity) {
    double tolerance = SilvaPostgresConstants.OPENING_CONTAINMENT_TOLERANCE_METERS;
    try {
      return opening.buffer(tolerance).covers(activity);
    } catch (TopologyException e) {
      // Legacy opening geometry can be invalid (e.g. overlapping parts); buffer(0) repairs it
      try {
        return opening.buffer(0).buffer(tolerance).covers(activity);
      } catch (TopologyException repairFailure) {
        log.warn(
            "Skipping activity containment check, opening {} geometry is invalid", openingId, e);
        return null;
      }
    }
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
