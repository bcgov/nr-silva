package ca.bc.gov.restapi.results.common.service;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.enums.ForestClientStatusEnum;
import ca.bc.gov.restapi.results.common.enums.ForestClientTypeEnum;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** This service contains methods for interacting with Forest Client API. */
@Slf4j
@Service
public class ForestClientService {

  /**
   * Get a {@link ForestClientDto} given a client number.
   *
   * @param clientNumber The client number to be fetched.
   * @return Optional of ForestsClientDto
   */
  public Optional<ForestClientDto> getClientByNumber(String clientNumber) {
    log.info("Received client number to fetch {}", clientNumber);

    String fixedNumber = checkClientNumber(clientNumber);
    log.info("Fixed client number {}", fixedNumber);

    Optional<ForestClientDto> clientDto = mockForestClient(fixedNumber);
    log.info("Client {} found!", (clientDto.isEmpty() ? "not" : ""));
    return clientDto;
  }

  private String checkClientNumber(String clientNumber) {
    if (Objects.isNull(clientNumber)) {
      return "00000000";
    }

    try {
      Integer parsed = Integer.parseInt(clientNumber);
      return String.format("%08d", parsed);
    } catch (NumberFormatException nfe) {
      return "00000000";
    }
  }

  private Optional<ForestClientDto> mockForestClient(String clientNumber) {
    if (clientNumber.equals("00132184")) {
      return Optional.of(
          new ForestClientDto(
              "00132184",
              "TIMBER SALES MANAGER BABINE",
              null,
              null,
              ForestClientStatusEnum.ACTIVE,
              ForestClientTypeEnum.MINISTRY_OF_FORESTS_AND_RANGE,
              "TBA"));
    }
    if (clientNumber.equals("00012797")) {
      return Optional.of(
          new ForestClientDto(
              "00012797",
              "MINISTRY OF FORESTS",
              null,
              null,
              ForestClientStatusEnum.ACTIVE,
              ForestClientTypeEnum.MINISTRY_OF_FORESTS_AND_RANGE,
              "MOF"));
    }
    if (clientNumber.equals("00001012")) {
      return Optional.of(
          new ForestClientDto(
              "00001012",
              "BELL LUMBER & POLE CANADA, ULC",
              null,
              null,
              ForestClientStatusEnum.ACTIVE,
              ForestClientTypeEnum.CORPORATION,
              "BELL"));
    }
    if (clientNumber.equals("00149081")) {
      return Optional.of(
          new ForestClientDto(
              "00149081",
              "WESTERN FOREST PRODUCTS INC.",
              null,
              null,
              ForestClientStatusEnum.ACTIVE,
              ForestClientTypeEnum.CORPORATION,
              "WFP"));
    }

    return Optional.empty();
  }
}
