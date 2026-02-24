package ca.bc.gov.restapi.results.common.service;

import ca.bc.gov.restapi.results.common.dto.activity.ActivitySearchFiltersDto;
import ca.bc.gov.restapi.results.common.dto.activity.ActivitySearchResponseDto;
import ca.bc.gov.restapi.results.common.dto.activity.DisturbanceSearchFilterDto;
import ca.bc.gov.restapi.results.common.dto.activity.DisturbanceSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActivityService {
  Page<ActivitySearchResponseDto> activitySearch(
      ActivitySearchFiltersDto filters, Pageable pagination);

  Page<DisturbanceSearchResponseDto> disturbanceSearch(
      DisturbanceSearchFilterDto filters, Pageable pagination);
}
