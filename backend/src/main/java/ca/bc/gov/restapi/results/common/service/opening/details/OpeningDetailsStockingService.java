package ca.bc.gov.restapi.results.common.service.opening.details;

import ca.bc.gov.restapi.results.common.dto.opening.OpeningDetailsStockingDto;

import java.util.List;

public interface OpeningDetailsStockingService {
  List<OpeningDetailsStockingDto> getOpeningStockingDetails(Long openingId);
}
