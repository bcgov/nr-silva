package ca.bc.gov.restapi.results.common.service;

import ca.bc.gov.restapi.results.common.dto.activity.ActivitySearchFiltersDto;
import ca.bc.gov.restapi.results.common.dto.activity.ActivitySearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActivityService {
  Page<ActivitySearchResponseDto> activitySearch(
      ActivitySearchFiltersDto filters, Pageable pagination);
}
