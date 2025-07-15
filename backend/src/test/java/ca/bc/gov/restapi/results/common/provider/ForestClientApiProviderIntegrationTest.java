package ca.bc.gov.restapi.results.common.provider;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.serviceUnavailable;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.enums.ForestClientStatusEnum;
import ca.bc.gov.restapi.results.common.enums.ForestClientTypeEnum;
import ca.bc.gov.restapi.results.extensions.WiremockLogNotifier;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.web.client.RestClient;

@DisplayName("Integrated Test | Forest Client API Provider")
class ForestClientApiProviderIntegrationTest {

  @RegisterExtension
  static WireMockExtension clientApiStub =
      WireMockExtension.newInstance()
          .options(
              wireMockConfig()
                  .port(10000)
                  .notifier(new WiremockLogNotifier())
                  .asynchronousResponseEnabled(true)
                  .stubRequestLoggingDisabled(false))
          .configureStaticDsl(true)
          .build();

  private final ForestClientApiProvider forestClientApiProvider =
      new ForestClientApiProvider(RestClient.builder().baseUrl("http://localhost:10000").build());

  @Test
  @DisplayName("Fetch client by number happy path should succeed")
  void fetchClientByNumber_happyPath_shouldSucceed() {

    String clientNumber = "00012797";
    clientApiStub.stubFor(
        get(urlPathEqualTo("/clients/findByClientNumber/" + clientNumber))
            .willReturn(
                okJson(
                    """
                {
                  "clientNumber": "00012797",
                  "clientName": "MINISTRY OF FORESTS",
                  "legalFirstName": null,
                  "legalMiddleName": null,
                  "clientStatusCode": "ACT",
                  "clientTypeCode": "F",
                  "acronym": "MOF"
                }
                """)));

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
        get(urlPathEqualTo("/clients/findByClientNumber/" + clientNumber)).willReturn(notFound()));

    Optional<ForestClientDto> clientDto = forestClientApiProvider.fetchClientByNumber(clientNumber);

    Assertions.assertTrue(clientDto.isEmpty());
  }

  @ParameterizedTest
  @DisplayName("Search clients by name, acronym, or number succeeded")
  @MethodSource("searchByNameAcronymNumberOk")
  void fetchClientByName_happyPath_shouldSucceed(int resultSize, String value) {

    clientApiStub.stubFor(
        get(urlPathEqualTo("/clients/search/by"))
            .willReturn(
                okJson(
                    """
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
                """)));

    var clients = forestClientApiProvider.searchByClients(1, 10, value);

    Assertions.assertEquals(resultSize, clients.size());
  }

  @Test
  @DisplayName("Search clients by name, acronym, or number not available should succeed")
  void fetchClientByName_unavailable_shouldSucceed() {

    clientApiStub.stubFor(
        get(urlPathEqualTo("/clients/search/by")).willReturn(serviceUnavailable()));

    var clients = forestClientApiProvider.searchByClients(1, 10, "COMPANY");

    Assertions.assertEquals(0, clients.size());
  }

  @Test
  @DisplayName("Fetch client locations happy path should succeed")
  void fetchClientLocations_happyPath_shouldSucceed() {

    clientApiStub.stubFor(
        get(urlPathEqualTo("/clients/00012797/locations"))
            .willReturn(
                okJson(
                    """
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
                """)));

    var locations = forestClientApiProvider.fetchLocationsByClientNumber("00012797");

    Assertions.assertEquals(2, locations.size());
  }

  private static Stream<Arguments> searchByNameAcronymNumberOk() {
    return Stream.of(
        Arguments.of(1, "INDIA"),
        Arguments.of(1, "SAMPLIBC"),
        Arguments.of(1, "00000001"),
        Arguments.of(1, "1"));
  }
}
