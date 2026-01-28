package ca.bc.gov.restapi.results.oracle.service.opening;

import ca.bc.gov.restapi.results.common.service.opening.AbstractOpeningSearchServiceIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.oracle.service.OpeningSearchOracleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@DisplayName("Integrated Test | Opening Search Service | Legacy(Oracle primary)")
@EnabledIfSystemProperty(named = "primary-db", matches = "oracle")
@WithMockJwt(value = "ttester")
class OpeningSearchOracleServiceIntegrationTest extends AbstractOpeningSearchServiceIntegrationTest<OpeningSearchOracleService> {
}
