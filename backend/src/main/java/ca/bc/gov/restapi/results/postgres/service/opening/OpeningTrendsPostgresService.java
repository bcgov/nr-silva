package ca.bc.gov.restapi.results.postgres.service.opening;

import ca.bc.gov.restapi.results.common.repository.OpeningRepository;
import ca.bc.gov.restapi.results.common.service.opening.impl.AbstractOpeningTrendsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
@Service
public class OpeningTrendsPostgresService extends AbstractOpeningTrendsService {

  public OpeningTrendsPostgresService(OpeningRepository openingRepository) {
    super(openingRepository);
  }
}
