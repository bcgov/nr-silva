package ca.bc.gov.restapi.results.common.service;

import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.enums.ForestClientStatusEnum;
import ca.bc.gov.restapi.results.common.enums.ForestClientTypeEnum;
import ca.bc.gov.restapi.results.common.provider.ForestClientApiProvider;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | Forest Client Service")
class ForestClientServiceTest {

  @Mock ForestClientApiProvider forestClientApiProvider;

  private ForestClientService forestClientService;

  @BeforeEach
  void setup() {
    forestClientService = new ForestClientService(forestClientApiProvider);
  }

  @Test
  @DisplayName("Get client by number happy path should succeed")
  void getClientByNumber_happyPath_shouldSucceed() {
    String clientNumber = "00132184";

    ForestClientDto clientDto =
        new ForestClientDto(
            clientNumber,
            "TIMBER SALES MANAGER BABINE",
            "First",
            "Middle",
            ForestClientStatusEnum.ACTIVE,
            ForestClientTypeEnum.MINISTRY_OF_FORESTS_AND_RANGE,
            "TBA");

    when(forestClientApiProvider.fetchClientByNumber(clientNumber))
        .thenReturn(Optional.of(clientDto));

    Optional<ForestClientDto> clientDtoOptional =
        forestClientService.getClientByNumber(clientNumber);

    Assertions.assertFalse(clientDtoOptional.isEmpty());

    ForestClientDto responseDto = clientDtoOptional.get();
    Assertions.assertEquals(clientNumber, responseDto.clientNumber());
    Assertions.assertEquals("TIMBER SALES MANAGER BABINE", responseDto.clientName());
    Assertions.assertEquals("First", responseDto.legalFirstName());
    Assertions.assertEquals("Middle", responseDto.legalMiddleName());
    Assertions.assertEquals(ForestClientStatusEnum.ACTIVE, responseDto.clientStatusCode());
    Assertions.assertEquals(
        ForestClientTypeEnum.MINISTRY_OF_FORESTS_AND_RANGE, responseDto.clientTypeCode());
    Assertions.assertEquals("TBA", responseDto.acronym());
  }

  @Test
  @DisplayName("Get client by number leading zeroes should succeed")
  void getClientByNumber_leadingZeroes_shouldSucceed() {
    String clientNumber = "132184";
    String fixedClientNumber = "00132184";

    ForestClientDto clientDto =
        new ForestClientDto(
            fixedClientNumber,
            "TIMBER SALES MANAGER BABINE",
            "First",
            "Middle",
            ForestClientStatusEnum.ACTIVE,
            ForestClientTypeEnum.MINISTRY_OF_FORESTS_AND_RANGE,
            "TBA");

    when(forestClientApiProvider.fetchClientByNumber(fixedClientNumber))
        .thenReturn(Optional.of(clientDto));

    Optional<ForestClientDto> clientDtoOptional =
        forestClientService.getClientByNumber(clientNumber);

    Assertions.assertFalse(clientDtoOptional.isEmpty());

    ForestClientDto responseDto = clientDtoOptional.get();
    Assertions.assertEquals(fixedClientNumber, responseDto.clientNumber());
    Assertions.assertEquals("TIMBER SALES MANAGER BABINE", responseDto.clientName());
    Assertions.assertEquals("First", responseDto.legalFirstName());
    Assertions.assertEquals("Middle", responseDto.legalMiddleName());
    Assertions.assertEquals(ForestClientStatusEnum.ACTIVE, responseDto.clientStatusCode());
    Assertions.assertEquals(
        ForestClientTypeEnum.MINISTRY_OF_FORESTS_AND_RANGE, responseDto.clientTypeCode());
    Assertions.assertEquals("TBA", responseDto.acronym());
  }

  @Test
  @DisplayName("Get client by number not found should succeed")
  void getClientByNumber_notFound_shouldSucceed() {
    String clientNumber = "00000011";

    when(forestClientApiProvider.fetchClientByNumber(clientNumber)).thenReturn(Optional.empty());

    Optional<ForestClientDto> clientDto = forestClientService.getClientByNumber(clientNumber);

    Assertions.assertTrue(clientDto.isEmpty());
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " ", "  ", "   "})
  @NullAndEmptySource
  @DisplayName("No client number should return empty")
  void shouldGetEmptyWhenNoClientNumber(String clientNumber) {

    when(forestClientApiProvider.fetchClientByNumber("00000000"))
        .thenReturn(Optional.empty());

    Optional<ForestClientDto> clientDtoOptional =
        forestClientService.getClientByNumber(clientNumber);

    Assertions.assertTrue(clientDtoOptional.isEmpty());
  }
}
