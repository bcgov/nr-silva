package ca.bc.gov.restapi.results.postgres.endpoint;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@DisplayName("Integration Test | Stocking Standard Species Validation Endpoint")
@AutoConfigureMockMvc
class StockingStandardSpeciesValidationEndpointIntegrationTest
    extends AbstractTestContainerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @WithMockJwt
  @DisplayName("Valid layer species returns 200 and per-species success")
  void validateSpecies_validLayerSpecies_returns200WithSuccess() throws Exception {
    mockMvc
        .perform(
            post("/api/stocking-standards/validate/species")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    [{"layerCode":"4","species":[{"speciesCode":"cw","speciesType":"PREFERRED","minHeight":1.0}]}]
                    """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.isValid").value(true))
        .andExpect(jsonPath("$.validationResults[0].layerCode").value("4"))
        .andExpect(jsonPath("$.validationResults[0].speciesValidationResults[0].isValid").value(true))
        .andExpect(jsonPath("$.duplicateConflicts").isEmpty());
  }

  @Test
  @WithMockJwt
  @DisplayName("Unknown species returns 200 and an accurate per-species error")
  void validateSpecies_unknownCode_returns200WithError() throws Exception {
    mockMvc
        .perform(
            post("/api/stocking-standards/validate/species")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    [{"layerCode":"3","species":[{"speciesCode":"ZZ","speciesType":"ACCEPTABLE"}]}]
                    """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.isValid").value(false))
        .andExpect(
            jsonPath("$.validationResults[0].speciesValidationResults[0].errorCode")
                .value("SPECIES_CODE_UNKNOWN_OR_INACTIVE"))
        .andExpect(
            jsonPath("$.validationResults[0].speciesValidationResults[0].errorMessage")
                .value("Unknown or inactive species code(s): ZZ"));
  }
}
