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

}