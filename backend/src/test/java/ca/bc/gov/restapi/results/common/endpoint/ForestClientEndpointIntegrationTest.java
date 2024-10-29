package ca.bc.gov.restapi.results.common.endpoint;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.enums.ForestClientStatusEnum;
import ca.bc.gov.restapi.results.common.enums.ForestClientTypeEnum;
import ca.bc.gov.restapi.results.common.service.ForestClientService;
import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WiremockLogNotifier;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
}
