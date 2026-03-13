package ca.bc.gov.restapi.results.common.service;

import ca.bc.gov.restapi.results.common.dto.cover.ForestCoverSearchFilterDto;
import ca.bc.gov.restapi.results.common.dto.cover.ForestCoverSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ForestCoverService {
  Page<ForestCoverSearchResponseDto> forestCoverSearch(
      ForestCoverSearchFilterDto filters, Pageable pagination);
}
