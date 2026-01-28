package ca.bc.gov.restapi.results.postgres.service.opening.history;

import ca.bc.gov.restapi.results.common.repository.ForestCoverRepository;
import ca.bc.gov.restapi.results.common.service.opening.history.AbstractOpeningForestCoverHistoryServiceTest;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Unit Test | Opening Forest Cover History Service | Postgres-only")
public class OpeningForestCoverHistoryPostgresServiceTest extends
    AbstractOpeningForestCoverHistoryServiceTest<OpeningForestCoverHistoryPostgresService> {

  @Override
  protected OpeningForestCoverHistoryPostgresService createService(
      ForestCoverRepository forestCoverRepository) {
    return new OpeningForestCoverHistoryPostgresService(forestCoverRepository);
  }
}
