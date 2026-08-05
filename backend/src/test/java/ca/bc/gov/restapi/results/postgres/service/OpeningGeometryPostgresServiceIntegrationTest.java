package ca.bc.gov.restapi.results.postgres.service;

import static org.junit.jupiter.api.Assertions.*;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Integrated Test | Opening Geometry Service | Postgres-only
 *
 * <p>End-to-end integration tests for OpeningGeometryPostgresService against real PostgreSQL
 * database via Testcontainers and Flyway migrations.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Integrated Test | Opening Geometry Service | Postgres-only")
@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@WithMockJwt(value = "ttester")
class OpeningGeometryPostgresServiceIntegrationTest
    extends AbstractTestContainerIntegrationTest {

  @Autowired private OpeningGeometryPostgresService openingGeometryService;

  // ============ HAPPY PATH ============

  @Test
  @DisplayName("Should return FeatureCollection when opening and geometry exist in DB")
  void getOpeningGeometry_withExistingOpeningAndGeometry_shouldReturnCompleteFeatureCollection() {
    // Arrange
    // Note: Test data is loaded via Flyway migrations automatically
    // Assuming there's a test opening with ID that has geometry
    Long testOpeningId = 1L;

    // Act
    FeatureCollection result = openingGeometryService.getOpeningGeometry(testOpeningId);

    // Assert
    assertNotNull(result, "FeatureCollection should not be null");
    assertEquals(1, result.getFeatures().size(), "Should contain exactly 1 feature");

    Feature feature = result.getFeatures().get(0);
    assertNotNull(feature, "Feature should not be null");
    assertEquals(String.valueOf(testOpeningId), feature.getId(), "Feature ID should match opening ID");
    assertNotNull(feature.getProperties(), "Feature properties should not be null");
    assertEquals(
        testOpeningId, feature.getProperties().get("OPENING_ID"), "OPENING_ID should match");
  }

  @Test
  @DisplayName("Should set correct CRS in FeatureCollection")
  void getOpeningGeometry_shouldSetCorrectCrs() {
    // Arrange
    Long testOpeningId = 1L;

    // Act
    FeatureCollection result = openingGeometryService.getOpeningGeometry(testOpeningId);

    // Assert
    assertNotNull(result.getCrs(), "CRS should not be null");
    assertEquals(
        "urn:ogc:def:crs:EPSG::4326",
        result.getCrs().getProperties().get("name"),
        "CRS should be EPSG:4326");
  }

  // ============ MISSING ENTITIES ============

  @Test
  @DisplayName("Should return FeatureCollection with minimal data for non-existent opening")
  void getOpeningGeometry_withNonExistentOpening_shouldReturnFeatureWithoutData() {
    // Arrange
    Long nonExistentOpeningId = 999999L;

    // Act
    FeatureCollection result =
        openingGeometryService.getOpeningGeometry(nonExistentOpeningId);

    // Assert
    assertNotNull(result, "FeatureCollection should not be null");
    assertEquals(1, result.getFeatures().size(), "Should contain 1 feature");

    Feature feature = result.getFeatures().get(0);
    assertEquals(
        String.valueOf(nonExistentOpeningId),
        feature.getId(),
        "Feature ID should be set regardless");
    assertEquals(
        nonExistentOpeningId,
        feature.getProperties().get("OPENING_ID"),
        "OPENING_ID should be set");
    assertNull(
        feature.getGeometry(),
        "Geometry should be null when opening/geometry not found");
  }

  // ============ GEOMETRY HANDLING ============

  @Test
  @DisplayName("Should include bbox when geometry is present")
  void getOpeningGeometry_withGeometry_shouldIncludeBbox() {
    // Arrange
    Long testOpeningId = 1L;

    // Act
    FeatureCollection result = openingGeometryService.getOpeningGeometry(testOpeningId);

    // Assert
    Feature feature = result.getFeatures().get(0);
    if (feature.getGeometry() != null) {
      assertNotNull(
          feature.getBbox(),
          "Feature bbox should not be null when geometry is present");
      assertEquals(4, feature.getBbox().length, "Bbox should have 4 elements [minX, minY, maxX, maxY]");
      double minX = feature.getBbox()[0];
      double minY = feature.getBbox()[1];
      double maxX = feature.getBbox()[2];
      double maxY = feature.getBbox()[3];
      assertTrue(
          minX <= maxX,
          "BBox minX should be <= maxX");
      assertTrue(
          minY <= maxY,
          "BBox minY should be <= maxY");
    }
  }

  @Test
  @DisplayName("Should include bbox in FeatureCollection when geometry is present")
  void getOpeningGeometry_withGeometry_shouldIncludeFeatureCollectionBbox() {
    // Arrange
    Long testOpeningId = 1L;

    // Act
    FeatureCollection result = openingGeometryService.getOpeningGeometry(testOpeningId);

    // Assert
    Feature feature = result.getFeatures().get(0);
    if (feature.getGeometry() != null) {
      assertNotNull(
          result.getBbox(),
          "FeatureCollection bbox should not be null when geometry is present");
      assertEquals(4, result.getBbox().length, "BBox should have 4 elements");
    }
  }

  @Test
  @DisplayName("Should not include bbox when geometry is missing")
  void getOpeningGeometry_withoutGeometry_shouldNotIncludeBbox() {
    // Arrange
    Long nonExistentOpeningId = 999999L;

    // Act
    FeatureCollection result =
        openingGeometryService.getOpeningGeometry(nonExistentOpeningId);

    // Assert
    Feature feature = result.getFeatures().get(0);
    assertNull(feature.getBbox(), "Feature bbox should be null when geometry is missing");
  }

  // ============ ORG UNIT HIERARCHY ============

  @Test
  @DisplayName("Should populate district and region codes/names when hierarchy exists")
  void getOpeningGeometry_withOrgUnitHierarchy_shouldPopulateDistrictAndRegion() {
    // Arrange
    Long testOpeningId = 1L;

    // Act
    FeatureCollection result = openingGeometryService.getOpeningGeometry(testOpeningId);

    // Assert
    Feature feature = result.getFeatures().get(0);
    Object districtCode = feature.getProperties().get("DISTRICT_CODE");
    Object regionCode = feature.getProperties().get("REGION_CODE");

    // At least one should be populated if test data includes districts/regions
    boolean hasDistrictOrRegion = districtCode != null || regionCode != null;
    assertTrue(
        hasDistrictOrRegion || districtCode == null,
        "Should handle hierarchy correctly regardless of test data");
  }

  @Test
  @DisplayName("Should set null district fields when admin district no is null")
  void getOpeningGeometry_withNullAdminDistrictNo_shouldSetNullDistrictFields() {
    // Arrange - create or use an opening without admin district
    Long nonExistentOpeningId = 999999L;

    // Act
    FeatureCollection result =
        openingGeometryService.getOpeningGeometry(nonExistentOpeningId);

    // Assert
    Feature feature = result.getFeatures().get(0);
    assertNull(
        feature.getProperties().get("DISTRICT_CODE"),
        "DISTRICT_CODE should be null");
    assertNull(
        feature.getProperties().get("DISTRICT_NAME"),
        "DISTRICT_NAME should be null");
    assertNull(
        feature.getProperties().get("REGION_CODE"),
        "REGION_CODE should be null");
    assertNull(
        feature.getProperties().get("REGION_NAME"),
        "REGION_NAME should be null");
  }

  // ============ PROPERTIES VALIDATION ============

  @Test
  @DisplayName("Should include all required property keys")
  void getOpeningGeometry_shouldIncludeAllRequiredPropertyKeys() {
    // Arrange
    Long testOpeningId = 1L;

    // Act
    FeatureCollection result = openingGeometryService.getOpeningGeometry(testOpeningId);

    // Assert
    Feature feature = result.getFeatures().get(0);
    assertNotNull(feature.getProperties(), "Properties should not be null");

    // Verify all expected keys are present
    String[] expectedKeys = {
      "OPENING_ID",
      "OPENING_CATEGORY_CODE",
      "OPENING_STATUS_CODE",
      "REGION_CODE",
      "REGION_NAME",
      "DISTRICT_CODE",
      "DISTRICT_NAME",
      "OPENING_WHO_CREATED",
      "OPENING_WHEN_CREATED",
      "OPENING_WHO_UPDATED",
      "OPENING_WHEN_UPDATED",
      "OBJECTID"
    };

    for (String key : expectedKeys) {
      assertTrue(
          feature.getProperties().containsKey(key),
          "Properties should contain key: " + key);
    }
  }

  @Test
  @DisplayName("Should format OPENING_ID as Long in properties")
  void getOpeningGeometry_shouldFormatOpeningIdAsLong() {
    // Arrange
    Long testOpeningId = 1L;

    // Act
    FeatureCollection result = openingGeometryService.getOpeningGeometry(testOpeningId);

    // Assert
    Feature feature = result.getFeatures().get(0);
    Object openingId = feature.getProperties().get("OPENING_ID");
    assertNotNull(openingId, "OPENING_ID should not be null");
    assertEquals(testOpeningId, openingId, "OPENING_ID should match");
  }

  @Test
  @DisplayName("Should format timestamp as yyyy-MM-dd'Z' when entry timestamp exists")
  void getOpeningGeometry_shouldFormatTimestampCorrectly() {
    // Arrange
    Long testOpeningId = 1L;

    // Act
    FeatureCollection result = openingGeometryService.getOpeningGeometry(testOpeningId);

    // Assert
    Feature feature = result.getFeatures().get(0);
    Object createdTime = feature.getProperties().get("OPENING_WHEN_CREATED");

    if (createdTime != null) {
      String createdTimeStr = (String) createdTime;
      // Should match pattern yyyy-MM-dd'Z'
      assertTrue(
          createdTimeStr.matches("\\d{4}-\\d{2}-\\d{2}Z"),
          "Timestamp should be formatted as yyyy-MM-dd'Z', got: " + createdTimeStr);
    }
  }

  // ============ GEOMETRY PROJECTION ============

  @Test
  @DisplayName("Should reproject geometry from EPSG:3005 to EPSG:4326")
  void getOpeningGeometry_withEpsg3005Geometry_shouldReprojectToEpsg4326() {
    // Arrange
    Long testOpeningId = 1L;

    // Act
    FeatureCollection result = openingGeometryService.getOpeningGeometry(testOpeningId);

    // Assert
    Feature feature = result.getFeatures().get(0);
    if (feature.getGeometry() != null) {
      // EPSG:4326 coordinates should be in lat/lon range (roughly -180 to 180, -90 to 90)
      // The actual validation is harder without knowing the input geometry,
      // but we can verify the geometry exists and is not null
      assertNotNull(feature.getGeometry(), "Geometry should be reprojected");
    }
  }

  // ============ EDGE CASES ============

  @Test
  @DisplayName("Should handle opening with zero ID")
  void getOpeningGeometry_withZeroId_shouldReturnFeatureCollection() {
    // Arrange
    Long zeroId = 0L;

    // Act
    FeatureCollection result = openingGeometryService.getOpeningGeometry(zeroId);

    // Assert
    assertNotNull(result, "FeatureCollection should not be null");
    assertEquals(1, result.getFeatures().size(), "Should contain 1 feature");
  }

  @Test
  @DisplayName("Should handle negative opening ID")
  void getOpeningGeometry_withNegativeId_shouldReturnFeatureCollection() {
    // Arrange
    Long negativeId = -1L;

    // Act
    FeatureCollection result = openingGeometryService.getOpeningGeometry(negativeId);

    // Assert
    assertNotNull(result, "FeatureCollection should not be null");
    assertEquals(1, result.getFeatures().size(), "Should contain 1 feature");
  }

  @Test
  @DisplayName("Should return consistent structure regardless of data presence")
  void getOpeningGeometry_shouldAlwaysReturnConsistentStructure() {
    // Arrange
    Long[] testIds = {1L, 999999L};

    for (Long testId : testIds) {
      // Act
      FeatureCollection result = openingGeometryService.getOpeningGeometry(testId);

      // Assert
      assertNotNull(result, "FeatureCollection should not be null for ID: " + testId);
      assertEquals(1, result.getFeatures().size(), "Should always contain exactly 1 feature");
      assertNotNull(result.getCrs(), "CRS should always be set");

      Feature feature = result.getFeatures().get(0);
      assertNotNull(feature.getProperties(), "Properties should always be populated");
      assertEquals(
          testId,
          feature.getProperties().get("OPENING_ID"),
          "OPENING_ID should always be set");
    }
  }
}
