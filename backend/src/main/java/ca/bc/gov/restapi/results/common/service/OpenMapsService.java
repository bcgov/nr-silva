package ca.bc.gov.restapi.results.common.service;

import ca.bc.gov.restapi.results.postgres.dto.MapsheetDto;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

/** This service provides method for doing requests and GET calls. */
@Slf4j
@Service
public class OpenMapsService {

  private final RestClient restClient;

  // null in oracle mode; present in postgres mode to read geometry from the local DB
  @Autowired(required = false)
  private OpeningGeometryService openingGeometryService;

  /**
   * Instantiates a new Open maps service.
   *
   * @param openMapsApi the open maps api
   */
  public OpenMapsService(@Qualifier("openMapsApi") RestClient openMapsApi) {
    this.restClient = openMapsApi;
  }

  /**
   * Get Opening polygons and properties from WFS given an opening id.
   *
   * @param openingId The Opening identification.
   * @return An object with the response from WFS
   */
  public FeatureCollection getOpeningPolygonAndProperties(Long openingId, String kind) {
    if (openingGeometryService != null && isOpeningSvwKind(kind)) {
      return openingGeometryService.getOpeningGeometry(openingId);
    }
    try {
      return restClient
          .get()
          .uri(
              builder ->
                  builder
                      .queryParam("service", "WFS")
                      .queryParam("version", "2.0.0")
                      .queryParam("request", "GetFeature")
                      .queryParam(
                          "typeName",
                          Optional.ofNullable(kind)
                              .orElse("WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW"))
                      .queryParam("outputFormat", "application/json")
                      .queryParam("SrsName", "EPSG:4326")
                      .queryParam("PROPERTYNAME", getPropertyName(kind))
                      .queryParam("CQL_FILTER", "OPENING_ID=" + openingId)
                      .build(Map.of()))
          .retrieve()
          .body(FeatureCollection.class);
    } catch (Exception e) {
      log.error("Exception when fetching from WFS {}", e.getMessage());
    }
    return null;
  }

  /**
   * Derives the BCGS 1:20K mapsheet components for a geographic point.
   *
   * <p>Calls the BC OpenMaps WFS {@code WHSE_BASEMAPPING.BCGS_20K_GRID} layer using a
   * spatial intersect filter and parses the 7-character {@code MAP_TILE} attribute
   * (e.g. {@code "092L057"}) into its components.
   *
   * @param lon longitude in decimal degrees (EPSG:4326)
   * @param lat latitude in decimal degrees (EPSG:4326)
   * @return a {@link MapsheetDto} containing the parsed mapsheet components
   * @throws ResponseStatusException with HTTP 422 when the WFS call fails or no tile is found
   */
  public MapsheetDto getMapsheetForPoint(double lon, double lat) {
    try {
      FeatureCollection fc = restClient
          .get()
          .uri(
              builder ->
                  builder
                      .queryParam("service", "WFS")
                      .queryParam("version", "2.0.0")
                      .queryParam("request", "GetFeature")
                      .queryParam("typeName", "WHSE_BASEMAPPING.BCGS_20K_GRID")
                      .queryParam("outputFormat", "application/json")
                      .queryParam("SrsName", "EPSG:4326")
                      .queryParam("PROPERTYNAME", "MAP_TILE")
                      .queryParam("CQL_FILTER", "INTERSECTS(GEOMETRY, SRID=4326;POINT(" + lon + " " + lat + "))")
                      .build(Map.of()))
          .retrieve()
          .body(FeatureCollection.class);

      if (fc == null || fc.getFeatures() == null || fc.getFeatures().isEmpty()) {
        throw new ResponseStatusException(
            HttpStatus.UNPROCESSABLE_ENTITY, "Unable to derive mapsheet for the provided geometry");
      }

      Feature feature = fc.getFeatures().get(0);
      Object mapTileObj = feature.getProperties().get("MAP_TILE");
      if (mapTileObj == null) {
        throw new ResponseStatusException(
            HttpStatus.UNPROCESSABLE_ENTITY, "Unable to derive mapsheet for the provided geometry");
      }

      String mapTile = mapTileObj.toString();
      if (mapTile.length() < 7) {
        throw new ResponseStatusException(
            HttpStatus.UNPROCESSABLE_ENTITY, "Unable to derive mapsheet for the provided geometry");
      }

      return new MapsheetDto(
          mapTile.substring(0, 3),
          mapTile.substring(3, 4),
          mapTile.substring(4, 7),
          "0",
          "0");
    } catch (ResponseStatusException e) {
      throw e;
    } catch (Exception e) {
      log.error("Exception when deriving mapsheet from WFS: {}", e.getMessage());
      throw new ResponseStatusException(
          HttpStatus.UNPROCESSABLE_ENTITY, "Unable to derive mapsheet for the provided geometry");
    }
  }

  private boolean isOpeningSvwKind(String kind) {
    return kind == null
        || kind.isEmpty()
        || "WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW".equals(kind);
  }

  private String getPropertyName(String kind) {
    if (kind == null || kind.isEmpty()) {
      return "OPENING_ID,GEOMETRY";
    }

    Set<String> kindsSet = new HashSet<>(Arrays.asList(kind.split(",")));
    String propertyName = "OPENING_ID,GEOMETRY";

    if (kindsSet.size() == 1 && kindsSet.contains("WHSE_FOREST_TENURE.FTEN_CUT_BLOCK_POLY_SVW")) {
      return propertyName + ",HARVEST_AUTH_CUTTING_PERMIT_ID";
    } else if (kindsSet.size() == 1
        && kindsSet.contains("WHSE_FOREST_VEGETATION.RSLT_ACTIVITY_TREATMENT_SVW")) {
      return propertyName + ",ACTUAL_TREATMENT_AREA,DISTURBANCE_CODE,ATU_COMPLETION_DATE";
    } else if (kindsSet.size() == 1
        && kindsSet.contains("WHSE_FOREST_VEGETATION.RSLT_STANDARDS_UNIT_SVW")) {
      return propertyName + ",STANDARDS_REGIME_ID";
    } else {
      return propertyName;
    }
  }
}
