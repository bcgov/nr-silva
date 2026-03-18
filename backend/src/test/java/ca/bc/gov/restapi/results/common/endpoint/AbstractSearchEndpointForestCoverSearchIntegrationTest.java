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
 * Abstract integration test for forest cover search endpoints. Defines test contract for all
 * SearchEndpoint forestCoverSearch implementations (Postgres and Oracle).
 */
@WithMockJwt(value = "ttester")
@AutoConfigureMockMvc
@DisplayName("Integrated Test | Forest Cover Search Endpoint | Contract")
public abstract class AbstractSearchEndpointForestCoverSearchIntegrationTest
    extends AbstractTestContainerIntegrationTest {

  @Autowired protected MockMvc mockMvc;

  @Test
  @DisplayName("GET /api/search/forest-cover with file ID filter should succeed")
  void getForestCover_withFileIdFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/forest-cover")
                .param("fileId", "TFL47")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.totalElements").exists())
        .andExpect(jsonPath("$.page.totalPages").exists());
  }

  @Test
  @DisplayName("GET /api/search/forest-cover with opening status filter should succeed")
  void getForestCover_withOpeningStatusFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/forest-cover")
                .param("openingStatuses", "APP")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/forest-cover with multiple opening statuses should succeed")
  void getForestCover_withMultipleOpeningStatuses_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/forest-cover")
                .param("openingStatuses", "APP")
                .param("openingStatuses", "AMD")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/forest-cover with org unit filter should succeed")
  void getForestCover_withOrgUnitFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/forest-cover")
                .param("orgUnits", "DCR")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/forest-cover with multiple org units should succeed")
  void getForestCover_withMultipleOrgUnits_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/forest-cover")
                .param("orgUnits", "DCR")
                .param("orgUnits", "DND")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/forest-cover with opening category filter should succeed")
  void getForestCover_withOpeningCategoryFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/forest-cover")
                .param("openingCategories", "FTML")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/forest-cover with update date range should succeed")
  void getForestCover_withUpdateDateRange_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/forest-cover")
                .param("updateDateStart", "2020-01-01")
                .param("updateDateEnd", "2024-12-31")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/forest-cover with pagination should succeed")
  void getForestCover_withPagination_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/forest-cover")
                .param("fileId", "TFL47")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page.number").value(0))
        .andExpect(jsonPath("$.page.size").value(10));
  }

  @Test
  @DisplayName("GET /api/search/forest-cover response should contain required fields")
  void getForestCover_response_shouldContainRequiredFields() throws Exception {
    mockMvc
        .perform(
            get("/api/search/forest-cover")
                .param("fileId", "TFL47")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].forestCoverId").exists())
        .andExpect(jsonPath("$.content[0].fileId").exists())
        .andExpect(jsonPath("$.content[0].openingId").exists())
        .andExpect(jsonPath("$.content[0].damageAgents").exists());
  }

  @Test
  @DisplayName("GET /api/search/forest-cover with combined filters should succeed")
  void getForestCover_withCombinedFilters_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/forest-cover")
                .param("fileId", "TFL47")
                .param("openingStatuses", "APP")
                .param("page", "0")
                .param("size", "20")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.page.size").value(20));
  }

  @Test
  @DisplayName("GET /api/search/forest-cover with invalid file ID should return empty")
  void getForestCover_withInvalidFileId_shouldReturnEmpty() throws Exception {
    mockMvc
        .perform(
            get("/api/search/forest-cover")
                .param("fileId", "INVALID_FILE_999999")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page.totalElements").value(0));
  }

  @Test
  @DisplayName("GET /api/search/forest-cover response should have pagination metadata")
  void getForestCover_response_shouldHavePaginationMetadata() throws Exception {
    mockMvc
        .perform(
            get("/api/search/forest-cover")
                .param("fileId", "TFL47")
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
  @DisplayName("GET /api/search/forest-cover without any filters should return error")
  void getForestCover_withoutAnyFilters_shouldReturnError() throws Exception {
    mockMvc
        .perform(get("/api/search/forest-cover").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("GET /api/search/forest-cover with stocking status filter should succeed")
  void getForestCover_withStockingStatusFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/forest-cover")
                .param("stockingStatuses", "FS")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/forest-cover with stocking type filter should succeed")
  void getForestCover_withStockingTypeFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/forest-cover")
                .param("stockingTypes", "RG")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/forest-cover with sorting should succeed")
  void getForestCover_withSorting_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/forest-cover")
                .param("fileId", "TFL47")
                .param("sort", "updateTimestamp,desc")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/forest-cover with all filter types should succeed")
  void getForestCover_withAllFilterTypes_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/forest-cover")
                .param("stockingStatuses", "FS")
                .param("stockingTypes", "RG")
                .param("openingStatuses", "APP")
                .param("fileId", "TFL47")
                .param("orgUnits", "DCR")
                .param("openingCategories", "FTML")
                .param("updateDateStart", "2020-01-01")
                .param("updateDateEnd", "2024-12-31")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }
}
