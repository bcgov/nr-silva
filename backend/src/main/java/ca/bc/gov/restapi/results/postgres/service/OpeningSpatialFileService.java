package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.postgres.SilvaConstants;
import ca.bc.gov.restapi.results.postgres.dto.ExtractedGeoDataDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.InputStream;
import java.io.StringReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.operation.valid.IsValidOp;
import org.locationtech.jts.operation.valid.TopologyValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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
   * Process an uploaded spatial file. Depending on the file extension, delegates to the appropriate
   * handler.
   *
   * @param file the uploaded spatial file
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

    if (lowerName.endsWith(".xml")) {
      processEsf(file);
      return null;
    } else if (lowerName.endsWith(".gml")) {
      return processGml(file);
    } else if (lowerName.endsWith(".geojson") || lowerName.endsWith(".json")) {
      return processGeojson(file);
    } else {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Unsupported file type: " + fileName);
    }
  }

  private void processEsf(MultipartFile file) {
    // TODO implement ESF processing
    log.info("Processing ESF/XML file: {}", file.getOriginalFilename());
  }

  private ExtractedGeoDataDto processGml(MultipartFile file) {
    log.info("Processing GML file: {}", file.getOriginalFilename());
    try {
      // Step 1: Detect CRS
      String crsCode = detectGmlCrs(file);
      log.info("Detected CRS for GML: EPSG:{}", crsCode);

      // Step 2: Parse GML to JTS Geometry
      Geometry gmlGeometry = parseGmlToGeometry(file);

      // Step 3, 4: Validate geometry (no curves) and within BC boundary
      validateGmlGeometryAndBoundary(gmlGeometry, crsCode);

      // Step 5: Vertex Thinning (Douglas-Peucker)
      int originalCoords = countCoordinates(gmlGeometry);
      Geometry thinned = thinGeometry(gmlGeometry, crsCode);
      int thinnedCoords = countCoordinates(thinned);
      int removed = originalCoords - thinnedCoords;
      log.info(
          "Thinning removed {} of {} coordinates ({} remain)",
          removed,
          originalCoords,
          thinnedCoords);

      // Convert thinned geometry to GeoJSON
      GeometryJSON gjson = new GeometryJSON();
      String geojson = gjson.toString(thinned);
      JsonNode geoJsonNode = mapper.readTree(geojson);

      // Return ExtractedGeoDataDto with metaData as null
      return ExtractedGeoDataDto.builder().metaData(null).geoJson(geoJsonNode).build();
    } catch (Exception e) {
      log.error("Failed to process GML file", e);
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Invalid GML file: " + e.getMessage());
    }
  }

  /** Parses the GML file into a JTS Geometry using GeoTools GML parser. */
  private Geometry parseGmlToGeometry(MultipartFile file) throws Exception {
    org.geotools.xsd.Parser parser =
        new org.geotools.xsd.Parser(new org.geotools.gml3.GMLConfiguration());
    try (InputStream is = file.getInputStream()) {
      Object parsed = parser.parse(is);
      if (parsed instanceof Geometry) {
        return (Geometry) parsed;
      } else {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "GML file does not contain a valid geometry.");
      }
    }
  }

  /**
   * Validates that the GML geometry is a simple feature (no curves) and is fully within the BC
   * boundary (GeoJSON, CRS-dependent).
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
   * Detects the CRS code from a GML file by searching for srsName attributes. Defaults to "4326" if
   * not found.
   *
   * @param file the uploaded GML file
   * @return the EPSG code as a string
   */
  private String detectGmlCrs(MultipartFile file) throws Exception {
    String gmlText = new String(file.getBytes());
    if (gmlText.contains("srsName=\"EPSG:3005\"")) {
      return "3005";
    } else if (gmlText.contains("srsName=\"EPSG:4326\"")) {
      return "4326";
    }
    // Default to 4326 if not specified
    return "4326";
  }

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

      // Return ExtractedGeoDataDto with metaData as null
      return ExtractedGeoDataDto.builder().metaData(null).geoJson(thinnedNode).build();
    } catch (Exception e) {
      log.error("Failed to process GeoJSON file", e);
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Invalid GeoJSON file: " + e.getMessage());
    }
  }

  /**
   * Applies Douglas-Peucker vertex thinning to all features in the GeoJSON using the appropriate
   * tolerance.
   *
   * <p>Tolerance values:
   *
   * <ul>
   *   <li>EPSG:3005 (meters): {@link SilvaConstants#THINNING_TOLERANCE_METERS}
   *   <li>EPSG:4326 (degrees): {@link SilvaConstants#THINNING_TOLERANCE_DEGREES}
   * </ul>
   *
   * @param geojson the input GeoJSON string
   * @param crsCode the EPSG code as a string ("3005" or "4326")
   * @return the thinned GeoJSON as a string
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
        Geometry geom = gjson.read(new java.io.StringReader(geomNode.toString()));

        // Count original coordinates (only for Polygon/MultiPolygon)
        int originalCoords = countCoordinates(geom);

        Geometry simplified =
            org.locationtech.jts.simplify.DouglasPeuckerSimplifier.simplify(geom, tolerance);

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

  /** Counts the number of coordinates in a Geometry (Polygon, MultiPolygon, LineString, etc.) */
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
   * Validates that all features in the uploaded GeoJSON are within the BC boundary. Loads the
   * ABMS_PROVINCE_SP_2025.geojson from resources/static. Throws ResponseStatusException if any
   * feature is outside the BC boundary.
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
   * Validates the geometry of a GeoJSON file using GeoTools/JTS. Throws ResponseStatusException if
   * any geometry is invalid.
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
   * Detects the CRS code from a GeoJSON root node. Defaults to "4326" if not specified.
   *
   * @param root the root JsonNode of the GeoJSON
   * @return the EPSG code as a string
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

  /** Returns true if the geometry type is a simple feature (no curves). */
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
   *
   * @param geometry The input JTS geometry.
   * @param crsCode The EPSG code as a string ("3005" or "4326").
   * @return The thinned geometry.
   */
  private Geometry thinGeometry(Geometry geometry, String crsCode) {
    double tolerance =
        crsCode.equals("3005")
            ? SilvaConstants.THINNING_TOLERANCE_METERS
            : SilvaConstants.THINNING_TOLERANCE_DEGREES;
    return org.locationtech.jts.simplify.DouglasPeuckerSimplifier.simplify(geometry, tolerance);
  }
}
