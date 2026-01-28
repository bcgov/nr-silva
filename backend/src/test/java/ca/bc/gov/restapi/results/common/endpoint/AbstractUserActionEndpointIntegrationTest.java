package ca.bc.gov.restapi.results.common.endpoint;

import ca.bc.gov.restapi.results.common.entity.BaseOpeningEntity;
import ca.bc.gov.restapi.results.common.repository.OpeningRepository;
import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WithMockJwt
@DisplayName("Integrated Test | User Action Endpoint | Contract")
public abstract class AbstractUserActionEndpointIntegrationTest<T extends
    OpeningRepository<? extends BaseOpeningEntity>> extends AbstractTestContainerIntegrationTest {

  @Autowired
  protected MockMvc mockMvc;

  protected abstract void setupTestData();

  @BeforeEach
  void setup() {
    this.setupTestData();
  }

  @Test
  @DisplayName("Openings submission trends happy path should succeed")
  void getOpeningsSubmissionTrends_happyPath_shouldSucceed() throws Exception {
    mockMvc.perform(get("/api/users/submission-trends")
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[12].month").value(LocalDate.now().getMonthValue()))
        .andExpect(jsonPath("$[12].amount").value(1));

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
            .param("statusCode", "FG")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[12].month").value(LocalDate.now().getMonthValue()))
        .andExpect(jsonPath("$[12].amount").value(1));

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
