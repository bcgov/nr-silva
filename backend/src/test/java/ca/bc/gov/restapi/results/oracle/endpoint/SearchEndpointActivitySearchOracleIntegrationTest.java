package ca.bc.gov.restapi.results.oracle.endpoint;

import ca.bc.gov.restapi.results.common.endpoint.AbstractSearchEndpointActivitySearchIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

/**
 * Integration test for activity search endpoint with Oracle database. Executes all activity
 * search endpoint tests against the Oracle database.
 */
@DisplayName("Integrated Test | Activity Search Endpoint | Legacy(Oracle primary)")
@EnabledIfSystemProperty(named = "server.primary-db", matches = "oracle")
@WithMockJwt(value = "ttester")
public class SearchEndpointActivitySearchOracleIntegrationTest
    extends AbstractSearchEndpointActivitySearchIntegrationTest {}
