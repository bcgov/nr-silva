package ca.bc.gov.restapi.results.common.provider;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import ca.bc.gov.restapi.results.common.config.ProvidersConfig;
import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.enums.ForestClientStatusEnum;
import ca.bc.gov.restapi.results.common.enums.ForestClientTypeEnum;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

@RestClientTest(ForestClientApiProvider.class)
class ForestClientApiProviderTest {

  @Autowired private ForestClientApiProvider forestClientApiProvider;

  @Autowired private MockRestServiceServer mockServer;

  @MockBean private ProvidersConfig providersConfig;

  @Test
  @DisplayName("Fetch client by number happy path should succeed")
  public void fetchClientByNumber_happyPath_shouldSucceed() {
    String clientNumber = "00012797";
    String url = "/null/clients/findByClientNumber/" + clientNumber;
    String json =
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
        """;

    when(providersConfig.getForestClientApiKey()).thenReturn("1z2x2a4s5d5");

    mockServer.expect(requestTo(url)).andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

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
    String url = "/null/clients/findByClientNumber/" + clientNumber;

    when(providersConfig.getForestClientApiKey()).thenReturn("1z2x2a4s5d5");

    mockServer.expect(requestTo(url)).andRespond(withStatus(HttpStatus.NOT_FOUND));

    Optional<ForestClientDto> clientDto = forestClientApiProvider.fetchClientByNumber(clientNumber);

    Assertions.assertTrue(clientDto.isEmpty());
  }
}
