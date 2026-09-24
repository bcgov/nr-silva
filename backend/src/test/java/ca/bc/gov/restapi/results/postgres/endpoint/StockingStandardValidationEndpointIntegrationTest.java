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
@DisplayName("Integration Test | Stocking Standard Validation Endpoint")
@AutoConfigureMockMvc
class StockingStandardValidationEndpointIntegrationTest extends AbstractTestContainerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @WithMockJwt
  @DisplayName("Valid BEC list returns 200 and per-entry success")
  void validateBec_validList_returns200WithPerEntrySuccess() throws Exception {
    mockMvc
        .perform(
            post("/api/stocking-standards/validate/bec")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    [{"bgcZoneCode":"CWH","bgcSubzoneCode":"wh","variant":"1","siteSeries":"01"}]
                    """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.isValid").value(true))
        .andExpect(jsonPath("$.validationResults[0].becIndex").value(0))
        .andExpect(jsonPath("$.validationResults[0].isValid").value(true))
        .andExpect(jsonPath("$.duplicateConflicts").isEmpty())
        .andExpect(jsonPath("$.validationErrors").isEmpty());
  }

  @Test
  @WithMockJwt
  @DisplayName("Invalid BEC list returns 200 with an accurate per-entry error")
  void validateBec_unknownCombination_returns200WithError() throws Exception {
    mockMvc
        .perform(
            post("/api/stocking-standards/validate/bec")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    [{"bgcZoneCode":"CWH","bgcSubzoneCode":"xm","siteSeries":"01","sitePhase":"A"}]
                    """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.isValid").value(false))
        .andExpect(jsonPath("$.validationResults[0].errorCode").value("BEC_COMBINATION_NOT_FOUND"))
        .andExpect(jsonPath("$.validationResults[0].errorMessage")
            .value("Unknown BEC combination(s): CWH/xm///01"));
  }

  @Test
  @WithMockJwt
  @DisplayName("BEC validation requires CSRF and authentication")
  void validateBec_withoutCsrf_returns403() throws Exception {
    String body = "[{\"bgcZoneCode\":\"CWH\",\"bgcSubzoneCode\":\"wh\",\"siteSeries\":\"01\"}]";

    mockMvc
        .perform(post("/api/stocking-standards/validate/bec").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("BEC validation without authentication returns 401")
  void validateBec_withoutAuthentication_returns401() throws Exception {
    mockMvc
        .perform(
            post("/api/stocking-standards/validate/bec")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content("[]"))
        .andExpect(status().isUnauthorized());
  }
}
