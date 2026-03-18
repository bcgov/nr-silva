package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.common.endpoint.AbstractSearchEndpointForestCoverSearchIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

/**
 * Integration test for forest cover search endpoint with PostgreSQL database. Executes all forest
 * cover search endpoint tests against the PostgreSQL database.
 */
@DisplayName("Integrated Test | Forest Cover Search Endpoint | Postgres-only")
@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@WithMockJwt(value = "ttester")
public class SearchEndpointForestCoverSearchPostgresIntegrationTest
    extends AbstractSearchEndpointForestCoverSearchIntegrationTest {}
