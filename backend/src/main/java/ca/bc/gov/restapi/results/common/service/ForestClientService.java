package ca.bc.gov.restapi.results.common.service;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.provider.ForestClientApiProvider;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

/** This service contains methods for interacting with Forest Client API. */
@Slf4j
@Service
@RequiredArgsConstructor
public class ForestClientService {

  private final ForestClientApiProvider forestClientApiProvider;

  /**
   * Get a {@link ForestClientDto} given a client number.
   *
   * @param clientNumber The client number to be fetched.
   * @return Optional of ForestsClientDto
   */
  public Optional<ForestClientDto> getClientByNumber(String clientNumber) {
    log.info("Received client number to fetch {}", clientNumber);

    String fixedNumber = checkClientNumber(clientNumber);
    if (!fixedNumber.equals(clientNumber)) {
      log.info("Fixed client number to fetch {}", fixedNumber);
    }

    try {
      return forestClientApiProvider.fetchClientByNumber(fixedNumber);
    } catch (HttpClientErrorException.NotFound e) {
      log.info(String.format("Client %s not found", clientNumber), e);
      return Optional.empty();
    }
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
}
