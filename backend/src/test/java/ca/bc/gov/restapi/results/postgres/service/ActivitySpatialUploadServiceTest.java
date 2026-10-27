package ca.bc.gov.restapi.results.postgres.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.exception.NotFoundGenericException;
import ca.bc.gov.restapi.results.common.service.OpeningGeometryService;
import ca.bc.gov.restapi.results.postgres.dto.ActivitySpatialUploadResponseDto;
import ca.bc.gov.restapi.results.postgres.dto.ExtractedGeoDataDto;
import ca.bc.gov.restapi.results.postgres.entity.opening.OpeningEntity;
import ca.bc.gov.restapi.results.postgres.repository.OpeningPostgresRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Optional;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | ActivitySpatialUploadService")
class ActivitySpatialUploadServiceTest {

  private static final Long OPENING_ID = 1001L;
  private static final byte[] FILE_BYTES = {1, 2, 3};

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Mock private SpatialFileService spatialFileService;
  @Mock private OpeningPostgresRepository openingRepository;
  @Mock private OpeningGrossAreaResolver grossAreaResolver;
  @Mock private OpeningGeometryService openingGeometryService;

  private ActivitySpatialUploadService service;
  private OpeningEntity opening;
  private ExtractedGeoDataDto activity;

  @BeforeEach
  void setUp() {
    service =
        new ActivitySpatialUploadService(
            spatialFileService,
            openingRepository,
            grossAreaResolver,
            openingGeometryService,
            objectMapper);
    opening = OpeningEntity.builder().id(OPENING_ID).build();
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
  @DisplayName("Returns the activity geometry with the opening gross area and geometry")
  void process_openingWithGeometry_returnsEverything() {
    when(openingRepository.findById(OPENING_ID)).thenReturn(Optional.of(opening));
    when(spatialFileService.processSpatialFile("activity.geojson", FILE_BYTES))
        .thenReturn(activity);
    when(grossAreaResolver.resolve(opening)).thenReturn(new BigDecimal("79.4"));
    when(openingGeometryService.getOpeningGeometry(OPENING_ID))
        .thenReturn(openingFeatures(true));

    ActivitySpatialUploadResponseDto response =
        service.process(OPENING_ID, "activity.geojson", FILE_BYTES);

    assertThat(response.geometryArea()).isEqualByComparingTo("4.3");
    assertThat(response.geoJson()).isSameAs(activity.geoJson());
    assertThat(response.openingGrossArea()).isEqualByComparingTo("79.4");
    JsonNode openingGeoJson = response.openingGeoJson();
    assertThat(openingGeoJson.get("type").asText()).isEqualTo("FeatureCollection");
    assertThat(openingGeoJson.at("/features/0/geometry/type").asText()).isEqualTo("Point");
  }

  @Test
  @DisplayName("Returns no opening geometry when the opening has none")
  void process_openingWithoutGeometry_returnsNullGeometry() {
    when(openingRepository.findById(OPENING_ID)).thenReturn(Optional.of(opening));
    when(spatialFileService.processSpatialFile("activity.geojson", FILE_BYTES))
        .thenReturn(activity);
    when(openingGeometryService.getOpeningGeometry(OPENING_ID))
        .thenReturn(openingFeatures(false));

    ActivitySpatialUploadResponseDto response =
        service.process(OPENING_ID, "activity.geojson", FILE_BYTES);

    assertThat(response.openingGeoJson()).isNull();
    assertThat(response.openingGrossArea()).isNull();
  }

  @Test
  @DisplayName("Fails with not found before processing when the opening does not exist")
  void process_unknownOpening_notFound() {
    when(openingRepository.findById(OPENING_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.process(OPENING_ID, "activity.geojson", FILE_BYTES))
        .isInstanceOf(NotFoundGenericException.class);
    verifyNoInteractions(spatialFileService, openingGeometryService);
  }

  @Test
  @DisplayName("Does not read the opening geometry when the file is invalid")
  void process_invalidFile_propagatesError() {
    when(openingRepository.findById(OPENING_ID)).thenReturn(Optional.of(opening));
    when(spatialFileService.processSpatialFile(any(), any()))
        .thenThrow(new IllegalArgumentException("invalid"));

    assertThatThrownBy(() -> service.process(OPENING_ID, "bad.geojson", FILE_BYTES))
        .isInstanceOf(IllegalArgumentException.class);
    verifyNoInteractions(openingGeometryService);
  }
}
