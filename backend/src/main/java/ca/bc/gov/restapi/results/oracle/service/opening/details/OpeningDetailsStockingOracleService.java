package ca.bc.gov.restapi.results.oracle.service.opening.details;

import ca.bc.gov.restapi.results.common.repository.OpeningRepository;
import ca.bc.gov.restapi.results.common.repository.SilvicultureCommentRepository;
import ca.bc.gov.restapi.results.common.service.opening.details.impl.AbstractOpeningDetailsStockingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OpeningDetailsStockingOracleService extends AbstractOpeningDetailsStockingService {

  public OpeningDetailsStockingOracleService(OpeningRepository openingRepository,
      SilvicultureCommentRepository commentRepository) {
    super(openingRepository, commentRepository);
  }
}
