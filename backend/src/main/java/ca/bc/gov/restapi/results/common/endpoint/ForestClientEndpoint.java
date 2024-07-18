package ca.bc.gov.restapi.results.common.endpoint;

import ca.bc.gov.restapi.results.common.dto.ForestsClientDto;
import ca.bc.gov.restapi.results.common.exception.ForestClientNotFoundException;
import ca.bc.gov.restapi.results.common.service.ForestClientService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/forest-clients")
@AllArgsConstructor
public class ForestClientEndpoint {

  private final ForestClientService forestClientService;

  @GetMapping("/{clientNumber}")
  public ForestsClientDto getForestClient(@RequestParam String clientNumber) {
    return forestClientService
        .getClientByNumber(clientNumber)
        .orElseThrow(ForestClientNotFoundException::new);
  }
}
