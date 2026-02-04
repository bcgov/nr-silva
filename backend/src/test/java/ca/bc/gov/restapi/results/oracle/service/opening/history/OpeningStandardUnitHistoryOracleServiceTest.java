package ca.bc.gov.restapi.results.oracle.service.opening.history;

import ca.bc.gov.restapi.results.common.repository.OpeningRepository;
import ca.bc.gov.restapi.results.common.repository.SilvicultureCommentRepository;
import ca.bc.gov.restapi.results.common.service.opening.history.AbstractOpeningStandardUnitHistoryServiceTest;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Unit Test | Opening Standard Unit History Service | Legacy(Oracle primary)")
public class OpeningStandardUnitHistoryOracleServiceTest extends
    AbstractOpeningStandardUnitHistoryServiceTest<OpeningStandardUnitHistoryOracleService> {

    @Override
    protected OpeningStandardUnitHistoryOracleService createService(OpeningRepository openingRepository,
        SilvicultureCommentRepository commentRepository) {
        return new OpeningStandardUnitHistoryOracleService(openingRepository, commentRepository);
    }
}
