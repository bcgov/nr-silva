package ca.bc.gov.restapi.results.common.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/** This service provides method for doing requests and GET calls. */
@Service
public class RestService {

  private final RestTemplate restTemplate;

  public RestService(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }

  /**
   * Get Opening polygons and properties from WFS given an opening id.
   *
   * @param openingId The Opening identification.
   * @return An object with the response from WFS
   */
  public Object getOpeningPolygonAndProperties(String openingId) {
    StringBuilder sb = new StringBuilder();
    sb.append("https://openmaps.gov.bc.ca/geo/ows");
    sb.append("?service=WFS");
    sb.append("&version=2.0.0");
    sb.append("&request=GetFeature");
    sb.append("&typeName=WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW");
    sb.append("&outputFormat=application/json");
    sb.append("&SrsName=EPSG:4326");
    sb.append("&PROPERTYNAME=");
    sb.append("OPENING_ID,GEOMETRY,REGION_NAME,REGION_CODE,DISTRICT_NAME,DISTRICT_CODE,");
    sb.append("CLIENT_NAME,CLIENT_NUMBER,OPENING_WHEN_CREATED");
    sb.append("&CQL_FILTER=OPENING_ID=").append(openingId);

    try {
      ResponseEntity<Object> response = restTemplate.getForEntity(sb.toString(), Object.class);
      return response.getBody();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
