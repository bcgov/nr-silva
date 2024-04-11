package ca.bc.gov.restapi.results.postgres.endpoint;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.postgres.dto.DashboardFiltesDto;
import ca.bc.gov.restapi.results.postgres.dto.FreeGrowingMilestonesDto;
import ca.bc.gov.restapi.results.postgres.dto.MyRecentActionsRequestsDto;
import ca.bc.gov.restapi.results.postgres.dto.OpeningsPerYearDto;
import ca.bc.gov.restapi.results.postgres.service.DashboardMetricsService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

  @Test
  @DisplayName("Free growing milestones test with no filters should succeed")
  void getFreeGrowingMilestonesData_noFilters_shouldSucceed() throws Exception {
    DashboardFiltesDto filtersDto = new DashboardFiltesDto(null, null, null, null, null);

    FreeGrowingMilestonesDto milestonesDto =
        new FreeGrowingMilestonesDto(0, "0 - 5 months", 25, new BigDecimal("100"));
    when(dashboardMetricsService.getFreeGrowingMilestoneChartData(filtersDto))
        .thenReturn(List.of(milestonesDto));

    mockMvc
        .perform(
            get("/api/dashboard-metrics/free-growing-milestones")
                .with(csrf().asHeader())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].index").value("0"))
        .andExpect(jsonPath("$[0].label").value("0 - 5 months"))
        .andExpect(jsonPath("$[0].amount").value("25"))
        .andExpect(jsonPath("$[0].percentage").value(new BigDecimal("100")))
        .andReturn();
  }

  @Test
  @DisplayName("Free growing milestones test with client number filter should succeed")
  void getFreeGrowingMilestonesData_clientNumberFilter_shouldSucceed() throws Exception {
    List<FreeGrowingMilestonesDto> dtoList = new ArrayList<>();
    dtoList.add(new FreeGrowingMilestonesDto(0, "0 - 5 months", 25, new BigDecimal("25")));
    dtoList.add(new FreeGrowingMilestonesDto(1, "6 - 11 months", 25, new BigDecimal("25")));
    dtoList.add(new FreeGrowingMilestonesDto(2, "12 - 17 months", 25, new BigDecimal("25")));
    dtoList.add(new FreeGrowingMilestonesDto(3, "18 months", 25, new BigDecimal("25")));

    DashboardFiltesDto filtersDto = new DashboardFiltesDto(null, null, null, null, "00012797");

    when(dashboardMetricsService.getFreeGrowingMilestoneChartData(filtersDto)).thenReturn(dtoList);

    mockMvc
        .perform(
            get("/api/dashboard-metrics/free-growing-milestones?clientNumber=00012797")
                .with(csrf().asHeader())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].index").value("0"))
        .andExpect(jsonPath("$[0].label").value("0 - 5 months"))
        .andExpect(jsonPath("$[0].amount").value("25"))
        .andExpect(jsonPath("$[0].percentage").value(new BigDecimal("25")))
        .andExpect(jsonPath("$[1].index").value("1"))
        .andExpect(jsonPath("$[1].label").value("6 - 11 months"))
        .andExpect(jsonPath("$[1].amount").value("25"))
        .andExpect(jsonPath("$[1].percentage").value(new BigDecimal("25")))
        .andExpect(jsonPath("$[2].index").value("2"))
        .andExpect(jsonPath("$[2].label").value("12 - 17 months"))
        .andExpect(jsonPath("$[2].amount").value("25"))
        .andExpect(jsonPath("$[2].percentage").value(new BigDecimal("25")))
        .andExpect(jsonPath("$[3].index").value("3"))
        .andExpect(jsonPath("$[3].label").value("18 months"))
        .andExpect(jsonPath("$[3].amount").value("25"))
        .andExpect(jsonPath("$[3].percentage").value(new BigDecimal("25")))
        .andReturn();
  }

  @Test
  @DisplayName("Free growing milestones test with no content should succeed")
  void getFreeGrowingMilestonesData_noData_shouldSucceed() throws Exception {
    DashboardFiltesDto filtersDto = new DashboardFiltesDto(null, null, null, null, "00012579");

    when(dashboardMetricsService.getFreeGrowingMilestoneChartData(filtersDto))
        .thenReturn(List.of());

    mockMvc
        .perform(
            get("/api/dashboard-metrics/free-growing-milestones")
                .with(csrf().asHeader())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent())
        .andReturn();
  }

  @Test
  @DisplayName("User recent actions requests test happy path should succeed")
  void getUserRecentOpeningsActions_happyPath_shouldSucceed() throws Exception {
    MyRecentActionsRequestsDto actionDto =
        new MyRecentActionsRequestsDto(
            "Created", 48L, "PEN", "Pending", "2 minutes ago", LocalDateTime.now());
    when(dashboardMetricsService.getUserRecentOpeningsActions()).thenReturn(List.of(actionDto));

    mockMvc
        .perform(
            get("/api/dashboard-metrics/my-recent-actions/requests")
                .with(csrf().asHeader())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].activityType").value("Created"))
        .andExpect(jsonPath("$[0].openingId").value("48"))
        .andExpect(jsonPath("$[0].statusCode").value("PEN"))
        .andExpect(jsonPath("$[0].statusDescription").value("Pending"))
        .andExpect(jsonPath("$[0].lastUpdatedLabel").value("2 minutes ago"))
        .andReturn();
  }

  @Test
  @DisplayName("User recent actions requests test no data should succeed")
  void getUserRecentOpeningsActions_noData_shouldSucceed() throws Exception {
    when(dashboardMetricsService.getUserRecentOpeningsActions()).thenReturn(List.of());

    mockMvc
        .perform(
            get("/api/dashboard-metrics/my-recent-actions/requests")
                .with(csrf().asHeader())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent())
        .andReturn();
  }
}
