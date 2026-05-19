package ca.bc.gov.restapi.results.oracle.endpoint;

import ca.bc.gov.restapi.results.common.endpoint.AbstractSearchEndpointCommentSearchIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

/**
 * Integration test for the comment search endpoint with Oracle database. Executes all comment
 * search endpoint tests against the Oracle database.
 */
@DisplayName("Integrated Test | Comment Search Endpoint | Legacy(Oracle primary)")
@EnabledIfSystemProperty(named = "server.primary-db", matches = "oracle")
@WithMockJwt(value = "ttester")
public class SearchEndpointCommentSearchOracleIntegrationTest
    extends AbstractSearchEndpointCommentSearchIntegrationTest {}
