package ca.bc.gov.restapi.results.oracle.service.opening.details;

import ca.bc.gov.restapi.results.common.service.opening.details.impl.AbstractOpeningDetailsForestCoverService;

import ca.bc.gov.restapi.results.oracle.repository.ForestCoverEntityOracleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OpeningDetailsForestCoverService extends AbstractOpeningDetailsForestCoverService {

  public OpeningDetailsForestCoverService(ForestCoverEntityOracleRepository coverRepository) {
    super(coverRepository);
  }
}
