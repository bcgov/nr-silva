package ca.bc.gov.restapi.results.common.service.opening.details;

import ca.bc.gov.restapi.results.common.dto.cover.OpeningForestCoverDetailsDto;
import ca.bc.gov.restapi.results.common.dto.cover.OpeningForestCoverDto;

import java.util.List;
import java.util.Optional;

public interface OpeningDetailsForestCoverService {
  List<OpeningForestCoverDto> getOpeningForestCoverList(Long openingId, String mainSearchTerm);
  Optional<OpeningForestCoverDetailsDto> getDetails(Long coverId);
}
