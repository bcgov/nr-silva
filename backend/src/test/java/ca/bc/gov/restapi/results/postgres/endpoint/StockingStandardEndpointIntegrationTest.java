package ca.bc.gov.restapi.results.postgres.endpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@DisplayName("Integration Test | Stocking Standard Endpoint")
@AutoConfigureMockMvc
@WithMockJwt(cognitoGroups = {"Submitter_00012797"})
class StockingStandardEndpointIntegrationTest extends AbstractTestContainerIntegrationTest {

  private static final String VALID_REQUEST =
      """
      {
        "objective": "Stage 3 stocking standard",
        "name": "Endpoint integration standard",
        "location": "Integration test location",
        "authorityType": "OPERATIONAL_PLAN",
        "orgUnitCodes": ["DAS"],
        "clientNumbers": ["00012797"],
        "becInfoSelected": true,
        "alternativeMethodSelected": true,
        "becData": [
          {
            "bgcZoneCode": "CWH",
            "bgcSubzoneCode": "wh",
            "variant": "1",
            "siteSeries": "01"
          }
        ],
        "stockingType": "REGEN_OBLIGATION",
        "regenDelayYears": 1,
        "freeGrowingYears": 20,
        "layerType": "MULTI",
        "multiLayers": [
          {
            "layerCode": "4",
            "species": [
              {"speciesCode": "CW", "speciesType": "PREFERRED", "minHeight": 1.0},
              {"speciesCode": "HW", "speciesType": "ACCEPTABLE", "minHeight": 1.2}
            ]
          },
          {
            "layerCode": "3",
            "species": [
              {"speciesCode": "CW", "speciesType": "ECOLOGICALLY_SUITABLE"}
            ]
          },
          {"layerCode": "2"},
          {"layerCode": "1"}
        ],
        "additionalStandards": "Endpoint integration additional standards"
      }
      """;

  private static final String INVALID_REQUEST =
      """
      {
        "objective": "Rejected Stage 3 stocking standard",
        "authorityType": "OPERATIONAL_PLAN",
        "orgUnitCodes": ["DAS"],
        "clientNumbers": ["00012797"],
        "becInfoSelected": false,
        "alternativeMethodSelected": false,
        "stockingType": "REGEN_OBLIGATION",
        "regenDelayYears": 1,
        "freeGrowingYears": 20,
        "layerType": "SINGLE",
        "singleLayer": {"layerCode": "I"}
      }
      """;

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private JdbcTemplate jdbcTemplate;

  @Test
  @DisplayName("Creates a submitted stocking standard and persists its complete graph")
  void createStockingStandard_validRequest_persistsCompleteGraph() throws Exception {
    MvcResult result =
        mockMvc
        .perform(
            post("/api/stocking-standards")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(VALID_REQUEST))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.stockingStandardsId").isNumber())
        .andReturn();

    JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
    long standardId = response.path("stockingStandardsId").asLong();

    assertThat(
            jdbcTemplate.queryForObject(
                "SELECT standards_regime_status_code FROM silva.standards_regime "
                    + "WHERE standards_regime_id = ?",
                String.class,
                standardId))
        .isEqualTo("SUB");
    assertThat(
            jdbcTemplate.queryForObject(
                "SELECT alternative_method_ind FROM silva.standards_regime "
                    + "WHERE standards_regime_id = ?",
                String.class,
                standardId))
        .isEqualTo("Y");
    assertThat(
            jdbcTemplate.queryForObject(
                "SELECT count(*) FROM silva.standards_regime_site_series "
                    + "WHERE standards_regime_id = ?",
                Integer.class,
                standardId))
        .isEqualTo(1);
    assertThat(
            jdbcTemplate.queryForObject(
                "SELECT count(*) FROM silva.standards_regime_org_unit "
                    + "WHERE standards_regime_id = ? AND org_unit_no = 1",
                Integer.class,
                standardId))
        .isEqualTo(1);
    assertThat(
            jdbcTemplate.queryForObject(
                "SELECT count(*) FROM silva.standards_regime_client "
                    + "WHERE standards_regime_id = ? AND client_number = '00012797'",
                Integer.class,
                standardId))
        .isEqualTo(1);
    assertThat(
            jdbcTemplate.queryForList(
                "SELECT layer.stocking_layer_code, species.silv_tree_species_code, "
                    + "species.species_order, species.species_type_code "
                    + "FROM silva.standards_regime_layer_species species "
                    + "JOIN silva.standards_regime_layer layer "
                    + "ON layer.standards_regime_layer_id = species.standards_regime_layer_id "
                    + "WHERE layer.standards_regime_id = ? "
                    + "ORDER BY layer.standards_regime_layer_id, species.species_order",
                standardId))
        .extracting(
            row -> row.get("stocking_layer_code"),
            row -> row.get("silv_tree_species_code"),
            row -> row.get("species_order"),
            row -> row.get("species_type_code"))
        .containsExactly(
            org.assertj.core.groups.Tuple.tuple("4", "CW", 1, "PRF"),
            org.assertj.core.groups.Tuple.tuple("4", "HW", 2, "ACC"),
            org.assertj.core.groups.Tuple.tuple("3", "CW", 1, "ECO"));
  }

  @Test
  @DisplayName("Rejects an invalid stocking-standard request before persisting a header")
  void createStockingStandard_invalidRequest_returns400WithoutPersisting() throws Exception {
    mockMvc
        .perform(
            post("/api/stocking-standards")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(INVALID_REQUEST))
        .andExpect(status().isBadRequest());

    assertThat(
            jdbcTemplate.queryForObject(
                "SELECT count(*) FROM silva.standards_regime "
                    + "WHERE standards_objective = 'Rejected Stage 3 stocking standard'",
                Integer.class))
        .isZero();
  }
}
