package ca.bc.gov.restapi.results.postgres.service.opening;

import ca.bc.gov.restapi.results.common.repository.OpeningRepository;
import ca.bc.gov.restapi.results.common.service.opening.AbstractOpeningTrendsServiceTest;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Unit Test | Opening Trends Service | Postgres-only")
public class OpeningTrendsPostgresServiceTest extends
    AbstractOpeningTrendsServiceTest<OpeningTrendsPostgresService> {

  @Override
  protected OpeningTrendsPostgresService createService(OpeningRepository openingRepository) {
    return new OpeningTrendsPostgresService(openingRepository);
  }
}
