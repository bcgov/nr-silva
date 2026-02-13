package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.common.service.AbstractActivityServiceIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

/**
 * Integration test for ActivityService with Oracle database. Executes activity search
 * integration tests against the Oracle database.
 */
@DisplayName("Integrated Test | Activity Service | Legacy(Oracle primary)")
@EnabledIfSystemProperty(named = "server.primary-db", matches = "oracle")
@WithMockJwt(value = "ttester")
public class ActivityServiceOracleIntegrationTest
    extends AbstractActivityServiceIntegrationTest {}
