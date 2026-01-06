package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.postgres.SilvaConstants;
import ca.bc.gov.restapi.results.postgres.dto.ExtractedGeoDataDto;
import ca.bc.gov.restapi.results.postgres.dto.GeoMetaDataDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureDto;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.xsd.Parser;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.operation.valid.IsValidOp;
import org.locationtech.jts.operation.valid.TopologyValidationError;
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This service handles processing of uploaded spatial files (GeoJSON, GML, ESF/XML).
 *
 * <p>Validation and processing steps:
 *
 * <ol>
 *   <li><b>CRS Validation:</b> Ensure the file is in EPSG:3005 or EPSG:4326.<br>
 *       <i>Reason:</i> All further checks depend on knowing the coordinate system.
 *   <li><b>Geometry Validity (OGC/ESRI):</b> Validate that the geometry is valid according to OGC
 *       and ESRI specs.<br>
 *       <i>Reason:</i> Invalid geometries can break downstream processing and thinning.
 *   <li><b>Simple Features (No Curves):</b> Ensure all geometries are simple features (no curves).
 *       <br>
 *       <i>Reason:</i> Thinning and extent checks require simple, linear geometries.
 *   <li><b>Province of BC Extents:</b> Check that all features and vertices are within BC extents.
 *       <br>
 *       <i>Reason:</i> You only want data within your area of interest.
 *   <li><b>Vertex Thinning (Douglas-Peucker, 2.5m tolerance):</b> Thin the vertices to the required
 *       accuracy/precision.<br>
 *       <i>Reason:</i> This is a data optimization step, best done after validation.
 * </ol>
 */
@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings("deprecation")
public class OpeningSpatialFileService {

  private final ObjectMapper mapper = new ObjectMapper();

  /**
   * Processes an uploaded spatial file (GeoJSON, GML, or ESF/XML) and delegates to the appropriate
   * handler based on file extension. Performs file size and type validation before processing.
   *
   * @param file the uploaded spatial file
   * @return ExtractedGeoDataDto containing parsed geometry, metadata, and tenure information
   * @throws ResponseStatusException if the file is invalid, unsupported, or exceeds size limits
   */
  public ExtractedGeoDataDto processOpeningSpatialFile(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uploaded file is null or empty");
    }

    // This is enforced at the spring boot level, but we double-check here
    if (file.getSize() > SilvaConstants.MAX_OPENING_FILE_SIZE_BYTES) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File exceeds 25MB size limit");
    }

    String fileName = file.getOriginalFilename();
    if (fileName == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uploaded file has no name");
    }

    String lowerName = fileName.toLowerCase();

    ExtractedGeoDataDto result;
    if (lowerName.endsWith(".xml")) {
      result = processEsf(file);
    } else if (lowerName.endsWith(".gml")) {
      result = processGml(file);
    } else if (lowerName.endsWith(".geojson") || lowerName.endsWith(".json")) {
      result = processGeojson(file);
    } else {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Unsupported file type: " + fileName);
    }

    // Just return the result from the handler; reprojection is handled in each process* method
    return result;
  }

  /**
   * Processes a GeoJSON file
   *
   * @param file the uploaded GeoJSON file
   * @return ExtractedGeoDataDto with geometry and metadata (null)
   * @throws ResponseStatusException if the file is invalid or fails validation
   */
  private ExtractedGeoDataDto processGeojson(MultipartFile file) {
    log.info("Processing GeoJSON file: {}", file.getOriginalFilename());
    try {
      String geojson = new String(file.getBytes());
      JsonNode root = mapper.readTree(geojson);

      // Step 1: CRS Validation
      String crsCode = detectGeoJsonCrs(root);
      log.info("Detected CRS for GeoJSON: EPSG:{}", crsCode);
      if (!crsCode.equals("4326") && !crsCode.equals("3005")) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "GeoJSON CRS must be EPSG:4326 or EPSG:3005 (BC Albers)");
      }

      // Step 2, 3: Geometry Validity (OGC/ESRI), Simple Features (No Curves)
      validateGeoJsonGeometry(geojson);

      // Step 4: Province of BC Extents
      validateGeoJsonWithinBcBoundary(geojson, crsCode);

      // Step 5: Vertex Thinning
      String thinnedGeojson = geoJsonThinning(geojson, crsCode);
      JsonNode thinnedNode = mapper.readTree(thinnedGeojson);

      // Step 6: Reproject to EPSG:4326 if needed
      if (!crsCode.equals("4326")) {
        GeometryJSON gjson = new GeometryJSON();
        ArrayNode features = (ArrayNode) thinnedNode.get("features");
        for (int i = 0; i < features.size(); i++) {
          ObjectNode feature = (ObjectNode) features.get(i);
          ObjectNode geometryNode = (ObjectNode) feature.get("geometry");
          String type = geometryNode.get("type").asText();
          if ("Polygon".equals(type) || "MultiPolygon".equals(type)) {
            try {
              Geometry geom = gjson.read(new StringReader(geometryNode.toString()));
              Geometry transformed = reprojectTo4326(geom, crsCode);
              feature.set("geometry", mapper.readTree(gjson.toString(transformed)));
            } catch (Exception e) {
              log.warn("Failed to reproject geometry to EPSG:4326: {}", e.getMessage());
            }
          }
        }
      }

      // Return ExtractedGeoDataDto with metaData as null
      return ExtractedGeoDataDto.builder().metaData(null).geoJson(thinnedNode).build();
    } catch (Exception e) {
      log.error("Failed to process GeoJSON file", e);
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Invalid GeoJSON file: " + e.getMessage());
    }
  }

  /**
   * Processes an ESF/XML file
   *
   * @param file the uploaded ESF/XML file
   * @return ExtractedGeoDataDto with geometry, metadata, and tenure list
   * @throws ResponseStatusException if the file is invalid or fails validation
   */
  private ExtractedGeoDataDto processEsf(MultipartFile file) {
    log.info("Processing ESF/XML file: {}", file.getOriginalFilename());
    try {
      // Step 1: Detect CRS from embedded GML using detectGmlCrs
      String crsCode = detectGmlCrs(file);
      log.info("Detected CRS for ESF: EPSG:{}", crsCode);

      String xmlText = new String(file.getBytes());

      GeoMetaDataDto metaData = extractEsfMetaData(xmlText);
      List<TenureDto> tenureList = extractEsfTenureList(xmlText);

      String gmlFragment = extractGeneralAreaGmlFragment(xmlText);
      Geometry gmlGeometry = parseGmlToGeometry(gmlFragment);
      log.info("Parsed general area GML geometry from ESF: {}", gmlGeometry.getGeometryType());

      int numGeoms = gmlGeometry.getNumGeometries();
      GeometryJSON gjson = new GeometryJSON();
      ArrayNode features = mapper.createArrayNode();
      for (int i = 0; i < numGeoms; i++) {
        Geometry subGeom = gmlGeometry.getGeometryN(i);
        validateGmlGeometryAndBoundary(subGeom, crsCode);
        int originalCoords = countCoordinates(subGeom);
        Geometry thinned = thinGeometry(subGeom, crsCode);
        int thinnedCoords = countCoordinates(thinned);
        int removed = originalCoords - thinnedCoords;
        log.info(
            "ESF sub-geometry {}: Thinning removed {} of {} coordinates ({} remain)",
            i + 1,
            removed,
            originalCoords,
            thinnedCoords);
        // Reproject to EPSG:4326 if needed
        Geometry finalGeom = reprojectTo4326(thinned, crsCode);
        String geojson = gjson.toString(finalGeom);
        JsonNode geomNode = mapper.readTree(geojson);
        ObjectNode feature = mapper.createObjectNode();
        feature.put("type", "Feature");
        feature.set("geometry", geomNode);
        feature.set("properties", mapper.createObjectNode());
        features.add(feature);
      }
      ObjectNode fc = mapper.createObjectNode();
      fc.put("type", "FeatureCollection");
      fc.set("features", features);

      return ExtractedGeoDataDto.builder()
          .metaData(metaData)
          .geoJson(fc)
          .tenureList(tenureList)
          .build();
    } catch (Exception e) {
      log.error("Failed to process ESF/XML file", e);
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Invalid ESF/XML file: " + e.getMessage());
    }
  }

  /**
   * Processes a GML file
   *
   * @param file the uploaded GML file
   * @return ExtractedGeoDataDto with geometry and metadata (null)
   * @throws ResponseStatusException if the file is invalid or contains no valid geometry
   */
  private ExtractedGeoDataDto processGml(MultipartFile file) {
    log.info("Processing GML file: {}", file.getOriginalFilename());
    try {
      // Step 1: Detect CRS
      String gmlText = new String(file.getBytes());
      String crsCode = detectGmlCrs(file);
      log.info("Detected CRS for GML: EPSG:{}", crsCode);

      // Step 2, 3: Parse GML to JTS Geometry (extract all geometry elements)
      List<String> geomXmls = extractGmlGeometriesXml(gmlText);
      List<Geometry> geometries = new ArrayList<>();
      for (String geomXml : geomXmls) {
        Geometry geom = parseGmlToGeometry(geomXml);
        geometries.add(geom);
      }

      // If no geometry elements found, try parsing the whole file as a naked geometry
      if (geometries.isEmpty()) {
        try {
          Geometry geom = parseGmlToGeometry(gmlText);
          geometries.add(geom);
        } catch (Exception e) {
          // If still fails, throw error
          throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST, "No valid GML geometry found in file");
        }
      }

      // Step 4, 5: Validate, thin, and convert all geometries to GeoJSON features
      GeometryJSON gjson = new GeometryJSON();
      ArrayNode features = mapper.createArrayNode();
      for (Geometry geom : geometries) {
        // 4: Validate geometry (no curves) and within BC boundary
        validateGmlGeometryAndBoundary(geom, crsCode);
        // 5: Vertex Thinning (Douglas-Peucker)
        int originalCoords = countCoordinates(geom);
        Geometry thinned = thinGeometry(geom, crsCode);
        int thinnedCoords = countCoordinates(thinned);
        int removed = originalCoords - thinnedCoords;
        log.info(
            "Thinning removed {} of {} coordinates ({} remain)",
            removed,
            originalCoords,
            thinnedCoords);
        // Reproject to EPSG:4326 if needed
        Geometry finalGeom = reprojectTo4326(thinned, crsCode);
        // Convert thinned/reprojected geometry to GeoJSON
        String geojson = gjson.toString(finalGeom);
        JsonNode geomNode = mapper.readTree(geojson);
        ObjectNode feature = mapper.createObjectNode();
        feature.put("type", "Feature");
        feature.set("geometry", geomNode);
        feature.set("properties", mapper.createObjectNode());
        features.add(feature);
      }

      // Step 5: Return ExtractedGeoDataDto with metaData as null
      ObjectNode fc = mapper.createObjectNode();
      fc.put("type", "FeatureCollection");
      fc.set("features", features);
      return ExtractedGeoDataDto.builder().metaData(null).geoJson(fc).build();
    } catch (Exception e) {
      log.error("Failed to process GML file", e);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to process GML file.");
    }
  }

  /**
   * Parses a GML string into a JTS Geometry using GeoTools GML2 or GML3 parser. Auto-detects GML2
   * vs GML3 based on XML content.
   *
   * @param gml the GML geometry string
   * @return parsed JTS Geometry
   * @throws Exception if parsing fails or geometry is invalid
   */
  private Geometry parseGmlToGeometry(String gml) throws Exception {
    try (InputStream is = new ByteArrayInputStream(gml.getBytes())) {
      // Auto-detect GML2 vs GML3
      boolean isGml2 = gml.contains("<gml:coordinates") || gml.contains("<coordinates");
      boolean isGml3 = gml.contains("<gml:posList") || gml.contains("<posList");
      Parser parser;
      if (isGml2 && !isGml3) {
        parser = new Parser(new org.geotools.gml2.GMLConfiguration());
      } else {
        parser = new Parser(new org.geotools.gml3.GMLConfiguration());
      }
      Object parsed = parser.parse(is);
      if (parsed instanceof Geometry geometry) {
        return geometry;
      } else {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "GML does not contain a valid geometry.");
      }
    }
  }

  /**
   * Validates that the GML geometry is a simple feature (no curves) and is fully within the
   * Province of BC boundary. Throws ResponseStatusException if validation fails.
   *
   * @param geometry the JTS Geometry to validate
   * @param crsCode the EPSG code as a string ("3005" or "4326")
   * @throws ResponseStatusException if geometry is not simple or not within BC boundary
   */
  private void validateGmlGeometryAndBoundary(Geometry geometry, String crsCode) {
    // Check for simple feature type (no curves)
    String geomType = geometry.getGeometryType();
    if (!isSimpleFeatureType(geomType)) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          String.format(
              "GML geometry type '%s' is not a simple feature (no curves allowed)", geomType));
    }

    // Validate geometry is within BC boundary
    try {
      String boundaryFile;
      if ("4326".equals(crsCode)) {
        boundaryFile = "static/BC_BOUNDARY_EPSG4326.geojson";
      } else if ("3005".equals(crsCode)) {
        boundaryFile = "static/BC_BOUNDARY_EPSG3005.geojson";
      } else {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Unsupported CRS for BC boundary validation: EPSG:" + crsCode);
      }
      InputStream is = getClass().getClassLoader().getResourceAsStream(boundaryFile);
      if (is == null) {
        throw new ResponseStatusException(
            HttpStatus.INTERNAL_SERVER_ERROR, "BC boundary file not found: " + boundaryFile);
      }
      String bcGeojson = new String(is.readAllBytes());
      JsonNode bcRoot = mapper.readTree(bcGeojson);
      JsonNode bcFeatures = bcRoot.get("features");
      if (bcFeatures == null || !bcFeatures.isArray() || bcFeatures.size() == 0) {
        throw new ResponseStatusException(
            HttpStatus.INTERNAL_SERVER_ERROR, "BC boundary file is invalid: " + boundaryFile);
      }
      // Assume first feature is the BC boundary polygon
      JsonNode bcGeomNode = bcFeatures.get(0).get("geometry");
      GeometryJSON gjson = new GeometryJSON();
      Geometry bcBoundary = gjson.read(new StringReader(bcGeomNode.toString()));

      if (!bcBoundary.contains(geometry)) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "GML geometry is not fully within the Province of BC boundary.");
      }
      log.info("GML geometry is within the Province of BC boundary.");
    } catch (ResponseStatusException e) {
      throw e;
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "BC boundary validation failed: " + e.getMessage());
    }
  }

  /**
   * Detects the CRS code from a GML or ESF/XML file by searching for srsName attributes in geometry
   * elements. Only EPSG:3005 and EPSG:4326 are supported. Defaults to "4326" if not found.
   *
   * @param file the uploaded GML or ESF/XML file
   * @return the EPSG code as a string ("3005" or "4326")
   * @throws ResponseStatusException if an unsupported CRS is found
   */
  private String detectGmlCrs(MultipartFile file) throws Exception {
    String gmlText = new String(file.getBytes());
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    // Secure XML parsing configuration to prevent XXE
    dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
    dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

    DocumentBuilder db = dbf.newDocumentBuilder();
    Document doc = db.parse(new ByteArrayInputStream(gmlText.getBytes()));
    String[] geomTags = {
      "MultiPolygon", "Polygon", "LineString", "MultiLineString", "Point", "MultiPoint"
    };
    for (String tag : geomTags) {
      NodeList nodes = doc.getElementsByTagNameNS("http://www.opengis.net/gml", tag);
      for (int i = 0; i < nodes.getLength(); i++) {
        Node node = nodes.item(i);
        NamedNodeMap attrs = node.getAttributes();
        if (attrs != null) {
          Node srsAttr = attrs.getNamedItem("srsName");
          if (srsAttr != null) {
            String srsName = srsAttr.getNodeValue();
            if (srsName.endsWith("EPSG:3005")) {
              return "3005";
            } else if (srsName.endsWith("EPSG:4326")) {
              return "4326";
            } else {
              throw new ResponseStatusException(
                  HttpStatus.BAD_REQUEST,
                  "Unsupported or invalid CRS in GML: "
                      + srsName
                      + ". Only EPSG:3005 and EPSG:4326 are supported.");
            }
          }
        }
      }
    }
    // If no srsName attribute found, default to 4326
    return "4326";
  }

  /**
   * Applies Douglas-Peucker vertex thinning to all features in the GeoJSON using the appropriate
   * tolerance. Tolerance is determined by CRS: meters for EPSG:3005, degrees for EPSG:4326.
   *
   * @param geojson the input GeoJSON string
   * @param crsCode the EPSG code as a string ("3005" or "4326")
   * @return the thinned GeoJSON as a string
   * @throws ResponseStatusException if thinning fails or input is invalid
   */
  private String geoJsonThinning(String geojson, String crsCode) {
    try {
      GeometryJSON gjson = new GeometryJSON();
      JsonNode root = mapper.readTree(geojson);
      double tolerance;
      if ("3005".equals(crsCode)) {
        tolerance = SilvaConstants.THINNING_TOLERANCE_METERS;
      } else {
        tolerance = SilvaConstants.THINNING_TOLERANCE_DEGREES;
      }

      if (!root.has("features") || !root.get("features").isArray()) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "GeoJSON missing 'features' array");
      }

      int featureIdx = 0;
      for (JsonNode feature : root.get("features")) {
        featureIdx++;
        JsonNode geomNode = feature.get("geometry");
        if (geomNode == null || geomNode.isNull()) continue;
        Geometry geom = gjson.read(new StringReader(geomNode.toString()));

        // Count original coordinates (only for Polygon/MultiPolygon)
        int originalCoords = countCoordinates(geom);

        Geometry simplified = DouglasPeuckerSimplifier.simplify(geom, tolerance);

        int thinnedCoords = countCoordinates(simplified);

        int removed = originalCoords - thinnedCoords;
        log.info(
            "Feature {}: Thinning removed {} of {} coordinates ({} remain)",
            featureIdx,
            removed,
            originalCoords,
            thinnedCoords);

        // Replace geometry in feature
        ((ObjectNode) feature).set("geometry", mapper.readTree(gjson.toString(simplified)));
      }
      // Return the updated GeoJSON as a string
      log.info("GeoJSON thinning complete");
      return mapper.writeValueAsString(root);
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "GeoJSON thinning failed: " + e.getMessage());
    }
  }

  /**
   * Counts the number of coordinates in a JTS Geometry (Polygon, MultiPolygon, LineString, etc.).
   *
   * @param geometry the JTS Geometry
   * @return total number of coordinates in all sub-geometries
   */
  private int countCoordinates(Geometry geometry) {
    if (geometry == null) return 0;
    int count = 0;
    for (int i = 0; i < geometry.getNumGeometries(); i++) {
      Geometry g = geometry.getGeometryN(i);
      count += g.getNumPoints();
    }
    return count;
  }

  /**
   * Validates that all features in the uploaded GeoJSON are within the Province of BC boundary.
   * Loads the boundary GeoJSON from resources/static and checks containment for each feature.
   * Throws ResponseStatusException if any feature is outside the boundary.
   *
   * @param geojson the input GeoJSON string
   * @param crsCode the EPSG code as a string ("3005" or "4326")
   * @throws ResponseStatusException if any feature is outside the BC boundary
   */
  private void validateGeoJsonWithinBcBoundary(String geojson, String crsCode) {
    try {
      // Select BC boundary file based on CRS
      String boundaryFile;
      if ("4326".equals(crsCode)) {
        boundaryFile = "static/BC_BOUNDARY_EPSG4326.geojson";
      } else if ("3005".equals(crsCode)) {
        boundaryFile = "static/BC_BOUNDARY_EPSG3005.geojson";
      } else {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Unsupported CRS for BC boundary validation: EPSG:" + crsCode);
      }

      InputStream is = getClass().getClassLoader().getResourceAsStream(boundaryFile);
      if (is == null) {
        throw new ResponseStatusException(
            HttpStatus.INTERNAL_SERVER_ERROR, "BC boundary file not found: " + boundaryFile);
      }
      String bcGeojson = new String(is.readAllBytes());
      JsonNode bcRoot = mapper.readTree(bcGeojson);
      JsonNode bcFeatures = bcRoot.get("features");
      if (bcFeatures == null || !bcFeatures.isArray() || bcFeatures.size() == 0) {
        throw new ResponseStatusException(
            HttpStatus.INTERNAL_SERVER_ERROR, "BC boundary file is invalid: " + boundaryFile);
      }
      // Assume first feature is the BC boundary polygon
      JsonNode bcGeomNode = bcFeatures.get(0).get("geometry");
      GeometryJSON gjson = new GeometryJSON();
      Geometry bcBoundary = gjson.read(new StringReader(bcGeomNode.toString()));

      // Parse uploaded geojson
      JsonNode root = mapper.readTree(geojson);
      JsonNode features = root.get("features");
      if (features == null || !features.isArray()) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "GeoJSON missing 'features' array");
      }
      int i = 0;
      for (JsonNode f : features) {
        i++;
        JsonNode geomNode = f.get("geometry");
        if (geomNode == null || geomNode.isNull()) {
          throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST, "Feature " + i + " missing geometry");
        }
        Geometry featureGeom = gjson.read(new StringReader(geomNode.toString()));
        if (!bcBoundary.contains(featureGeom)) {
          throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST,
              String.format("Feature %d is not fully within the Province of BC boundary.", i));
        }
      }
      log.info("All features are within the Province of BC boundary.");
    } catch (ResponseStatusException e) {
      throw e;
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "BC boundary validation failed: " + e.getMessage());
    }
  }

  /**
   * Validates the geometry of a GeoJSON file using GeoTools/JTS. Checks for valid geometry type and
   * topology for all features. Throws ResponseStatusException if any geometry is invalid.
   *
   * @param geojson the input GeoJSON string
   * @throws ResponseStatusException if any geometry is invalid or not a simple feature
   */
  private void validateGeoJsonGeometry(String geojson) {
    try {
      JsonNode root = mapper.readTree(geojson);

      if (!root.has("type")) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "GeoJSON missing 'type'");
      }

      String type = root.get("type").asText();
      GeometryJSON gjson = new GeometryJSON();

      switch (type) {
        case "FeatureCollection":
          JsonNode features = root.get("features");
          if (features == null || !features.isArray()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "FeatureCollection missing 'features' array");
          }
          int i = 0;
          for (JsonNode f : features) {
            i++;
            JsonNode geomNode = f.get("geometry");
            validateGeoJsonGeometryNode(gjson, geomNode, i);
          }
          break;
        case "Feature":
          validateGeoJsonGeometryNode(gjson, root.get("geometry"), 1);
          break;
        default:
          // Assume it's a raw Geometry object
          validateGeoJsonGeometryNode(gjson, root, 1);
          break;
      }
      log.info("All geometries in the GeoJSON file have been validated and are valid.");
    } catch (ResponseStatusException e) {
      throw e;
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Failed geometry validation: " + e.getMessage());
    }
  }

  /**
   * Validates a single GeoJSON geometry node for type and topology. Throws ResponseStatusException
   * if invalid or not a simple feature.
   *
   * @param gjson GeometryJSON instance
   * @param geomNode the geometry node to validate
   * @param index feature index (for error reporting)
   * @throws ResponseStatusException if geometry is invalid or not a simple feature
   */
  private void validateGeoJsonGeometryNode(GeometryJSON gjson, JsonNode geomNode, int index) {
    if (geomNode == null || geomNode.isNull()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Feature " + index + " missing geometry");
    }
    // Check for simple feature geometry type (no curves)
    if (!geomNode.has("type")) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Feature " + index + " geometry missing 'type'");
    }
    String geomType = geomNode.get("type").asText();
    if (!isSimpleFeatureType(geomType)) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          String.format(
              "Feature %d geometry type '%s' is not a simple feature (no curves allowed)",
              index, geomType));
    }
    try {
      Geometry geometry = gjson.read(new StringReader(geomNode.toString()));
      IsValidOp validator = new IsValidOp(geometry);
      TopologyValidationError err = validator.getValidationError();
      if (err != null) {
        String message =
            String.format(
                "Invalid geometry at feature %d: %s (coord=%s)",
                index, err.getMessage(), err.getCoordinate());
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
      }
    } catch (ResponseStatusException e) {
      throw e;
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Invalid geometry at feature " + index + ": " + e.getMessage());
    }
  }

  /**
   * Detects the CRS code from a GeoJSON root node. Defaults to "4326" if not specified. Supports
   * legacy 'crs' property and RFC 7946 default.
   *
   * @param root the root JsonNode of the GeoJSON
   * @return the EPSG code as a string ("3005" or "4326")
   */
  private String detectGeoJsonCrs(JsonNode root) {
    String crsCode = null;
    // GeoJSON RFC 7946: CRS is always EPSG:4326, but older files may have a 'crs' property
    if (root.has("crs")) {
      JsonNode crsNode = root.get("crs");
      // Try to extract EPSG code from crs property
      if (crsNode.has("properties")) {
        JsonNode props = crsNode.get("properties");
        if (props.has("name")) {
          String name = props.get("name").asText();
          if (name.contains("EPSG::")) {
            crsCode = name.substring(name.indexOf("EPSG::") + 6);
          } else if (name.contains("EPSG:")) {
            crsCode = name.substring(name.indexOf("EPSG:") + 5);
          } else {
            crsCode = name;
          }
        }
      }
    }
    if (crsCode == null || crsCode.isEmpty()) {
      // Per RFC 7946, default to EPSG:4326
      crsCode = "4326";
    }
    return crsCode;
  }

  /**
   * Returns true if the geometry type is a simple feature (no curves). Accepts Point, MultiPoint,
   * LineString, MultiLineString, Polygon, MultiPolygon.
   *
   * @param geomType the geometry type string
   * @return true if simple feature, false otherwise
   */
  private static boolean isSimpleFeatureType(String geomType) {
    switch (geomType) {
      case "Point":
      case "MultiPoint":
      case "LineString":
      case "MultiLineString":
      case "Polygon":
      case "MultiPolygon":
        return true;
      default:
        return false;
    }
  }

  /**
   * Applies Douglas-Peucker vertex thinning to a geometry using the appropriate tolerance.
   * Tolerance is determined by CRS: meters for EPSG:3005, degrees for EPSG:4326.
   *
   * @param geometry the input JTS geometry
   * @param crsCode the EPSG code as a string ("3005" or "4326")
   * @return the thinned geometry
   */
  private Geometry thinGeometry(Geometry geometry, String crsCode) {
    double tolerance =
        crsCode.equals("3005")
            ? SilvaConstants.THINNING_TOLERANCE_METERS
            : SilvaConstants.THINNING_TOLERANCE_DEGREES;
    return DouglasPeuckerSimplifier.simplify(geometry, tolerance);
  }

  /**
   * Extracts all GML geometry elements (Polygon, MultiPolygon, etc.) from a GML/XML string using
   * XML parsing. Preserves namespaces and returns a list of geometry XML strings for further
   * parsing.
   *
   * @param gmlText the GML/XML string
   * @return list of geometry XML strings
   * @throws Exception if XML parsing fails
   */
  private List<String> extractGmlGeometriesXml(String gmlText) throws Exception {
    List<String> geometries = new ArrayList<>();
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    // Secure XML parser configuration: disable DTD, XXE
    dbf.setNamespaceAware(true);
    dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
    dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    dbf.setXIncludeAware(false);
    dbf.setExpandEntityReferences(false);

    DocumentBuilder db = dbf.newDocumentBuilder();
    Document doc = db.parse(new ByteArrayInputStream(gmlText.getBytes()));
    String[] geomTags = {
      "MultiPolygon", "Polygon", "LineString", "MultiLineString", "Point", "MultiPoint"
    };
    for (String tag : geomTags) {
      NodeList nodes = doc.getElementsByTagNameNS("http://www.opengis.net/gml", tag);
      for (int i = 0; i < nodes.getLength(); i++) {
        Node node = nodes.item(i);
        // Serialize node back to string, including namespace context
        TransformerFactory tf = TransformerFactory.newInstance();
        // Harden TransformerFactory against XXE / external resource access
        try {
          tf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        } catch (Exception e) {
          // Some TransformerFactory implementations (or older JDKs) may not support
          // setting this feature. This is non-fatal; rely on parser-level protections.
          log.debug("Could not set TransformerFactory FEATURE_SECURE_PROCESSING", e);
        }
        try {
          tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        } catch (Exception e) {
          // Best-effort hardening; if unsupported we proceed. Log at debug level
          // so issues can be diagnosed in environments with incompatible factories.
          log.debug("Could not set TransformerFactory ACCESS_EXTERNAL_DTD", e);
        }
        try {
          tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        } catch (Exception e) {
          // Best-effort hardening; if unsupported we proceed. Log at debug level
          // so issues can be diagnosed in environments with incompatible factories.
          log.debug("Could not set TransformerFactory ACCESS_EXTERNAL_STYLESHEET", e);
        }
        Transformer transformer = tf.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(node), new StreamResult(writer));
        String geomXml = writer.toString();
        // Add xmlns:gml if not present (for fragments)
        if (!geomXml.contains("xmlns:gml")) {
          geomXml = geomXml.replaceFirst(">", " xmlns:gml=\"http://www.opengis.net/gml\">");
        }
        geometries.add(geomXml);
      }
    }
    return geometries;
  }

  /**
   * Extracts the GML fragment for the general area definition geometry (OpeningDefinition/extentOf)
   * from ESF/XML. Uses XPath to locate the geometry node and serializes it to a string. Throws
   * ResponseStatusException if not found.
   *
   * @param xmlText the ESF/XML string
   * @return GML fragment as a string
   * @throws ResponseStatusException if geometry is not found or extraction fails
   */
  private String extractGeneralAreaGmlFragment(String xmlText) {
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(true);
      // Prevent XXE by disabling DTDs and external entities
      dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
      dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
      dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
      dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
      dbf.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD", "");
      dbf.setAttribute("http://javax.xml.XMLConstants/property/accessExternalSchema", "");

      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(new ByteArrayInputStream(xmlText.getBytes()));
      XPathFactory xpf = XPathFactory.newInstance();
      XPath xpath = xpf.newXPath();
      NamespaceContext nsContext =
          new NamespaceContext() {
            public String getNamespaceURI(String prefix) {
              switch (prefix) {
                case "rst":
                  return "http://www.for.gov.bc.ca/schema/results";
                case "gml":
                  return "http://www.opengis.net/gml";
                case "esf":
                  return "http://www.for.gov.bc.ca/schema/esf";
                default:
                  return javax.xml.XMLConstants.NULL_NS_URI;
              }
            }

            public String getPrefix(String uri) {
              return null;
            }

            public java.util.Iterator<String> getPrefixes(String uri) {
              return null;
            }
          };
      xpath.setNamespaceContext(nsContext);
      String expr =
          "/esf:ESFSubmission/esf:submissionContent/rst:ResultsSubmission/rst:submissionItem/rst:Opening/rst:definedBy/rst:OpeningDefinition/rst:extentOf/*";
      Node geomNode = (Node) xpath.evaluate(expr, doc, XPathConstants.NODE);
      if (geomNode == null) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "No general area GML geometry found in ESF/XML file");
      }
      TransformerFactory tf = TransformerFactory.newInstance();
      // Harden TransformerFactory against XXE / external resource access
      try {
        tf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
      } catch (Exception e) {
        // Some TransformerFactory implementations (or older JDKs) may not support
        // setting this feature. This is non-fatal; rely on parser-level protections.
        log.debug("Could not set TransformerFactory FEATURE_SECURE_PROCESSING", e);
      }
      try {
        tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
      } catch (Exception e) {
        // Best-effort hardening; if unsupported we proceed. Log at debug level
        // so issues can be diagnosed in environments with incompatible factories.
        log.debug("Could not set TransformerFactory ACCESS_EXTERNAL_DTD", e);
      }
      try {
        tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
      } catch (Exception e) {
        // Best-effort hardening; if unsupported we proceed. Log at debug level
        // so issues can be diagnosed in environments with incompatible factories.
        log.debug("Could not set TransformerFactory ACCESS_EXTERNAL_STYLESHEET", e);
      }
      Transformer transformer = tf.newTransformer();
      StringWriter writer = new StringWriter();
      transformer.transform(new DOMSource(geomNode), new StreamResult(writer));
      return writer.toString();
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Failed to extract general area GML: " + e.getMessage());
    }
  }

  /**
   * Extracts orgUnit, openingGrossArea, maxAllowablePermAccessPerc, and openingCategory from ESF
   * XML text. Uses XPath to locate and parse metadata fields.
   *
   * @param xmlText the ESF/XML string
   * @return GeoMetaDataDto with extracted metadata fields
   */
  private GeoMetaDataDto extractEsfMetaData(String xmlText) {
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(true);
      // Prevent XXE and DTD attacks
      dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
      dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
      dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
      dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
      dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
      dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(new ByteArrayInputStream(xmlText.getBytes()));
      XPathFactory xpf = XPathFactory.newInstance();
      XPath xpath = xpf.newXPath();
      NamespaceContext nsContext =
          new NamespaceContext() {
            public String getNamespaceURI(String prefix) {
              switch (prefix) {
                case "rst":
                  return "http://www.for.gov.bc.ca/schema/results";
                case "gml":
                  return "http://www.opengis.net/gml";
                case "esf":
                  return "http://www.for.gov.bc.ca/schema/esf";
                case "mof":
                  return "http://www.for.gov.bc.ca/schema/base";
                default:
                  return XMLConstants.NULL_NS_URI;
              }
            }

            public String getPrefix(String uri) {
              return null;
            }

            public Iterator<String> getPrefixes(String uri) {
              return null;
            }
          };
      xpath.setNamespaceContext(nsContext);

      String orgUnit =
          (String)
              xpath.evaluate(
                  "/esf:ESFSubmission/esf:submissionContent/rst:ResultsSubmission/rst:submissionMetadataProperty/rst:SubmissionMetadata/rst:districtCode",
                  doc,
                  XPathConstants.STRING);
      String openingGrossAreaStr =
          (String)
              xpath.evaluate(
                  "/esf:ESFSubmission/esf:submissionContent/rst:ResultsSubmission/rst:submissionItem/rst:Opening/rst:definedBy/rst:OpeningDefinition/rst:openingGrossArea",
                  doc,
                  XPathConstants.STRING);
      String maxAllowablePermAccessPerc =
          (String)
              xpath.evaluate(
                  "/esf:ESFSubmission/esf:submissionContent/rst:ResultsSubmission/rst:submissionItem/rst:Opening/rst:definedBy/rst:OpeningDefinition/rst:maximumAllowableSoilDisturbancePercentage",
                  doc,
                  XPathConstants.STRING);

      BigDecimal openingGrossAreaVal = null;
      try {
        if (openingGrossAreaStr != null && !openingGrossAreaStr.isBlank()) {
          openingGrossAreaVal = new BigDecimal(openingGrossAreaStr);
        }
      } catch (Exception e) {
        log.info("Failed to parse openingGrossArea as BigDecimal: {}", e.getMessage());
      }

      BigDecimal maxPermAccessPercVal = null;
      try {
        if (maxAllowablePermAccessPerc != null && !maxAllowablePermAccessPerc.isBlank()) {
          maxPermAccessPercVal = new BigDecimal(maxAllowablePermAccessPerc);
        }
      } catch (Exception e) {
        log.info("Failed to parse maxAllowablePermAccessPerc as BigDecimal: {}", e.getMessage());
      }

      return new GeoMetaDataDto(
          orgUnit,
          null, // openingCategory
          openingGrossAreaVal,
          maxPermAccessPercVal);
    } catch (Exception e) {
      log.info("Failed to extract ESF metadata: {}", e.getMessage());
      return new GeoMetaDataDto(null, null, null, null);
    }
  }

  /**
   * Extracts tenure information from ESF XML and returns a list of TenureDto objects. Uses XPath to
   * locate Tenure nodes and parses relevant fields.
   *
   * @param xmlText the ESF/XML string
   * @return list of TenureDto objects
   */
  private List<TenureDto> extractEsfTenureList(String xmlText) {
    List<TenureDto> result = new ArrayList<>();
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(true);
      dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
      dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
      dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
      dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
      dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(new ByteArrayInputStream(xmlText.getBytes()));
      XPathFactory xpf = XPathFactory.newInstance();
      XPath xpath = xpf.newXPath();
      NamespaceContext nsContext =
          new NamespaceContext() {
            public String getNamespaceURI(String prefix) {
              switch (prefix) {
                case "rst":
                  return "http://www.for.gov.bc.ca/schema/results";
                case "gml":
                  return "http://www.opengis.net/gml";
                case "esf":
                  return "http://www.for.gov.bc.ca/schema/esf";
                case "mof":
                  return "http://www.for.gov.bc.ca/schema/base";
                default:
                  return javax.xml.XMLConstants.NULL_NS_URI;
              }
            }

            public String getPrefix(String uri) {
              return null;
            }

            public Iterator<String> getPrefixes(String uri) {
              return null;
            }
          };
      xpath.setNamespaceContext(nsContext);

      // Find all rst:Tenure nodes under rst:tenureProperty
      String tenureExpr =
          "/esf:ESFSubmission/esf:submissionContent/rst:ResultsSubmission/rst:submissionItem/rst:Opening/rst:definedBy/rst:OpeningDefinition/rst:tenureProperty/rst:Tenure";
      NodeList tenureNodes = (NodeList) xpath.evaluate(tenureExpr, doc, XPathConstants.NODESET);
      boolean foundPrimary = false;
      for (int i = 0; i < tenureNodes.getLength(); i++) {
        Node tenureNode = tenureNodes.item(i);
        // Extract fields
        String forestFileId = null;
        String cutBlock = null;
        String cuttingPermit = null;
        boolean isPrimary = false;

        // licenceNumber
        NodeList licenceNodes =
            ((Element) tenureNode)
                .getElementsByTagNameNS("http://www.for.gov.bc.ca/schema/results", "licenceNumber");
        if (licenceNodes.getLength() > 0) {
          forestFileId = licenceNodes.item(0).getTextContent();
        }
        // cutblock
        NodeList cutBlockNodes =
            ((Element) tenureNode)
                .getElementsByTagNameNS("http://www.for.gov.bc.ca/schema/results", "cutblock");
        if (cutBlockNodes.getLength() > 0) {
          cutBlock = cutBlockNodes.item(0).getTextContent();
        }
        // cuttingPermitID
        NodeList cuttingPermitNodes =
            ((Element) tenureNode)
                .getElementsByTagNameNS(
                    "http://www.for.gov.bc.ca/schema/results", "cuttingPermitID");
        if (cuttingPermitNodes.getLength() > 0) {
          cuttingPermit = cuttingPermitNodes.item(0).getTextContent();
        }
        // primeLicenceIndicator
        NodeList primeNodes =
            ((Element) tenureNode)
                .getElementsByTagNameNS(
                    "http://www.for.gov.bc.ca/schema/results", "primeLicenceIndicator");
        if (primeNodes.getLength() > 0) {
          String primeText = primeNodes.item(0).getTextContent();
          isPrimary = "true".equalsIgnoreCase(primeText.trim());
          if (isPrimary) foundPrimary = true;
        }

        result.add(
            new TenureDto(
                isPrimary, forestFileId, cutBlock, cuttingPermit, null // timberMark ignored for now
                ));
      }
      // If no primary found, set first as primary if exists
      if (!foundPrimary && !result.isEmpty()) {
        TenureDto first = result.get(0);
        result.set(
            0,
            new TenureDto(
                true, first.forestFileId(), first.cutBlock(), first.cuttingPermit(), null));
      }
    } catch (Exception e) {
      log.info("Failed to extract tenure list from ESF: {}", e.getMessage());
    }
    return result;
  }

  /**
   * Reprojects a JTS Geometry to EPSG:4326 if the source CRS is not already 4326. Uses axis order
   * hint for correct GeoJSON output.
   *
   * @param geometry the input JTS geometry
   * @param sourceCrsCode the EPSG code as a string ("3005" or "4326")
   * @return reprojected geometry if needed, otherwise original geometry
   */
  private Geometry reprojectTo4326(Geometry geometry, String sourceCrsCode) {
    if ("4326".equals(sourceCrsCode)) {
      return geometry;
    }
    try {
      CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:" + sourceCrsCode);
      CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4326", true);
      MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, true);
      return JTS.transform(geometry, transform);
    } catch (Exception e) {
      log.warn("Failed to reproject geometry to EPSG:4326: {}", e.getMessage());
      return geometry;
    }
  }
}
