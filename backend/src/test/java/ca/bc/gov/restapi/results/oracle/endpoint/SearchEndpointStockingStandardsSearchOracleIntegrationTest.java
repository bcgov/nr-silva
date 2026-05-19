package ca.bc.gov.restapi.results.oracle.endpoint;

import ca.bc.gov.restapi.results.common.endpoint.AbstractSearchEndpointStockingStandardsSearchIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

/**
 * Integration test for stocking standards search endpoint with Oracle database. Executes all
 * stocking standards search endpoint tests against the Oracle database.
 */
@DisplayName("Integrated Test | Stocking Standards Search Endpoint | Legacy(Oracle primary)")
@EnabledIfSystemProperty(named = "server.primary-db", matches = "oracle")
@WithMockJwt(value = "ttester")
public class SearchEndpointStockingStandardsSearchOracleIntegrationTest
    extends AbstractSearchEndpointStockingStandardsSearchIntegrationTest {}
