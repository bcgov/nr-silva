package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.service.AbstractForestCoverServiceIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

/**
 * Integration test for ForestCoverService with Oracle database. Executes forest cover search
 * integration tests against the Oracle database.
 */
@DisplayName("Integrated Test | Forest Cover Service | Legacy(Oracle primary)")
@EnabledIfSystemProperty(named = "server.primary-db", matches = "oracle")
@WithMockJwt(value = "ttester")
public class ForestCoverServiceOracleIntegrationTest
    extends AbstractForestCoverServiceIntegrationTest {}
