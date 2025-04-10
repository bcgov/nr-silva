package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WiremockLogNotifier;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsTombstoneOverviewDto;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@DisplayName("Integrated Test | Opening Service")
@WithMockJwt(value = "ttester")
public class OpeningServiceIntegrationTest extends AbstractTestContainerIntegrationTest {
    @RegisterExtension
    static WireMockExtension clientApiStub = WireMockExtension
            .newInstance()
            .options(
                    wireMockConfig()
                            .port(10000)
                            .notifier(new WiremockLogNotifier())
                            .asynchronousResponseEnabled(true)
                            .stubRequestLoggingDisabled(false)
            )
            .configureStaticDsl(true)
            .build();

    @Autowired
    private OpeningService openingService;

    @Test
    @DisplayName("Opening with existing openingId should succeed")
    void getOpeningTombstone_existingOpeningId_shouldSucceed() {
        Optional<OpeningDetailsTombstoneOverviewDto> result =
                openingService.getOpeningTombstone(101017L);

        Assertions.assertTrue(result.isPresent(), "Result should be present");
        Assertions.assertEquals(101017L, result.get().openingId(), "Opening ID should match");

        // Assertions to check the tombstone details
        Assertions.assertEquals(" 92K 014 0.0  514", result.get().tombstone().openingNumber(), "Opening number should match");
        Assertions.assertEquals("TFL47", result.get().tombstone().fileId(), "File ID should match");
        Assertions.assertEquals("00010003", result.get().tombstone().client().clientNumber(), "Client number should match");

        // Assertions to check the overview details
        Assertions.assertEquals("A02", result.get().overview().opening().tenureType().code(), "Tenure type code should match");
        Assertions.assertEquals(1, result.get().overview().opening().comments().size(), "Number of comments should match");
        Assertions.assertEquals("A", result.get().overview().milestones().standardsUnitId(), "Standards unit ID should match");

    }

    @Test
    @DisplayName("Opening with non-existing openingId should succeed and return null")
    void getOpeningTombstone_nonExistingOpeningId_shouldSucceedAndReturnNull() {
        Optional<OpeningDetailsTombstoneOverviewDto> result = openingService.getOpeningTombstone(1L);

        Assertions.assertFalse(result.isPresent(), "Result should be null");
    }

}
