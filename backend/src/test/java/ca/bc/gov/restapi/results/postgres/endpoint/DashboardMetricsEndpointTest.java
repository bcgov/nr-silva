package ca.bc.gov.restapi.results.postgres.endpoint;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.postgres.dto.FreeGrowingMilestonesDto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("Integrated Test | Dashboard Metrics Endpoint")
@AutoConfigureMockMvc
@WithMockJwt
class DashboardMetricsEndpointTest extends AbstractTestContainerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("Opening submission trends with no filters should succeed")
  void getOpeningsSubmissionTrends_noFilters_shouldSucceed() throws Exception {

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
        .andExpect(jsonPath("$[0].amount").value("1"))
        .andReturn();
  }

  @Test
  @DisplayName("Opening submission trends with no data should succeed")
  void getOpeningsSubmissionTrends_orgUnitFilter_shouldSucceed() throws Exception {

    mockMvc
        .perform(
            get("/api/dashboard-metrics/submission-trends")
                .with(csrf().asHeader())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Test
  @DisplayName("Free growing milestones test with no filters should succeed")
  void getFreeGrowingMilestonesData_noFilters_shouldSucceed() throws Exception {

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
        .andExpect(jsonPath("$[0].amount").value("0"))
        .andExpect(jsonPath("$[0].percentage").value(new BigDecimal("0")))
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
        .andExpect(jsonPath("$[0].amount").value("0"))
        .andReturn();
  }

  @Test
  @DisplayName("Free growing milestones test with no content should succeed")
  void getFreeGrowingMilestonesData_noData_shouldSucceed() throws Exception {

    mockMvc
        .perform(
            get("/api/dashboard-metrics/free-growing-milestones")
                .with(csrf().asHeader())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
  }
}
