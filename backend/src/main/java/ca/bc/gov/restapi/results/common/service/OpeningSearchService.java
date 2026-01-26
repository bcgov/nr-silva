package ca.bc.gov.restapi.results.common.service;

import ca.bc.gov.restapi.results.common.dto.opening.OpeningSearchExactFiltersDto;
import ca.bc.gov.restapi.results.common.dto.opening.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.common.dto.opening.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.common.projection.SilvicultureSearchProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OpeningSearchService {
  Page<OpeningSearchResponseDto> openingSearch(
      OpeningSearchFiltersDto filtersDto, Pageable pagination);

  Page<OpeningSearchResponseDto> parsePageResult(
      Page<SilvicultureSearchProjection> searchResultPage);
  
  Page<OpeningSearchResponseDto> openingSearchExact(
    OpeningSearchExactFiltersDto filtersDto, Pageable pagination);
}
