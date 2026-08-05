package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.service.OpeningGeometryService;
import ca.bc.gov.restapi.results.postgres.entity.OpeningGeometryEntity;
import ca.bc.gov.restapi.results.postgres.entity.OrgUnitEntity;
import ca.bc.gov.restapi.results.postgres.entity.opening.OpeningEntity;
import ca.bc.gov.restapi.results.postgres.repository.OpeningGeometryPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OrgUnitPostgresRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geojson.Crs;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;
import org.geojson.jackson.CrsType;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.geojson.GeoJsonWriter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/** Reads opening geometry from the local postgres DB; used in postgres-only mode. */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class OpeningGeometryPostgresService implements OpeningGeometryService {

  private static final DateTimeFormatter DATE_Z_FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'Z'");
  private static final String CRS_URN = "urn:ogc:def:crs:EPSG::4326";

  private final OpeningGeometryPostgresRepository openingGeometryRepository;
  private final OpeningPostgresRepository openingRepository;
  private final OrgUnitPostgresRepository orgUnitRepository;
  private final ObjectMapper objectMapper;

  @Override
  public FeatureCollection getOpeningGeometry(Long openingId) {
    Optional<OpeningGeometryEntity> geometryOpt = openingGeometryRepository.findById(openingId);
    Optional<OpeningEntity> openingOpt = openingRepository.findById(openingId);

    OrgUnitEntity district = null;
    OrgUnitEntity region = null;
    if (openingOpt.isPresent() && openingOpt.get().getAdminDistrictNo() != null) {
      Optional<OrgUnitEntity> districtOpt =
          orgUnitRepository.findById(openingOpt.get().getAdminDistrictNo());
      if (districtOpt.isPresent()) {
        district = districtOpt.get();
        region = orgUnitRepository.findById(district.getRollupRegionNo()).orElse(null);
      }
    }

    Map<String, Object> properties =
        buildProperties(openingId, openingOpt.orElse(null), district, region);

    Feature feature = new Feature();
    feature.setId(String.valueOf(openingId));
    feature.setProperties(properties);

    FeatureCollection fc = buildFeatureCollection();

    if (geometryOpt.isPresent() && geometryOpt.get().getGeometry() != null) {
      Geometry geom4326 = reprojectTo4326(geometryOpt.get().getGeometry());
      feature.setGeometry(convertToGeoJson(geom4326));
      Envelope env = geom4326.getEnvelopeInternal();
      double[] bbox = {env.getMinX(), env.getMinY(), env.getMaxX(), env.getMaxY()};
      feature.setBbox(bbox);
      fc.setBbox(bbox);
    }

    fc.add(feature);
    return fc;
  }

  private Map<String, Object> buildProperties(
      Long openingId, OpeningEntity opening, OrgUnitEntity district, OrgUnitEntity region) {
    Map<String, Object> props = new LinkedHashMap<>();
    props.put("OPENING_ID", openingId);
    props.put("OPENING_CATEGORY_CODE", null);
    props.put("OPENING_STATUS_CODE", null);
    props.put("REGION_CODE", district != null ? district.getRollupRegionCode() : null);
    props.put("REGION_NAME", region != null ? region.getOrgUnitName() : null);
    props.put("DISTRICT_CODE", district != null ? district.getOrgUnitCode() : null);
    props.put("DISTRICT_NAME", district != null ? district.getOrgUnitName() : null);
    props.put("CLIENT_NAME", null);
    props.put("CLIENT_NUMBER", null);
    props.put("OPENING_WHO_CREATED", null);
    props.put(
        "OPENING_WHEN_CREATED",
        opening != null && opening.getEntryTimestamp() != null
            ? opening.getEntryTimestamp().format(DATE_Z_FORMAT)
            : null);
    props.put("OPENING_WHO_UPDATED", null);
    props.put("OPENING_WHEN_UPDATED", null);
    props.put("OBJECTID", null); // BCGW-internal identifier, not stored in postgres
    return props;
  }

  private FeatureCollection buildFeatureCollection() {
    FeatureCollection fc = new FeatureCollection();
    Crs crs = new Crs();
    crs.setType(CrsType.name);
    crs.getProperties().put("name", CRS_URN);
    fc.setCrs(crs);
    return fc;
  }

  private Geometry reprojectTo4326(Geometry geometry3005) {
    try {
      CoordinateReferenceSystem sourceCrs = CRS.decode("EPSG:3005");
      CoordinateReferenceSystem targetCrs = CRS.decode("EPSG:4326", true);
      MathTransform transform = CRS.findMathTransform(sourceCrs, targetCrs, true);
      Geometry reprojected = JTS.transform(geometry3005, transform);
      reprojected.setSRID(4326);
      return reprojected;
    } catch (Exception e) {
      log.error("Failed to reproject geometry from EPSG:3005 to EPSG:4326", e);
      throw new IllegalStateException(
          "Failed to reproject geometry from EPSG:3005 to EPSG:4326", e);
    }
  }

  private GeoJsonObject convertToGeoJson(Geometry jtsGeometry) {
    try {
      GeoJsonWriter writer = new GeoJsonWriter();
      String geojsonStr = writer.write(jtsGeometry);
      return objectMapper.readValue(geojsonStr, GeoJsonObject.class);
    } catch (Exception e) {
      log.error("Failed to convert JTS geometry to GeoJSON: {}", e.getMessage());
      return null;
    }
  }
}
