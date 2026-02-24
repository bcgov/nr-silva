package ca.bc.gov.restapi.results.oracle.endpoint;

import ca.bc.gov.restapi.results.common.endpoint.AbstractCodesEndpointIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "oracle")
@DisplayName("Integrated Test | Codes Endpoint (Oracle)")
class CodesEndpointOracleIntegrationTest extends AbstractCodesEndpointIntegrationTest {}
