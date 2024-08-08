package ca.bc.gov.restapi.results.common.endpoint;

import ca.bc.gov.restapi.results.common.dto.WmsLayersWhitelistUserDto;
import io.swagger.v3.oas.annotations.Hidden;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** This class holds resources for getting secrets from the backend. */
@RestController
@RequestMapping("/api/secrets")
@Hidden
public class SecretsServiceEndpoint {

  @Value("${nr.results.config.wms-layers.whitelist}")
  private String[] wmsLayersWhitelistUsers;

  /**
   * Get all users allowed to see the WMS layers information.
   *
   * @return List of users.
   */
  @GetMapping("/wms-layers-whitelist")
  public List<WmsLayersWhitelistUserDto> getWmsLayersWhitelistUsers() {
    return Stream.of(wmsLayersWhitelistUsers).map(WmsLayersWhitelistUserDto::new).toList();
  }
}
