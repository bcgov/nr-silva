package ca.bc.gov.restapi.results.postgres.service.opening.history;

import ca.bc.gov.restapi.results.common.repository.ForestCoverRepository;
import ca.bc.gov.restapi.results.common.service.opening.history.impl.AbstractOpeningForestCoverHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
@Service
public class OpeningForestCoverHistoryPostgresService extends
    AbstractOpeningForestCoverHistoryService {

  public OpeningForestCoverHistoryPostgresService(ForestCoverRepository coverRepository) {
    super(coverRepository);
  }
}
