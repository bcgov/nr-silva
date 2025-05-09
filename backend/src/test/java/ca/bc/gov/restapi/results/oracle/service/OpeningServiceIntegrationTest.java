package ca.bc.gov.restapi.results.oracle.service;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WiremockLogNotifier;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsStockingDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsTombstoneOverviewDto;
import ca.bc.gov.restapi.results.oracle.service.opening.details.OpeningDetailsService;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;

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
  private OpeningDetailsService openingService;

  @Test
  @DisplayName("Opening with existing openingId should succeed")
  void getOpeningTombstone_existingOpeningId_shouldSucceed() {
    Optional<OpeningDetailsTombstoneOverviewDto> result =
        openingService.getOpeningTombstone(101017L);

    Assertions.assertTrue(result.isPresent(), "Result should be present");
    Assertions.assertEquals(101017L, result.get().openingId(), "Opening ID should match");

    // Assertions to check the tombstone details
    Assertions.assertEquals(" 92K 014 0.0  514", result.get().tombstone().openingNumber(),
        "Opening number should match");
    Assertions.assertEquals("TFL47", result.get().tombstone().fileId(), "File ID should match");
    Assertions.assertEquals("00010003", result.get().tombstone().client().clientNumber(),
        "Client number should match");

    // Assertions to check the overview details
    Assertions.assertEquals("A02", result.get().overview().opening().tenureType().code(),
        "Tenure type code should match");
    Assertions.assertEquals(1, result.get().overview().opening().comments().size(),
        "Number of comments should match");
    Assertions.assertEquals("A", result.get().overview().milestones().standardsUnitId(),
        "Standards unit ID should match");

  }

  @Test
  @DisplayName("Opening with non-existing openingId should succeed and return null")
  void getOpeningTombstone_nonExistingOpeningId_shouldSucceedAndReturnNull() {
    Optional<OpeningDetailsTombstoneOverviewDto> result = openingService.getOpeningTombstone(1L);

    Assertions.assertFalse(result.isPresent(), "Result should be null");
  }

  @Test
  @DisplayName("Opening with existing openingId 1524010 should succeed")
  void getOpeningTombstone_existingOpeningId_1524010_shouldSucceed() {
    Optional<OpeningDetailsTombstoneOverviewDto> result =
        openingService.getOpeningTombstone(1524010L);

    Assertions.assertTrue(result.isPresent(), "Result should be present");
    Assertions.assertEquals(1524010L, result.get().openingId(), "Opening ID should match");

  }

  @Test
  @DisplayName("Opening Stocking Details with existing openingId should succeed")
  void getOpeningStockingDetails_existingOpeningId_shouldSucceed() {
    Long openingId = 1009974L;

    // Execute
    List<OpeningDetailsStockingDto> result = openingService.getOpeningStockingDetails(openingId);

    // Verify
    Assertions.assertFalse(result.isEmpty(), "Result should not be empty");
    Assertions.assertEquals(1, result.size(), "Result size should be 1");

    OpeningDetailsStockingDto dto = result.get(0);

    // Verify stocking details
    Assertions.assertEquals("A", dto.stocking().stockingStandardUnit(),
        "Stocking standard unit should match");
    Assertions.assertEquals(1013720L, dto.stocking().ssid(), "SSID should match");
    Assertions.assertEquals(25.5F, dto.stocking().netArea(), "Net area should match");
    Assertions.assertEquals(5.0F, dto.stocking().soilDisturbancePercent(),
        "Soil disturbance percent should match");

    // Verify preferred species
    Assertions.assertEquals(3, dto.preferredSpecies().size(),
        "Preferred species size should match");
    Assertions.assertEquals("CW", dto.preferredSpecies().get(0).species().code(),
        "Preferred species code should match");
    Assertions.assertEquals("western redcedar",
        dto.preferredSpecies().get(0).species().description(),
        "Preferred species description should match");
    Assertions.assertEquals(1L, dto.preferredSpecies().get(0).minHeight(),
        "Preferred species min height should match");

    // Verify acceptable species
    Assertions.assertEquals(1, dto.acceptableSpecies().size(),
        "Acceptable species size should match");
    Assertions.assertEquals("BA", dto.acceptableSpecies().get(0).species().code(),
        "Acceptable species code should match");
    Assertions.assertEquals("amabilis fir", dto.acceptableSpecies().get(0).species().description(),
        "Acceptable species description should match");
    Assertions.assertEquals(1L, dto.acceptableSpecies().get(0).minHeight(),
        "Acceptable species min height should match");

    // Verify stocking layer
    Assertions.assertNotNull(dto.layer(), "Stocking layer should not be null");
    Assertions.assertEquals(500, dto.layer().minWellspacedTrees(),
        "Min well-spaced trees should match");
    Assertions.assertEquals(400, dto.layer().minPreferredWellspacedTrees(),
        "Min preferred well-spaced trees should match");
    Assertions.assertEquals(2, dto.layer().minHorizontalDistanceWellspacedTrees(),
        "Min horizontal distance well-spaced trees should match");
    Assertions.assertEquals(900, dto.layer().targetWellspacedTrees(),
        "Target well-spaced trees should match");
    Assertions.assertEquals(800, dto.layer().minPostspacingDensity(),
        "Min post-spacing density should match");
    Assertions.assertEquals(2000, dto.layer().maxPostspacingDensity(),
        "Max post-spacing density should match");
    Assertions.assertEquals(10000, dto.layer().maxConiferous(), "Max coniferous should match");
    Assertions.assertEquals(150, dto.layer().heightRelativeToComp(),
        "Height relative to competition should match");
  }

  @Test
  @DisplayName("Opening Stocking Details with non-existing openingId should return empty")
  void getOpeningStockingDetails_nonExistingOpeningId_shouldReturnEmpty() {
    Long openingId = 1L;

    // Execute
    List<OpeningDetailsStockingDto> result = openingService.getOpeningStockingDetails(openingId);

    // Verify
    Assertions.assertTrue(result.isEmpty(), "Result should be empty");
  }
}
