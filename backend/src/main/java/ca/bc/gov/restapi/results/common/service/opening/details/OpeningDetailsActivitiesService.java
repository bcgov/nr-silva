package ca.bc.gov.restapi.results.common.service.opening.details;


import ca.bc.gov.restapi.results.common.dto.activity.OpeningActivityBaseDto;
import ca.bc.gov.restapi.results.common.dto.opening.OpeningDetailsActivitiesActivitiesDto;
import ca.bc.gov.restapi.results.common.dto.opening.OpeningDetailsActivitiesDisturbanceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OpeningDetailsActivitiesService {
  Page<OpeningDetailsActivitiesDisturbanceDto> getOpeningActivitiesDisturbances(
      Long openingId, Pageable pageable);
  Page<OpeningDetailsActivitiesActivitiesDto> getOpeningActivitiesActivities(
      Long openingId, String mainSearchTerm, Pageable pageable);
  OpeningActivityBaseDto getOpeningActivitiesActivity(Long openingId, Long atuId);
}
