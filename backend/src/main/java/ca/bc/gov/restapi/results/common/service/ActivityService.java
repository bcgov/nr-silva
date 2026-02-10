package ca.bc.gov.restapi.results.common.service;

import ca.bc.gov.restapi.results.common.dto.opening.OpeningSearchExactFiltersDto;
import ca.bc.gov.restapi.results.common.dto.opening.OpeningSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActivityService {
  Page<OpeningSearchResponseDto> activitySearch(
      OpeningSearchExactFiltersDto filtersDto, Pageable pagination);
}
