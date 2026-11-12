package ca.bc.gov.restapi.results.postgres.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.postgres.dto.ExtractedGeoDataDto;
import ca.bc.gov.restapi.results.postgres.entity.activity.ActivityTreatmentGeometryEntity;
import ca.bc.gov.restapi.results.postgres.repository.ActivityTreatmentGeometryPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningGeometryPostgresRepository;
import ca.bc.gov.restapi.results.postgres.service.ActivityGeometryService.ProcessedGeometry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | ActivityGeometryService | Process and save")
class ActivityGeometryServiceProcessTest {

  private static final Long OPENING_ID = 1001L;
  private static final ObjectMapper MAPPER = new ObjectMapper();
  // ~1 km x 1 km square near Victoria, BC, in EPSG:4326
  private static final String GEOJSON_FEATURES =
      "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{},"
          + "\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[-123.40,48.40],"
          + "[-123.39,48.40],[-123.39,48.41],[-123.40,48.41],[-123.40,48.40]]]}}]}";

  @Mock private SpatialFileService spatialFileService;
  @Mock private ActivityTreatmentGeometryPostgresRepository geometryRepository;
  @Mock private OpeningGeometryPostgresRepository openingGeometryRepository;

  private ActivityGeometryService service;

  @BeforeEach
  void setUp() {
    service =
        new ActivityGeometryService(
            spatialFileService, geometryRepository, openingGeometryRepository, MAPPER);
  }

  private void givenProcessedFile(String geoJson) throws Exception {
    JsonNode node = geoJson == null ? null : MAPPER.readTree(geoJson);
    when(spatialFileService.processSpatialFile(eq("a.geojson"), any(byte[].class), any()))
        .thenReturn(ExtractedGeoDataDto.builder().geoJson(node).build());
  }

  @Test
  @DisplayName("Reprojects to 3005 and computes area and perimeter")
  void process_returnsGeometryInAlbers() throws Exception {
    givenProcessedFile(GEOJSON_FEATURES);

    ProcessedGeometry result = service.process(OPENING_ID, "a.geojson", new byte[] {1});

    assertThat(result.geometry().getSRID()).isIn(0, 3005);
    assertThat(result.area()).isPositive().hasScaleOf(4);
    assertThat(result.perimeter()).isPositive().hasScaleOf(4);
  }

  @Test
  @DisplayName("Passes an opening containment check to the spatial file service")
  void process_wiresContainmentCheck() throws Exception {
    givenProcessedFile(GEOJSON_FEATURES);
    when(openingGeometryRepository.findById(OPENING_ID)).thenReturn(Optional.empty());

    service.process(OPENING_ID, "a.geojson", new byte[] {1});

    @SuppressWarnings("unchecked")
    ArgumentCaptor<java.util.function.Consumer<Geometry>> check =
        ArgumentCaptor.forClass(java.util.function.Consumer.class);
    verify(spatialFileService)
        .processSpatialFile(eq("a.geojson"), any(byte[].class), check.capture());
    check.getValue().accept(new GeometryFactory().createPoint());
    verify(openingGeometryRepository).findById(OPENING_ID);
  }

  @Test
  @DisplayName("Rejects a file without GeoJSON or features")
  void process_noFeatures_badRequest() throws Exception {
    givenProcessedFile("{\"type\":\"FeatureCollection\",\"features\":[]}");
    assertThatThrownBy(() -> service.process(OPENING_ID, "a.geojson", new byte[] {1}))
        .isInstanceOfSatisfying(
            ResponseStatusException.class,
            e -> {
              assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
              assertThat(e.getReason()).isEqualTo("Spatial file contains no features");
            });
  }

  @Test
  @DisplayName("Rejects a missing GeoJSON")
  void process_nullGeoJson_badRequest() throws Exception {
    givenProcessedFile(null);
    assertThatThrownBy(() -> service.process(OPENING_ID, "a.geojson", new byte[] {1}))
        .isInstanceOfSatisfying(
            ResponseStatusException.class,
            e -> assertThat(e.getReason()).isEqualTo("Spatial file contains no features"));
  }

  @Test
  @DisplayName("Rejects a feature whose geometry cannot be parsed")
  void process_unparseableGeometry_badRequest() throws Exception {
    givenProcessedFile(
        "{\"features\":[{\"type\":\"Feature\",\"geometry\":{\"type\":\"Nope\","
            + "\"coordinates\":[]}}]}");
    assertThatThrownBy(() -> service.process(OPENING_ID, "a.geojson", new byte[] {1}))
        .isInstanceOfSatisfying(
            ResponseStatusException.class,
            e -> {
              assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
              assertThat(e.getReason()).isEqualTo("Failed to parse spatial geometry");
            });
  }

  @Test
  @DisplayName("Saves the geometry of a new activity with audit fields")
  void save_persistsNewEntity() throws ParseException {
    Geometry geometry = new WKTReader().read("POLYGON((0 0, 10 0, 10 10, 0 10, 0 0))");
    LocalDateTime now = LocalDateTime.of(2026, 1, 2, 3, 4);

    service.save(
        55L,
        new ProcessedGeometry(geometry, new BigDecimal("1.2345"), new BigDecimal("40.0000")),
        "IDIR\\USER",
        now);

    ArgumentCaptor<ActivityTreatmentGeometryEntity> saved =
        ArgumentCaptor.forClass(ActivityTreatmentGeometryEntity.class);
    verify(geometryRepository).save(saved.capture());
    ActivityTreatmentGeometryEntity entity = saved.getValue();
    assertThat(entity.getActivityTreatmentUnitId()).isEqualTo(55L);
    assertThat(entity.getGeometry()).isSameAs(geometry);
    assertThat(entity.getFeatureArea()).isEqualByComparingTo("1.2345");
    assertThat(entity.getFeaturePerimeter()).isEqualByComparingTo("40.0000");
    assertThat(entity.getFeatureClassSkey())
        .isEqualTo(ActivityGeometryService.ACTIVITY_FEATURE_CLASS_SKEY);
    assertThat(entity.getEntryUserId()).isEqualTo("IDIR\\USER");
    assertThat(entity.getUpdateTimestamp()).isEqualTo(now);
    assertThat(entity.getRevisionCount()).isEqualTo(1);
  }
}
