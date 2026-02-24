package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.common.endpoint.AbstractCodesEndpointIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@DisplayName("Integrated Test | Codes Endpoint (Postgres)")
class CodesEndpointPostgresIntegrationTest extends AbstractCodesEndpointIntegrationTest {}
