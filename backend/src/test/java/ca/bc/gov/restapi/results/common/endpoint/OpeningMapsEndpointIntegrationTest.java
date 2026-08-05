package ca.bc.gov.restapi.results.common.endpoint;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WiremockLogNotifier;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser(roles = "user_read")
@AutoConfigureMockMvc
@DisplayName("Integrated Test | Feature Service Endpoint")
class OpeningMapsEndpointIntegrationTest extends AbstractTestContainerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @RegisterExtension
  static WireMockExtension clientApiStub =
      WireMockExtension.newInstance()
          .options(
              wireMockConfig()
                  .port(10001)
                  .notifier(new WiremockLogNotifier())
                  .asynchronousResponseEnabled(true)
                  .stubRequestLoggingDisabled(false))
          .configureStaticDsl(true)
          .build();

  @Test
  @EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
  @DisplayName("Get opening polygon and properties happy path should succeed")
  void getOpeningPolygonAndProperties_happyPath_shouldSucceed() throws Exception {
    // Opening 101017 is seeded via V999.1.0__test_data_101017.sql with admin_district_no=1
    // (org_unit: code='DAS', name='Development Unit', rollup_region_no=111).
    // No org_unit with no=111 exists in seed data, so region resolves to null.
    long openingId = 101017L;

    mockMvc
        .perform(
            get("/api/openings/map/{openingId}", openingId)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.type").value("FeatureCollection"))
        .andExpect(jsonPath("$.features[0].type").value("Feature"))
        .andExpect(jsonPath("$.features[0].id").value(String.valueOf(openingId)))
        .andExpect(jsonPath("$.features[0].properties.OPENING_ID").value(openingId))
        .andExpect(jsonPath("$.features[0].properties.OPENING_CATEGORY_CODE").isEmpty())
        .andExpect(jsonPath("$.features[0].properties.OPENING_STATUS_CODE").isEmpty())
        .andExpect(jsonPath("$.features[0].properties.REGION_CODE").value("1Code"))
        .andExpect(jsonPath("$.features[0].properties.REGION_NAME").isEmpty())
        .andExpect(jsonPath("$.features[0].properties.DISTRICT_CODE").value("DAS"))
        .andExpect(jsonPath("$.features[0].properties.DISTRICT_NAME").value("Development Unit"))
        .andExpect(jsonPath("$.features[0].properties.CLIENT_NAME").isEmpty())
        .andExpect(jsonPath("$.features[0].properties.CLIENT_NUMBER").isEmpty())
        .andExpect(jsonPath("$.features[0].properties.OPENING_WHO_CREATED").isEmpty())
        .andExpect(jsonPath("$.features[0].properties.OPENING_WHEN_CREATED").value("2001-06-07Z"))
        .andExpect(jsonPath("$.features[0].properties.OPENING_WHO_UPDATED").isEmpty())
        .andExpect(jsonPath("$.features[0].properties.OPENING_WHEN_UPDATED").isEmpty())
        .andExpect(jsonPath("$.features[0].properties.OBJECTID").isEmpty())
        .andReturn();
  }

  @Test
  @DisplayName("Get opening polygon and properties with non numeric opening id should fail")
  void getOpeningPolygonAndProperties_nonNumericOpeningId_shouldFail() throws Exception {
    mockMvc
        .perform(
            get("/api/openings/map/{openingId}", "123 OR 1=1")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }
}
