package ca.bc.gov.restapi.results.common.service;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import ca.bc.gov.restapi.results.TestConstants;
import ca.bc.gov.restapi.results.extensions.WiremockLogNotifier;
import ca.bc.gov.restapi.results.postgres.dto.MapsheetDto;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import java.util.Map;
import org.geojson.FeatureCollection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

@DisplayName("Integrated Test | OpenMapsService")
class OpenMapsServiceIntegrationTest {

  @RegisterExtension
  static WireMockExtension clientApiStub =
      WireMockExtension.newInstance()
          .options(
              wireMockConfig()
                  .port(10001)
                  .notifier(new WiremockLogNotifier())
                  .asynchronousResponseEnabled(true)
                  .stubRequestLoggingDisabled(false))
          .configureStaticDsl(true)
          .build();

  private final OpenMapsService openMapsService =
      new OpenMapsService(RestClient.builder().baseUrl("http://localhost:10001").build());

  @Test
  @DisplayName("Get opening polygon and properties happy path should succeed")
  void getOpeningPolygonAndProperties_happyPath_shouldSucceed() {

    Long openingId = 123L;
    clientApiStub.stubFor(
        get(urlPathEqualTo("/"))
            .withQueryParam("service", equalTo("WFS"))
            .withQueryParam("version", equalTo("2.0.0"))
            .withQueryParam("request", equalTo("GetFeature"))
            .withQueryParam("typeName", equalTo("WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW"))
            .withQueryParam("outputFormat", equalTo("application/json"))
            .withQueryParam("SrsName", equalTo("EPSG:4326"))
            .withQueryParam("PROPERTYNAME", equalTo("OPENING_ID,GEOMETRY"))
            .withQueryParam("CQL_FILTER", equalTo("OPENING_ID=" + openingId))
            .willReturn(okJson(TestConstants.WFS_OPENING)));

    FeatureCollection response = openMapsService.getOpeningPolygonAndProperties(openingId, null);

    Assertions.assertNotNull(response);
  }

  @Test
  @DisplayName("Get opening polygon and properties not found should fail")
  void getOpeningPolygonAndProperties_notFound_shouldFail() {

    Long openingId = 123L;
    clientApiStub.stubFor(
        get(urlPathEqualTo("/"))
            .withQueryParam("service", equalTo("WFS"))
            .withQueryParam("version", equalTo("2.0.0"))
            .withQueryParam("request", equalTo("GetFeature"))
            .withQueryParam("typeName", equalTo("WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW"))
            .withQueryParam("outputFormat", equalTo("application/json"))
            .withQueryParam("SrsName", equalTo("EPSG:4326"))
            .withQueryParam(
                "PROPERTYNAME",
                equalTo(
                    "OPENING_ID,"
                        + "GEOMETRY,"
                        + "REGION_NAME,"
                        + "REGION_CODE,"
                        + "DISTRICT_NAME,"
                        + "DISTRICT_CODE,"
                        + "CLIENT_NAME,"
                        + "CLIENT_NUMBER,"
                        + "OPENING_WHEN_CREATED"))
            .withQueryParam("CQL_FILTER", equalTo("OPENING_ID=" + openingId))
            .willReturn(notFound()));

    Object response = openMapsService.getOpeningPolygonAndProperties(openingId, null);

    Assertions.assertNull(response);
  }

  @Test
  @DisplayName("Get cut block polygon and properties happy path should succeed")
  void getCutBlockPolygonAndProperties_happyPath_shouldSucceed() {

    Long openingId = 123L;
    clientApiStub.stubFor(
        get(urlPathEqualTo("/"))
            .withQueryParam("service", equalTo("WFS"))
            .withQueryParam("version", equalTo("2.0.0"))
            .withQueryParam("request", equalTo("GetFeature"))
            .withQueryParam("typeName", equalTo("WHSE_FOREST_TENURE.FTEN_CUT_BLOCK_POLY_SVW"))
            .withQueryParam("outputFormat", equalTo("application/json"))
            .withQueryParam("SrsName", equalTo("EPSG:4326"))
            .withQueryParam(
                "PROPERTYNAME", equalTo("OPENING_ID,GEOMETRY,HARVEST_AUTH_CUTTING_PERMIT_ID"))
            .withQueryParam("CQL_FILTER", equalTo("OPENING_ID=" + openingId))
            .willReturn(okJson(TestConstants.FTEN_CUT_BLOCK_POLY_SVW)));

    FeatureCollection response =
        openMapsService.getOpeningPolygonAndProperties(
            openingId, "WHSE_FOREST_TENURE.FTEN_CUT_BLOCK_POLY_SVW");

    Assertions.assertNotNull(response);
    Assertions.assertFalse(response.getFeatures().isEmpty());
    Map<String, Object> properties = response.getFeatures().get(0).getProperties();
    Assertions.assertTrue(properties.containsKey("HARVEST_AUTH_CUTTING_PERMIT_ID"));
  }

  // ─── getPropertyName branches ────────────────────────────────────────────

  @Test
  @DisplayName("Get activity treatment SVW polygon and properties happy path should succeed")
  void getActivityTreatmentPolygonAndProperties_happyPath_shouldSucceed() {
    Long openingId = 123L;
    String kind = "WHSE_FOREST_VEGETATION.RSLT_ACTIVITY_TREATMENT_SVW";
    clientApiStub.stubFor(
        get(urlPathEqualTo("/"))
            .withQueryParam("typeName", equalTo(kind))
            .withQueryParam(
                "PROPERTYNAME",
                equalTo(
                    "OPENING_ID,GEOMETRY,ACTUAL_TREATMENT_AREA,DISTURBANCE_CODE,ATU_COMPLETION_DATE"))
            .withQueryParam("CQL_FILTER", equalTo("OPENING_ID=" + openingId))
            .willReturn(okJson(TestConstants.WFS_OPENING)));

    FeatureCollection response = openMapsService.getOpeningPolygonAndProperties(openingId, kind);

    Assertions.assertNotNull(response);
  }

  @Test
  @DisplayName("Get standards unit SVW polygon and properties happy path should succeed")
  void getStandardsUnitPolygonAndProperties_happyPath_shouldSucceed() {
    Long openingId = 123L;
    String kind = "WHSE_FOREST_VEGETATION.RSLT_STANDARDS_UNIT_SVW";
    clientApiStub.stubFor(
        get(urlPathEqualTo("/"))
            .withQueryParam("typeName", equalTo(kind))
            .withQueryParam("PROPERTYNAME", equalTo("OPENING_ID,GEOMETRY,STANDARDS_REGIME_ID"))
            .withQueryParam("CQL_FILTER", equalTo("OPENING_ID=" + openingId))
            .willReturn(okJson(TestConstants.WFS_OPENING)));

    FeatureCollection response = openMapsService.getOpeningPolygonAndProperties(openingId, kind);

    Assertions.assertNotNull(response);
  }

  @Test
  @DisplayName("Get polygon for unknown kind uses default PROPERTYNAME should succeed")
  void getPolygonAndProperties_unknownKind_shouldUseDefaultPropertyName() {
    Long openingId = 123L;
    String kind = "SOME_UNKNOWN_LAYER";
    clientApiStub.stubFor(
        get(urlPathEqualTo("/"))
            .withQueryParam("typeName", equalTo(kind))
            .withQueryParam("PROPERTYNAME", equalTo("OPENING_ID,GEOMETRY"))
            .withQueryParam("CQL_FILTER", equalTo("OPENING_ID=" + openingId))
            .willReturn(okJson(TestConstants.WFS_OPENING)));

    FeatureCollection response = openMapsService.getOpeningPolygonAndProperties(openingId, kind);

    Assertions.assertNotNull(response);
  }

  // ─── getMapsheetForPoint ─────────────────────────────────────────────────

  private static final String MAPSHEET_FC_092L057 =
      """
      {
        "type": "FeatureCollection",
        "features": [{
          "type": "Feature",
          "geometry": null,
          "properties": { "MAP_TILE": "092L057" }
        }]
      }
      """;

  @Test
  @DisplayName("getMapsheetForPoint happy path should return parsed MapsheetDto")
  void getMapsheetForPoint_happyPath_shouldReturnDto() {
    clientApiStub.stubFor(
        get(urlPathEqualTo("/"))
            .withQueryParam("typeName", equalTo("WHSE_BASEMAPPING.BCGS_20K_GRID"))
            .willReturn(okJson(MAPSHEET_FC_092L057)));

    MapsheetDto result = openMapsService.getMapsheetForPoint(-125.0, 50.0);

    Assertions.assertNotNull(result);
    Assertions.assertEquals("092", result.grid());
    Assertions.assertEquals("L", result.letter());
    Assertions.assertEquals("057", result.square());
    Assertions.assertEquals("0", result.quad());
    Assertions.assertEquals("0", result.subQuad());
  }

  @Test
  @DisplayName("getMapsheetForPoint with empty features should throw 422")
  void getMapsheetForPoint_emptyFeatures_shouldThrow422() {
    String emptyFc = """
        {"type":"FeatureCollection","features":[]}
        """;
    clientApiStub.stubFor(
        get(urlPathEqualTo("/"))
            .withQueryParam("typeName", equalTo("WHSE_BASEMAPPING.BCGS_20K_GRID"))
            .willReturn(okJson(emptyFc)));

    Assertions.assertThrows(
        ResponseStatusException.class, () -> openMapsService.getMapsheetForPoint(-125.0, 50.0));
  }

  @Test
  @DisplayName("getMapsheetForPoint with null MAP_TILE should throw 422")
  void getMapsheetForPoint_nullMapTile_shouldThrow422() {
    String fcNullTile =
        """
        {
          "type": "FeatureCollection",
          "features": [{"type":"Feature","geometry":null,"properties":{"MAP_TILE":null}}]
        }
        """;
    clientApiStub.stubFor(
        get(urlPathEqualTo("/"))
            .withQueryParam("typeName", equalTo("WHSE_BASEMAPPING.BCGS_20K_GRID"))
            .willReturn(okJson(fcNullTile)));

    Assertions.assertThrows(
        ResponseStatusException.class, () -> openMapsService.getMapsheetForPoint(-125.0, 50.0));
  }

  @Test
  @DisplayName("getMapsheetForPoint with MAP_TILE shorter than 7 chars should throw 422")
  void getMapsheetForPoint_shortMapTile_shouldThrow422() {
    String fcShortTile =
        """
        {
          "type": "FeatureCollection",
          "features": [{"type":"Feature","geometry":null,"properties":{"MAP_TILE":"092L"}}]
        }
        """;
    clientApiStub.stubFor(
        get(urlPathEqualTo("/"))
            .withQueryParam("typeName", equalTo("WHSE_BASEMAPPING.BCGS_20K_GRID"))
            .willReturn(okJson(fcShortTile)));

    Assertions.assertThrows(
        ResponseStatusException.class, () -> openMapsService.getMapsheetForPoint(-125.0, 50.0));
  }

  @Test
  @DisplayName("getMapsheetForPoint with WFS server error should throw 422")
  void getMapsheetForPoint_serverError_shouldThrow422() {
    clientApiStub.stubFor(
        get(urlPathEqualTo("/"))
            .withQueryParam("typeName", equalTo("WHSE_BASEMAPPING.BCGS_20K_GRID"))
            .willReturn(serverError()));

    Assertions.assertThrows(
        ResponseStatusException.class, () -> openMapsService.getMapsheetForPoint(-125.0, 50.0));
  }
}
