package ca.bc.gov.restapi.results.common.endpoint;

import ca.bc.gov.restapi.results.common.dto.ForestClientAutocompleteResultDto;
import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.exception.ForestClientNotFoundException;
import ca.bc.gov.restapi.results.common.service.ForestClientService;
import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** This class holds resources for the Forest Client API interaction. */
@RestController
@RequestMapping("/api/forest-clients")
@AllArgsConstructor
public class ForestClientEndpoint {

  private final ForestClientService forestClientService;

  /**
   * Get a {@link ForestClientDto} given a client number.
   *
   * @param clientNumber The client number to be fetched.
   * @return ForestsClientDto
   * @throws ForestClientNotFoundException when client not found
   */
  @GetMapping("/{clientNumber}")
  public ForestClientDto getForestClient(@PathVariable String clientNumber) {
    return forestClientService
        .getClientByNumber(clientNumber)
        .orElseThrow(ForestClientNotFoundException::new);
  }

  /**
   * Search for clients by name, acronym or number.
   *
   * @param page The page number to be fetched.
   * @param size The size of the page to be fetched.
   * @param value The value to be searched.
   * @return List of {@link ForestClientAutocompleteResultDto} with found clients.
   */
  @GetMapping("/byNameAcronymNumber")
  public List<ForestClientAutocompleteResultDto> searchForestClients(
      @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
      @RequestParam(value = "value") String value) {
    return forestClientService.searchByClients(page, size, value);
  }

  /**
   * Get the locations of a client.
   *
   * @param clientNumber The client number to be fetched.
   * @return List of {@link CodeDescriptionDto} with found locations.
   */
  @GetMapping("/{clientNumber}/locations")
  public List<CodeDescriptionDto> getForestClientLocations(@PathVariable String clientNumber) {
    return forestClientService.getClientLocations(clientNumber);
  }

  /**
   * Search for multiple ForestClients by a list of client numbers.
   *
   * @param page The page number for pagination (optional, defaults to 0).
   * @param size The number of records per page (optional, defaults to 10).
   * @param clientNumbers List of client numbers to search for (required).
   * @return List of {@link ForestClientDto} objects matching the given client numbers.
   */
  @GetMapping("/search")
  public List<ForestClientDto> searchByClientNumbers(
      @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
      @RequestParam(value = "id") List<String> clientNumbers) {
    return forestClientService.searchByClientNumbers(page, size, clientNumbers);
  }
}
