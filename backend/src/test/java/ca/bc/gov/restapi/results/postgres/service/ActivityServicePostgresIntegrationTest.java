package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.common.service.AbstractActivityServiceIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

/**
 * Integration test for ActivityService with PostgreSQL database. Executes activity search
 * integration tests against the PostgreSQL database.
 */
@DisplayName("Integrated Test | Activity Service | Postgres-only")
@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@WithMockJwt(value = "ttester")
public class ActivityServicePostgresIntegrationTest
    extends AbstractActivityServiceIntegrationTest {}
