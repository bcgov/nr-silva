package ca.bc.gov.restapi.results.postgres.service.opening.history;

import ca.bc.gov.restapi.results.common.repository.OpeningRepository;
import ca.bc.gov.restapi.results.common.repository.SilvicultureCommentRepository;
import ca.bc.gov.restapi.results.common.service.opening.history.AbstractOpeningStandardUnitHistoryServiceTest;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Unit Test | Opening Standard Unit History Service | Postgres-only")
public class OpeningStandardUnitHistoryPostgresServiceTest
    extends AbstractOpeningStandardUnitHistoryServiceTest<
        OpeningStandardUnitHistoryPostgresService> {

  @Override
  protected OpeningStandardUnitHistoryPostgresService createService(
      OpeningRepository<?> openingRepository, SilvicultureCommentRepository commentRepository) {
    return new OpeningStandardUnitHistoryPostgresService(openingRepository, commentRepository);
  }
}
