package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.common.endpoint.AbstractSearchEndpointActivitySearchIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

/**
 * Integration test for activity search endpoint with PostgreSQL database. Executes all activity
 * search endpoint tests against the PostgreSQL database.
 */
@DisplayName("Integrated Test | Activity Search Endpoint | Postgres-only")
@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@WithMockJwt(value = "ttester")
public class SearchEndpointActivitySearchPostgresIntegrationTest
    extends AbstractSearchEndpointActivitySearchIntegrationTest {}
