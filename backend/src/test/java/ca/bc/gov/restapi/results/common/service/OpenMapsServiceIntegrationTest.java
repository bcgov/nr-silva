package ca.bc.gov.restapi.results.common.service;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import ca.bc.gov.restapi.results.TestConstants;
import ca.bc.gov.restapi.results.extensions.WiremockLogNotifier;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.geojson.FeatureCollection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.client.RestClient;

@DisplayName("Integrated Test | OpenMapsService")
class OpenMapsServiceIntegrationTest {

  @RegisterExtension
  static WireMockExtension clientApiStub = WireMockExtension
      .newInstance()
      .options(
          wireMockConfig()
              .port(10001)
              .notifier(new WiremockLogNotifier())
              .asynchronousResponseEnabled(true)
              .stubRequestLoggingDisabled(false)
      )
      .configureStaticDsl(true)
      .build();

  private final OpenMapsService openMapsService = new OpenMapsService(
      RestClient.builder().baseUrl("http://localhost:10001").build()
  );

  @Test
  @DisplayName("Get opening polygon and properties happy path should succeed")
  void getOpeningPolygonAndProperties_happyPath_shouldSucceed() {

    String openingId = "123";
    clientApiStub.stubFor(
        get(urlPathEqualTo("/"))
            .withQueryParam("service", equalTo("WFS"))
            .withQueryParam("version", equalTo("2.0.0"))
            .withQueryParam("request", equalTo("GetFeature"))
            .withQueryParam("typeName", equalTo("WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW"))
            .withQueryParam("outputFormat", equalTo("application/json"))
            .withQueryParam("SrsName", equalTo("EPSG:4326"))
            .withQueryParam("PROPERTYNAME", equalTo("GEOMETRY,OPENING_ID"))
            .withQueryParam("CQL_FILTER", equalTo("OPENING_ID=" + openingId))
            .willReturn(okJson(TestConstants.WFS_OPENING))
    );

    FeatureCollection response = openMapsService.getOpeningPolygonAndProperties(openingId, null);

    Assertions.assertNotNull(response);
  }

  @Test
  @DisplayName("Get opening polygon and properties not found should fail")
  void getOpeningPolygonAndProperties_notFound_shouldFail() {

    String openingId = "123";
    clientApiStub.stubFor(
        get(urlPathEqualTo("/"))
            .withQueryParam("service", equalTo("WFS"))
            .withQueryParam("version", equalTo("2.0.0"))
            .withQueryParam("request", equalTo("GetFeature"))
            .withQueryParam("typeName", equalTo("WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW"))
            .withQueryParam("outputFormat", equalTo("application/json"))
            .withQueryParam("SrsName", equalTo("EPSG:4326"))
            .withQueryParam("PROPERTYNAME",
                equalTo("OPENING_ID,"
                        + "GEOMETRY,"
                        + "REGION_NAME,"
                        + "REGION_CODE,"
                        + "DISTRICT_NAME,"
                        + "DISTRICT_CODE,"
                        + "CLIENT_NAME,"
                        + "CLIENT_NUMBER,"
                        + "OPENING_WHEN_CREATED"
                ))
            .withQueryParam("CQL_FILTER", equalTo("OPENING_ID=" + openingId))
            .willReturn(notFound())
    );

    Object response = openMapsService.getOpeningPolygonAndProperties(openingId, null);

    Assertions.assertNull(response);
  }
}
