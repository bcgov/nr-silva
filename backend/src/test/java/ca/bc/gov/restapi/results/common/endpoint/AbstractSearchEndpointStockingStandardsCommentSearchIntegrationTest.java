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
 * Abstract integration test for stocking-standards comment search endpoint. Defines the test
 * contract for all implementations (Postgres and Oracle).
 */
@WithMockJwt(value = "ttester")
@AutoConfigureMockMvc
@DisplayName("Integrated Test | Stocking Standards Comment Search Endpoint | Contract")
public abstract class AbstractSearchEndpointStockingStandardsCommentSearchIntegrationTest
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
  @DisplayName("GET /api/search/stocking-standards/comments with searchTerm should return 200")
  void commentSearch_withSearchTerm_shouldReturn200() throws Exception {
    mockMvc
        .perform(
            get("/api/search/stocking-standards/comments")
                .param("searchTerm", "Baseline")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.totalElements").exists());
  }

  @Test
  @DisplayName("GET /api/search/stocking-standards/comments missing searchTerm returns 400")
  void commentSearch_missingSearchTerm_returns400() throws Exception {
    mockMvc
        .perform(
            get("/api/search/stocking-standards/comments").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("GET /api/search/stocking-standards/comments too-short searchTerm returns 400")
  void commentSearch_tooShortSearchTerm_returns400() throws Exception {
    mockMvc
        .perform(
            get("/api/search/stocking-standards/comments")
                .param("searchTerm", "AB")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("GET /api/search/stocking-standards/comments with non-matching term returns empty")
  void commentSearch_withNonMatchingTerm_returnsEmpty() throws Exception {
    mockMvc
        .perform(
            get("/api/search/stocking-standards/comments")
                .param("searchTerm", "ZZZNOMATCHZZZXXX999")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page.totalElements").value(0));
  }

  @Test
  @DisplayName("GET /api/search/stocking-standards/comments with STANDARDS_NAME location filter")
  void commentSearch_withStandardsNameLocation_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/stocking-standards/comments")
                .param("searchTerm", "Baseline")
                .param("commentLocations", "STANDARDS_NAME")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/stocking-standards/comments with multiple commentLocations")
  void commentSearch_withMultipleCommentLocations_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/stocking-standards/comments")
                .param("searchTerm", "Baseline")
                .param("commentLocations", "STANDARDS_NAME")
                .param("commentLocations", "ADDITIONAL_STANDARDS")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/stocking-standards/comments with orgUnits filter should succeed")
  void commentSearch_withOrgUnits_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/stocking-standards/comments")
                .param("searchTerm", "Baseline")
                .param("orgUnits", "DAS")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/stocking-standards/comments with date range should succeed")
  void commentSearch_withDateRange_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/stocking-standards/comments")
                .param("searchTerm", "Baseline")
                .param("updateDateStart", "2000-01-01")
                .param("updateDateEnd", "2030-12-31")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/stocking-standards/comments pagination page size respected")
  void commentSearch_withPagination_shouldRespectPageSize() throws Exception {
    mockMvc
        .perform(
            get("/api/search/stocking-standards/comments")
                .param("searchTerm", "Baseline")
                .param("page", "0")
                .param("size", "1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page.size").value(1))
        .andExpect(jsonPath("$.page.number").value(0));
  }

  @Test
  @DisplayName(
      "GET /api/search/stocking-standards/comments response should have full page metadata")
  void commentSearch_shouldHaveFullPageMetadata() throws Exception {
    mockMvc
        .perform(
            get("/api/search/stocking-standards/comments")
                .param("searchTerm", "Baseline")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page.number").exists())
        .andExpect(jsonPath("$.page.size").exists())
        .andExpect(jsonPath("$.page.totalElements").exists())
        .andExpect(jsonPath("$.page.totalPages").exists());
  }
}
