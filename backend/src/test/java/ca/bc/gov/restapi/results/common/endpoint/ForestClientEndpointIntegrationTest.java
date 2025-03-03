package ca.bc.gov.restapi.results.common.endpoint;

import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.serviceUnavailable;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WiremockLogNotifier;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser(roles = "user_read")
@AutoConfigureMockMvc
@DisplayName("Integrated Test | Forest Client Endpoint")
class ForestClientEndpointIntegrationTest extends AbstractTestContainerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @RegisterExtension
  static WireMockExtension clientApiStub = WireMockExtension
      .newInstance()
      .options(
          wireMockConfig()
              .port(10000)
              .notifier(new WiremockLogNotifier())
              .asynchronousResponseEnabled(true)
              .stubRequestLoggingDisabled(false)
      )
      .configureStaticDsl(true)
      .build();

  @Test
  @DisplayName("Get forest client happy path should succeed")
  void getForestClient_happyPath_shouldSucceed() throws Exception {
    String clientNumber = "149081";

    clientApiStub.stubFor(
        WireMock.get(urlPathEqualTo("/clients/findByClientNumber/00149081"))
            .willReturn(okJson("""
                {
                  "clientNumber": 149081,
                  "clientName": "WESTERN FOREST PRODUCTS INC.",
                  "legalFirstName": null,
                  "legalMiddleName": null,
                  "clientStatusCode": "ACT",
                  "clientTypeCode": "C",
                  "acronym": "WFP"
                }""")
            )
    );

    mockMvc
        .perform(
            get("/api/forest-clients/{clientNumber}", clientNumber)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.clientNumber").value(clientNumber))
        .andExpect(jsonPath("$.clientName").value("WESTERN FOREST PRODUCTS INC."))
        .andExpect(jsonPath("$.legalFirstName").isEmpty())
        .andExpect(jsonPath("$.legalMiddleName").isEmpty())
        .andExpect(jsonPath("$.clientStatusCode.code").value("ACT"))
        .andExpect(jsonPath("$.clientTypeCode.code").value("C"))
        .andExpect(jsonPath("$.acronym").value("WFP"))
        .andReturn();
  }

  @Test
  @DisplayName("Get forest client not found should succeed")
  void getForestClient_notFound_shouldSucceed() throws Exception {
    String clientNumber = "111";

    clientApiStub.stubFor(
        WireMock.get(urlPathEqualTo("/clients/findByClientNumber/"+clientNumber))
            .willReturn(WireMock.notFound().withBody("Client not found"))
    );

    mockMvc
        .perform(
            get("/api/forest-clients/{clientNumber}", clientNumber)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn();
  }


  @ParameterizedTest
  @DisplayName("Search clients by name, acronym, or number succeeded")
  @MethodSource("searchByNameAcronymNumberOk")
  void fetchClientByName_happyPath_shouldSucceed(
      String value
  ) throws Exception {

    clientApiStub.stubFor(
        WireMock.get(urlPathEqualTo("/clients/search/by"))
            .willReturn(okJson("""
                [
                  {
                    "clientNumber": "00012797",
                    "clientName": "MINISTRY OF FORESTS",
                    "legalFirstName": null,
                    "legalMiddleName": null,
                    "clientStatusCode": "ACT",
                    "clientTypeCode": "F",
                    "acronym": "MOF"
                  }
                ]
                """)
            )
    );

    mockMvc
        .perform(
            get("/api/forest-clients/byNameAcronymNumber?value={value}", value)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$[0].id").value("00012797"))
        .andExpect(jsonPath("$[0].name").value("MINISTRY OF FORESTS"))
        .andExpect(jsonPath("$[0].acronym").value("MOF"))
        .andReturn();

  }

  @Test
  @DisplayName("Search clients by name, acronym, or number not available should succeed")
  void fetchClientByName_unavailable_shouldSucceed() throws Exception {

    clientApiStub.stubFor(
        WireMock.get(urlPathEqualTo("/clients/search/by"))
            .willReturn(serviceUnavailable())
    );

    mockMvc
        .perform(
            get("/api/forest-clients/byNameAcronymNumber?value={value}", "COMPANY")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andReturn();

  }

  @Test
  @DisplayName("Fetch client locations happy path should succeed")
  void fetchClientLocations_happyPath_shouldSucceed() throws Exception {

    clientApiStub.stubFor(
        WireMock.get(urlPathEqualTo("/clients/00012797/locations"))
            .willReturn(okJson("""
                [
                  {
                    "locationCode": "00",
                    "locationName": "Location 1"
                  },
                  {
                    "locationCode": "01",
                    "locationName": "Location 2"
                  }
                ]
                """)
            )
    );

    mockMvc
        .perform(
            get("/api/forest-clients/{clientNumber}/locations", "12797")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$[0].code").value("00"))
        .andExpect(jsonPath("$[0].description").value("Location 1"))
        .andExpect(jsonPath("$[1].code").value("01"))
        .andExpect(jsonPath("$[1].description").value("Location 2"))
        .andReturn();

  }


  private static Stream<Arguments> searchByNameAcronymNumberOk() {
    return Stream.of(
        Arguments.of( "INDIA"),
        Arguments.of( "SAMPLIBC"),
        Arguments.of( "00000001"),
        Arguments.of( "1")
    );
  }
}
