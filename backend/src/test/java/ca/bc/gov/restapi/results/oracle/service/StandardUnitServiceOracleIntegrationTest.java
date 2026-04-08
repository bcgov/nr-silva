package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.service.AbstractStandardUnitServiceIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

/**
 * Integration test for StandardUnitService with Oracle database. Executes standard unit search
 * integration tests against the Oracle database.
 */
@DisplayName("Integrated Test | Standard Unit Service | Legacy(Oracle primary)")
@EnabledIfSystemProperty(named = "server.primary-db", matches = "oracle")
@WithMockJwt(value = "ttester")
public class StandardUnitServiceOracleIntegrationTest
    extends AbstractStandardUnitServiceIntegrationTest {}
