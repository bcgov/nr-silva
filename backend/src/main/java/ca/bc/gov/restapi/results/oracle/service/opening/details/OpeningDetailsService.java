package ca.bc.gov.restapi.results.oracle.service.opening.details;

import ca.bc.gov.restapi.results.oracle.dto.activity.OpeningActivityBaseDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsActivitiesActivitiesDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsActivitiesDisturbanceDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsStockingDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsTenuresDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsTombstoneOverviewDto;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpeningDetailsService {

  private final OpeningDetailsTombstoneService tombstoneService;
  private final OpeningDetailsStockingService stockingService;
  private final OpeningDetailsActivitiesService activitiesService;
  private final OpeningDetailsTenureService tenureService;


  public Optional<OpeningDetailsTombstoneOverviewDto> getOpeningTombstone(Long openingId) {
    return tombstoneService.getOpeningTombstone(openingId);
  }

  public List<OpeningDetailsStockingDto> getOpeningStockingDetails(Long openingId) {
    return stockingService.getOpeningStockingDetails(openingId);
  }

  public Page<OpeningDetailsActivitiesDisturbanceDto> getOpeningActivitiesDisturbances(
      Long openingId, Pageable pageable) {
    return activitiesService.getOpeningActivitiesDisturbances(openingId, pageable);
  }

  public Page<OpeningDetailsActivitiesActivitiesDto> getOpeningActivitiesActivities(
      Long openingId, String mainSearchTerm, Pageable pageable) {
    return activitiesService.getOpeningActivitiesActivities(openingId, mainSearchTerm, pageable);
  }

  public OpeningActivityBaseDto getOpeningActivitiesActivity(Long openingId, Long atuId) {
    return activitiesService.getOpeningActivitiesActivity(openingId, atuId);
  }

  public OpeningDetailsTenuresDto getOpeningTenures(Long openingId, String filter,
      Pageable pageable) {
    return tenureService.getOpeningTenures(openingId, filter, pageable);
  }
}
