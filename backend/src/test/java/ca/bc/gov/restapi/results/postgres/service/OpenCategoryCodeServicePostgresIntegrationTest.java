package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.service.AbstractOpenCategoryCodeServiceIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@DisplayName("Integrated Test | Open Category Code Service | Postgres-only")
@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
public class OpenCategoryCodeServicePostgresIntegrationTest extends
    AbstractOpenCategoryCodeServiceIntegrationTest<OpenCategoryCodePostgresService> {
}
