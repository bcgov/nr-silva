package ca.bc.gov.restapi.results.common.endpoint;

import ca.bc.gov.restapi.results.common.service.OpenMapsService;
import lombok.RequiredArgsConstructor;
import org.geojson.FeatureCollection;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class holds resources for calling WFS.
 */
@RestController
@RequestMapping("/api/openings/map")
@RequiredArgsConstructor
public class OpeningMapsEndpoint {

  private final OpenMapsService openMapsService;

  /**
   * Fetch Opening data from WFS.
   *
   * @param openingId The Opening identification (id)
   * @return GeoJSON object with response from WFS request
   */
  @GetMapping("/{openingId}")
  public FeatureCollection getOpeningPolygonAndProperties(
      @PathVariable String openingId,
      @RequestParam(required = false, defaultValue = "WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW") String kind
  ) {
    return openMapsService.getOpeningPolygonAndProperties(openingId, kind);
  }
}
