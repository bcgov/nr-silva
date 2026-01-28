package ca.bc.gov.restapi.results.oracle.endpoint;

import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WiremockLogNotifier;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.jayway.jsonpath.JsonPath;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@AutoConfigureMockMvc
@WithMockJwt
@DisplayName("Integrated Test | Search Endpoint")
class SearchEndpointIntegrationTest extends AbstractTestContainerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @RegisterExtension
  static WireMockExtension clientApiStub =
      WireMockExtension.newInstance()
          .options(
              wireMockConfig()
                  .port(10000)
                  .notifier(new WiremockLogNotifier())
                  .asynchronousResponseEnabled(true)
                  .stubRequestLoggingDisabled(false))
          .configureStaticDsl(true)
          .build();

  @Test
  @DisplayName("Opening search exact with mapsheet key should succeed")
  void openingSearchExact_mapsheetKey_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/openings?mapsheetKey=514")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.page.number").value("0"))
        .andExpect(jsonPath("$.page.size").value("20"))
        .andExpect(jsonPath("$.page.totalElements").isNumber())
        .andExpect(jsonPath("$.content").isArray())
        .andReturn();
  }

  @Test
  @DisplayName("Opening search exact with mapsheet grid should succeed")
  void openingSearchExact_mapsheetGrid_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/openings?mapsheetGrid=93")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.page.number").value("0"))
        .andExpect(jsonPath("$.page.size").value("20"))
        .andExpect(jsonPath("$.page.totalElements").isNumber())
        .andExpect(jsonPath("$.content").isArray())
        .andReturn();
  }

  @Test
  @DisplayName("Opening search exact with opening ID should succeed")
  void openingSearchExact_openingId_shouldSucceed() throws Exception {
    String clientNumber = "00010003";
    clientApiStub.stubFor(
        WireMock.get(urlPathEqualTo("/clients/findByClientNumber/" + clientNumber))
            .willReturn(
                okJson(
                    """
                {
                  "clientNumber": "00010003",
                  "clientName": "MINISTRY OF FORESTS",
                  "legalFirstName": null,
                  "legalMiddleName": null,
                  "clientStatusCode": "ACT",
                  "clientTypeCode": "F",
                  "acronym": "MOF"
                }
                """)));

    mockMvc
        .perform(
            get("/api/search/openings?openingId=101017")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.page.number").value("0"))
        .andExpect(jsonPath("$.page.size").value("20"))
        .andExpect(jsonPath("$.page.totalElements").isNumber())
        .andExpect(jsonPath("$.content[0].openingId").value(101017L))
        .andExpect(jsonPath("$.content[0].mapsheetKey").exists())
        .andExpect(jsonPath("$.content[0].licenseeOpeningId").exists())
        .andReturn();
  }

  @Test
  @DisplayName("Opening search exact with no results should return empty")
  void openingSearchExact_noResults_shouldReturnEmpty() throws Exception {
    mockMvc
        .perform(
            get("/api/search/openings?openingId=999999999")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.page.number").value("0"))
        .andExpect(jsonPath("$.page.size").value("20"))
        .andExpect(jsonPath("$.page.totalElements").value("0"))
        .andExpect(jsonPath("$.content", Matchers.empty()))
        .andReturn();
  }

  @Test
  @DisplayName("Opening search exact with invalid mapsheet grid should return 400")
  void openingSearchExact_invalidMapsheetGrid_shouldReturn400() throws Exception {
    mockMvc
        .perform(
            get("/api/search/openings?mapsheetGrid=99")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn();
  }

  @Test
  @DisplayName("Opening search exact with invalid mapsheet letter should return 400")
  void openingSearchExact_invalidMapsheetLetter_shouldReturn400() throws Exception {
    mockMvc
        .perform(
            get("/api/search/openings?mapsheetLetter=X")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn();
  }

  @Test
  @DisplayName("Opening search exact with invalid mapsheet quad should return 400")
  void openingSearchExact_invalidMapsheetQuad_shouldReturn400() throws Exception {
    mockMvc
        .perform(
            get("/api/search/openings?mapsheetQuad=5")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn();
  }

  @Test
  @DisplayName("Opening search exact with invalid mapsheet sub quad should return 400")
  void openingSearchExact_invalidMapsheetSubQuad_shouldReturn400() throws Exception {
    mockMvc
        .perform(
            get("/api/search/openings?mapsheetSubQuad=6")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn();
  }

  @Test
  @DisplayName("Opening search exact with invalid entry date range should return 400")
  void openingSearchExact_invalidEntryDateRange_shouldReturn400() throws Exception {
    mockMvc
        .perform(
            get("/api/search/openings?entryDateStart=2025-01-16&entryDateEnd=2025-01-01")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn();
  }

  @Test
  @DisplayName("Opening search exact with valid entry date range should succeed")
  void openingSearchExact_validEntryDateRange_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/openings?entryDateStart=2001-01-01&entryDateEnd=2025-12-31")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.page.number").value("0"))
        .andExpect(jsonPath("$.page.size").value("20"))
        .andReturn();
  }

  @Test
  @DisplayName("Opening search exact with page size should succeed")
  void openingSearchExact_pageSize_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/openings?openingId=101017&page=0&size=10")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andReturn();
  }

  @Test
  @DisplayName("Opening search exact with composed mapsheet key should be formatted correctly")
  void openingSearchExact_composedMapsheetKey_shouldBeFormatted() throws Exception {
    String clientNumber = "00010003";
    clientApiStub.stubFor(
        WireMock.get(urlPathEqualTo("/clients/findByClientNumber/" + clientNumber))
            .willReturn(
                okJson(
                    """
                {
                  "clientNumber": "00010003",
                  "clientName": "MINISTRY OF FORESTS",
                  "legalFirstName": null,
                  "legalMiddleName": null,
                  "clientStatusCode": "ACT",
                  "clientTypeCode": "F",
                  "acronym": "MOF"
                }
                """)));

    MvcResult result =
        mockMvc
            .perform(
                get("/api/search/openings?openingId=101017")
                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andReturn();

    // Extract and verify the mapsheet key is properly composed (not just the raw DB value)
    String responseBody = result.getResponse().getContentAsString();
    String mapsheetKey = JsonPath.read(responseBody, "$.content[0].mapsheetKey");
    Assertions.assertNotNull(mapsheetKey);
    // Should be like "92K 014 0.0  514" not just "514"
    Assertions.assertTrue(
        mapsheetKey.contains(" "), "Mapsheet key should be composed with spaces");
  }
}
