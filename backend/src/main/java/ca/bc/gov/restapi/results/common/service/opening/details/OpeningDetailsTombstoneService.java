package ca.bc.gov.restapi.results.common.service.opening.details;

import ca.bc.gov.restapi.results.common.dto.opening.OpeningDetailsTombstoneOverviewDto;

import java.util.Optional;

public interface OpeningDetailsTombstoneService {
  Optional<OpeningDetailsTombstoneOverviewDto> getOpeningTombstone(Long openingId);
}
