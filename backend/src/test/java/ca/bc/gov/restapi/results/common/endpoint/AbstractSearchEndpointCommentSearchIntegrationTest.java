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
 * Abstract integration test for the comment search endpoint. Defines the test contract executed
 * against both Postgres and Oracle database implementations.
 */
@WithMockJwt(value = "ttester")
@AutoConfigureMockMvc
@DisplayName("Integrated Test | Comment Search Endpoint | Contract")
public abstract class AbstractSearchEndpointCommentSearchIntegrationTest
    extends AbstractTestContainerIntegrationTest {

  @Autowired protected MockMvc mockMvc;

  @Test
  @DisplayName("GET /api/search/comments with matching searchTerm should return results")
  void commentSearch_withMatchingSearchTerm_shouldReturnResults() throws Exception {
    mockMvc
        .perform(
            get("/api/search/comments")
                .param("searchTerm", "good")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.totalElements").exists());
  }

  @Test
  @DisplayName("GET /api/search/comments with non-matching searchTerm should return empty")
  void commentSearch_withNonMatchingSearchTerm_shouldReturnEmpty() throws Exception {
    mockMvc
        .perform(
            get("/api/search/comments")
                .param("searchTerm", "ZZZNOMATCHZZZXXX999")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page.totalElements").value(0));
  }

  @Test
  @DisplayName("GET /api/search/comments with commentLocation=OPENING should succeed")
  void commentSearch_withCommentLocationOpening_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/comments")
                .param("searchTerm", "far")
                .param("commentLocation", "OPENING")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName(
      "GET /api/search/comments with commentLocation=ACTIVITIES and no match should return empty")
  void commentSearch_withCommentLocationActivities_withNoData_shouldReturnEmpty() throws Exception {
    mockMvc
        .perform(
            get("/api/search/comments")
                .param("searchTerm", "far")
                .param("commentLocation", "ACTIVITIES")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page.totalElements").value(0));
  }

  @Test
  @DisplayName("GET /api/search/comments with pagination should respect page size")
  void commentSearch_withPagination_shouldRespectPageSize() throws Exception {
    mockMvc
        .perform(
            get("/api/search/comments")
                .param("searchTerm", "far")
                .param("page", "0")
                .param("size", "1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page.size").value(1))
        .andExpect(jsonPath("$.page.number").value(0));
  }

  @Test
  @DisplayName("GET /api/search/comments response should contain required fields")
  void commentSearch_response_shouldContainRequiredFields() throws Exception {
    mockMvc
        .perform(
            get("/api/search/comments")
                .param("searchTerm", "good")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].openingId").exists())
        .andExpect(jsonPath("$.content[0].commentLocation").exists())
        .andExpect(jsonPath("$.content[0].commentText").exists());
  }

  @Test
  @DisplayName("GET /api/search/comments should have pagination metadata")
  void commentSearch_response_shouldHavePaginationMetadata() throws Exception {
    mockMvc
        .perform(
            get("/api/search/comments")
                .param("searchTerm", "far")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page.number").exists())
        .andExpect(jsonPath("$.page.size").exists())
        .andExpect(jsonPath("$.page.totalElements").exists())
        .andExpect(jsonPath("$.page.totalPages").exists());
  }

  @Test
  @DisplayName("GET /api/search/comments with valid date range should succeed")
  void commentSearch_withValidDateRange_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/search/comments")
                .param("searchTerm", "far")
                .param("updateDateStart", "2010-01-01")
                .param("updateDateEnd", "2030-12-31")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("GET /api/search/comments with inverted date range should return 400")
  void commentSearch_withInvertedDateRange_shouldReturnBadRequest() throws Exception {
    mockMvc
        .perform(
            get("/api/search/comments")
                .param("searchTerm", "far")
                .param("updateDateStart", "2030-01-01")
                .param("updateDateEnd", "2010-12-31")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("GET /api/search/comments without searchTerm should return 400")
  void commentSearch_withoutSearchTerm_shouldReturnBadRequest() throws Exception {
    mockMvc
        .perform(get("/api/search/comments").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("GET /api/search/comments with 1-char searchTerm should return 400")
  void commentSearch_withOneCharSearchTerm_shouldReturnBadRequest() throws Exception {
    mockMvc
        .perform(
            get("/api/search/comments")
                .param("searchTerm", "a")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("GET /api/search/comments with 2-char searchTerm should return 400")
  void commentSearch_withTwoCharSearchTerm_shouldReturnBadRequest() throws Exception {
    mockMvc
        .perform(
            get("/api/search/comments")
                .param("searchTerm", "go")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("GET /api/search/comments with searchTerm case-insensitive should match")
  void commentSearch_withUpperCaseSearchTerm_shouldMatchCaseInsensitive() throws Exception {
    mockMvc
        .perform(
            get("/api/search/comments")
                .param("searchTerm", "GOOD")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$.page.totalElements").value(org.hamcrest.Matchers.greaterThanOrEqualTo(1)));
  }
}
