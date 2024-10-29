package ca.bc.gov.restapi.results.common.endpoint;

import ca.bc.gov.restapi.results.common.service.OpenMapsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class holds resources for calling WFS.
 */
@RestController
@RequestMapping("/api/feature-service")
@RequiredArgsConstructor
public class FeatureServiceEndpoint {

  private final OpenMapsService openMapsService;

  /**
   * Fetch Opening data from WFS.
   *
   * @param openingId The Opening identification (id)
   * @return JSON object with response from WFS request
   */
  @GetMapping("/polygon-and-props/{openingId}")
  public Object getOpeningPolygonAndProperties(
      @PathVariable
      String openingId) {
    return openMapsService.getOpeningPolygonAndProperties(openingId);
  }
}
