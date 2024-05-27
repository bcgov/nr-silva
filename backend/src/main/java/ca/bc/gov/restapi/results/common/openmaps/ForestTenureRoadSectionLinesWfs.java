package ca.bc.gov.restapi.results.common.openmaps;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Component
public class ForestTenureRoadSectionLinesWfs {

  public String getBaseMapLayer() {
    String uri = "https://openmaps.gov.bc.ca/geo/ows";
    // service
    uri += "?service=WFS";
    // version
    uri += "&version=2.0.0";
    // request
    uri += "&request=GetFeature";
    // typeName (layer !?)
    uri += "&typeName=WHSE_FOREST_TENURE.FTEN_ROAD_SECTION_LINES_SVW";
    // output format
    uri += "&outputFormat=application/json";
    // Srs name
    uri += "&SrsName=EPSG:4326";
    // Properties name
    uri += "&PROPERTYNAME=ROAD_SECTION_ID,ROAD_SECTION_NAME,SECTION_WIDTH,FEATURE_LENGTH,GEOMETRY";
    log.info("Starting GET request to {}", uri);

    try {
      URL url = new URL(uri);
      HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

      if (conn != null) {
        StringBuilder builder = new StringBuilder();
        if (conn.getResponseCode() != 200) {
          throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST, "Response error " + conn.getResponseCode());
        } else {
          BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
          String input;
          while ((input = br.readLine()) != null) {
            builder.append(input);
          }
          br.close();
        }

        conn.disconnect();
        return builder.toString();
      }

      log.info("Finished Open Maps request for BCGW WFS - 200 OK!");
      return "{}";
    } catch (Exception httpExc) {
      log.error("Finished Open Maps request for BCGW WFS - {}", httpExc.getMessage());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, httpExc.getMessage(), httpExc);
    }
  }
}
