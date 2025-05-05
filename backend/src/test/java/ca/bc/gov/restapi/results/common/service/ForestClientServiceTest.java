package ca.bc.gov.restapi.results.common.service;

import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.dto.ForestClientLocationDto;
import ca.bc.gov.restapi.results.common.enums.ForestClientStatusEnum;
import ca.bc.gov.restapi.results.common.enums.ForestClientTypeEnum;
import ca.bc.gov.restapi.results.common.provider.ForestClientApiProvider;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
            "TBA",
            "TIMBER SALES MANAGER BABINE"
            );

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
            "TBA",
            "TIMBER SALES MANAGER BABINE");

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

  @ParameterizedTest
  @DisplayName("Search clients by name, acronym, or number succeeded")
  @MethodSource("searchByNameAcronymNumberOk")
  void fetchClientByName_happyPath_shouldSucceed(
      int resultSize,
      String value
  ){

    when(forestClientApiProvider.searchClients(1, 10, value))
        .thenReturn(
            List.of(
                new ForestClientDto(
                    "00012797",
                    "MINISTRY OF FORESTS",
                    null,
                    null,
                    ForestClientStatusEnum.ACTIVE,
                    ForestClientTypeEnum.MINISTRY_OF_FORESTS_AND_RANGE,
                    "MOF",
                    "MINISTRY OF FORESTS"
                )
            )
        );

    var clients = forestClientService.searchClients(1, 10, value);

    Assertions.assertEquals(resultSize, clients.size());

  }

  @Test
  @DisplayName("Search clients by name, acronym, or number not available should succeed")
  void fetchClientByName_unavailable_shouldSucceed(){

    when(forestClientApiProvider.searchClients(1, 10, "COMPANY"))
        .thenReturn(List.of());

    var clients = forestClientService.searchClients(1, 10, "COMPANY");

    Assertions.assertEquals(0, clients.size());

  }

  @Test
  @DisplayName("Fetch client locations happy path should succeed")
  void fetchClientLocations_happyPath_shouldSucceed(){

    when(forestClientApiProvider.fetchLocationsByClientNumber("00012797"))
        .thenReturn(
            List.of(
                new ForestClientLocationDto(
                    null,
                    "00",
                    "Location 1",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                ),
                new ForestClientLocationDto(
                    null,
                    "01",
                    "Location 2",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                )
            )
        );

    var locations = forestClientService.getClientLocations("00012797");

    Assertions.assertEquals(2, locations.size());

  }

  private static Stream<Arguments> searchByNameAcronymNumberOk() {
    return Stream.of(
        Arguments.of(1, "INDIA"),
        Arguments.of(1, "SAMPLIBC"),
        Arguments.of(1, "00000001"),
        Arguments.of(1, "1")
    );
  }

}
