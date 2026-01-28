package ca.bc.gov.restapi.results.postgres.service.opening;

import ca.bc.gov.restapi.results.common.service.opening.AbstractOpeningServiceIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.postgres.service.opening.details.OpeningDetailsPostgresService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@DisplayName("Integrated Test | Opening Service | Postgres-only")
@EnabledIfSystemProperty(named = "primary-db", matches = "postgres")
@WithMockJwt(value = "ttester")
public class OpeningPostgresServiceIntegrationTest extends
    AbstractOpeningServiceIntegrationTest<OpeningDetailsPostgresService> {
}
