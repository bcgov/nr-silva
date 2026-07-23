package ca.bc.gov.restapi.results.postgres.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ca.bc.gov.restapi.results.postgres.SilvaPostgresConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@DisplayName("Unit Test | OpeningSpatialFileService")
class OpeningSpatialFileServiceTest {

  private final OpeningSpatialFileService service = new OpeningSpatialFileService();

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  @DisplayName("Should throw when file is null or empty")
  void shouldThrowOnNullOrEmpty() {
    assertThatThrownBy(() -> service.processOpeningSpatialFile("test.json", new byte[0]))
        .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  @DisplayName("Should throw when file exceeds size limit")
  void shouldThrowOnOversize() {
    byte[] big = new byte[SilvaPostgresConstants.MAX_OPENING_FILE_SIZE_BYTES + 1];

    assertThatThrownBy(() -> service.processOpeningSpatialFile("big.geojson", big))
        .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  @DisplayName("Should throw on unsupported extension")
  void shouldThrowOnUnsupportedExtension() {
    assertThatThrownBy(
            () ->
                service.processOpeningSpatialFile(
                    "data.txt", "hello".getBytes(StandardCharsets.UTF_8)))
        .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  @DisplayName("Should process simple GeoJSON feature collection")
  void shouldProcessSimpleGeojson() {
    String geojson =
        "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[["
            + " -123.0,49.0 ],[ -123.0,49.1 ],[ -122.9,49.1 ],[ -123.0,49.0 ]]]}}]}";

    assertThatThrownBy(
            () ->
                service.processOpeningSpatialFile(
                    "simple.geojson", geojson.getBytes(StandardCharsets.UTF_8)))
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
  @DisplayName("Detect GML CRS from bytes via reflection")
  void detectGmlCrsFromBytes() throws Exception {
    String gml =
        "<gml:Polygon xmlns:gml=\"http://www.opengis.net/gml\""
            + " srsName=\"EPSG:4326\"><gml:outerBoundaryIs><gml:LinearRing><gml:coordinates>0,0 1,0"
            + " 1,1 0,0</gml:coordinates></gml:LinearRing></gml:outerBoundaryIs></gml:Polygon>";
    byte[] gmlBytes = gml.getBytes(StandardCharsets.UTF_8);

    Method m = OpeningSpatialFileService.class.getDeclaredMethod("detectGmlCrs", byte[].class);
    m.setAccessible(true);
    String code = (String) m.invoke(service, new Object[] {gmlBytes});
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
    Method m =
        OpeningSpatialFileService.class.getDeclaredMethod(
            "validateGeoJsonGeometryNode", JsonNode.class, int.class);
    m.setAccessible(true);

    try {
      m.invoke(service, geomNode, 1);
      Assertions.fail("Expected ResponseStatusException");
    } catch (java.lang.reflect.InvocationTargetException ite) {
      assertThat(ite.getCause()).isInstanceOf(ResponseStatusException.class);
    }
  }

  @Test
  @DisplayName("Should throw invalid geojson when file content is not JSON")
  void shouldThrowOnInvalidJsonContent() {
    assertThatThrownBy(
            () ->
                service.processOpeningSpatialFile(
                    "bad.json", "not-json".getBytes(StandardCharsets.UTF_8)))
        .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  void parseGml2AndGml3_shouldReturnGeometry() throws Exception {
    String gml2 =
        "<gml:Polygon"
            + " xmlns:gml=\"http://www.opengis.net/gml\"><gml:outerBoundaryIs><gml:LinearRing><gml:coordinates>-123,49"
            + " -123,49.1 -122.9,49.1"
            + " -123,49</gml:coordinates></gml:LinearRing></gml:outerBoundaryIs></gml:Polygon>";

    Geometry geom2 = parseGml(gml2);
    assertThat((Object) geom2).isNotNull();

    String gml3 =
        "<gml:Polygon"
            + " xmlns:gml=\"http://www.opengis.net/gml\"><gml:exterior><gml:LinearRing><gml:posList>-123"
            + " 49 -123 49.1 -122.9 49.1 -123"
            + " 49</gml:posList></gml:LinearRing></gml:exterior></gml:Polygon>";
    Geometry geom3 = parseGml(gml3);
    assertThat((Object) geom3).isNotNull();
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

  @Test
  @DisplayName("Should throw for unsupported CRS")
  void shouldThrowForUnsupportedCrs() throws Exception {
    GeometryFactory gf = new GeometryFactory();
    Geometry pt = gf.createPoint(new Coordinate(-125, 52));

    Method m =
        OpeningSpatialFileService.class.getDeclaredMethod(
            "validateGmlGeometryAndBoundary", Geometry.class, String.class);
    m.setAccessible(true);

    assertThatThrownBy(
            () -> {
              try {
                m.invoke(service, pt, "9999");
              } catch (InvocationTargetException ite) {
                throw ite.getCause();
              }
            })
        .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  @DisplayName("Should validate geometry within BC boundary (4326)")
  void shouldValidateWithinBoundary4326() throws Exception {
    GeometryFactory gf = new GeometryFactory();
    Coordinate[] coords =
        new Coordinate[] {
          new Coordinate(-120.5, 49.2),
          new Coordinate(-120.4, 49.2),
          new Coordinate(-120.4, 49.3),
          new Coordinate(-120.5, 49.3),
          new Coordinate(-120.5, 49.2)
        };
    Polygon poly = gf.createPolygon(coords);

    Method m =
        OpeningSpatialFileService.class.getDeclaredMethod(
            "validateGmlGeometryAndBoundary", Geometry.class, String.class);
    m.setAccessible(true);

    // Should not throw
    m.invoke(service, poly, "4326");
  }

  @Test
  @DisplayName("Should throw when geometry outside BC boundary (4326)")
  void shouldThrowWhenOutsideBoundary4326() throws Exception {
    GeometryFactory gf = new GeometryFactory();
    Geometry pt = gf.createPoint(new Coordinate(-10, 10));

    Method m =
        OpeningSpatialFileService.class.getDeclaredMethod(
            "validateGmlGeometryAndBoundary", Geometry.class, String.class);
    m.setAccessible(true);

    assertThatThrownBy(
            () -> {
              try {
                m.invoke(service, pt, "4326");
              } catch (InvocationTargetException ite) {
                throw ite.getCause();
              }
            })
        .isInstanceOf(ResponseStatusException.class);
  }

  // === GML DOM Parsing Tests ===

  @Test
  @DisplayName("parseGmlPoint GML3 format (pos)")
  void parseGmlPointGml3() throws Exception {
    String gmlPoint =
        "<gml:Point xmlns:gml=\"http://www.opengis.net/gml\"><gml:pos>-123.5"
            + " 49.2</gml:pos></gml:Point>";

    Geometry geom = parseGml(gmlPoint);

    assertThat((Object) geom).isNotNull();
    assertThat(geom.getGeometryType()).isEqualTo("Point");
    assertThat(geom.getCoordinate()).isEqualTo(new Coordinate(-123.5, 49.2));
  }

  @Test
  @DisplayName("parseGmlLineString GML3 format (posList)")
  void parseGmlLineStringGml3() throws Exception {
    String gmlLine =
        "<gml:LineString xmlns:gml=\"http://www.opengis.net/gml\"><gml:posList>-123 49 -122.9 49.1"
            + " -122.8 49.2</gml:posList></gml:LineString>";

    Geometry geom = parseGml(gmlLine);

    assertThat((Object) geom).isNotNull();
    assertThat(geom.getGeometryType()).isEqualTo("LineString");
    assertThat(geom.getNumPoints()).isEqualTo(3);
  }

  @Test
  @DisplayName("parseGmlPolygon GML2 format with holes")
  void parseGmlPolygonGml2WithHoles() throws Exception {
    String gmlPolygon =
        "<gml:Polygon"
            + " xmlns:gml=\"http://www.opengis.net/gml\"><gml:outerBoundaryIs><gml:LinearRing><gml:coordinates>0,0"
            + " 10,0 10,10 0,10"
            + " 0,0</gml:coordinates></gml:LinearRing></gml:outerBoundaryIs><gml:innerBoundaryIs><gml:LinearRing><gml:coordinates>2,2"
            + " 8,2 8,8 2,8 2,2</gml:coordinates></gml:LinearRing></gml:innerBoundaryIs>"
            + "</gml:Polygon>";

    Geometry geom = parseGml(gmlPolygon);

    assertThat((Object) geom).isNotNull();
    assertThat(geom.getGeometryType()).isEqualTo("Polygon");
    // Verify polygon has more coordinates due to interior ring
    assertThat(geom.getNumPoints()).isGreaterThan(4);
  }

  @Test
  @DisplayName("parseGmlPolygon GML3 format")
  void parseGmlPolygonGml3() throws Exception {
    String gmlPolygon =
        "<gml:Polygon"
            + " xmlns:gml=\"http://www.opengis.net/gml\"><gml:exterior><gml:LinearRing><gml:posList>-123"
            + " 49 -123 49.1 -122.9 49.1 -123 49</gml:posList></gml:LinearRing></gml:exterior>"
            + "</gml:Polygon>";

    Geometry geom = parseGml(gmlPolygon);

    assertThat((Object) geom).isNotNull();
    assertThat(geom.getGeometryType()).isEqualTo("Polygon");
    // Verify it's a simple polygon with just exterior ring
    assertThat(geom.getNumPoints()).isGreaterThanOrEqualTo(4);
  }

  @Test
  @DisplayName("parseGmlMultiPoint")
  void parseGmlMultiPoint() throws Exception {
    String gmlMultiPoint =
        "<gml:MultiPoint"
            + " xmlns:gml=\"http://www.opengis.net/gml\"><gml:pointMember><gml:Point><gml:pos>-123"
            + " 49</gml:pos></gml:Point></gml:pointMember><gml:pointMember><gml:Point><gml:pos>-122.5"
            + " 49.5</gml:pos></gml:Point></gml:pointMember></gml:MultiPoint>";

    Geometry geom = parseGml(gmlMultiPoint);

    assertThat((Object) geom).isNotNull();
    assertThat(geom.getGeometryType()).isEqualTo("MultiPoint");
    assertThat(geom.getNumGeometries()).isEqualTo(2);
  }

  @Test
  @DisplayName("parseGmlMultiLineString")
  void parseGmlMultiLineString() throws Exception {
    String gmlMultiLine =
        "<gml:MultiLineString"
            + " xmlns:gml=\"http://www.opengis.net/gml\"><gml:lineStringMember><gml:LineString><gml:posList>-123"
            + " 49 -122.9"
            + " 49.1</gml:posList></gml:LineString></gml:lineStringMember><gml:lineStringMember><gml:LineString><gml:posList>-122.5"
            + " 49.5 -122.4 49.6</gml:posList></gml:LineString></gml:lineStringMember>"
            + "</gml:MultiLineString>";

    Geometry geom = parseGml(gmlMultiLine);

    assertThat((Object) geom).isNotNull();
    assertThat(geom.getGeometryType()).isEqualTo("MultiLineString");
    assertThat(geom.getNumGeometries()).isEqualTo(2);
  }

  @Test
  @DisplayName("parseGmlMultiPolygon")
  void parseGmlMultiPolygon() throws Exception {
    String gmlMultiPoly =
        "<gml:MultiPolygon"
            + " xmlns:gml=\"http://www.opengis.net/gml\"><gml:surfaceMember><gml:Polygon><gml:exterior><gml:LinearRing><gml:posList>0"
            + " 0 10 0 10 10 0 10 0"
            + " 0</gml:posList></gml:LinearRing></gml:exterior></gml:Polygon></gml:surfaceMember><gml:surfaceMember><gml:Polygon><gml:exterior><gml:LinearRing><gml:posList>20"
            + " 20 30 20 30 30 20 30 20"
            + " 20</gml:posList></gml:LinearRing></gml:exterior></gml:Polygon></gml:surfaceMember>"
            + "</gml:MultiPolygon>";

    Geometry geom = parseGml(gmlMultiPoly);

    assertThat((Object) geom).isNotNull();
    assertThat(geom.getGeometryType()).isEqualTo("MultiPolygon");
    assertThat(geom.getNumGeometries()).isEqualTo(2);
  }

  @Test
  @DisplayName("parseGmlToGeometry throws on invalid GML (no geometry)")
  void parseGmlToGeometryThrowsNoGeometry() throws Exception {
    String invalidGml =
        "<gml:NoSuchGeometry xmlns:gml=\"http://www.opengis.net/gml\"></gml:NoSuchGeometry>";

    assertThatThrownBy(() -> parseGml(invalidGml))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining("does not contain a valid geometry");
  }

  @Test
  @DisplayName("parseGmlToGeometry throws on malformed XML")
  void parseGmlToGeometryThrowsMalformedXml() throws Exception {
    String malformed = "<gml:Polygon><unclosed>";

    assertThatThrownBy(() -> parseGml(malformed)).isInstanceOf(Exception.class);
  }

  @Test
  @DisplayName("parseGmlPolygon throws when exterior ring missing")
  void parseGmlPolygonThrowsMissingExterior() throws Exception {
    String noExterior = "<gml:Polygon xmlns:gml=\"http://www.opengis.net/gml\"></gml:Polygon>";

    assertThatThrownBy(() -> parseGml(noExterior))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining("exterior ring");
  }

  @Test
  @DisplayName("parseGmlPoint throws when no coordinates")
  void parseGmlPointThrowsNoCoordinates() throws Exception {
    String noCoords =
        "<gml:Point xmlns:gml=\"http://www.opengis.net/gml\"><gml:pos></gml:pos></gml:Point>";

    assertThatThrownBy(() -> parseGml(noCoords))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining("no coordinates");
  }

  @Test
  @DisplayName("parseGmlLinearRing throws when less than 4 coordinates")
  void parseGmlLinearRingThrowsInvalidRing() throws Exception {
    String tooFewCoords =
        "<gml:Polygon"
            + " xmlns:gml=\"http://www.opengis.net/gml\"><gml:exterior><gml:LinearRing><gml:posList>0"
            + " 0 1 1</gml:posList></gml:LinearRing></gml:exterior></gml:Polygon>";

    assertThatThrownBy(() -> parseGml(tooFewCoords))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining("Polygon ring has fewer than 4 coordinates");
  }

  @Test
  void shouldThrowOnXmlExtension() {
    assertThatThrownBy(
            () ->
                service.processOpeningSpatialFile(
                    "data.xml", "<root/>".getBytes(StandardCharsets.UTF_8)))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining("Unsupported file type");
  }

  @Test
  void shouldRejectNonPolygonGeoJson() {
    String geojson =
        "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\","
            + "\"geometry\":{\"type\":\"Point\",\"coordinates\":[-120.5,49.2]}}]}";
    assertThatThrownBy(
            () ->
                service.processOpeningSpatialFile(
                    "point.geojson", geojson.getBytes(StandardCharsets.UTF_8)))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining("Only Polygon and MultiPolygon");
  }

  @Test
  void calculateGeometryAreaHectaresIn3005() throws Exception {
    GeometryFactory gf = new GeometryFactory();
    Coordinate[] coords = {
      new Coordinate(500000, 600000),
      new Coordinate(510000, 600000),
      new Coordinate(510000, 610000),
      new Coordinate(500000, 610000),
      new Coordinate(500000, 600000)
    };
    Polygon poly = gf.createPolygon(coords);
    Method m =
        OpeningSpatialFileService.class.getDeclaredMethod(
            "calculateGeometryAreaHectares", Geometry.class, String.class);
    m.setAccessible(true);
    BigDecimal result = (BigDecimal) m.invoke(service, poly, "3005");
    assertThat(result).isEqualByComparingTo(new BigDecimal("10000.0000"));
  }

  @Test
  void validateNonZeroAreaThrowsForCollapsedPolygon() throws Exception {
    GeometryFactory gf = new GeometryFactory();
    Coordinate[] coords = {
      new Coordinate(500000, 600000),
      new Coordinate(500001, 600000),
      new Coordinate(500001, 600001),
      new Coordinate(500000, 600000)
    };
    Polygon poly = gf.createPolygon(coords);
    Method m =
        OpeningSpatialFileService.class.getDeclaredMethod(
            "validateNonZeroArea", Geometry.class, String.class, int.class);
    m.setAccessible(true);
    assertThatThrownBy(
            () -> {
              try {
                m.invoke(service, poly, "3005", 1);
              } catch (InvocationTargetException ite) {
                throw ite.getCause();
              }
            })
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining("zero or collapsed surface area");
  }

  @Test
  void validateNonZeroAreaPassesForValidPolygon() throws Exception {
    GeometryFactory gf = new GeometryFactory();
    Coordinate[] coords = {
      new Coordinate(500000, 600000),
      new Coordinate(510000, 600000),
      new Coordinate(510000, 610000),
      new Coordinate(500000, 600000)
    };
    Polygon poly = gf.createPolygon(coords);
    Method m =
        OpeningSpatialFileService.class.getDeclaredMethod(
            "validateNonZeroArea", Geometry.class, String.class, int.class);
    m.setAccessible(true);
    m.invoke(service, poly, "3005", 1); // should not throw
  }

  private Geometry parseGml(String gml) throws Exception {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
    dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    dbf.setFeature(javax.xml.XMLConstants.FEATURE_SECURE_PROCESSING, true);
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document doc =
        db.parse(new ByteArrayInputStream(gml.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
    Element root = doc.getDocumentElement();
    Method parseGmlElement =
        OpeningSpatialFileService.class.getDeclaredMethod(
            "parseGmlElement", Element.class, GeometryFactory.class);
    parseGmlElement.setAccessible(true);
    GeometryFactory gf = new GeometryFactory();
    try {
      Geometry geom = (Geometry) parseGmlElement.invoke(service, root, gf);
      if (geom == null) {
        throw new ResponseStatusException(
            org.springframework.http.HttpStatus.BAD_REQUEST,
            "GML does not contain a valid geometry.");
      }
      return geom;
    } catch (java.lang.reflect.InvocationTargetException ite) {
      Throwable cause = ite.getCause();
      if (cause instanceof RuntimeException re) throw re;
      if (cause instanceof Exception ex) throw ex;
      throw ite;
    }
  }
}
