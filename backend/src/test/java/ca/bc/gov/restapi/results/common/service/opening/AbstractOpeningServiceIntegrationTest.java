package ca.bc.gov.restapi.results.common.service.opening;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import ca.bc.gov.restapi.results.common.dto.opening.OpeningDetailsStockingDto;
import ca.bc.gov.restapi.results.common.dto.opening.OpeningDetailsTombstoneOverviewDto;
import ca.bc.gov.restapi.results.common.service.opening.details.OpeningDetailsService;
import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WiremockLogNotifier;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("Integrated Test | Opening Service | Contract")
@WithMockJwt(value = "ttester")
public abstract class AbstractOpeningServiceIntegrationTest<T extends OpeningDetailsService>
    extends AbstractTestContainerIntegrationTest {

  @RegisterExtension
  static WireMockExtension clientApiStub =
      WireMockExtension.newInstance()
          .options(
              wireMockConfig()
                  .port(10000)
                  .notifier(new WiremockLogNotifier())
                  .asynchronousResponseEnabled(true)
                  .stubRequestLoggingDisabled(false))
          .configureStaticDsl(true)
          .build();

  @Autowired protected OpeningDetailsService openingService;

  @Test
  @DisplayName("Opening with existing openingId should succeed")
  void getOpeningTombstone_existingOpeningId_shouldSucceed() {
    Optional<OpeningDetailsTombstoneOverviewDto> result =
        openingService.getOpeningTombstone(101017L);

    Assertions.assertTrue(result.isPresent(), "Result should be present");
    Assertions.assertEquals(101017L, result.get().openingId(), "Opening ID should match");

    // Assertions to check the tombstone details
    Assertions.assertEquals(
        " 92K 014 0.0  514",
        result.get().tombstone().openingNumber(),
        "Opening number should match");
    Assertions.assertEquals("TFL47", result.get().tombstone().fileId(), "File ID should match");
    Assertions.assertEquals(
        "00010003", result.get().tombstone().client().clientNumber(), "Client number should match");

    // Assertions to check the overview details
    Assertions.assertEquals(
        "A02",
        result.get().overview().opening().tenureType().code(),
        "Tenure type code should match");
    Assertions.assertEquals(
        1, result.get().overview().opening().comments().size(), "Number of comments should match");
    Assertions.assertEquals(
        "A",
        result.get().overview().milestones().standardsUnitId(),
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
    Assertions.assertEquals(
        "A", dto.stocking().stockingStandardUnit(), "Stocking standard unit should match");
    Assertions.assertEquals(1013720L, dto.stocking().ssuId(), "SSUID should match");
    Assertions.assertEquals(25.5F, dto.stocking().netArea(), "Net area should match");
    Assertions.assertEquals(
        5.0F, dto.stocking().soilDisturbancePercent(), "Soil disturbance percent should match");

    Assertions.assertEquals(
        "2005-01-24",
        dto.stocking().milestones().postHarvestDeclaredDate().toString(),
        "Post-harvest declared date should match");
    Assertions.assertEquals(
        "2007-06-15",
        dto.stocking().milestones().regenDeclaredDate().toString(),
        "Regen declared date should match");
    Assertions.assertEquals(
        6, dto.stocking().milestones().regenOffsetYears(), "Regen offset years should match");
    Assertions.assertEquals(
        "2010-01-19",
        dto.stocking().milestones().regenDueDate().toString(),
        "Regen due date should match");
    Assertions.assertNull(
        dto.stocking().milestones().noRegenDeclaredDate(), "No regen declared date should be null");
    Assertions.assertNull(
        dto.stocking().milestones().noRegenOffsetYears(), "No regen offset years should be null");
    Assertions.assertNull(
        dto.stocking().milestones().noRegenDueDate(), "No regen due date should be null");
    Assertions.assertEquals(
        "2017-05-30",
        dto.stocking().milestones().freeGrowingDeclaredDate().toString(),
        "Free growing declared date should match");
    Assertions.assertEquals(
        14,
        dto.stocking().milestones().freeGrowingOffsetYears(),
        "Free growing offset years should match");
    Assertions.assertEquals(
        "2018-01-19",
        dto.stocking().milestones().freeGrowingDueDate().toString(),
        "Free growing due date should match");
    Assertions.assertFalse(
        dto.stocking().milestones().noRegenIndicated(), "No regen indicated should be false");
    Assertions.assertNotNull(dto.stocking().milestones().comments(), "Comments should not be null");
    Assertions.assertTrue(
        dto.stocking().milestones().comments().isEmpty(), "Comments should be empty");

    // Verify preferred species
    Assertions.assertEquals(
        3, dto.preferredSpecies().size(), "Preferred species size should match");
    Assertions.assertEquals(
        "CW",
        dto.preferredSpecies().get(0).species().code(),
        "Preferred species code should match");
    Assertions.assertEquals(
        "western redcedar",
        dto.preferredSpecies().get(0).species().description(),
        "Preferred species description should match");
    Assertions.assertEquals(
        1.5f,
        dto.preferredSpecies().get(0).minHeight(),
        "Preferred species min height should match");

    // Verify acceptable species
    Assertions.assertEquals(
        1, dto.acceptableSpecies().size(), "Acceptable species size should match");
    Assertions.assertEquals(
        "BA",
        dto.acceptableSpecies().get(0).species().code(),
        "Acceptable species code should match");
    Assertions.assertEquals(
        "amabilis fir",
        dto.acceptableSpecies().get(0).species().description(),
        "Acceptable species description should match");
    Assertions.assertEquals(
        1.8f,
        dto.acceptableSpecies().get(0).minHeight(),
        "Acceptable species min height should match");

    // Verify stocking layers
    Assertions.assertNotNull(dto.layers(), "Stocking layers should not be null");
    Assertions.assertEquals(
        500, dto.layers().get(0).minWellspacedTrees(), "Min well-spaced trees should match");
    Assertions.assertEquals(
        400,
        dto.layers().get(0).minPreferredWellspacedTrees(),
        "Min preferred well-spaced trees should match");
    Assertions.assertEquals(
        2,
        dto.layers().get(0).minHorizontalDistanceWellspacedTrees(),
        "Min horizontal distance well-spaced trees should match");
    Assertions.assertEquals(
        900, dto.layers().get(0).targetWellspacedTrees(), "Target well-spaced trees should match");
    Assertions.assertEquals(
        800, dto.layers().get(0).minPostspacingDensity(), "Min post-spacing density should match");
    Assertions.assertEquals(
        2000, dto.layers().get(0).maxPostspacingDensity(), "Max post-spacing density should match");
    Assertions.assertEquals(
        10000, dto.layers().get(0).maxConiferous(), "Max coniferous should match");
    Assertions.assertEquals(
        150,
        dto.layers().get(0).heightRelativeToComp(),
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
