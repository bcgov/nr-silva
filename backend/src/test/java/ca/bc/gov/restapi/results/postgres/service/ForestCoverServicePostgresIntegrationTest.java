package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.service.AbstractForestCoverServiceIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

/**
 * Integration test for ForestCoverService with PostgreSQL database. Executes forest cover search
 * integration tests against the PostgreSQL database.
 */
@DisplayName("Integrated Test | Forest Cover Service | Postgres-only")
@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@WithMockJwt(value = "ttester")
public class ForestCoverServicePostgresIntegrationTest
    extends AbstractForestCoverServiceIntegrationTest {}
