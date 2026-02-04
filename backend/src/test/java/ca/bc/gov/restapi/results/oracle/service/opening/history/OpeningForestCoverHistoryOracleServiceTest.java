package ca.bc.gov.restapi.results.oracle.service.opening.history;

import ca.bc.gov.restapi.results.common.repository.ForestCoverRepository;
import ca.bc.gov.restapi.results.common.service.opening.history.AbstractOpeningForestCoverHistoryServiceTest;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Unit Test | Opening Forest Cover History Service | Legacy(Oracle primary)")
public class OpeningForestCoverHistoryOracleServiceTest extends
    AbstractOpeningForestCoverHistoryServiceTest<OpeningForestCoverHistoryOracleService> {

    @Override
    protected OpeningForestCoverHistoryOracleService createService(
        ForestCoverRepository forestCoverRepository) {
        return new OpeningForestCoverHistoryOracleService(forestCoverRepository);
    }
}
