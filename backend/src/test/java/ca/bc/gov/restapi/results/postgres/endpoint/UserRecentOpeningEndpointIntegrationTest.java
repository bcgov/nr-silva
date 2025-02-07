package ca.bc.gov.restapi.results.postgres.endpoint;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.postgres.entity.UserRecentOpeningEntity;
import ca.bc.gov.restapi.results.postgres.repository.UserRecentOpeningRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("Integrated Test | Recent Openings")
@AutoConfigureMockMvc
@WithMockJwt("jakethedog")
class UserRecentOpeningEndpointIntegrationTest extends AbstractTestContainerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRecentOpeningRepository userRecentOpeningRepository;

  @BeforeEach
  public void dataSetUp() {
    userRecentOpeningRepository.deleteAll();
    userRecentOpeningRepository.saveAllAndFlush(
        List.of(
            new UserRecentOpeningEntity(null, "IDIR\\JAKETHEDOG", 100L, LocalDateTime.now()),
            new UserRecentOpeningEntity(null, "IDIR\\JAKETHEDOG", 101L, LocalDateTime.now().plusMinutes(3)),
            new UserRecentOpeningEntity(null, "IDIR\\JAKETHEDOG", 101L, LocalDateTime.now().minusMinutes(10))
        )
    );
  }

  @Test
  @DisplayName("Should fail if we have a conflicting data")
  void shouldReturnProblemDetailsError() throws Exception {

    mockMvc
        .perform(
            get("/api/openings/recent")
                .with(csrf().asHeader())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE));
  }

  @Test
  @DisplayName("Should return a failure when no opening with that ID exists")
  void shouldReturnNotFoundWhenUserHasNoRecentOpenings() throws Exception {
    mockMvc
        .perform(
            put("/api/openings/recent/123456")
                .with(csrf().asHeader())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
        .andExpect(content().json("{\"type\":\"about:blank\",\"title\":\"Not Found\",\"status\":404,\"detail\":\"UserOpening record(s) not found!\",\"instance\":\"/api/openings/recent/123456\"}"));
  }



}