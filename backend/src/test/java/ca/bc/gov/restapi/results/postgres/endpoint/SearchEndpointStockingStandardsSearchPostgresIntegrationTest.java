package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.common.endpoint.AbstractSearchEndpointStockingStandardsSearchIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

/**
 * Integration test for stocking standards search endpoint with PostgreSQL database. Executes all
 * stocking standards search endpoint tests against the PostgreSQL database.
 */
@DisplayName("Integrated Test | Stocking Standards Search Endpoint | Postgres-only")
@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@WithMockJwt(value = "ttester")
public class SearchEndpointStockingStandardsSearchPostgresIntegrationTest
    extends AbstractSearchEndpointStockingStandardsSearchIntegrationTest {}
