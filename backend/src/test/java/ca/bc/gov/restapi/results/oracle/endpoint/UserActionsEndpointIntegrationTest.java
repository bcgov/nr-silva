package ca.bc.gov.restapi.results.oracle.endpoint;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@WithMockJwt
@DisplayName("Integrated Test | User Actions Endpoint")
class UserActionsEndpointIntegrationTest extends AbstractTestContainerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private OpeningRepository openingRepository;

  @BeforeEach
  @Transactional
  void setTup() {
    openingRepository
        .findById(1009974L)
        .map(
            openingEntity ->
                openingEntity
                    .withUpdateTimestamp(
                        LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIDNIGHT))
                    .withEntryTimestamp(
                        LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIDNIGHT)))
        .map(openingRepository::save);
  }

  @Test
  @DisplayName("Openings submission trends happy path should succeed")
  void getOpeningsSubmissionTrends_happyPath_shouldSucceed() throws Exception {
    mockMvc
        .perform(get("/api/users/submission-trends").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[12].month").value(LocalDate.now().getMonthValue()))
        .andExpect(jsonPath("$[12].amount").value(1));
  }

  @Test
  @DisplayName("Openings submission trends no data should return no content")
  void getOpeningsSubmissionTrends_noData_shouldReturnNoContent() throws Exception {

    mockMvc
        .perform(
            get("/api/users/submission-trends")
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

    mockMvc
        .perform(
            get("/api/users/submission-trends")
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

    mockMvc
        .perform(
            get("/api/users/submission-trends")
                .param("entryDateStart", "2023-01-01")
                .param("entryDateEnd", "2022-01-01")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }
}
