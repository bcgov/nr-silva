package ca.bc.gov.restapi.results.common.endpoint;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WiremockLogNotifier;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Abstract integration test for stocking standards search endpoint. Defines test contract for all
 * SearchEndpoint stockingStandardsSearch implementations (Postgres and Oracle).
 */
@WithMockJwt(value = "ttester")
@AutoConfigureMockMvc
@DisplayName("Integrated Test | Stocking Standards Search Endpoint | Contract")
public abstract class AbstractSearchEndpointStockingStandardsSearchIntegrationTest
    extends AbstractTestContainerIntegrationTest {

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

  @Autowired protected MockMvc mockMvc;

  @Test
  @DisplayName("GET /api/search/stocking-standards with standardsRegimeId filter should succeed")
  void getStockingStandards_withStandardsRegimeIdFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/stocking-standards")
                .param("standardsRegimeId", "36109")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.totalElements").exists())
        .andExpect(jsonPath("$.page.totalPages").exists());
  }

  @Test
  @DisplayName("GET /api/search/stocking-standards with bgcZone filter should succeed")
  void getStockingStandards_withBgcZoneFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/stocking-standards")
                .param("bgcZone", "CWH")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/stocking-standards with org unit filter should succeed")
  void getStockingStandards_withOrgUnitFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/stocking-standards")
                .param("orgUnits", "DAS")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/stocking-standards with multiple org units should succeed")
  void getStockingStandards_withMultipleOrgUnits_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/stocking-standards")
                .param("orgUnits", "DAS")
                .param("orgUnits", "TWO")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/stocking-standards with client number filter should succeed")
  void getStockingStandards_withClientNumberFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/stocking-standards")
                .param("clientNumbers", "00010002")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/stocking-standards with preferred species filter should succeed")
  void getStockingStandards_withPreferredSpeciesFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/stocking-standards")
                .param("preferredSpecies", "CW")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/stocking-standards with fspId filter should succeed")
  void getStockingStandards_withFspIdFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/stocking-standards")
                .param("fspId", "1234")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/stocking-standards with becSiteType filter should succeed")
  void getStockingStandards_withBecSiteTypeFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/stocking-standards")
                .param("becSiteType", "01")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/stocking-standards with update date range should succeed")
  void getStockingStandards_withUpdateDateRange_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/stocking-standards")
                .param("updateDateStart", "2005-01-01")
                .param("updateDateEnd", "2025-12-31")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/stocking-standards with pagination should succeed")
  void getStockingStandards_withPagination_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/stocking-standards")
                .param("standardsRegimeId", "36109")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page.number").value(0))
        .andExpect(jsonPath("$.page.size").value(10));
  }

  @Test
  @DisplayName("GET /api/search/stocking-standards response should contain required fields")
  void getStockingStandards_response_shouldContainRequiredFields() throws Exception {
    mockMvc
        .perform(
            get("/api/search/stocking-standards")
                .param("standardsRegimeId", "36109")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].standardsRegimeId").exists())
        .andExpect(jsonPath("$.content[0].isExpired").exists())
        .andExpect(jsonPath("$.content[0].preferredSpecies").isArray())
        .andExpect(jsonPath("$.content[0].orgUnits").isArray())
        .andExpect(jsonPath("$.content[0].clients").isArray())
        .andExpect(jsonPath("$.content[0].fspIds").isArray());
  }

  @Test
  @DisplayName("GET /api/search/stocking-standards with combined filters should succeed")
  void getStockingStandards_withCombinedFilters_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/stocking-standards")
                .param("standardsRegimeId", "36109")
                .param("bgcZone", "CWH")
                .param("page", "0")
                .param("size", "20")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.page.size").value(20));
  }

  @Test
  @DisplayName(
      "GET /api/search/stocking-standards with non-matching standardsRegimeId should return empty")
  void getStockingStandards_withNonMatchingStandardsRegimeId_shouldReturnEmpty() throws Exception {
    mockMvc
        .perform(
            get("/api/search/stocking-standards")
                .param("standardsRegimeId", "999999999")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.page.totalElements").value(0));
  }

  @Test
  @DisplayName("GET /api/search/stocking-standards with no filters should return 400")
  void getStockingStandards_withNoFilters_shouldReturn400() throws Exception {
    mockMvc
        .perform(get("/api/search/stocking-standards").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }
}
