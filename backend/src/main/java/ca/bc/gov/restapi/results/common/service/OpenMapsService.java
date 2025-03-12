package ca.bc.gov.restapi.results.common.service;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.geojson.FeatureCollection;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * This service provides method for doing requests and GET calls.
 */
@Slf4j
@Service
public class OpenMapsService {

  private final RestClient restClient;

  /**
   * Instantiates a new Open maps service.
   *
   * @param openMapsApi the open maps api
   */
  public OpenMapsService(
      @Qualifier("openMapsApi") RestClient openMapsApi) {
    this.restClient = openMapsApi;
  }

  /**
   * Get Opening polygons and properties from WFS given an opening id.
   *
   * @param openingId The Opening identification.
   * @return An object with the response from WFS
   */
  public FeatureCollection getOpeningPolygonAndProperties(String openingId) {
    try {
      return restClient
          .get()
          .uri(builder ->
              builder
                  .queryParam("service", "WFS")
                  .queryParam("version", "2.0.0")
                  .queryParam("request", "GetFeature")
                  .queryParam("typeName", "WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW")
                  .queryParam("outputFormat", "application/json")
                  .queryParam("SrsName", "EPSG:4326")
                  .queryParam("PROPERTYNAME",
                      "OPENING_ID,"
                      + "GEOMETRY,"
                      + "REGION_NAME,"
                      + "REGION_CODE,"
                      + "DISTRICT_NAME,"
                      + "DISTRICT_CODE,"
                      + "CLIENT_NAME,"
                      + "CLIENT_NUMBER,"
                      + "OPENING_WHEN_CREATED"
                  )
                  .queryParam("CQL_FILTER", "OPENING_ID=" + openingId)
                  .build(Map.of())
          )
          .retrieve()
          .body(FeatureCollection.class);
    } catch (Exception e) {
      log.error("Exception when fetching from WFS {}", e.getMessage());
    }
    return null;
  }
}
