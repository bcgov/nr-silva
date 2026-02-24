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
 * Abstract integration test for activity search endpoints. Defines test contract for all
 * SearchEndpoint activitySearch implementations (Postgres and Oracle).
 */
@WithMockJwt(value = "ttester")
@AutoConfigureMockMvc
@DisplayName("Integrated Test | Activity Search Endpoint | Contract")
public abstract class AbstractSearchEndpointActivitySearchIntegrationTest
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
  @DisplayName("GET /api/search/activities with default parameters should succeed")
  void getActivities_withDefaultParameters_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/activities")
                .param("bases", "PR")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.totalElements").exists())
        .andExpect(jsonPath("$.page.totalPages").exists());
  }

  @Test
  @DisplayName("GET /api/search/activities with file ID filter should succeed")
  void getActivities_withFileIdFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/activities")
                .param("fileId", "TFL47")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.page").exists());
  }

  @Test
  @DisplayName("GET /api/search/activities with base code filter should succeed")
  void getActivities_withBaseCodeFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/activities")
                .param("bases", "PR")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/activities with multiple base codes should succeed")
  void getActivities_withMultipleBaseCodes_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/activities")
                .param("bases", "PR")
                .param("bases", "SP")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/activities with technique filter should succeed")
  void getActivities_withTechniqueFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/activities")
                .param("techniques", "DEC")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/activities with pagination should succeed")
  void getActivities_withPagination_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/activities")
                .param("bases", "PR")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page.number").value(0))
        .andExpect(jsonPath("$.page.size").value(10));
  }

  @Test
  @DisplayName("GET /api/search/activities with sorting should succeed")
  void getActivities_withSorting_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/activities")
                .param("bases", "PR")
                .param("sort", "silvBaseCode,desc")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/activities response should contain required fields")
  void getActivities_response_shouldContainRequiredFields() throws Exception {
    mockMvc
        .perform(
            get("/api/search/activities")
                .param("fileId", "TFL47")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].openingId").exists())
        .andExpect(jsonPath("$.content[0].activityId").exists())
        .andExpect(jsonPath("$.content[0].base").exists());
  }

  @Test
  @DisplayName("GET /api/search/activities with combined filters should succeed")
  void getActivities_withCombinedFilters_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/activities")
                .param("fileId", "TFL47")
                .param("bases", "PR")
                .param("page", "0")
                .param("size", "20")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.page.size").value(20));
  }

  @Test
  @DisplayName("GET /api/search/activities with invalid file ID should return empty")
  void getActivities_withInvalidFileId_shouldReturnEmpty() throws Exception {
    mockMvc
        .perform(
            get("/api/search/activities")
                .param("fileId", "INVALID_FILE_999999")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page.totalElements").value(0));
  }

  @Test
  @DisplayName("GET /api/search/activities response should have pagination metadata")
  void getActivities_response_shouldHavePaginationMetadata() throws Exception {
    mockMvc
        .perform(
            get("/api/search/activities")
                .param("bases", "PR")
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
  @DisplayName("GET /api/search/activities without any filters should return error")
  void getActivities_withoutAnyFilters_shouldReturnError() throws Exception {
    mockMvc
        .perform(get("/api/search/activities").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("GET /api/search/disturbances with default parameters should succeed")
  void getDisturbances_withDefaultParameters_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/disturbances")
                .param("disturbances", "BR")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.totalElements").exists())
        .andExpect(jsonPath("$.page.totalPages").exists());
  }

  @Test
  @DisplayName("GET /api/search/disturbances with file ID filter should succeed")
  void getDisturbances_withFileIdFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/disturbances")
                .param("fileId", "TFL47")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.page").exists());
  }

  @Test
  @DisplayName("GET /api/search/disturbances with disturbance code filter should succeed")
  void getDisturbances_withDisturbanceCodeFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/disturbances")
                .param("disturbances", "BR")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/disturbances with multiple disturbance codes should succeed")
  void getDisturbances_withMultipleDisturbanceCodes_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/disturbances")
                .param("disturbances", "BR")
                .param("disturbances", "SL")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/disturbances with silviculture system filter should succeed")
  void getDisturbances_withSilvSystemFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/disturbances")
                .param("silvSystems", "CC")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/disturbances with variant filter should succeed")
  void getDisturbances_withVariantFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/disturbances")
                .param("variants", "CTN")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/disturbances with lowercase variant filter should succeed")
  void getDisturbances_withLowercaseVariantFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/disturbances")
                .param("variants", "ctn")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/disturbances with cut phase filter should succeed")
  void getDisturbances_withCutPhaseFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/disturbances")
                .param("cutPhases", "FI")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/disturbances with org unit filter should succeed")
  void getDisturbances_withOrgUnitFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/disturbances")
                .param("orgUnits", "DCR")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/disturbances with opening category filter should succeed")
  void getDisturbances_withOpeningCategoryFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/disturbances")
                .param("openingCategories", "FTML")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/disturbances with client number filter should succeed")
  void getDisturbances_withClientNumberFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/disturbances")
                .param("clientNumbers", "00012797")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/disturbances with opening status filter should succeed")
  void getDisturbances_withOpeningStatusFilter_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/disturbances")
                .param("openingStatuses", "APP")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/disturbances with update date range should succeed")
  void getDisturbances_withUpdateDateRange_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/disturbances")
                .param("updateDateStart", "2020-01-01")
                .param("updateDateEnd", "2024-12-31")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/disturbances with pagination should succeed")
  void getDisturbances_withPagination_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/disturbances")
                .param("disturbances", "BR")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page.number").value(0))
        .andExpect(jsonPath("$.page.size").value(10));
  }

  @Test
  @DisplayName("GET /api/search/disturbances with sorting should succeed")
  void getDisturbances_withSorting_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/disturbances")
                .param("disturbances", "BR")
                .param("sort", "disturbanceCode,desc")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/disturbances response should contain required fields")
  void getDisturbances_response_shouldContainRequiredFields() throws Exception {
    mockMvc
        .perform(
            get("/api/search/disturbances")
                .param("fileId", "TFL47")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].openingId").exists())
        .andExpect(jsonPath("$.content[0].activityId").exists())
        .andExpect(jsonPath("$.content[0].disturbance").exists());
  }

  @Test
  @DisplayName("GET /api/search/disturbances with combined filters should succeed")
  void getDisturbances_withCombinedFilters_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/disturbances")
                .param("fileId", "TFL47")
                .param("disturbances", "BR")
                .param("silvSystems", "CC")
                .param("page", "0")
                .param("size", "20")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.page.size").value(20));
  }

  @Test
  @DisplayName("GET /api/search/disturbances with invalid file ID should return empty")
  void getDisturbances_withInvalidFileId_shouldReturnEmpty() throws Exception {
    mockMvc
        .perform(
            get("/api/search/disturbances")
                .param("fileId", "INVALID_FILE_999999")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page.totalElements").value(0));
  }

  @Test
  @DisplayName("GET /api/search/disturbances response should have pagination metadata")
  void getDisturbances_response_shouldHavePaginationMetadata() throws Exception {
    mockMvc
        .perform(
            get("/api/search/disturbances")
                .param("disturbances", "BR")
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
  @DisplayName("GET /api/search/disturbances without any filters should return error")
  void getDisturbances_withoutAnyFilters_shouldReturnError() throws Exception {
    mockMvc
        .perform(get("/api/search/disturbances").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("GET /api/search/disturbances with multiple variants should succeed")
  void getDisturbances_withMultipleVariants_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/disturbances")
                .param("variants", "CTN")
                .param("variants", "STN")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/disturbances with multiple org units should succeed")
  void getDisturbances_withMultipleOrgUnits_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/disturbances")
                .param("orgUnits", "DCR")
                .param("orgUnits", "DND")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/disturbances with all filter types should succeed")
  void getDisturbances_withAllFilterTypes_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/disturbances")
                .param("disturbances", "BR")
                .param("silvSystems", "CC")
                .param("variants", "CTN")
                .param("cutPhases", "FI")
                .param("orgUnits", "DCR")
                .param("openingCategories", "FTML")
                .param("fileId", "TFL47")
                .param("clientNumbers", "00012797")
                .param("openingStatuses", "APP")
                .param("updateDateStart", "2020-01-01")
                .param("updateDateEnd", "2024-12-31")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }
}
