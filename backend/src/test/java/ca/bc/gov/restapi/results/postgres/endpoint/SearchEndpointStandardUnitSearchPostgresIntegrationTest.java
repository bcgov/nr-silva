package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.common.endpoint.AbstractSearchEndpointStandardUnitSearchIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

/**
 * Integration test for standard unit search endpoint with PostgreSQL database. Executes all
 * standard unit search endpoint tests against the PostgreSQL database.
 */
@DisplayName("Integrated Test | Standard Unit Search Endpoint | Postgres-only")
@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@WithMockJwt(value = "ttester")
public class SearchEndpointStandardUnitSearchPostgresIntegrationTest
    extends AbstractSearchEndpointStandardUnitSearchIntegrationTest {}
