package ca.bc.gov.restapi.results.common.provider;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.enums.ForestClientStatusEnum;
import ca.bc.gov.restapi.results.common.enums.ForestClientTypeEnum;
import ca.bc.gov.restapi.results.extensions.WiremockLogNotifier;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.client.RestClient;

@DisplayName("Integrated Test | Forest Client API Provider")
class ForestClientApiProviderIntegrationTest {

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

  private final ForestClientApiProvider forestClientApiProvider = new ForestClientApiProvider(
      RestClient.builder().baseUrl("http://localhost:10000").build()
  );

  @Test
  @DisplayName("Fetch client by number happy path should succeed")
  void fetchClientByNumber_happyPath_shouldSucceed() {

    String clientNumber = "00012797";
    clientApiStub.stubFor(
        get(urlPathEqualTo("/clients/findByClientNumber/" + clientNumber))
            .willReturn(okJson("""
                {
                  "clientNumber": "00012797",
                  "clientName": "MINISTRY OF FORESTS",
                  "legalFirstName": null,
                  "legalMiddleName": null,
                  "clientStatusCode": "ACT",
                  "clientTypeCode": "F",
                  "acronym": "MOF"
                }
                """)
            )
    );

    Optional<ForestClientDto> clientDto = forestClientApiProvider.fetchClientByNumber(clientNumber);

    Assertions.assertTrue(clientDto.isPresent());

    ForestClientDto forestClient = clientDto.get();
    Assertions.assertEquals("00012797", forestClient.clientNumber());
    Assertions.assertEquals("MINISTRY OF FORESTS", forestClient.clientName());
    Assertions.assertNull(forestClient.legalFirstName());
    Assertions.assertNull(forestClient.legalMiddleName());
    Assertions.assertEquals(ForestClientStatusEnum.ACTIVE, forestClient.clientStatusCode());
    Assertions.assertEquals(
        ForestClientTypeEnum.MINISTRY_OF_FORESTS_AND_RANGE, forestClient.clientTypeCode());
    Assertions.assertEquals("MOF", forestClient.acronym());
  }

  @Test
  @DisplayName("Fetch client by number client not found should succeed")
  void fetchClientByNumber_clientNotFound_shouldSucceed() {
    String clientNumber = "00012797";
    clientApiStub.stubFor(
        get(urlPathEqualTo("/clients/findByClientNumber/" + clientNumber))
            .willReturn(notFound())
    );

    Optional<ForestClientDto> clientDto = forestClientApiProvider.fetchClientByNumber(clientNumber);

    Assertions.assertTrue(clientDto.isEmpty());
  }
}
