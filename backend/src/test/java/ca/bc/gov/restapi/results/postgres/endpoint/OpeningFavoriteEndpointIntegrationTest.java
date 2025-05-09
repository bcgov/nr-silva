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
  @DisplayName("No favourites to begin with")
  void shouldBeEmpty() throws Exception {

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/openings/favourites")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty());
  }

  @Test
  @Order(2)
  @DisplayName("Should check if entry is favourite and get false")
  void shouldCheckIfEntryIsFavouriteGetFails() throws Exception {
    checkFavourite(101017,false);
  }

  @Test
  @Order(3)
  @DisplayName("Should add to favorite")
  void shouldAddToFavorite() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/api/openings/favourites/{openingId}", 101017)
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
  @Order(4)
  @DisplayName("Should check if entry is favourite")
  void shouldCheckIfEntryIsFavourite() throws Exception {
    checkFavourite(101017,true);
  }

  @Test
  @Order(5)
  @DisplayName("Should not add to favorite if doesn't exist")
  void shouldNotAddIfDoesNotExist() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/api/openings/favourites/{openingId}", 987)
                .with(csrf().asHeader())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
        .andExpect(content().json("{\"type\":\"about:blank\",\"title\":\"Not Found\",\"status\":404,\"detail\":\"UserOpening record(s) not found!\",\"instance\":\"/api/openings/favourites/987\"}"));
  }

  @Test
  @Order(6)
  @DisplayName("Multiple requests to add to favorite should not fail, nor duplicate")
  void shouldAddToFavoriteAgain() throws Exception {
    shouldAddToFavorite();
  }

  @Test
  @Order(7)
  @DisplayName("Should see list of favourites")
  void shouldBeAbleToSeeOpening() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/openings/favourites")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[0]").value(101017));
  }

  @Test
  @Order(8)
  @DisplayName("Should remove from favorite")
  void shouldRemoveFromFavorites() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/api/openings/favourites/{openingId}", 101017)
                .with(csrf().asHeader())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent())
        .andExpect(content().string(StringUtils.EMPTY));

    assertThat(userOpeningRepository.findAll())
        .isNotNull()
        .isEmpty();
  }

  @Test
  @Order(9)
  @DisplayName("Should thrown an error if trying to remove entry that doesn't exist")
  void shouldThrownErrorIfNoFavoriteFound() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/api/openings/favourites/{openingId}", 101)
                .with(csrf().asHeader())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
        .andExpect(content().json("{\"type\":\"about:blank\",\"title\":\"Not Found\",\"status\":404,\"detail\":\"UserFavoriteEntity record(s) not found!\",\"instance\":\"/api/openings/favourites/101\"}"));

  }

  private void checkFavourite(long id, boolean expected) throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/openings/favourites/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(expected+""));
  }


}