package ca.bc.gov.restapi.results.common.service;

import ca.bc.gov.restapi.results.common.dto.ForestClientAutocompleteResultDto;
import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.provider.ForestClientApiProvider;
import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import java.util.List;
import java.util.Objects;
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

  /**
   * Search for clients by name, acronym or number.
   *
   * @param page  The page number to be fetched.
   * @param size  The size of the page to be fetched.
   * @param value The value to be searched.
   * @return List of {@link ForestClientAutocompleteResultDto} with found clients.
   */
  public List<ForestClientAutocompleteResultDto> searchClients(
      int page,
      int size,
      String value
  ) {
    log.info("Searching forest client by {} as name, acronym or number with page {} and size {}",
        value, page, size);
    return forestClientApiProvider
        .searchClients(page, size, value)
        .stream()
        .map(client -> new ForestClientAutocompleteResultDto(
                client.clientNumber(),
                client.name(),
                client.acronym()
            )
        )
        .toList();
  }

  /**
   * Get the locations of a client.
   *
   * @param clientNumber The client number to be fetched.
   * @return List of {@link CodeDescriptionDto} with found locations.
   */
  public List<CodeDescriptionDto> getClientLocations(String clientNumber) {
    String fixedNumber = checkClientNumber(clientNumber);

    log.info("Fetching locations for client number {}", fixedNumber);

    return
        forestClientApiProvider
            .fetchLocationsByClientNumber(fixedNumber)
            .stream()
            .map(location -> new CodeDescriptionDto(
                location.locationCode(),
                Objects.toString(location.locationName(), "No name provided")
            ))
            .toList();
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

}
