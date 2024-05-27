package ca.bc.gov.restapi.results.common.service;

import ca.bc.gov.restapi.results.common.openmaps.ForestTenureRoadSectionLinesWfs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BaseMapService {

  private final ForestTenureRoadSectionLinesWfs forestTenureRoadSectionLinesWfs;

  public String getBaseMapLayer(String name) {
    log.info("Getting base map layer {}", name);

    if (name.equals("WHSE_FOREST_TENURE.FTEN_ROAD_SECTION_LINES_SVW")) {
      return forestTenureRoadSectionLinesWfs.getBaseMapLayer();
    }

    return null;
  }
}
