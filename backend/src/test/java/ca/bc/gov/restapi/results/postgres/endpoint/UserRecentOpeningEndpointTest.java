package ca.bc.gov.restapi.results.postgres.endpoint;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
class UserRecentOpeningEndpointTest extends AbstractTestContainerIntegrationTest {

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
  void shouldReturnProblemDetailsError() throws Exception {

    mockMvc
        .perform(
            get("/api/openings/recent")
                .with(csrf().asHeader())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().is4xxClientError());
  }



}