package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.postgres.SilvaConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateFilter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.locationtech.jts.io.geojson.GeoJsonWriter;
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.CoordinateTransform;
import org.locationtech.proj4j.CoordinateTransformFactory;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/** This service handles processing of uploaded spatial files (GeoJSON, GML, ESF/XML). */
@Slf4j
@Service
@RequiredArgsConstructor
public class OpeningSpatialFileService {

  private static final CRSFactory CRS_FACTORY = new CRSFactory();
  private static final CoordinateTransformFactory TRANSFORM_FACTORY =
      new CoordinateTransformFactory();
  private static final CoordinateReferenceSystem CRS_3005 =
      CRS_FACTORY.createFromParameters(
          "EPSG:3005",
          "+proj=aea +lat_1=50 +lat_2=58.5 +lat_0=45 +lon_0=-126 "
              + "+x_0=1000000 +y_0=0 +datum=NAD83 +units=m +no_defs");
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
      // Read file content into a string
      String geoJsonContent = new String(file.getBytes());
      // Normalize to EPSG:3005
      String normalizedGeoJson = normalizeTo3005(geoJsonContent);
      // Generalize geometry with 2.5 meters tolerance
      String generalizedGeoJson = generalizeGeoJson(normalizedGeoJson, 2.5);
      // Validate within BC boundary
      String validatedGeoJson = validateWithinBC(generalizedGeoJson);
      // Log the validated result
      log.info("Validated GeoJSON within BC boundary: {}", validatedGeoJson);
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Failed to process GeoJSON file", e);
    }
  }

  private String normalizeTo3005(String geoJson) {
    try {
      JsonNode root = mapper.readTree(geoJson);
      String sourceEpsg = "EPSG:3005"; // default

      if (root.has("crs") && root.get("crs").has("properties")) {
        JsonNode props = root.get("crs").get("properties");
        if (props.has("name")) {
          sourceEpsg = props.get("name").asText();
        }
      }

      if ("EPSG:3005".equalsIgnoreCase(sourceEpsg)) {
        return geoJson;
      }

      log.info("Converting from {} to EPSG:3005", sourceEpsg);

      CoordinateReferenceSystem srcCrs = CRS_FACTORY.createFromName(sourceEpsg);
      CoordinateTransform transform = TRANSFORM_FACTORY.createTransform(srcCrs, CRS_3005);

      GeoJsonReader reader = new GeoJsonReader();
      GeoJsonWriter writer = new GeoJsonWriter();

      if ("FeatureCollection".equalsIgnoreCase(root.get("type").asText())) {
        for (JsonNode feature : root.get("features")) {
          ObjectNode featureObj = (ObjectNode) feature;
          JsonNode geomNode = featureObj.get("geometry");

          Geometry geom = reader.read(geomNode.toString());
          Geometry transformed = transformGeometry(geom, transform);

          featureObj.set("geometry", mapper.readTree(writer.write(transformed)));
        }
        ((ObjectNode) root)
            .putObject("crs")
            .put("type", "name")
            .putObject("properties")
            .put("name", "EPSG:3005");
      }

      return mapper.writeValueAsString(root);

    } catch (Exception e) {
      log.error("Failed to normalize GeoJSON to EPSG:3005", e);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid GeoJSON CRS", e);
    }
  }

  private String generalizeGeoJson(String geoJson, double toleranceMeters) {
    try {
      JsonNode root = mapper.readTree(geoJson);
      GeoJsonReader reader = new GeoJsonReader();
      GeoJsonWriter writer = new GeoJsonWriter();
      writer.setEncodeCRS(false);

      if ("FeatureCollection".equalsIgnoreCase(root.get("type").asText())) {
        for (JsonNode feature : root.get("features")) {
          ObjectNode featureObj = (ObjectNode) feature;
          JsonNode geomNode = featureObj.get("geometry");

          if (geomNode == null || geomNode.isNull()) {
            continue; // skip features without geometry
          }

          Geometry geom = reader.read(geomNode.toString());
          Geometry simplified = DouglasPeuckerSimplifier.simplify(geom, toleranceMeters);

          if (simplified.isEmpty() || !simplified.isValid()) {
            log.warn(
                "Simplified geometry is empty/invalid, falling back to original for feature {}",
                featureObj);
            simplified = geom.buffer(0);
            if (simplified.isEmpty()) {
              simplified = geom; // last fallback
            }
          }

          featureObj.set("geometry", mapper.readTree(writer.write(simplified)));
        }
        ((ObjectNode) root)
            .putObject("crs")
            .put("type", "name")
            .putObject("properties")
            .put("name", "EPSG:3005");
      }

      String result = mapper.writeValueAsString(root);
      log.info("Generalized GeoJSON: {}", result);
      return result;
    } catch (Exception e) {
      log.error("Failed to generalize GeoJSON", e);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to generalize GeoJSON", e);
    }
  }

  private String validateWithinBC(String geoJson) {
    try {
      // Load BC boundary GeoJSON from resources
      ClassPathResource resource = new ClassPathResource("static/ABMS_PROVINCE_SP_2025.geojson");
      InputStream inputStream = resource.getInputStream();
      String bcGeoJson;
      try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
        bcGeoJson = scanner.useDelimiter("\\A").next();
      }

      JsonNode bcRoot = mapper.readTree(bcGeoJson);
      ((ObjectNode) bcRoot).remove("crs");
      String bcGeoJsonNoCrs = mapper.writeValueAsString(bcRoot);

      GeoJsonReader reader = new GeoJsonReader();
      Geometry bcBoundary = reader.read(bcGeoJsonNoCrs);

      JsonNode root = mapper.readTree(geoJson);
      if (!"FeatureCollection".equalsIgnoreCase(root.get("type").asText())) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "GeoJSON must be a FeatureCollection");
      }

      for (JsonNode feature : root.get("features")) {
        JsonNode geomNode = feature.get("geometry");
        if (geomNode == null || geomNode.isNull()) {
          continue; // skip features without geometry
        }
        Geometry geom = reader.read(geomNode.toString());
        if (geom.isEmpty()) {
          log.warn("Skipping empty geometry during BC boundary validation");
          continue;
        }
        if (!(geom.within(bcBoundary) || geom.coveredBy(bcBoundary))) {
          throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST, "Feature geometry is outside BC boundary");
        }
      }

      return geoJson;
    } catch (ResponseStatusException e) {
      throw e;
    } catch (Exception e) {
      log.error("Failed to validate GeoJSON within BC boundary", e);
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Failed to validate GeoJSON within BC boundary", e);
    }
  }

  private Geometry transformGeometry(Geometry geom, CoordinateTransform transform) {
    Geometry copy = geom.copy();
    copy.apply(
        new CoordinateFilter() {
          @Override
          public void filter(Coordinate coord) {
            ProjCoordinate in = new ProjCoordinate(coord.x, coord.y);
            ProjCoordinate out = new ProjCoordinate();
            transform.transform(in, out);
            coord.setX(out.x);
            coord.setY(out.y);
          }
        });
    return copy;
  }
}
