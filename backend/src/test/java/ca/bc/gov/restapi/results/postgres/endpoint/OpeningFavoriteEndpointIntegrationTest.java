package ca.bc.gov.restapi.results.postgres.endpoint;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.postgres.repository.UserOpeningRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@DisplayName("Integration Test | Favorite Openings Endpoint")
@TestMethodOrder(OrderAnnotation.class)
@WithMockJwt
@AutoConfigureMockMvc
class OpeningFavoriteEndpointIntegrationTest extends AbstractTestContainerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserOpeningRepository userOpeningRepository;

  @Test
  @Order(1)
  @DisplayName("No favorites to begin with")
  void shouldBeEmpty() throws Exception {

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/openings/favorites")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty());
  }

  @Test
  @Order(2)
  @DisplayName("Should add to favorite")
  void shouldAddToFavorite() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/api/openings/favorites/{openingId}", 101)
                .with(csrf().asHeader())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isAccepted())
        .andExpect(content().string(StringUtils.EMPTY));

    assertThat(userOpeningRepository.findAll())
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);
  }

  @Test
  @Order(3)
  @DisplayName("Should not add to favorite if doesn't exist")
  void shouldNotAddIfDoesNotExist() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/api/openings/favorites/{openingId}", 987)
                .with(csrf().asHeader())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string(StringUtils.EMPTY));
        //.andExpect(content().string("UserOpening record(s) not found!"));
  }

  @Test
  @Order(4)
  @DisplayName("Multiple requests to add to favorite should not fail, nor duplicate")
  void shouldAddToFavoriteAgain() throws Exception {
    shouldAddToFavorite();
  }

  @Test
  @Order(5)
  @DisplayName("Should see list of favorites")
  void shouldBeAbleToSeeOpening() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/openings/favorites")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[0]").value(101));
  }

  @Test
  @Order(6)
  @DisplayName("Should remove from favorite")
  void shouldRemoveFromFavorites() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/api/openings/favorites/{openingId}", 101)
                .with(csrf().asHeader())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent())
        .andExpect(content().string(StringUtils.EMPTY));

    assertThat(userOpeningRepository.findAll())
        .isNotNull()
        .isEmpty();
  }

  @Test
  @Order(7)
  @DisplayName("Should thrown an error if trying to remove entry that doesn't exist")
  void shouldThrownErrorIfNoFavoriteFound() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/api/openings/favorites/{openingId}", 101)
                .with(csrf().asHeader())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string(StringUtils.EMPTY));

  }


}