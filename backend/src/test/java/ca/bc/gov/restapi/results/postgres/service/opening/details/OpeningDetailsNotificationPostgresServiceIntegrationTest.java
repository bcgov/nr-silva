package ca.bc.gov.restapi.results.postgres.service.opening.details;

import ca.bc.gov.restapi.results.common.service.opening.details.AbstractOpeningDetailsNotificationServiceIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@DisplayName("Integrated Test | Opening Details Notification Service | Postgres-only")
@EnabledIfSystemProperty(named = "primary-db", matches = "postgres")
public class OpeningDetailsNotificationPostgresServiceIntegrationTest extends
    AbstractOpeningDetailsNotificationServiceIntegrationTest<OpeningDetailsNotificationPostgresService> {
}
