package ca.bc.gov.restapi.results.oracle.endpoint;

import ca.bc.gov.restapi.results.common.endpoint.AbstractSearchEndpointForestCoverSearchIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

/**
 * Integration test for forest cover search endpoint with Oracle database. Executes all forest cover
 * search endpoint tests against the Oracle database.
 */
@DisplayName("Integrated Test | Forest Cover Search Endpoint | Legacy(Oracle primary)")
@EnabledIfSystemProperty(named = "server.primary-db", matches = "oracle")
@WithMockJwt(value = "ttester")
public class SearchEndpointForestCoverSearchOracleIntegrationTest
    extends AbstractSearchEndpointForestCoverSearchIntegrationTest {}
