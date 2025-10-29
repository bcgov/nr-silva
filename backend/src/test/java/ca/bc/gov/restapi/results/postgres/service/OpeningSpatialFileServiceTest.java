package ca.bc.gov.restapi.results.postgres.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ca.bc.gov.restapi.results.postgres.SilvaConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
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
  void shouldProcessSimpleGeojson() throws Exception {
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
    org.assertj.core.api.Assertions.assertThat(code).isEqualTo("3005");
  }

  @Test
  @DisplayName("Detect GML CRS from MultipartFile via reflection")
  void detectGmlCrsFromMultipart() throws Exception {
    String gml =
        "<gml:Polygon xmlns:gml=\"http://www.opengis.net/gml\""
            + " srsName=\"EPSG:4326\"><gml:outerBoundaryIs><gml:LinearRing><gml:coordinates>0,0 1,0"
            + " 1,1 0,0</gml:coordinates></gml:LinearRing></gml:outerBoundaryIs></gml:Polygon>";
    org.springframework.mock.web.MockMultipartFile mf =
        new org.springframework.mock.web.MockMultipartFile(
            "file", "geom.gml", "application/gml+xml", gml.getBytes(StandardCharsets.UTF_8));

    java.lang.reflect.Method m =
        OpeningSpatialFileService.class.getDeclaredMethod(
            "detectGmlCrs", org.springframework.web.multipart.MultipartFile.class);
    m.setAccessible(true);
    String code = (String) m.invoke(service, mf);
    org.assertj.core.api.Assertions.assertThat(code).isEqualTo("4326");
  }

  @Test
  @DisplayName("GeoJSON thinning returns FeatureCollection string")
  void geoJsonThinningReturnsString() throws Exception {
    String geojson =
        "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[["
            + " -123.0,49.0 ],[ -123.0,49.0001 ],[ -122.9999,49.0001 ],[ -123.0,49.0 ]]]}}]}";
    java.lang.reflect.Method m =
        OpeningSpatialFileService.class.getDeclaredMethod(
            "geoJsonThinning", String.class, String.class);
    m.setAccessible(true);
    String out = (String) m.invoke(service, geojson, "4326");
    org.assertj.core.api.Assertions.assertThat(out).contains("FeatureCollection");
  }

  @Test
  @DisplayName("validateGeoJsonGeometryNode throws when geometry missing type")
  void validateGeoJsonGeometryNodeMissingType() throws Exception {
    com.fasterxml.jackson.databind.node.ObjectNode geomNode = mapper.createObjectNode();
    Object gjson =
        Class.forName("org.geotools.geojson.geom.GeometryJSON")
            .getDeclaredConstructor()
            .newInstance();

    Class<?> gjsonClass = Class.forName("org.geotools.geojson.geom.GeometryJSON");
    java.lang.reflect.Method m =
        OpeningSpatialFileService.class.getDeclaredMethod(
            "validateGeoJsonGeometryNode",
            gjsonClass,
            com.fasterxml.jackson.databind.JsonNode.class,
            int.class);
    m.setAccessible(true);

    try {
      m.invoke(service, gjson, geomNode, 1);
      org.junit.jupiter.api.Assertions.fail("Expected ResponseStatusException");
    } catch (java.lang.reflect.InvocationTargetException ite) {
      org.assertj.core.api.Assertions.assertThat(ite.getCause())
          .isInstanceOf(ResponseStatusException.class);
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
}
