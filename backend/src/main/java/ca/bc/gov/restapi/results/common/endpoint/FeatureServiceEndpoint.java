package ca.bc.gov.restapi.results.common.endpoint;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/feature-service")
@Tag(
    name = "Feature Service from WFS (Common)",
    description = "Endpoints for fetching polygons from WFS")
public class FeatureServiceEndpoint {

  @GetMapping("/polygon-and-props/{openingId}")
  public Object getPolygon(
      @Parameter(
              name = "openingId",
              in = ParameterIn.PATH,
              description = "The opening Id.",
              required = true)
          @PathVariable
          String openingId) {
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
      RestTemplate restTemplate = new RestTemplate();
      ResponseEntity<Object> response = restTemplate.getForEntity(sb.toString(), Object.class);
      return response.getBody();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }
}
