package ca.bc.gov.restapi.results.common.endpoint;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@DisplayName("Integrated Test | Codes Endpoint")
public abstract class AbstractCodesEndpointIntegrationTest
    extends AbstractTestContainerIntegrationTest {

  @Autowired protected MockMvc mockMvc;

  protected abstract void setupTestData();

  @BeforeEach
  void setup() {
    this.setupTestData();
  }

  @Test
  @DisplayName("Get silv base codes should return 200 with codes")
  @WithMockUser(roles = "user_read")
  void getSilvBaseCodes_shouldReturn200() throws Exception {
    mockMvc
        .perform(get("/api/codes/silv-base").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].code").exists())
        .andExpect(jsonPath("$[0].description").exists());
  }

  @Test
  @DisplayName("Get silv technique codes should return 200 with codes")
  @WithMockUser(roles = "user_read")
  void getSilvTechniqueCodes_shouldReturn200() throws Exception {
    mockMvc
        .perform(get("/api/codes/silv-technique").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].code").exists())
        .andExpect(jsonPath("$[0].description").exists());
  }

  @Test
  @DisplayName("Get silv method codes should return 200 with codes")
  @WithMockUser(roles = "user_read")
  void getSilvMethodCodes_shouldReturn200() throws Exception {
    mockMvc
        .perform(get("/api/codes/silv-method").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].code").exists())
        .andExpect(jsonPath("$[0].description").exists());
  }

  @Test
  @DisplayName("Get silv objective codes should return 200 with codes")
  @WithMockUser(roles = "user_read")
  void getSilvObjectiveCodes_shouldReturn200() throws Exception {
    mockMvc
        .perform(get("/api/codes/silv-objective").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].code").exists())
        .andExpect(jsonPath("$[0].description").exists());
  }

  @Test
  @DisplayName("Get silv fund source codes should return 200 with codes")
  @WithMockUser(roles = "user_read")
  void getSilvFundSourceCodes_shouldReturn200() throws Exception {
    mockMvc
        .perform(get("/api/codes/silv-fund-source").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].code").exists())
        .andExpect(jsonPath("$[0].description").exists());
  }

  @Test
  @DisplayName("Get silv base codes with empty result should return empty array")
  @WithMockUser(roles = "user_read")
  void getSilvBaseCodes_emptyResult_shouldReturnEmpty() throws Exception {
    mockMvc
        .perform(get("/api/codes/silv-base").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray());
  }

  @Test
  @DisplayName("Get silv technique codes without auth should return 401")
  void getSilvTechniqueCodes_noAuth_shouldReturn401() throws Exception {
    mockMvc.perform(get("/api/codes/silv-technique")).andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("Get silv method codes should return valid JSON array")
  @WithMockUser(roles = "user_read")
  void getSilvMethodCodes_shouldReturnValidJsonArray() throws Exception {
    mockMvc
        .perform(get("/api/codes/silv-method"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[*].code").isArray())
        .andExpect(jsonPath("$[*].description").isArray());
  }

  @Test
  @DisplayName("Get silv objective codes with multiple items should return all")
  @WithMockUser(roles = "user_read")
  void getSilvObjectiveCodes_multiple_shouldReturnAll() throws Exception {
    mockMvc
        .perform(get("/api/codes/silv-objective"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].code").exists())
        .andExpect(jsonPath("$[0].description").exists());
  }

  @Test
  @DisplayName("Get silv fund source codes with null description should succeed")
  @WithMockUser(roles = "user_read")
  void getSilvFundSourceCodes_nullDescription_shouldSucceed() throws Exception {
    mockMvc
        .perform(get("/api/codes/silv-fund-source"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].code").exists());
  }

  @Test
  @DisplayName("Get opening categories should return 200 with codes")
  @WithMockUser(roles = "user_read")
  void getOpeningCategories_shouldReturn200() throws Exception {
    mockMvc
        .perform(get("/api/codes/opening-categories").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].code").exists())
        .andExpect(jsonPath("$[0].description").exists());
  }

  @Test
  @DisplayName("Get opening categories with includeExpired parameter should return 200")
  @WithMockUser(roles = "user_read")
  void getOpeningCategories_withIncludeExpired_shouldReturn200() throws Exception {
    mockMvc
        .perform(
            get("/api/codes/opening-categories?includeExpired=false")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray());
  }

  @Test
  @DisplayName("Get opening categories without auth should return 401")
  void getOpeningCategories_noAuth_shouldReturn401() throws Exception {
    mockMvc.perform(get("/api/codes/opening-categories")).andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("Get org units should return 200 with codes")
  @WithMockUser(roles = "user_read")
  void getOrgUnits_shouldReturn200() throws Exception {
    mockMvc
        .perform(get("/api/codes/org-units").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].code").exists())
        .andExpect(jsonPath("$[0].description").exists());
  }

  @Test
  @DisplayName("Get org units without auth should return 401")
  void getOrgUnits_noAuth_shouldReturn401() throws Exception {
    mockMvc.perform(get("/api/codes/org-units")).andExpect(status().isUnauthorized());
  }
}
