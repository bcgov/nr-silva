package ca.bc.gov.restapi.results.common.service;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

@RestClientTest(RestService.class)
class RestServiceTest {

  @Autowired private RestService restService;

  @Autowired private MockRestServiceServer mockRestServiceServer;

  @Test
  @DisplayName("Get opening polygon and properties happy path should succeed")
  void getOpeningPolygonAndProperties_happyPath_shouldSucceed() {
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

    String openingId = "123";
    sb.append("&CQL_FILTER=OPENING_ID%3D").append(openingId);

    String json = "{}";

    mockRestServiceServer
        .expect(requestTo(sb.toString()))
        .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

    Object response = restService.getOpeningPolygonAndProperties(openingId);

    Assertions.assertNotNull(response);
  }

  @Test
  @DisplayName("Get opening polygon and properties not found should fail")
  void getOpeningPolygonAndProperties_notFound_shouldFail() {
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

    String openingId = "123";
    sb.append("&CQL_FILTER=OPENING_ID%3D").append(openingId);

    mockRestServiceServer
        .expect(requestTo(sb.toString()))
        .andRespond(withStatus(HttpStatusCode.valueOf(404)));

    Object response = restService.getOpeningPolygonAndProperties(openingId);

    Assertions.assertNull(response);
  }
}
