package ca.bc.gov.restapi.results.postgres.service.opening.details;

import ca.bc.gov.restapi.results.common.repository.OpeningRepository;
import ca.bc.gov.restapi.results.common.service.opening.details.AbstractOpeningDetailsNotificationServiceTest;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Unit Test | Opening Details Notification Service | Postgres-only")
public class OpeningDetailsNotificationPostgresServiceTest extends
    AbstractOpeningDetailsNotificationServiceTest<OpeningDetailsNotificationPostgresService> {

  @Override
  protected OpeningDetailsNotificationPostgresService createService(
      OpeningRepository openingRepository) {
    return new OpeningDetailsNotificationPostgresService(openingRepository);
  }
}
