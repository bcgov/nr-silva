package ca.bc.gov.restapi.results.postgres.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ca.bc.gov.restapi.results.postgres.SilvaConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@DisplayName("Unit Test | OpeningSpatialFileService")
class OpeningSpatialFileServiceTest {

  private final OpeningSpatialFileService service = new OpeningSpatialFileService();

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  @DisplayName("Should throw when file is null or empty")
  void shouldThrowOnNullOrEmpty() {
    MockMultipartFile empty = new MockMultipartFile("file", "", "application/json", new byte[0]);

    assertThatThrownBy(() -> service.processOpeningSpatialFile(empty))
        .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  @DisplayName("Should throw when file exceeds size limit")
  void shouldThrowOnOversize() {
    byte[] big = new byte[SilvaConstants.MAX_OPENING_FILE_SIZE_BYTES + 1];
    MockMultipartFile bigFile =
        new MockMultipartFile("file", "big.geojson", "application/json", big);

    assertThatThrownBy(() -> service.processOpeningSpatialFile(bigFile))
        .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  @DisplayName("Should throw on unsupported extension")
  void shouldThrowOnUnsupportedExtension() {
    MockMultipartFile file =
        new MockMultipartFile(
            "file", "data.txt", "text/plain", "hello".getBytes(StandardCharsets.UTF_8));

    assertThatThrownBy(() -> service.processOpeningSpatialFile(file))
        .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  @DisplayName("Should process simple GeoJSON feature collection")
  void shouldProcessSimpleGeojson() {
    String geojson =
        "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[["
            + " -123.0,49.0 ],[ -123.0,49.1 ],[ -122.9,49.1 ],[ -123.0,49.0 ]]]}}]}";
    MockMultipartFile file =
        new MockMultipartFile(
            "file", "simple.geojson", "application/json", geojson.getBytes(StandardCharsets.UTF_8));

    assertThatThrownBy(() -> service.processOpeningSpatialFile(file))
        .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  @DisplayName("Detect CRS from GeoJSON crs property via reflection")
  void detectGeoJsonCrsReflection() throws Exception {
    ObjectNode root = mapper.createObjectNode();
    root.put("type", "FeatureCollection");
    ObjectNode props = mapper.createObjectNode();
    props.put("name", "urn:ogc:def:crs:EPSG::3005");
    ObjectNode crs = mapper.createObjectNode();
    crs.set("properties", props);
    root.set("crs", crs);

    Method m =
        OpeningSpatialFileService.class.getDeclaredMethod(
            "detectGeoJsonCrs", com.fasterxml.jackson.databind.JsonNode.class);
    m.setAccessible(true);
    String code = (String) m.invoke(service, root);
    assertThat(code).isEqualTo("3005");
  }

  @Test
  @DisplayName("Detect GML CRS from MultipartFile via reflection")
  void detectGmlCrsFromMultipart() throws Exception {
    String gml =
        "<gml:Polygon xmlns:gml=\"http://www.opengis.net/gml\""
            + " srsName=\"EPSG:4326\"><gml:outerBoundaryIs><gml:LinearRing><gml:coordinates>0,0 1,0"
            + " 1,1 0,0</gml:coordinates></gml:LinearRing></gml:outerBoundaryIs></gml:Polygon>";
    MockMultipartFile mf =
        new MockMultipartFile(
            "file", "geom.gml", "application/gml+xml", gml.getBytes(StandardCharsets.UTF_8));

    Method m =
        OpeningSpatialFileService.class.getDeclaredMethod("detectGmlCrs", MultipartFile.class);
    m.setAccessible(true);
    String code = (String) m.invoke(service, mf);
    assertThat(code).isEqualTo("4326");
  }

  @Test
  @DisplayName("GeoJSON thinning returns FeatureCollection string")
  void geoJsonThinningReturnsString() throws Exception {
    String geojson =
        "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[["
            + " -123.0,49.0 ],[ -123.0,49.0001 ],[ -122.9999,49.0001 ],[ -123.0,49.0 ]]]}}]}";
    Method m =
        OpeningSpatialFileService.class.getDeclaredMethod(
            "geoJsonThinning", String.class, String.class);
    m.setAccessible(true);
    String out = (String) m.invoke(service, geojson, "4326");
    assertThat(out).contains("FeatureCollection");
  }

  @Test
  @DisplayName("validateGeoJsonGeometryNode throws when geometry missing type")
  void validateGeoJsonGeometryNodeMissingType() throws Exception {
    ObjectNode geomNode = mapper.createObjectNode();
    Object gjson =
        Class.forName("org.geotools.geojson.geom.GeometryJSON")
            .getDeclaredConstructor()
            .newInstance();

    Class<?> gjsonClass = Class.forName("org.geotools.geojson.geom.GeometryJSON");
    Method m =
        OpeningSpatialFileService.class.getDeclaredMethod(
            "validateGeoJsonGeometryNode", gjsonClass, JsonNode.class, int.class);
    m.setAccessible(true);

    try {
      m.invoke(service, gjson, geomNode, 1);
      Assertions.fail("Expected ResponseStatusException");
    } catch (java.lang.reflect.InvocationTargetException ite) {
      assertThat(ite.getCause()).isInstanceOf(ResponseStatusException.class);
    }
  }

  @Test
  @DisplayName("Should throw invalid geojson when file content is not JSON")
  void shouldThrowOnInvalidJsonContent() {
    MockMultipartFile bad =
        new MockMultipartFile(
            "file", "bad.json", "application/json", "not-json".getBytes(StandardCharsets.UTF_8));
    assertThatThrownBy(() -> service.processOpeningSpatialFile(bad))
        .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  void parseGml2AndGml3_shouldReturnGeometry() throws Exception {
    String gml2 =
        "<gml:Polygon"
            + " xmlns:gml=\"http://www.opengis.net/gml\"><gml:outerBoundaryIs><gml:LinearRing><gml:coordinates>-123,49"
            + " -123,49.1 -122.9,49.1"
            + " -123,49</gml:coordinates></gml:LinearRing></gml:outerBoundaryIs></gml:Polygon>";

    Method parse =
        OpeningSpatialFileService.class.getDeclaredMethod("parseGmlToGeometry", String.class);
    parse.setAccessible(true);
    Geometry geom2 = (Geometry) parse.invoke(service, gml2);
    assertThat((Object) geom2).isNotNull();

    String gml3 =
        "<gml:Polygon"
            + " xmlns:gml=\"http://www.opengis.net/gml\"><gml:exterior><gml:LinearRing><gml:posList>-123"
            + " 49 -123 49.1 -122.9 49.1 -123"
            + " 49</gml:posList></gml:LinearRing></gml:exterior></gml:Polygon>";
    Geometry geom3 = (Geometry) parse.invoke(service, gml3);
    assertThat((Object) geom3).isNotNull();
  }

  @Test
  void extractGmlGeometriesXml_shouldFindGeoms() throws Exception {
    String doc =
        "<root"
            + " xmlns:gml=\"http://www.opengis.net/gml\"><gml:Polygon><gml:outerBoundaryIs><gml:LinearRing><gml:coordinates>-123,49"
            + " -123,49.1 -122.9,49.1"
            + " -123,49</gml:coordinates></gml:LinearRing></gml:outerBoundaryIs></gml:Polygon>"
            + "</root>";
    Method m =
        OpeningSpatialFileService.class.getDeclaredMethod("extractGmlGeometriesXml", String.class);
    m.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<String> list = (List<String>) m.invoke(service, doc);
    assertThat(list).isNotEmpty();
  }

  @Test
  void esfMetaDataAndTenureExtraction_and_generalAreaFragment() throws Exception {
    String esf =
        "<esf:ESFSubmission xmlns:esf=\"http://www.for.gov.bc.ca/schema/esf\""
            + " xmlns:rst=\"http://www.for.gov.bc.ca/schema/results\"><esf:submissionContent>"
            + "<rst:ResultsSubmission>"
            + "<rst:submissionMetadataProperty><rst:SubmissionMetadata><rst:districtCode>MYDIST</rst:districtCode></rst:SubmissionMetadata></rst:submissionMetadataProperty>"
            + "<rst:submissionItem><rst:Opening><rst:definedBy><rst:OpeningDefinition>"
            + "<rst:openingGrossArea>123.45</rst:openingGrossArea>"
            + "<rst:maximumAllowableSoilDisturbancePercentage>9.9</rst:maximumAllowableSoilDisturbancePercentage>"
            + "<rst:extentOf><gml:Polygon"
            + " xmlns:gml=\"http://www.opengis.net/gml\"><gml:outerBoundaryIs><gml:LinearRing><gml:coordinates>-123,49"
            + " -123,49.1 -122.9,49.1"
            + " -123,49</gml:coordinates></gml:LinearRing></gml:outerBoundaryIs></gml:Polygon>"
            + "</rst:extentOf><rst:tenureProperty><rst:Tenure>"
            + "<rst:licenceNumber>FF123</rst:licenceNumber><rst:cutblock>CB1</rst:cutblock>"
            + "<rst:cuttingPermitID>CP1</rst:cuttingPermitID>"
            + "<rst:primeLicenceIndicator>true</rst:primeLicenceIndicator></rst:Tenure>"
            + "</rst:tenureProperty></rst:OpeningDefinition></rst:definedBy></rst:Opening>"
            + "</rst:submissionItem></rst:ResultsSubmission></esf:submissionContent>"
            + "</esf:ESFSubmission>";

    Method frag =
        OpeningSpatialFileService.class.getDeclaredMethod(
            "extractGeneralAreaGmlFragment", String.class);
    frag.setAccessible(true);
    String fragOut = (String) frag.invoke(service, esf);
    assertThat(fragOut).contains("gml:Polygon");

    Method meta =
        OpeningSpatialFileService.class.getDeclaredMethod("extractEsfMetaData", String.class);
    meta.setAccessible(true);
    Object metaObj = meta.invoke(service, esf);
    assertThat(metaObj).isNotNull();
    // Validate fields via record accessors
    Method orgUnit = metaObj.getClass().getDeclaredMethod("orgUnit");
    assertThat(orgUnit.invoke(metaObj)).isEqualTo("MYDIST");
    Method openingGrossArea = metaObj.getClass().getDeclaredMethod("openingGrossArea");
    BigDecimal area = (BigDecimal) openingGrossArea.invoke(metaObj);
    assertThat(area).isEqualByComparingTo(new BigDecimal("123.45"));

    Method tenure =
        OpeningSpatialFileService.class.getDeclaredMethod("extractEsfTenureList", String.class);
    tenure.setAccessible(true);
    List<?> tlist = (List<?>) tenure.invoke(service, esf);
    assertThat(tlist).hasSize(1);
    Object first = tlist.get(0);
    Method isPrimary = first.getClass().getDeclaredMethod("isPrimary");
    assertThat(isPrimary.invoke(first)).isEqualTo(true);
  }

  @Test
  void reprojectTo4326_changesCoordinates() throws Exception {
    GeometryFactory gf = new GeometryFactory();
    Geometry pt = gf.createPoint(new Coordinate(500000, 1700000));
    Method m =
        OpeningSpatialFileService.class.getDeclaredMethod(
            "reprojectTo4326", Geometry.class, String.class);
    m.setAccessible(true);
    Geometry out = (Geometry) m.invoke(service, pt, "3005");
    assertThat((Object) out).isNotNull();
    // coordinates should not be identical
    assertThat(out.getCoordinates()[0].x).isNotEqualTo(pt.getCoordinates()[0].x);
  }
}
