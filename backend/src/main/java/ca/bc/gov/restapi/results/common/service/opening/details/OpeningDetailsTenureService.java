package ca.bc.gov.restapi.results.common.service.opening.details;

import ca.bc.gov.restapi.results.common.dto.opening.OpeningDetailsTenuresDto;
import org.springframework.data.domain.Pageable;

public interface OpeningDetailsTenureService {
  OpeningDetailsTenuresDto getOpeningTenures(
      Long openingId, String mainSearchTerm, Pageable pageable);

}
