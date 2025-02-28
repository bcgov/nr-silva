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

/**
 * This class holds resources for the Forest Client API interaction.
 */
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

  @GetMapping("/byNameAcronymNumber")
  public List<ForestClientAutocompleteResultDto> searchForestClients(
      @RequestParam(value = "page",required = false,defaultValue = "0") Integer page,
      @RequestParam(value = "size",required = false,defaultValue = "10") Integer size,
      @RequestParam(value = "value") String value
  ) {
    return forestClientService.searchClients(page,size,value);
  }

  @GetMapping("/{clientNumber}/locations")
  public List<CodeDescriptionDto> getForestClientLocations(@PathVariable String clientNumber) {
    return forestClientService.getClientLocations(clientNumber);
  }
}
