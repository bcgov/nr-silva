package ca.bc.gov.restapi.results.oracle.endpoint;

import ca.bc.gov.restapi.results.common.endpoint.AbstractSearchEndpointStandardUnitSearchIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

/**
 * Integration test for standard unit search endpoint with Oracle database. Executes all standard
 * unit search endpoint tests against the Oracle database.
 */
@DisplayName("Integrated Test | Standard Unit Search Endpoint | Legacy(Oracle primary)")
@EnabledIfSystemProperty(named = "server.primary-db", matches = "oracle")
@WithMockJwt(value = "ttester")
public class SearchEndpointStandardUnitSearchOracleIntegrationTest
    extends AbstractSearchEndpointStandardUnitSearchIntegrationTest {}
