package ca.bc.gov.restapi.results.postgres.endpoint;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.common.service.DashboardExtractionService;
import ca.bc.gov.restapi.results.postgres.config.DashboardUserManagerConfig;
import ca.bc.gov.restapi.results.postgres.entity.OracleExtractionLogsEntity;
import ca.bc.gov.restapi.results.postgres.repository.OracleExtractionLogsRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DashboardExtractionEnpoint.class)
@WithMockUser
class DashboardExtractionEnpointTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private OracleExtractionLogsRepository oracleExtractionLogsRepository;

  @MockBean private DashboardExtractionService dashboardExtractionService;

  @MockBean private DashboardUserManagerConfig dashboardUserManagerConfig;

  @MockBean private LoggedUserService loggedUserService;

  @Test
  @DisplayName("Start extraction process manually happy path should succeed")
  void startExtractionProcessManually_happyPath_shouldSucceed() throws Exception {
    Integer months = 24;
    Boolean debug = Boolean.FALSE;

    when(dashboardUserManagerConfig.getUserList()).thenReturn(List.of("TEST"));
    when(loggedUserService.getLoggedUserIdirOrBceId()).thenReturn("TEST");
    doNothing().when(dashboardExtractionService).extractDataForTheDashboard(months, debug, true);

    mockMvc
        .perform(
            post("/api/dashboard-extraction/start?months={months}&debug={debug}", months, debug)
                .with(csrf().asHeader())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent())
        .andReturn();
  }

  @Test
  @DisplayName("Start extraction process manually user not authorized should fail")
  void startExtractionProcessManually_userNotAuthorized_shouldFail() throws Exception {
    Integer months = 24;
    Boolean debug = Boolean.FALSE;

    when(dashboardUserManagerConfig.getUserList()).thenReturn(List.of("TEST"));
    when(loggedUserService.getLoggedUserIdirOrBceId()).thenReturn("TEST");
    doNothing().when(dashboardExtractionService).extractDataForTheDashboard(months, debug, true);

    mockMvc
        .perform(
            post("/api/dashboard-extraction/start?months={months}&debug={debug}", months, debug)
                .with(csrf().asHeader())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent())
        .andReturn();
  }

  @Test
  @DisplayName("Start extraction process manually empty users should fail")
  void getLastExtractionLogs_emptyUsers_shouldFail() throws Exception {
    Integer months = 24;
    Boolean debug = Boolean.FALSE;

    when(dashboardUserManagerConfig.getUserList()).thenReturn(List.of());

    mockMvc
        .perform(
            post("/api/dashboard-extraction/start?months={months}&debug={debug}", months, debug)
                .with(csrf().asHeader())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andReturn();
  }

  @Test
  @DisplayName("Start extraction process manually user not authorized should fail")
  void getLastExtractionLogs_userNotAuthorized_shouldFail() throws Exception {
    Integer months = 24;
    Boolean debug = Boolean.FALSE;

    when(dashboardUserManagerConfig.getUserList()).thenReturn(List.of("AA"));
    when(loggedUserService.getLoggedUserIdirOrBceId()).thenReturn("BB");

    mockMvc
        .perform(
            post("/api/dashboard-extraction/start?months={months}&debug={debug}", months, debug)
                .with(csrf().asHeader())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andReturn();
  }

  @Test
  @DisplayName("Get last extraction logs happy path should succeed")
  void getLastExtractionLogs_happyPath_shouldSucceed() throws Exception {
    OracleExtractionLogsEntity extractionLogs = new OracleExtractionLogsEntity();
    extractionLogs.setId(1L);
    extractionLogs.setLogMessage("Test message");
    extractionLogs.setManuallyTriggered(false);
    when(oracleExtractionLogsRepository.findAll(any(Sort.class)))
        .thenReturn(List.of(extractionLogs));

    mockMvc
        .perform(
            get("/api/dashboard-extraction/logs")
                .with(csrf().asHeader())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].id").value("1"))
        .andExpect(jsonPath("$[0].logMessage").value("Test message"))
        .andExpect(jsonPath("$[0].manuallyTriggered").value("false"))
        .andReturn();
  }

  @Test
  @DisplayName("Get last extraction logs empty logs should succeed")
  void getLastExtractionLogs_emptyLogs_shouldSucceed() throws Exception {
    when(oracleExtractionLogsRepository.findAll(any(Sort.class))).thenReturn(List.of());

    mockMvc
        .perform(
            get("/api/dashboard-extraction/logs")
                .with(csrf().asHeader())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent())
        .andReturn();
  }
}
