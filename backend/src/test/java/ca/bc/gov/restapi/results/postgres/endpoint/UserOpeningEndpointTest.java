package ca.bc.gov.restapi.results.postgres.endpoint;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.postgres.dto.MyRecentActionsRequestsDto;
import ca.bc.gov.restapi.results.postgres.service.UserOpeningService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserOpeningEndpoint.class)
@WithMockUser
class UserOpeningEndpointTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private UserOpeningService userOpeningService;

  @Test
  @DisplayName("Get user tracked openings happy path should succeed")
  void getUserTrackedOpenings_happyPath_shouldSucceed() throws Exception {
    MyRecentActionsRequestsDto action =
        new MyRecentActionsRequestsDto(
            "Update", 123456L, "APP", "Approved", "2 minutes ago", LocalDateTime.now());
    when(userOpeningService.getUserTrackedOpenings()).thenReturn(List.of(action));

    mockMvc
        .perform(
            get("/api/user-openings/dashboard-track-openings")
                .with(csrf().asHeader())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].activityType").value("Update"))
        .andExpect(jsonPath("$[0].openingId").value("123456"))
        .andExpect(jsonPath("$[0].statusCode").value("APP"))
        .andExpect(jsonPath("$[0].statusDescription").value("Approved"))
        .andExpect(jsonPath("$[0].lastUpdatedLabel").value("2 minutes ago"))
        .andReturn();
  }

  @Test
  @DisplayName("Get user tracked openings no data should succeed")
  void getUserTrackedOpenings_noData_shouldSucceed() throws Exception {
    when(userOpeningService.getUserTrackedOpenings()).thenReturn(List.of());

    mockMvc
        .perform(
            get("/api/user-openings/dashboard-track-openings")
                .with(csrf().asHeader())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent())
        .andReturn();
  }

  void saveUserOpening_happyPath_shouldSucceed() throws Exception {
    //
  }

  void deleteUserOpening_happyPath_shouldSucceed() throws Exception {
    //
  }

  void deleteUserOpening_notFound_shouldFail() throws Exception {
    //
  }
}
