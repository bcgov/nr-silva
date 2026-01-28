package ca.bc.gov.restapi.results.oracle.service.opening.details;

import ca.bc.gov.restapi.results.common.service.opening.details.AbstractOpeningDetailsNotificationServiceIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@DisplayName("Integrated Test | Opening Details Notification Service | Legacy(Oracle primary)")
@EnabledIfSystemProperty(named = "primary-db", matches = "oracle")
public class OpeningDetailsNotificationOracleServiceIntegrationTest extends
    AbstractOpeningDetailsNotificationServiceIntegrationTest<OpeningDetailsNotificationOracleService> {
}
