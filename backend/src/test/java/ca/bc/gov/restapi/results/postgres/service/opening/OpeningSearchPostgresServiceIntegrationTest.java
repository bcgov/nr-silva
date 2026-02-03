package ca.bc.gov.restapi.results.postgres.service.opening;

import ca.bc.gov.restapi.results.common.service.opening.AbstractOpeningSearchServiceIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.postgres.service.OpeningSearchPostgresService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@DisplayName("Integrated Test | Opening Search Service | Postgres-only")
@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@WithMockJwt(value = "ttester")
public class OpeningSearchPostgresServiceIntegrationTest extends
    AbstractOpeningSearchServiceIntegrationTest<OpeningSearchPostgresService> {
}
