package ca.bc.gov.restapi.results.common.endpoint;

import ca.bc.gov.restapi.results.common.configuration.SilvaConfiguration;
import ca.bc.gov.restapi.results.common.dto.WmsLayersWhitelistUserDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class holds resources for getting secrets from the backend.
 */
@RestController
@RequestMapping("/api/secrets")
@RequiredArgsConstructor
public class SecretsServiceEndpoint {

  private final SilvaConfiguration silvaConfiguration;

  /**
   * Get all users allowed to see the WMS layers information.
   *
   * @return List of users.
   */
  @GetMapping("/wms-layers-whitelist")
  public List<WmsLayersWhitelistUserDto> getWmsLayersWhitelistUsers() {
    return silvaConfiguration
        .getWmsWhitelist()
        .stream()
        .map(WmsLayersWhitelistUserDto::new)
        .toList();
  }
}
