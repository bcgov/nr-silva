package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.service.AbstractStandardUnitServiceIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

/**
 * Integration test for StandardUnitService with PostgreSQL database. Executes standard unit search
 * integration tests against the PostgreSQL database.
 */
@DisplayName("Integrated Test | Standard Unit Service | Postgres-only")
@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@WithMockJwt(value = "ttester")
public class StandardUnitServicePostgresIntegrationTest
    extends AbstractStandardUnitServiceIntegrationTest {}
