package ca.bc.gov.restapi.results.oracle.service.opening;

import ca.bc.gov.restapi.results.common.service.opening.AbstractOpeningServiceIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.oracle.service.opening.details.OpeningDetailsOracleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@DisplayName("Integrated Test | Opening Service | Legacy(Oracle primary)")
@EnabledIfSystemProperty(named = "primary-db", matches = "oracle")
@WithMockJwt(value = "ttester")
public class OpeningOracleServiceIntegrationTest extends
    AbstractOpeningServiceIntegrationTest<OpeningDetailsOracleService> {
}
