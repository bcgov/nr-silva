package ca.bc.gov.restapi.results.common.service;

import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.geojson.FeatureCollection;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

/** This service provides method for doing requests and GET calls. */
@Slf4j
@Service
public class OpenMapsService {

  private final RestClient restClient;

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
  public FeatureCollection getOpeningPolygonAndProperties(String openingId, String kind) {
    try {
      log.debug(
          "Fetching opening polygon and properties for openingId: {} and kind: {}",
          openingId,
          kind);

      FeatureCollection result =
          restClient
              .get()
              .uri(
                  builder -> {
                    java.net.URI uri =
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
                            .build();
                    log.debug("Built URI: {}", uri);
                    return uri;
                  })
              .retrieve()
              .body(FeatureCollection.class);

      log.debug("Successfully retrieved FeatureCollection: {}", result);
      return result;
    } catch (HttpClientErrorException | HttpServerErrorException httpExc) {
      log.error(
          "HTTP error when fetching from WFS for openingId {} and kind {}: {} - {}",
          openingId,
          kind,
          httpExc.getStatusCode(),
          httpExc.getMessage());
      return null;
    } catch (Exception e) {
      log.error(
          "Exception when fetching from WFS for openingId {} and kind {}: {}",
          openingId,
          kind,
          e.getMessage(),
          e);
      return null;
    }
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
    } else {
      return propertyName;
    }
  }
}
