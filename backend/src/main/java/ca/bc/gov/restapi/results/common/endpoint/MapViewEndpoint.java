package ca.bc.gov.restapi.results.common.endpoint;

import ca.bc.gov.restapi.results.common.service.BaseMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/map-view")
@RequiredArgsConstructor
public class MapViewEndpoint {

  private final BaseMapService baseMapService;

  @GetMapping("/base-map-layer/{name}")
  public String getForestTenureRoadSectionLines(@PathVariable String name) {
    return baseMapService.getBaseMapLayer(name);
  }
}
