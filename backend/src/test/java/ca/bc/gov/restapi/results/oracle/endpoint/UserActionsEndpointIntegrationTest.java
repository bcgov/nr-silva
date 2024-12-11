package ca.bc.gov.restapi.results.oracle.endpoint;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

@AutoConfigureMockMvc
@WithMockJwt
@DisplayName("Integrated Test | User Actions Endpoint")
class UserActionsEndpointIntegrationTest extends AbstractTestContainerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("User recent actions requests test with data should succeed")
  void getUserRecentOpeningsActions_withData_shouldSucceed() throws Exception {

    mockMvc
        .perform(
            get("/api/users/recent-actions")
                .with(csrf().asHeader())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].activityType").value("Submitted"))
        .andExpect(jsonPath("$[0].openingId").value("102"))
        .andExpect(jsonPath("$[0].statusCode").value("FTML"))
        .andExpect(jsonPath("$[0].statusDescription").value("Forest Tenure - Major Licensee"))
        .andReturn();
  }

  @Test
  @DisplayName("User recent actions requests test no data should succeed")
  @WithMockJwt(value = "no-data")
  void getUserRecentOpeningsActions_noData_shouldSucceed() throws Exception {

    mockMvc
        .perform(
            get("/api/users/recent-actions")
                .with(csrf().asHeader())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent())
        .andReturn();
  }

  @Test
  @DisplayName("Openings submission trends happy path should succeed")
  void getOpeningsSubmissionTrends_happyPath_shouldSucceed() throws Exception {

    mockMvc.perform(get("/api/users/submission-trends")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].month").value("12"))
        .andExpect(jsonPath("$[0].amount").value(0))
        .andExpect(jsonPath("$[1].monthName").value("Jan"))
        .andExpect(jsonPath("$[1].amount").value(3));
  }

  @Test
  @DisplayName("Openings submission trends no data should return no content")
  void getOpeningsSubmissionTrends_noData_shouldReturnNoContent() throws Exception {

    mockMvc.perform(get("/api/users/submission-trends")
            .param("orgUnitCode", "ORG1")
            .param("statusCode", "STATUS1")
            .param("entryDateStart", "2022-01-01")
            .param("entryDateEnd", "2023-01-01")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("Openings submission trends with filters should succeed")
  void getOpeningsSubmissionTrends_withFilters_shouldSucceed() throws Exception {

    mockMvc.perform(get("/api/users/submission-trends")
            .param("statusCode", "APP")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].month").value("12"))
        .andExpect(jsonPath("$[0].amount").value(0));
  }

  @Test
  @DisplayName("Openings submission trends with invalid date range should return no content")
  void getOpeningsSubmissionTrends_invalidDateRange_shouldReturnNoContent() throws Exception {

    mockMvc.perform(get("/api/users/submission-trends")
            .param("entryDateStart", "2023-01-01")
            .param("entryDateEnd", "2022-01-01")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }
}