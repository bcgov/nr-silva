package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.common.endpoint.AbstractSearchEndpointCommentSearchIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

/**
 * Integration test for the comment search endpoint with PostgreSQL database. Executes all comment
 * search endpoint tests against the PostgreSQL database.
 */
@DisplayName("Integrated Test | Comment Search Endpoint | Postgres-only")
@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@WithMockJwt(value = "ttester")
public class SearchEndpointCommentSearchPostgresIntegrationTest
    extends AbstractSearchEndpointCommentSearchIntegrationTest {}
