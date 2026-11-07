package ca.bc.gov.restapi.results.postgres.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.exception.NotFoundGenericException;
import ca.bc.gov.restapi.results.common.service.OpeningGeometryService;
import ca.bc.gov.restapi.results.postgres.dto.ActivitySpatialUploadResponseDto;
import ca.bc.gov.restapi.results.postgres.dto.ExtractedGeoDataDto;
import ca.bc.gov.restapi.results.postgres.repository.OpeningPostgresRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.function.Consumer;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | ActivitySpatialUploadService")
class ActivitySpatialUploadServiceTest {

  private static final Long OPENING_ID = 1001L;
  private static final byte[] FILE_BYTES = {1, 2, 3};

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Mock private SpatialFileService spatialFileService;
  @Mock private ActivityGeometryService geometryService;
  @Mock private OpeningPostgresRepository openingRepository;
  @Mock private OpeningGeometryService openingGeometryService;

  private ActivitySpatialUploadService service;
  private ExtractedGeoDataDto activity;

  @BeforeEach
  void setUp() {
    service =
        new ActivitySpatialUploadService(
            spatialFileService,
            geometryService,
            openingRepository,
            openingGeometryService,
            objectMapper);
    activity = new ExtractedGeoDataDto(new BigDecimal("4.3"), objectMapper.createObjectNode());
  }

  private FeatureCollection openingFeatures(boolean withGeometry) {
    Feature feature = new Feature();
    if (withGeometry) {
      feature.setGeometry(new Point(-126.0, 50.0));
    }
    FeatureCollection collection = new FeatureCollection();
    collection.add(feature);
    return collection;
  }

  @Test
  @DisplayName("Returns the activity geometry with the opening geometry")
  void process_openingWithGeometry_returnsEverything() {
    when(openingRepository.existsById(OPENING_ID)).thenReturn(true);
    when(spatialFileService.processSpatialFile(eq("activity.geojson"), eq(FILE_BYTES), any()))
        .thenReturn(activity);
    when(openingGeometryService.getOpeningGeometry(OPENING_ID))
        .thenReturn(openingFeatures(true));

    ActivitySpatialUploadResponseDto response =
        service.process(OPENING_ID, "activity.geojson", FILE_BYTES);

    assertThat(response.geometryArea()).isEqualByComparingTo("4.3");
    assertThat(response.geoJson()).isSameAs(activity.geoJson());
    JsonNode openingGeoJson = response.openingGeoJson();
    assertThat(openingGeoJson.get("type").asText()).isEqualTo("FeatureCollection");
    assertThat(openingGeoJson.at("/features/0/geometry/type").asText()).isEqualTo("Point");
  }

  @Test
  @DisplayName("Returns no opening geometry when the opening has none")
  void process_openingWithoutGeometry_returnsNullGeometry() {
    when(openingRepository.existsById(OPENING_ID)).thenReturn(true);
    when(spatialFileService.processSpatialFile(eq("activity.geojson"), eq(FILE_BYTES), any()))
        .thenReturn(activity);
    when(openingGeometryService.getOpeningGeometry(OPENING_ID))
        .thenReturn(openingFeatures(false));

    ActivitySpatialUploadResponseDto response =
        service.process(OPENING_ID, "activity.geojson", FILE_BYTES);

    assertThat(response.openingGeoJson()).isNull();
  }

  @Test
  @DisplayName("Fails with not found before processing when the opening does not exist")
  void process_unknownOpening_notFound() {
    when(openingRepository.existsById(OPENING_ID)).thenReturn(false);

    assertThatThrownBy(() -> service.process(OPENING_ID, "activity.geojson", FILE_BYTES))
        .isInstanceOf(NotFoundGenericException.class);
    verifyNoInteractions(spatialFileService, geometryService, openingGeometryService);
  }

  @Test
  @DisplayName("Does not read the opening geometry when the file is invalid")
  void process_invalidFile_propagatesError() {
    when(openingRepository.existsById(OPENING_ID)).thenReturn(true);
    when(spatialFileService.processSpatialFile(any(), any(), any()))
        .thenThrow(new IllegalArgumentException("invalid"));

    assertThatThrownBy(() -> service.process(OPENING_ID, "bad.geojson", FILE_BYTES))
        .isInstanceOf(IllegalArgumentException.class);
    verifyNoInteractions(geometryService, openingGeometryService);
  }

  @Test
  @DisplayName("Rejects the file and skips the opening geometry when outside the opening")
  void process_outsideOpening_propagatesError() {
    when(openingRepository.existsById(OPENING_ID)).thenReturn(true);
    Geometry rawGeometry = new GeometryFactory().createPoint(new Coordinate(1, 1));
    // The check is handed to the file processing, which runs it on the geometry before thinning
    when(spatialFileService.processSpatialFile(eq("activity.geojson"), eq(FILE_BYTES), any()))
        .thenAnswer(
            invocation -> {
              Consumer<Geometry> check = invocation.getArgument(2);
              check.accept(rawGeometry);
              return activity;
            });
    doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "outside"))
        .when(geometryService)
        .validateWithinOpening(OPENING_ID, rawGeometry);

    assertThatThrownBy(() -> service.process(OPENING_ID, "activity.geojson", FILE_BYTES))
        .isInstanceOf(ResponseStatusException.class);
    verifyNoInteractions(openingGeometryService);
  }
}
