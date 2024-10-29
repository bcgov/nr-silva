package ca.bc.gov.restapi.results.common.endpoint;

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

@WithMockJwt
@AutoConfigureMockMvc
class SecretsServiceEndpointTest extends AbstractTestContainerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("Get WMS layers whitelist users happy path should succeed")
  void getWmsLayersWhitelistUsers_happyPath_shouldSucceed() throws Exception {
    mockMvc
        .perform(
            get("/api/secrets/wms-layers-whitelist")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$[0].userName").value("NONE"))
        .andReturn();
  }
}
