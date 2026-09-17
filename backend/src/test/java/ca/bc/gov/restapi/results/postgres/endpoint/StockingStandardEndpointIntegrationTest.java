package ca.bc.gov.restapi.results.postgres.endpoint;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.postgres.config.StockingStandardEndpointTestConfig;
import ca.bc.gov.restapi.results.postgres.dto.CreateStockingStandardResponseDto;
import ca.bc.gov.restapi.results.postgres.service.CreateStockingStandardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@DisplayName("Integration Test | Stocking Standard Endpoint")
@AutoConfigureMockMvc
@WithMockJwt
@Import(StockingStandardEndpointTestConfig.class)
class StockingStandardEndpointIntegrationTest extends AbstractTestContainerIntegrationTest {

  private static final String VALID_REQUEST =
      """
      {
        "objective": "Reforestation objective",
        "authorityType": "OPERATIONAL_PLAN",
        "becInfoSelected": false,
        "alternativeMethodSelected": true,
        "stockingType": "REGEN_OBLIGATION",
        "regenDelayYears": 1,
        "freeGrowingYears": 20,
        "layerType": "SINGLE",
        "singleLayer": {
          "layerCode": "I",
          "species": [
            {
              "speciesCode": "CW",
              "speciesType": "PREFERRED",
              "minHeight": 1.0,
              "milestone": "BOTH"
            }
          ]
        }
      }
      """;

  @Autowired private MockMvc mockMvc;

  @Autowired private CreateStockingStandardService createStockingStandardService;

  @Test
  @DisplayName("Creates a stocking standard with a 201 response")
  void createStockingStandard_validRequest_returns201() throws Exception {
    when(createStockingStandardService.create(any()))
        .thenReturn(new CreateStockingStandardResponseDto(123L));

    mockMvc
        .perform(
            post("/api/stocking-standards")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(VALID_REQUEST))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.stockingStandardsId").value(123));
  }

  @Test
  @DisplayName("Rejects a malformed stocking-standard request with a 400 response")
  void createStockingStandard_invalidRequest_returns400() throws Exception {
    mockMvc
        .perform(
            post("/api/stocking-standards")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
        .andExpect(status().isBadRequest());
  }
}
