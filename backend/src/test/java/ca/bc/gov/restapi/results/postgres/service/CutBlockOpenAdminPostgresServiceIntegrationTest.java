package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.service.AbstractCutBlockOpenAdminServiceIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@DisplayName("Integrated Test | CutBlockOpenAdminService | Postgres-only")
@EnabledIfSystemProperty(named = "primary-db", matches = "postgres")
public class CutBlockOpenAdminPostgresServiceIntegrationTest extends
    AbstractCutBlockOpenAdminServiceIntegrationTest<CutBlockOpenAdminPostgresService> {
}
