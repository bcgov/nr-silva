package ca.bc.gov.restapi.results.common.service;

import ca.bc.gov.restapi.results.common.dto.StandardUnitSearchFilterDto;
import ca.bc.gov.restapi.results.common.dto.StandardUnitSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StandardUnitService {
  Page<StandardUnitSearchResponseDto> standardUnitSearch(
      StandardUnitSearchFilterDto filters, Pageable pagination);
}
