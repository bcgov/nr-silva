package ca.bc.gov.restapi.results.postgres.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.postgres.entity.activity.ActivityTreatmentGeometryEntity;
import ca.bc.gov.restapi.results.postgres.entity.activity.ActivityTreatmentUnitEntity;
import ca.bc.gov.restapi.results.postgres.repository.ActivityTreatmentGeometryPostgresRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | ActivityGeometryPostgresService")
class ActivityGeometryPostgresServiceTest {

  @Mock private ActivityTreatmentGeometryPostgresRepository repository;

  @Test
  @DisplayName("Maps unit and geometry to a BCGW-compatible feature in EPSG:4326")
  void getActivityGeometries_withRow_shouldReturnFeatureWithBcgwProperties() {
    ActivityTreatmentUnitEntity unit =
        ActivityTreatmentUnitEntity.builder()
            .id(55L)
            .openingId(100L)
            .silvBaseCode("PL")
            .atuCompletionDate(LocalDateTime.of(2024, 5, 20, 0, 0))
            .build();
    var point = new GeometryFactory().createPoint(new Coordinate(1235567.0, 517778.0));
    point.setSRID(3005);
    ActivityTreatmentGeometryEntity geometry =
        ActivityTreatmentGeometryEntity.builder()
            .activityTreatmentUnitId(55L)
            .geometry(point)
            .featureArea(new BigDecimal("1.2500"))
            .build();
    when(repository.findNonDisturbanceByOpeningId(100L))
        .thenReturn(Collections.singletonList(new Object[] {unit, geometry}));

    FeatureCollection fc =
        new ActivityGeometryPostgresService(repository, new ObjectMapper())
            .getActivityGeometries(100L);

    assertEquals(1, fc.getFeatures().size());
    Feature feature = fc.getFeatures().get(0);
    Map<String, Object> props = feature.getProperties();
    assertEquals("55", feature.getId());
    assertEquals(55L, props.get("ACTIVITY_TREATMENT_UNIT_ID"));
    assertEquals(100L, props.get("OPENING_ID"));
    assertEquals("PL", props.get("SILV_BASE_CODE"));
    assertEquals(new BigDecimal("1.2500"), props.get("ACTUAL_TREATMENT_AREA"));
    assertEquals("2024-05-20Z", props.get("ATU_COMPLETION_DATE"));
    assertNull(props.get("DISTURBANCE_CODE"));
    assertNull(props.get("OBJECTID"));
    assertEquals(4, feature.getBbox().length);
    assertTrue(feature.getBbox()[0] < 0, "longitude should be reprojected to EPSG:4326");
  }

  @Test
  @DisplayName("Returns an empty collection when the opening has no activity geometry")
  void getActivityGeometries_noRows_shouldReturnEmptyCollection() {
    when(repository.findNonDisturbanceByOpeningId(1L)).thenReturn(List.of());

    FeatureCollection fc =
        new ActivityGeometryPostgresService(repository, new ObjectMapper())
            .getActivityGeometries(1L);

    assertTrue(fc.getFeatures().isEmpty());
    assertNull(fc.getBbox());
  }
}
