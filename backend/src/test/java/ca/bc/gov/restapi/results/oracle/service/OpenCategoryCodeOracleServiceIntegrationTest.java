package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.service.AbstractOpenCategoryCodeServiceIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@DisplayName("Integrated Test | Open Category Code Service | Legacy(Oracle primary)")
@EnabledIfSystemProperty(named = "server.primary-db", matches = "oracle")
public class OpenCategoryCodeOracleServiceIntegrationTest extends
    AbstractOpenCategoryCodeServiceIntegrationTest<OpenCategoryCodeOracleService> {
}
