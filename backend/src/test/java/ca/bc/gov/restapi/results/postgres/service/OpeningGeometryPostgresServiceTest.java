package ca.bc.gov.restapi.results.postgres.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.bc.gov.restapi.results.postgres.entity.OpeningGeometryEntity;
import ca.bc.gov.restapi.results.postgres.entity.OrgUnitEntity;
import ca.bc.gov.restapi.results.postgres.entity.opening.OpeningEntity;
import ca.bc.gov.restapi.results.postgres.repository.OpeningGeometryPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OrgUnitPostgresRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Optional;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit Test | OpeningGeometryPostgresService
 *
 * <p>Tests logic of geometry retrieval, reprojection, and GeoJSON conversion without database
 * context using mocked repositories.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | OpeningGeometryPostgresService")
class OpeningGeometryPostgresServiceTest {

  @Mock private OpeningGeometryPostgresRepository openingGeometryRepository;
  @Mock private OpeningPostgresRepository openingRepository;
  @Mock private OrgUnitPostgresRepository orgUnitRepository;
  @Mock private ObjectMapper objectMapper;

  private OpeningGeometryPostgresService service;
  private GeometryFactory geometryFactory;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    service =
        new OpeningGeometryPostgresService(
            openingGeometryRepository, openingRepository, orgUnitRepository, objectMapper);
    geometryFactory = new GeometryFactory();
  }

  // ============ HAPPY PATH ============

  @Test
  @DisplayName("Should return FeatureCollection with geometry when all entities present")
  void getOpeningGeometry_withAllEntitiesPresent_shouldReturnCompleteFeatureCollection()
      throws Exception {
    // Arrange
    Long openingId = 100L;
    LocalDateTime entryTime = LocalDateTime.of(2024, 1, 15, 10, 30, 0);

    OpeningGeometryEntity geometryEntity = mock(OpeningGeometryEntity.class);
    Point pointGeometry = geometryFactory.createPoint(new Coordinate(1235567.0, 517778.0)); // EPSG:3005
    pointGeometry.setSRID(3005);
    when(geometryEntity.getGeometry()).thenReturn(pointGeometry);

    OpeningEntity openingEntity = mock(OpeningEntity.class);
    when(openingEntity.getAdminDistrictNo()).thenReturn(1L);
    when(openingEntity.getEntryTimestamp()).thenReturn(entryTime);

    OrgUnitEntity districtEntity = mock(OrgUnitEntity.class);
    when(districtEntity.getOrgUnitCode()).thenReturn("DCT");
    when(districtEntity.getOrgUnitName()).thenReturn("District Name");
    when(districtEntity.getRollupRegionNo()).thenReturn(2L);
    when(districtEntity.getRollupRegionCode()).thenReturn("RGN");

    OrgUnitEntity regionEntity = mock(OrgUnitEntity.class);
    when(regionEntity.getOrgUnitName()).thenReturn("Region Name");

    when(openingGeometryRepository.findById(openingId)).thenReturn(Optional.of(geometryEntity));
    when(openingRepository.findById(openingId)).thenReturn(Optional.of(openingEntity));
    when(orgUnitRepository.findById(1L)).thenReturn(Optional.of(districtEntity));
    when(orgUnitRepository.findById(2L)).thenReturn(Optional.of(regionEntity));

    org.geojson.Point geoJsonPoint = new org.geojson.Point();
    when(objectMapper.readValue(anyString(), eq(GeoJsonObject.class)))
        .thenReturn(geoJsonPoint);

    // Act
    FeatureCollection result = service.getOpeningGeometry(openingId);

    // Assert
    assertNotNull(result, "FeatureCollection should not be null");
    assertEquals(1, result.getFeatures().size(), "FeatureCollection should contain 1 feature");

    Feature feature = result.getFeatures().get(0);
    assertEquals("100", feature.getId(), "Feature ID should match opening ID");
    assertNotNull(feature.getProperties(), "Feature properties should not be null");
    assertEquals(100L, feature.getProperties().get("OPENING_ID"), "OPENING_ID should match");
    assertEquals("DCT", feature.getProperties().get("DISTRICT_CODE"), "District code should match");
    assertEquals(
        "District Name", feature.getProperties().get("DISTRICT_NAME"), "District name should match");
    assertEquals("RGN", feature.getProperties().get("REGION_CODE"), "Region code should match");
    assertEquals(
        "Region Name", feature.getProperties().get("REGION_NAME"), "Region name should match");
    assertEquals(
        "2024-01-15Z",
        feature.getProperties().get("OPENING_WHEN_CREATED"),
        "Entry timestamp should be formatted as YYYY-MM-DDTZ");

    assertNotNull(feature.getGeometry(), "Feature geometry should not be null");
    assertNotNull(feature.getBbox(), "Feature bbox should not be null");
    assertEquals(4, feature.getBbox().length, "Bbox should have 4 elements [minX, minY, maxX, maxY]");

    assertNotNull(result.getCrs(), "FeatureCollection CRS should not be null");
  }

  // ============ MISSING ENTITIES ============

  @Test
  @DisplayName("Should return FeatureCollection with null geometry when geometry entity missing")
  void getOpeningGeometry_withMissingGeometryEntity_shouldReturnNullGeometry() throws Exception {
    // Arrange
    Long openingId = 100L;
    OpeningEntity openingEntity = mock(OpeningEntity.class);
    when(openingEntity.getAdminDistrictNo()).thenReturn(null);

    when(openingGeometryRepository.findById(openingId)).thenReturn(Optional.empty());
    when(openingRepository.findById(openingId)).thenReturn(Optional.of(openingEntity));

    // Act
    FeatureCollection result = service.getOpeningGeometry(openingId);

    // Assert
    assertNotNull(result, "FeatureCollection should not be null");
    assertEquals(1, result.getFeatures().size(), "Should contain 1 feature");

    Feature feature = result.getFeatures().get(0);
    assertNull(feature.getGeometry(), "Feature geometry should be null when geometry entity missing");
    assertNull(feature.getBbox(), "Feature bbox should be null when geometry entity missing");
  }

  @Test
  @DisplayName("Should return FeatureCollection with null properties when opening entity missing")
  void getOpeningGeometry_withMissingOpeningEntity_shouldReturnNullProperties()
      throws Exception {
    // Arrange
    Long openingId = 100L;
    when(openingGeometryRepository.findById(openingId)).thenReturn(Optional.empty());
    when(openingRepository.findById(openingId)).thenReturn(Optional.empty());

    // Act
    FeatureCollection result = service.getOpeningGeometry(openingId);

    // Assert
    assertNotNull(result, "FeatureCollection should not be null");
    assertEquals(1, result.getFeatures().size(), "Should contain 1 feature");

    Feature feature = result.getFeatures().get(0);
    assertNull(feature.getProperties().get("OPENING_WHEN_CREATED"),
        "OPENING_WHEN_CREATED should be null");
    assertNull(
        feature.getProperties().get("REGION_CODE"),
        "REGION_CODE should be null when opening missing");
    assertNull(
        feature.getProperties().get("DISTRICT_CODE"),
        "DISTRICT_CODE should be null when opening missing");
  }

  @Test
  @DisplayName("Should skip org unit queries when admin district No is null")
  void getOpeningGeometry_withNullAdminDistrictNo_shouldSkipOrgUnitLookup() throws Exception {
    // Arrange
    Long openingId = 100L;
    OpeningEntity openingEntity = mock(OpeningEntity.class);
    when(openingEntity.getAdminDistrictNo()).thenReturn(null);

    when(openingGeometryRepository.findById(openingId)).thenReturn(Optional.empty());
    when(openingRepository.findById(openingId)).thenReturn(Optional.of(openingEntity));

    // Act
    FeatureCollection result = service.getOpeningGeometry(openingId);

    // Assert
    assertNotNull(result, "FeatureCollection should not be null");
    verify(orgUnitRepository, never().description("Should not call orgUnitRepository"))
        .findById(anyLong());
  }

  @Test
  @DisplayName("Should handle missing district entity gracefully")
  void getOpeningGeometry_withMissingDistrictEntity_shouldHandleGracefully() throws Exception {
    // Arrange
    Long openingId = 100L;
    OpeningEntity openingEntity = mock(OpeningEntity.class);
    when(openingEntity.getAdminDistrictNo()).thenReturn(1L);

    when(openingGeometryRepository.findById(openingId)).thenReturn(Optional.empty());
    when(openingRepository.findById(openingId)).thenReturn(Optional.of(openingEntity));
    when(orgUnitRepository.findById(1L)).thenReturn(Optional.empty());

    // Act
    FeatureCollection result = service.getOpeningGeometry(openingId);

    // Assert
    assertNotNull(result, "FeatureCollection should not be null");
    Feature feature = result.getFeatures().get(0);
    assertNull(
        feature.getProperties().get("DISTRICT_CODE"),
        "DISTRICT_CODE should be null when district missing");
    assertNull(
        feature.getProperties().get("REGION_CODE"),
        "REGION_CODE should be null when district missing");
  }

  @Test
  @DisplayName("Should handle missing region entity gracefully")
  void getOpeningGeometry_withMissingRegionEntity_shouldHandleGracefully() throws Exception {
    // Arrange
    Long openingId = 100L;
    OpeningEntity openingEntity = mock(OpeningEntity.class);
    when(openingEntity.getAdminDistrictNo()).thenReturn(1L);

    OrgUnitEntity districtEntity = mock(OrgUnitEntity.class);
    when(districtEntity.getOrgUnitCode()).thenReturn("DCT");
    when(districtEntity.getOrgUnitName()).thenReturn("District Name");
    when(districtEntity.getRollupRegionNo()).thenReturn(2L);

    when(openingGeometryRepository.findById(openingId)).thenReturn(Optional.empty());
    when(openingRepository.findById(openingId)).thenReturn(Optional.of(openingEntity));
    when(orgUnitRepository.findById(1L)).thenReturn(Optional.of(districtEntity));
    when(orgUnitRepository.findById(2L)).thenReturn(Optional.empty());

    // Act
    FeatureCollection result = service.getOpeningGeometry(openingId);

    // Assert
    assertNotNull(result, "FeatureCollection should not be null");
    Feature feature = result.getFeatures().get(0);
    assertEquals("DCT", feature.getProperties().get("DISTRICT_CODE"),
        "District code should be populated");
    assertNull(
        feature.getProperties().get("REGION_CODE"),
        "REGION_CODE should be null when region missing");
  }

  // ============ NULL FIELDS ============

  @Test
  @DisplayName("Should set null OPENING_WHEN_CREATED when entry timestamp is null")
  void getOpeningGeometry_withNullEntryTimestamp_shouldSetNullCreatedTime() throws Exception {
    // Arrange
    Long openingId = 100L;
    OpeningEntity openingEntity = mock(OpeningEntity.class);
    when(openingEntity.getAdminDistrictNo()).thenReturn(null);
    when(openingEntity.getEntryTimestamp()).thenReturn(null);

    when(openingGeometryRepository.findById(openingId)).thenReturn(Optional.empty());
    when(openingRepository.findById(openingId)).thenReturn(Optional.of(openingEntity));

    // Act
    FeatureCollection result = service.getOpeningGeometry(openingId);

    // Assert
    Feature feature = result.getFeatures().get(0);
    assertNull(
        feature.getProperties().get("OPENING_WHEN_CREATED"),
        "OPENING_WHEN_CREATED should be null");
  }

  // ============ PROPERTIES MAP STRUCTURE ============

  @Test
  @DisplayName("Should include all required properties with correct keys")
  void getOpeningGeometry_shouldIncludeAllRequiredPropertyKeys() throws Exception {
    // Arrange
    Long openingId = 100L;
    OpeningEntity openingEntity = mock(OpeningEntity.class);
    when(openingEntity.getAdminDistrictNo()).thenReturn(null);

    when(openingGeometryRepository.findById(openingId)).thenReturn(Optional.empty());
    when(openingRepository.findById(openingId)).thenReturn(Optional.of(openingEntity));

    // Act
    FeatureCollection result = service.getOpeningGeometry(openingId);

    // Assert
    Feature feature = result.getFeatures().get(0);
    assertNotNull(feature.getProperties());
    assertTrue(
        feature.getProperties().containsKey("OPENING_ID"),
        "Should contain OPENING_ID key");
    assertTrue(
        feature.getProperties().containsKey("OPENING_CATEGORY_CODE"),
        "Should contain OPENING_CATEGORY_CODE key");
    assertTrue(
        feature.getProperties().containsKey("OPENING_STATUS_CODE"),
        "Should contain OPENING_STATUS_CODE key");
    assertTrue(
        feature.getProperties().containsKey("REGION_CODE"),
        "Should contain REGION_CODE key");
    assertTrue(
        feature.getProperties().containsKey("REGION_NAME"),
        "Should contain REGION_NAME key");
    assertTrue(
        feature.getProperties().containsKey("DISTRICT_CODE"),
        "Should contain DISTRICT_CODE key");
    assertTrue(
        feature.getProperties().containsKey("DISTRICT_NAME"),
        "Should contain DISTRICT_NAME key");
    assertTrue(
        feature.getProperties().containsKey("OPENING_WHO_CREATED"),
        "Should contain OPENING_WHO_CREATED key");
    assertTrue(
        feature.getProperties().containsKey("OPENING_WHEN_CREATED"),
        "Should contain OPENING_WHEN_CREATED key");
    assertTrue(
        feature.getProperties().containsKey("OPENING_WHO_UPDATED"),
        "Should contain OPENING_WHO_UPDATED key");
    assertTrue(
        feature.getProperties().containsKey("OPENING_WHEN_UPDATED"),
        "Should contain OPENING_WHEN_UPDATED key");
    assertTrue(
        feature.getProperties().containsKey("OBJECTID"),
        "Should contain OBJECTID key");
  }

  // ============ DATE FORMATTING ============

  @Test
  @DisplayName("Should format entry timestamp as yyyy-MM-dd'Z'")
  void getOpeningGeometry_shouldFormatTimestampCorrectly() throws Exception {
    // Arrange
    Long openingId = 100L;
    LocalDateTime entryTime = LocalDateTime.of(2024, 12, 25, 14, 30, 45);
    OpeningEntity openingEntity = mock(OpeningEntity.class);
    when(openingEntity.getAdminDistrictNo()).thenReturn(null);
    when(openingEntity.getEntryTimestamp()).thenReturn(entryTime);

    when(openingGeometryRepository.findById(openingId)).thenReturn(Optional.empty());
    when(openingRepository.findById(openingId)).thenReturn(Optional.of(openingEntity));

    // Act
    FeatureCollection result = service.getOpeningGeometry(openingId);

    // Assert
    Feature feature = result.getFeatures().get(0);
    assertEquals(
        "2024-12-25Z",
        feature.getProperties().get("OPENING_WHEN_CREATED"),
        "Timestamp should be formatted as yyyy-MM-dd'Z' (time portion omitted)");
  }

  // ============ GEOMETRY HANDLING ============

  @Test
  @DisplayName("Should handle null geometry in entity gracefully")
  void getOpeningGeometry_withNullGeometryInEntity_shouldHandleGracefully() throws Exception {
    // Arrange
    Long openingId = 100L;
    OpeningGeometryEntity geometryEntity = mock(OpeningGeometryEntity.class);
    when(geometryEntity.getGeometry()).thenReturn(null);

    OpeningEntity openingEntity = mock(OpeningEntity.class);
    when(openingEntity.getAdminDistrictNo()).thenReturn(null);

    when(openingGeometryRepository.findById(openingId)).thenReturn(Optional.of(geometryEntity));
    when(openingRepository.findById(openingId)).thenReturn(Optional.of(openingEntity));

    // Act
    FeatureCollection result = service.getOpeningGeometry(openingId);

    // Assert
    assertNotNull(result, "FeatureCollection should not be null");
    Feature feature = result.getFeatures().get(0);
    assertNull(feature.getGeometry(), "Feature geometry should be null");
  }

  // ============ BBOX CALCULATION ============

  @Test
  @DisplayName("Should calculate bbox correctly from geometry envelope")
  void getOpeningGeometry_shouldCalculateBboxCorrectly() throws Exception {
    // Arrange
    Long openingId = 100L;
    LocalDateTime entryTime = LocalDateTime.of(2024, 1, 15, 10, 30, 0);

    // Create a simple point geometry
    Point pointGeometry = geometryFactory.createPoint(new Coordinate(1000.0, 2000.0));
    pointGeometry.setSRID(3005);

    OpeningGeometryEntity geometryEntity = mock(OpeningGeometryEntity.class);
    when(geometryEntity.getGeometry()).thenReturn(pointGeometry);

    OpeningEntity openingEntity = mock(OpeningEntity.class);
    when(openingEntity.getAdminDistrictNo()).thenReturn(null);
    when(openingEntity.getEntryTimestamp()).thenReturn(entryTime);

    when(openingGeometryRepository.findById(openingId)).thenReturn(Optional.of(geometryEntity));
    when(openingRepository.findById(openingId)).thenReturn(Optional.of(openingEntity));

    org.geojson.Point geoJsonPoint = new org.geojson.Point();
    when(objectMapper.readValue(anyString(), eq(GeoJsonObject.class)))
        .thenReturn(geoJsonPoint);

    // Act
    FeatureCollection result = service.getOpeningGeometry(openingId);

    // Assert
    Feature feature = result.getFeatures().get(0);
    double[] bbox = feature.getBbox();
    assertNotNull(bbox, "Bbox should not be null");
    assertEquals(4, bbox.length, "Bbox should have 4 elements");
    // For a point, min and max should be the same
    assertEquals(bbox[0], bbox[2], "Min X should equal max X for point");
    assertEquals(bbox[1], bbox[3], "Min Y should equal max Y for point");
  }

  // ============ CRS VALIDATION ============

  @Test
  @DisplayName("Should set correct CRS URN in FeatureCollection")
  void getOpeningGeometry_shouldSetCorrectCrsUrn() throws Exception {
    // Arrange
    Long openingId = 100L;
    OpeningEntity openingEntity = mock(OpeningEntity.class);
    when(openingEntity.getAdminDistrictNo()).thenReturn(null);

    when(openingGeometryRepository.findById(openingId)).thenReturn(Optional.empty());
    when(openingRepository.findById(openingId)).thenReturn(Optional.of(openingEntity));

    // Act
    FeatureCollection result = service.getOpeningGeometry(openingId);

    // Assert
    assertNotNull(result.getCrs(), "CRS should not be null");
    assertEquals(
        "urn:ogc:def:crs:EPSG::4326",
        result.getCrs().getProperties().get("name"),
        "CRS URN should be correct");
  }

  // ============ FEATURE ID ============

  @Test
  @DisplayName("Should set feature ID as string of opening ID")
  void getOpeningGeometry_shouldSetFeatureIdAsStringOfOpeningId() throws Exception {
    // Arrange
    Long openingId = 12345L;
    OpeningEntity openingEntity = mock(OpeningEntity.class);
    when(openingEntity.getAdminDistrictNo()).thenReturn(null);

    when(openingGeometryRepository.findById(openingId)).thenReturn(Optional.empty());
    when(openingRepository.findById(openingId)).thenReturn(Optional.of(openingEntity));

    // Act
    FeatureCollection result = service.getOpeningGeometry(openingId);

    // Assert
    Feature feature = result.getFeatures().get(0);
    assertEquals("12345", feature.getId(), "Feature ID should be string of opening ID");
  }

  // ============ REPOSITORY VERIFICATION ============

  @Test
  @DisplayName("Should call all three repositories")
  void getOpeningGeometry_shouldCallAllRepositories() throws Exception {
    // Arrange
    Long openingId = 100L;
    when(openingGeometryRepository.findById(openingId)).thenReturn(Optional.empty());
    when(openingRepository.findById(openingId)).thenReturn(Optional.empty());

    // Act
    service.getOpeningGeometry(openingId);

    // Assert
    verify(openingGeometryRepository, times(1)).findById(openingId);
    verify(openingRepository, times(1)).findById(openingId);
  }

  @Test
  @DisplayName("Should call org unit repository twice when district and region both needed")
  void getOpeningGeometry_shouldCallOrgUnitRepositoryTwiceForDistrictAndRegion()
      throws Exception {
    // Arrange
    Long openingId = 100L;
    OpeningEntity openingEntity = mock(OpeningEntity.class);
    when(openingEntity.getAdminDistrictNo()).thenReturn(1L);

    OrgUnitEntity districtEntity = mock(OrgUnitEntity.class);
    when(districtEntity.getRollupRegionNo()).thenReturn(2L);

    when(openingGeometryRepository.findById(openingId)).thenReturn(Optional.empty());
    when(openingRepository.findById(openingId)).thenReturn(Optional.of(openingEntity));
    when(orgUnitRepository.findById(1L)).thenReturn(Optional.of(districtEntity));
    when(orgUnitRepository.findById(2L)).thenReturn(Optional.empty());

    // Act
    service.getOpeningGeometry(openingId);

    // Assert
    verify(orgUnitRepository, times(2)).findById(anyLong());
    verify(orgUnitRepository).findById(1L); // District lookup
    verify(orgUnitRepository).findById(2L); // Region lookup
  }
}
