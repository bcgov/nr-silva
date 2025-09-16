package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.postgres.SilvaConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
  public void processOpeningSpatialFile(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uploaded file is null or empty");
    }

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
    } else if (lowerName.endsWith(".gml")) {
      processGml(file);
    } else if (lowerName.endsWith(".geojson") || lowerName.endsWith(".json")) {
      processGeojson(file);
    } else {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Unsupported file type: " + fileName);
    }
  }

  private void processEsf(MultipartFile file) {
    // TODO implement ESF processing
    log.info("Processing ESF/XML file: {}", file.getOriginalFilename());
  }

  private void processGml(MultipartFile file) {
    // TODO implement GML processing
    log.info("Processing GML file: {}", file.getOriginalFilename());
  }

  private void processGeojson(MultipartFile file) {
    log.info("Processing GeoJSON file: {}", file.getOriginalFilename());
    try {
      String geojson = new String(file.getBytes());
      JsonNode root = mapper.readTree(geojson);
      String crsCode = detectGeoJsonCrs(root);
      log.info("Detected CRS for GeoJSON: EPSG:{}", crsCode);
      if (!crsCode.equals("4326") && !crsCode.equals("3005")) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "GeoJSON CRS must be EPSG:4326 or EPSG:3005 (BC Albers)");
      }

      // Step 2: Geometry Validity (OGC/ESRI)
      validateGeoJsonGeometry(geojson);

      // Continue with further processing as needed...
    } catch (Exception e) {
      log.error("Failed to process GeoJSON file", e);
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Invalid GeoJSON file: " + e.getMessage());
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
            validateGeometryNode(gjson, geomNode, i);
          }
          break;
        case "Feature":
          validateGeometryNode(gjson, root.get("geometry"), 1);
          break;
        default:
          // Assume it's a raw Geometry object
          validateGeometryNode(gjson, root, 1);
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

  private void validateGeometryNode(GeometryJSON gjson, JsonNode geomNode, int index) {
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
    // Only allow simple feature types
    switch (geomType) {
      case "Point":
      case "MultiPoint":
      case "LineString":
      case "MultiLineString":
      case "Polygon":
      case "MultiPolygon":
        // valid simple feature
        break;
      default:
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            String.format(
                "Feature %d geometry type '%s' is not a simple feature (no curves allowed)",
                index, geomType));
    }
    try {
      Geometry geometry = gjson.read(new java.io.StringReader(geomNode.toString()));
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
}
