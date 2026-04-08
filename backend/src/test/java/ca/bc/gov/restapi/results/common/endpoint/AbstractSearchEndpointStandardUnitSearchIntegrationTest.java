package ca.bc.gov.restapi.results.common.endpoint;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Abstract integration test for standard unit search endpoints. Defines test contract for all
 * SearchEndpoint standardUnitSearch implementations (Postgres and Oracle).
 */
@WithMockJwt(value = "ttester")
@AutoConfigureMockMvc
@DisplayName("Integrated Test | Standard Unit Search Endpoint | Contract")
public abstract class AbstractSearchEndpointStandardUnitSearchIntegrationTest
    extends AbstractTestContainerIntegrationTest {

  @Autowired protected MockMvc mockMvc;

  @Test
  @DisplayName("GET /api/search/standard-unit with standardsRegimeId filter should succeed")
  void getStandardUnit_withStandardsRegimeIdFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/standard-unit")
                .param("standardsRegimeId", "36109")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.totalElements").exists())
        .andExpect(jsonPath("$.page.totalPages").exists());
  }

  @Test
  @DisplayName("GET /api/search/standard-unit with bgcZone filter should succeed")
  void getStandardUnit_withBgcZoneFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/standard-unit")
                .param("bgcZone", "CWH")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/standard-unit with org unit filter should succeed")
  void getStandardUnit_withOrgUnitFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/standard-unit")
                .param("orgUnits", "DAS")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/standard-unit with multiple org units should succeed")
  void getStandardUnit_withMultipleOrgUnits_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/standard-unit")
                .param("orgUnits", "DAS")
                .param("orgUnits", "TWO")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/standard-unit with client number filter should succeed")
  void getStandardUnit_withClientNumberFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/standard-unit")
                .param("clientNumbers", "00010002")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/standard-unit with becSiteType filter should succeed")
  void getStandardUnit_withBecSiteTypeFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/standard-unit")
                .param("becSiteType", "01")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/standard-unit with preferred species filter should succeed")
  void getStandardUnit_withPreferredSpeciesFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/standard-unit")
                .param("preferredSpecies", "CW")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/standard-unit with update date range should succeed")
  void getStandardUnit_withUpdateDateRange_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/standard-unit")
                .param("updateDateStart", "2005-01-01")
                .param("updateDateEnd", "2025-12-31")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/standard-unit with pagination should succeed")
  void getStandardUnit_withPagination_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/standard-unit")
                .param("standardsRegimeId", "36109")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page.number").value(0))
        .andExpect(jsonPath("$.page.size").value(10));
  }

  @Test
  @DisplayName("GET /api/search/standard-unit response should contain required fields")
  void getStandardUnit_response_shouldContainRequiredFields() throws Exception {
    mockMvc
        .perform(
            get("/api/search/standard-unit")
                .param("standardsRegimeId", "36109")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].stockingStandardUnitId").exists())
        .andExpect(jsonPath("$.content[0].openingId").exists())
        .andExpect(jsonPath("$.content[0].standardsUnitId").exists())
        .andExpect(jsonPath("$.content[0].standardsRegimeId").exists())
        .andExpect(jsonPath("$.content[0].isStandardsRegimeExpired").exists())
        .andExpect(jsonPath("$.content[0].regenDueDate").exists())
        .andExpect(jsonPath("$.content[0].freeGrowingDueDate").exists())
        .andExpect(jsonPath("$.content[0].preferredSpecies").isArray());
  }

  @Test
  @DisplayName("GET /api/search/standard-unit with combined filters should succeed")
  void getStandardUnit_withCombinedFilters_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/standard-unit")
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
      "GET /api/search/standard-unit with non-matching standardsRegimeId should return empty")
  void getStandardUnit_withNonMatchingStandardsRegimeId_shouldReturnEmpty() throws Exception {
    mockMvc
        .perform(
            get("/api/search/standard-unit")
                .param("standardsRegimeId", "999999999")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page.totalElements").value(0));
  }

  @Test
  @DisplayName("GET /api/search/standard-unit response should have pagination metadata")
  void getStandardUnit_response_shouldHavePaginationMetadata() throws Exception {
    mockMvc
        .perform(
            get("/api/search/standard-unit")
                .param("standardsRegimeId", "36109")
                .param("page", "0")
                .param("size", "5")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page.number").exists())
        .andExpect(jsonPath("$.page.size").exists())
        .andExpect(jsonPath("$.page.totalElements").exists())
        .andExpect(jsonPath("$.page.totalPages").exists());
  }

  @Test
  @DisplayName("GET /api/search/standard-unit without any filters should return error")
  void getStandardUnit_withoutAnyFilters_shouldReturnError() throws Exception {
    mockMvc
        .perform(get("/api/search/standard-unit").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }
}
