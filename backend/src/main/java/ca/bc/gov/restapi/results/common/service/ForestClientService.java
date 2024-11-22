package ca.bc.gov.restapi.results.common.service;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.provider.ForestClientApiProvider;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * This service contains methods for interacting with Forest Client API.
 */
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
    return forestClientApiProvider.fetchClientByNumber(fixedNumber);
  }

  private String checkClientNumber(String clientNumber) {
    if (StringUtils.isEmpty(clientNumber)) {
      return "00000000";
    }

    try {
      Integer parsed = Integer.parseInt(clientNumber);
      return String.format("%08d", parsed);
    } catch (NumberFormatException nfe) {
      return "00000000";
    }
  }

  public List<ForestClientDto> searchByNameAcronymNumber(String value) {
    log.info("Received search value {}", value);
    return forestClientApiProvider.searchByNameAcronymNumber(value);
  }
}
