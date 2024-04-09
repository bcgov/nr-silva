package ca.bc.gov.restapi.results.postgres.endpoint;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.postgres.dto.DashboardFiltesDto;
import ca.bc.gov.restapi.results.postgres.dto.OpeningsPerYearDto;
import ca.bc.gov.restapi.results.postgres.service.DashboardMetricsService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DashboardMetricsEndpoint.class)
@WithMockUser
class DashboardMetricsEndpointTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private DashboardMetricsService dashboardMetricsService;

  @Test
  @DisplayName("Opening submission trends with no filters should succeed")
  void getOpeningsSubmissionTrends_noFilters_shouldSucceed() throws Exception {
    DashboardFiltesDto filtersDto = new DashboardFiltesDto(null, null, null, null, null);

    OpeningsPerYearDto dto = new OpeningsPerYearDto(1, "Jan", 70);
    when(dashboardMetricsService.getOpeningsSubmissionTrends(filtersDto)).thenReturn(List.of(dto));

    mockMvc
        .perform(
            get("/api/dashboard-metrics/submission-trends")
                .with(csrf().asHeader())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].month").value("1"))
        .andExpect(jsonPath("$[0].monthName").value("Jan"))
        .andExpect(jsonPath("$[0].amount").value("70"))
        .andReturn();
  }

  @Test
  @DisplayName("Opening submission trends with no data should succeed")
  void getOpeningsSubmissionTrends_orgUnitFilter_shouldSucceed() throws Exception {
    DashboardFiltesDto filtersDto = new DashboardFiltesDto("DCR", null, null, null, null);

    when(dashboardMetricsService.getOpeningsSubmissionTrends(filtersDto)).thenReturn(List.of());

    mockMvc
        .perform(
            get("/api/dashboard-metrics/submission-trends")
                .with(csrf().asHeader())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent())
        .andReturn();
  }
}
