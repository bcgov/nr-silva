package ca.bc.gov.restapi.results.oracle.service.opening;

import ca.bc.gov.restapi.results.common.repository.OpeningRepository;
import ca.bc.gov.restapi.results.common.service.opening.impl.AbstractOpeningTrendsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OpeningTrendsOracleService extends AbstractOpeningTrendsService {

  public OpeningTrendsOracleService(OpeningRepository openingRepository) {
    super(openingRepository);
  }

}
