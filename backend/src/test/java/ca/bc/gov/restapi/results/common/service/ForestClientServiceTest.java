package ca.bc.gov.restapi.results.common.service;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.enums.ForestClientStatusEnum;
import ca.bc.gov.restapi.results.common.enums.ForestClientTypeEnum;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ForestClientServiceTest {

  private ForestClientService forestClientService;

  @BeforeEach
  void setup() {
    forestClientService = new ForestClientService();
  }

  @Test
  @DisplayName("Get client by number happy path should succeed")
  void getClientByNumber_happyPath_shouldSucceed() {
    String clientNumber = "00132184";

    Optional<ForestClientDto> clientDto = forestClientService.getClientByNumber(clientNumber);

    Assertions.assertFalse(clientDto.isEmpty());
    Assertions.assertEquals(clientNumber, clientDto.get().clientNumber());
    Assertions.assertEquals("TIMBER SALES MANAGER BABINE", clientDto.get().clientName());
    Assertions.assertNull(clientDto.get().legalFirstName());
    Assertions.assertNull(clientDto.get().legalMiddleName());
    Assertions.assertEquals(ForestClientStatusEnum.ACTIVE, clientDto.get().clientStatus());
    Assertions.assertEquals(
        ForestClientTypeEnum.MINISTRY_OF_FORESTS_AND_RANGE, clientDto.get().clientType());
    Assertions.assertEquals("TBA", clientDto.get().acronym());
  }

  @Test
  @DisplayName("Get client by number leading zeroes should succeed")
  void getClientByNumber_leadingZeroes_shouldSucceed() {
    String clientNumber = "132184";

    Optional<ForestClientDto> clientDto = forestClientService.getClientByNumber(clientNumber);

    Assertions.assertFalse(clientDto.isEmpty());
    Assertions.assertEquals("00132184", clientDto.get().clientNumber());
    Assertions.assertEquals("TIMBER SALES MANAGER BABINE", clientDto.get().clientName());
    Assertions.assertNull(clientDto.get().legalFirstName());
    Assertions.assertNull(clientDto.get().legalMiddleName());
    Assertions.assertEquals(ForestClientStatusEnum.ACTIVE, clientDto.get().clientStatus());
    Assertions.assertEquals(
        ForestClientTypeEnum.MINISTRY_OF_FORESTS_AND_RANGE, clientDto.get().clientType());
    Assertions.assertEquals("TBA", clientDto.get().acronym());
  }

  @Test
  @DisplayName("Get all clients by number should succeed")
  void getClientByNumber_allClients_shouldSucceed() {
    Optional<ForestClientDto> clientOne = forestClientService.getClientByNumber("00132184");
    Assertions.assertFalse(clientOne.isEmpty());
    
    Optional<ForestClientDto> clientTwo = forestClientService.getClientByNumber("00012797");
    Assertions.assertFalse(clientTwo.isEmpty());

    Optional<ForestClientDto> clientThree = forestClientService.getClientByNumber("00001012");
    Assertions.assertFalse(clientThree.isEmpty());

    Optional<ForestClientDto> clientFour = forestClientService.getClientByNumber("00149081");
    Assertions.assertFalse(clientFour.isEmpty());
  }

  @Test
  @DisplayName("Get client by number not found should succeed")
  void getClientByNumber_notFound_shouldSucceed() {
    Optional<ForestClientDto> clientDto = forestClientService.getClientByNumber("11");

    Assertions.assertTrue(clientDto.isEmpty());
  }
}
