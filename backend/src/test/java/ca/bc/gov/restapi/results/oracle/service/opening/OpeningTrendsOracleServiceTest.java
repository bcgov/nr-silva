package ca.bc.gov.restapi.results.oracle.service.opening;

import ca.bc.gov.restapi.results.common.repository.OpeningRepository;
import ca.bc.gov.restapi.results.common.service.opening.AbstractOpeningTrendsServiceTest;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Unit Test | Opening Trends Service | Legacy(Oracle primary)")
class OpeningTrendsOracleServiceTest extends AbstractOpeningTrendsServiceTest<OpeningTrendsOracleService> {

  @Override
  protected OpeningTrendsOracleService createService(OpeningRepository openingRepository) {
    return new OpeningTrendsOracleService(openingRepository);
  }
}
