package ca.bc.gov.restapi.results.oracle.service.opening.details;

import ca.bc.gov.restapi.results.common.repository.CutBlockOpenAdminRepository;
import ca.bc.gov.restapi.results.common.service.opening.details.impl.AbstractOpeningDetailsTenureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
@Service
public class OpeningDetailsTenureOracleService extends AbstractOpeningDetailsTenureService {

  public OpeningDetailsTenureOracleService(CutBlockOpenAdminRepository cutBlockOpenAdminRepository) {
    super(cutBlockOpenAdminRepository);
  }
}
