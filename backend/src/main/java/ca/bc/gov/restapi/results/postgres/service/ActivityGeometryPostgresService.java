package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.service.ActivityGeometryReadService;
import ca.bc.gov.restapi.results.postgres.entity.activity.ActivityTreatmentGeometryEntity;
import ca.bc.gov.restapi.results.postgres.entity.activity.ActivityTreatmentUnitEntity;
import ca.bc.gov.restapi.results.postgres.repository.ActivityTreatmentGeometryPostgresRepository;
import ca.bc.gov.restapi.results.postgres.util.GeometryReprojectionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geojson.Crs;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;
import org.geojson.jackson.CrsType;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.geojson.GeoJsonWriter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Reads activity geometry from the local postgres DB; used in postgres-only mode. Features carry
 * the same properties as the BCGW activity treatment layer.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class ActivityGeometryPostgresService implements ActivityGeometryReadService {

  private static final DateTimeFormatter DATE_Z_FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'Z'");
  private static final String CRS_URN = "urn:ogc:def:crs:EPSG::4326";

  private final ActivityTreatmentGeometryPostgresRepository geometryRepository;
  private final ObjectMapper objectMapper;

  @Override
  public FeatureCollection getActivityGeometries(Long openingId) {
    FeatureCollection fc = new FeatureCollection();
    Crs crs = new Crs();
    crs.setType(CrsType.name);
    crs.getProperties().put("name", CRS_URN);
    fc.setCrs(crs);

    Envelope total = new Envelope();
    for (Object[] row : geometryRepository.findNonDisturbanceByOpeningId(openingId)) {
      ActivityTreatmentUnitEntity unit = (ActivityTreatmentUnitEntity) row[0];
      ActivityTreatmentGeometryEntity geometryEntity = (ActivityTreatmentGeometryEntity) row[1];

      Geometry geom4326 = GeometryReprojectionUtils.to4326(geometryEntity.getGeometry());
      Feature feature = new Feature();
      feature.setId(String.valueOf(unit.getId()));
      feature.setProperties(buildProperties(unit, geometryEntity));
      feature.setGeometry(convertToGeoJson(geom4326));

      Envelope env = geom4326.getEnvelopeInternal();
      feature.setBbox(new double[] {env.getMinX(), env.getMinY(), env.getMaxX(), env.getMaxY()});
      total.expandToInclude(env);
      fc.add(feature);
    }
    if (!total.isNull()) {
      fc.setBbox(new double[] {total.getMinX(), total.getMinY(), total.getMaxX(), total.getMaxY()});
    }
    return fc;
  }

  private Map<String, Object> buildProperties(
      ActivityTreatmentUnitEntity unit, ActivityTreatmentGeometryEntity geometry) {
    Map<String, Object> props = new LinkedHashMap<>();
    props.put("OPENING_ID", unit.getOpeningId());
    props.put("ACTIVITY_TREATMENT_UNIT_ID", unit.getId());
    props.put("SILV_BASE_CODE", unit.getSilvBaseCode());
    props.put("DISTURBANCE_CODE", null);
    props.put("ACTUAL_TREATMENT_AREA", geometry.getFeatureArea());
    props.put(
        "ATU_COMPLETION_DATE",
        unit.getAtuCompletionDate() != null
            ? unit.getAtuCompletionDate().format(DATE_Z_FORMAT)
            : null);
    props.put("OBJECTID", null); // BCGW-internal identifier, not stored in postgres
    return props;
  }

  private GeoJsonObject convertToGeoJson(Geometry jtsGeometry) {
    try {
      return objectMapper.readValue(new GeoJsonWriter().write(jtsGeometry), GeoJsonObject.class);
    } catch (Exception e) {
      log.error("Failed to convert JTS geometry to GeoJSON: {}", e.getMessage());
      return null;
    }
  }
}
