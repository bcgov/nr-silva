package ca.bc.gov.restapi.results.oracle.service;

import org.junit.jupiter.api.DisplayName;

import ca.bc.gov.restapi.results.common.service.AbstractCutBlockOpenAdminServiceIntegrationTest;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@DisplayName("Integrated Test | CutBlockOpenAdminService | Legacy(Oracle primary)")
@EnabledIfSystemProperty(named = "server.primary-db", matches = "oracle")
class CutBlockOpenAdminOracleServiceIntegrationTest extends AbstractCutBlockOpenAdminServiceIntegrationTest<CutBlockOpenAdminOracleService> {
}
