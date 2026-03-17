package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.service.impl.AbstractForestCoverService;
import ca.bc.gov.restapi.results.postgres.repository.ForestCoverEntityPostgresRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class ForestCoverPostgresService extends AbstractForestCoverService {

  public ForestCoverPostgresService(ForestCoverEntityPostgresRepository forestCoverRepository) {
    super(forestCoverRepository);
  }
}
