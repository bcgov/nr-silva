package ca.bc.gov.restapi.results.oracle.service.opening.details;

import ca.bc.gov.restapi.results.common.repository.OpeningRepository;
import ca.bc.gov.restapi.results.common.service.opening.details.AbstractOpeningDetailsNotificationServiceTest;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Unit Test | Opening Details Notification Service | Legacy(Oracle primary)")
public class OpeningDetailsNotificationOracleServiceTest
    extends AbstractOpeningDetailsNotificationServiceTest<OpeningDetailsNotificationOracleService> {

  @Override
  protected OpeningDetailsNotificationOracleService createService(
      OpeningRepository<?> openingRepository) {
    return new OpeningDetailsNotificationOracleService(openingRepository);
  }
}
