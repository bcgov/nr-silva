package ca.bc.gov.restapi.results.common.provider.mock;

import static org.assertj.core.api.Assertions.assertThat;

import ca.bc.gov.restapi.results.common.dto.ExternalCutBlockDetails;
import ca.bc.gov.restapi.results.common.dto.ExternalTenureSummary;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test | MockTenureAdapter")
class MockTenureAdapterTest {

  private MockTenureAdapter adapter;

  @BeforeEach
  void setUp() {
    adapter = new MockTenureAdapter();
  }

  // -------------------------------------------------------------------------
  // Baseline Fixtures Verification
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("verifyCutBlock returns valid details for baseline fixture TFL001/CP1/CB001")
  void verifyCutBlock_baselineTfl001_returnsValidDetails() {
    ExternalCutBlockDetails details =
        adapter.verifyCutBlock("TFL001", "CP1", "CB001", "00010003");

    assertThat(details.exists()).isTrue();
    assertThat(details.cbSkey()).isEqualTo(1001L);
    assertThat(details.clientIsLicensee()).isTrue();
    assertThat(details.timberMark()).isEqualTo("47/12K");
    assertThat(details.status()).isEqualTo("ACT");
  }

  @Test
  @DisplayName("verifyCutBlock returns clientIsLicensee=false for baseline fixture TFL001 when client is not licensee")
  void verifyCutBlock_baselineTfl001_unauthorizedClient_returnsFalseLicensee() {
    ExternalCutBlockDetails details =
        adapter.verifyCutBlock("TFL001", "CP1", "CB001", "99999999");

    assertThat(details.exists()).isTrue();
    assertThat(details.cbSkey()).isEqualTo(1001L);
    assertThat(details.clientIsLicensee()).isFalse();
    assertThat(details.timberMark()).isEqualTo("47/12K");
    assertThat(details.status()).isEqualTo("ACT");
  }

  @Test
  @DisplayName("verifyCutBlock returns valid details for baseline fixture TFL002 with null cuttingPermit")
  void verifyCutBlock_baselineTfl002_returnsValidDetails() {
    ExternalCutBlockDetails details =
        adapter.verifyCutBlock("TFL002", null, "CB002", "00010003");

    assertThat(details.exists()).isTrue();
    assertThat(details.cbSkey()).isEqualTo(1002L);
    assertThat(details.clientIsLicensee()).isTrue();
    assertThat(details.timberMark()).isEqualTo("47/12M");
    assertThat(details.status()).isEqualTo("ACT");
  }

  @Test
  @DisplayName("verifyCutBlock returns clientIsLicensee=false for baseline fixture TFL003/CP03/CB003 with non-licensee client")
  void verifyCutBlock_baselineTfl003_returnsUnauthorizedClient() {
    ExternalCutBlockDetails details =
        adapter.verifyCutBlock("TFL003", "CP03", "CB003", "00010003");

    assertThat(details.exists()).isTrue();
    assertThat(details.cbSkey()).isEqualTo(1003L);
    assertThat(details.clientIsLicensee()).isFalse();
    assertThat(details.timberMark()).isEqualTo("47/12N");
  }

  @Test
  @DisplayName("verifyCutBlock returns clientIsLicensee=true for baseline fixture TFL003/CP03/CB003 with licensee client")
  void verifyCutBlock_baselineTfl003_returnsAuthorizedClient() {
    ExternalCutBlockDetails details =
        adapter.verifyCutBlock("TFL003", "CP03", "CB003", "99999999");

    assertThat(details.exists()).isTrue();
    assertThat(details.cbSkey()).isEqualTo(1003L);
    assertThat(details.clientIsLicensee()).isTrue();
    assertThat(details.timberMark()).isEqualTo("47/12N");
  }

  @Test
  @DisplayName("verifyCutBlock returns not found for baseline fixture TFL999/CB999")
  void verifyCutBlock_baselineTfl999_returnsNotFound() {
    ExternalCutBlockDetails details =
        adapter.verifyCutBlock("TFL999", null, "CB999", "00010003");

    assertThat(details.exists()).isFalse();
    assertThat(details.cbSkey()).isNull();
    assertThat(details.clientIsLicensee()).isFalse();
  }

  @Test
  @DisplayName("verifyCutBlock returns not found for unconfigured tenure")
  void verifyCutBlock_unconfiguredTenure_returnsNotFound() {
    ExternalCutBlockDetails details =
        adapter.verifyCutBlock("RANDOM", "CP9", "BLK1", "12345678");

    assertThat(details.exists()).isFalse();
    assertThat(details.cbSkey()).isNull();
    assertThat(details.clientIsLicensee()).isFalse();
    assertThat(details.timberMark()).isNull();
    assertThat(details.status()).isNull();
  }

  @Test
  @DisplayName("verifyCutBlock resolves seed data from V999.3.0__seed_data.sql for TFL01/01 with authorized client")
  void verifyCutBlock_seedDataTfl01_authorizedClient() {
    ExternalCutBlockDetails details =
        adapter.verifyCutBlock("TFL01", null, "01", "00012797");

    assertThat(details.exists()).isTrue();
    assertThat(details.cbSkey()).isEqualTo(10000000L);
    assertThat(details.clientIsLicensee()).isTrue();
    assertThat(details.timberMark()).isEqualTo("47/12K");
    assertThat(details.status()).isEqualTo("HRS");
  }

  @Test
  @DisplayName("verifyCutBlock resolves seed data from V999.3.0__seed_data.sql for TFL01/01 with unauthorized client")
  void verifyCutBlock_seedDataTfl01_unauthorizedClient() {
    ExternalCutBlockDetails details =
        adapter.verifyCutBlock("TFL01", null, "01", "99999999");

    assertThat(details.exists()).isTrue();
    assertThat(details.cbSkey()).isEqualTo(10000000L);
    assertThat(details.clientIsLicensee()).isFalse();
    assertThat(details.timberMark()).isEqualTo("47/12K");
    assertThat(details.status()).isEqualTo("HRS");
  }

  @Test
  @DisplayName("verifyCutBlock resolves seed data with cutting permit from V999.3.0__seed_data.sql for TFL01/104/04")
  void verifyCutBlock_seedDataTfl01Permit104_authorizedClient() {
    ExternalCutBlockDetails details =
        adapter.verifyCutBlock("TFL01", "104", "04", "00146886");

    assertThat(details.exists()).isTrue();
    assertThat(details.cbSkey()).isEqualTo(10000003L);
    assertThat(details.clientIsLicensee()).isTrue();
    assertThat(details.timberMark()).isEqualTo("44/270");
    assertThat(details.status()).isEqualTo("EE");
  }

  @Test
  @DisplayName("getTenureSummary resolves seed data from V999.3.0__seed_data.sql for TFL01/01")
  void getTenureSummary_seedDataTfl01_returnsSummary() {
    Optional<ExternalTenureSummary> summaryOpt =
        adapter.getTenureSummary("TFL01", null, "01");

    assertThat(summaryOpt).isPresent();
    ExternalTenureSummary summary = summaryOpt.get();
    assertThat(summary.forestFileId()).isEqualTo("TFL01");
    assertThat(summary.cuttingPermitId()).isNull();
    assertThat(summary.cutBlockId()).isEqualTo("01");
    assertThat(summary.timberMark()).isEqualTo("47/12K");
    assertThat(summary.licenseeClientNumber()).isEqualTo("00012797");
    assertThat(summary.status()).isEqualTo("HRS");
    assertThat(summary.cbSkey()).isEqualTo(10000000L);
  }

  @Test
  @DisplayName("getTenureSummary resolves seed data with cutting permit from V999.3.0__seed_data.sql for TFL01/104/04")
  void getTenureSummary_seedDataTfl01Permit104_returnsSummary() {
    Optional<ExternalTenureSummary> summaryOpt =
        adapter.getTenureSummary("TFL01", "104", "04");

    assertThat(summaryOpt).isPresent();
    ExternalTenureSummary summary = summaryOpt.get();
    assertThat(summary.forestFileId()).isEqualTo("TFL01");
    assertThat(summary.cuttingPermitId()).isEqualTo("104");
    assertThat(summary.cutBlockId()).isEqualTo("04");
    assertThat(summary.timberMark()).isEqualTo("44/270");
    assertThat(summary.licenseeClientNumber()).isEqualTo("00146886");
    assertThat(summary.status()).isEqualTo("EE");
    assertThat(summary.cbSkey()).isEqualTo(10000003L);
  }

  // -------------------------------------------------------------------------
  // Custom Fixture Registration
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("registerFixture registers and retrieves custom details by components")
  void registerFixture_byComponents_retrievesRegisteredDetails() {
    ExternalCutBlockDetails custom = ExternalCutBlockDetails.of(5555L, true, "TM555", "S");
    adapter.registerFixture("TFL050", "CP5", "BLK5", custom);

    ExternalCutBlockDetails result = adapter.verifyCutBlock("TFL050", "CP5", "BLK5", "00012345");

    assertThat(result).isEqualTo(custom);
  }

  @Test
  @DisplayName("registerFixture with clientNumber gives priority to client-specific match")
  void registerFixture_withClientNumber_takesPrecedenceOverGeneral() {
    ExternalCutBlockDetails general = ExternalCutBlockDetails.of(1000L, false, "TM_GEN", "ACT");
    ExternalCutBlockDetails clientSpecific =
        ExternalCutBlockDetails.of(1000L, true, "TM_CLIENT", "ACT");

    adapter.registerFixture("TFL060", "CP6", "BLK6", general);
    adapter.registerFixture("TFL060", "CP6", "BLK6", "00099999", clientSpecific);

    ExternalCutBlockDetails matchClient =
        adapter.verifyCutBlock("TFL060", "CP6", "BLK6", "00099999");
    ExternalCutBlockDetails matchOther =
        adapter.verifyCutBlock("TFL060", "CP6", "BLK6", "00088888");

    assertThat(matchClient).isEqualTo(clientSpecific);
    assertThat(matchOther).isEqualTo(general);
  }

  @Test
  @DisplayName("registerFixture with raw key registers and returns details")
  void registerFixture_withRawKey_retrievesDetails() {
    String key = MockTenureAdapter.buildKey("TFL070", "CP7", "BLK7");
    ExternalCutBlockDetails custom = ExternalCutBlockDetails.of(7777L, true, "TM777", "LOG");

    adapter.registerFixture(key, custom);

    assertThat(adapter.verifyCutBlock("TFL070", "CP7", "BLK7", "11111111")).isEqualTo(custom);
  }

  @Test
  @DisplayName("registerNotFound registers cut block not found fixture")
  void registerNotFound_setsNotFoundDetails() {
    adapter.registerNotFound("TFL080", "CP8", "BLK8");

    ExternalCutBlockDetails details = adapter.verifyCutBlock("TFL080", "CP8", "BLK8", "12345678");

    assertThat(details.exists()).isFalse();
    assertThat(details.cbSkey()).isNull();
  }

  // -------------------------------------------------------------------------
  // Summary Fixtures Verification
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("getTenureSummary returns baseline summary for TFL001")
  void getTenureSummary_baselineTfl001_returnsSummary() {
    Optional<ExternalTenureSummary> summaryOpt =
        adapter.getTenureSummary("TFL001", "CP1", "CB001");

    assertThat(summaryOpt).isPresent();
    ExternalTenureSummary summary = summaryOpt.get();
    assertThat(summary.forestFileId()).isEqualTo("TFL001");
    assertThat(summary.cuttingPermitId()).isEqualTo("CP1");
    assertThat(summary.cutBlockId()).isEqualTo("CB001");
    assertThat(summary.timberMark()).isEqualTo("47/12K");
    assertThat(summary.licenseeClientNumber()).isEqualTo("00010003");
    assertThat(summary.cbSkey()).isEqualTo(1001L);
  }

  @Test
  @DisplayName("getTenureSummary returns empty when cut block is explicitly not found")
  void getTenureSummary_explicitlyNotFound_returnsEmpty() {
    Optional<ExternalTenureSummary> summaryOpt =
        adapter.getTenureSummary("TFL999", null, "CB999");

    assertThat(summaryOpt).isEmpty();
  }

  @Test
  @DisplayName("getTenureSummary returns empty when registered as summary not found")
  void getTenureSummary_registerSummaryNotFound_returnsEmpty() {
    adapter.registerSummaryNotFound("TFL001", "CP1", "CB001");

    Optional<ExternalTenureSummary> summaryOpt =
        adapter.getTenureSummary("TFL001", "CP1", "CB001");

    assertThat(summaryOpt).isEmpty();
  }

  @Test
  @DisplayName("registerSummaryFixture restores summary after registerSummaryNotFound")
  void registerSummaryFixture_afterRegisterSummaryNotFound_restoresSummary() {
    adapter.registerSummaryNotFound("TFL001", "CP1", "CB001");
    assertThat(adapter.getTenureSummary("TFL001", "CP1", "CB001")).isEmpty();

    ExternalTenureSummary restored =
        new ExternalTenureSummary("TFL001", "CP1", "CB001", "47/12K", "00010003", "ACT", 1001L);
    adapter.registerSummaryFixture("TFL001", "CP1", "CB001", restored);

    assertThat(adapter.getTenureSummary("TFL001", "CP1", "CB001")).contains(restored);
  }

  @Test
  @DisplayName("registerSummaryFixture with raw key restores summary after registerSummaryNotFound")
  void registerSummaryFixture_withRawKeyAfterRegisterSummaryNotFound_restoresSummary() {
    String key = MockTenureAdapter.buildKey("TFL002", null, "CB002");
    adapter.registerSummaryNotFound("TFL002", null, "CB002");
    assertThat(adapter.getTenureSummary("TFL002", null, "CB002")).isEmpty();

    ExternalTenureSummary restored =
        new ExternalTenureSummary("TFL002", null, "CB002", "47/12M", "00010003", "ACT", 1002L);
    adapter.registerSummaryFixture(key, restored);

    assertThat(adapter.getTenureSummary("TFL002", null, "CB002")).contains(restored);
  }

  @Test
  @DisplayName("getTenureSummary returns custom summary when registered by components")
  void getTenureSummary_customRegistered_returnsCustomSummary() {
    ExternalTenureSummary custom =
        new ExternalTenureSummary("TFL040", "CP4", "BLK4", "TM4", "00044444", "ACT", 4004L);
    adapter.registerSummaryFixture("TFL040", "CP4", "BLK4", custom);

    Optional<ExternalTenureSummary> summaryOpt =
        adapter.getTenureSummary("TFL040", "CP4", "BLK4");

    assertThat(summaryOpt).contains(custom);
  }

  @Test
  @DisplayName("getTenureSummary returns custom summary when registered with raw key")
  void getTenureSummary_customRegisteredWithRawKey_returnsCustomSummary() {
    String key = MockTenureAdapter.buildKey("TFL040", "CP4", "BLK4");
    ExternalTenureSummary custom =
        new ExternalTenureSummary("TFL040", "CP4", "BLK4", "TM4", "00044444", "ACT", 4004L);
    adapter.registerSummaryFixture(key, custom);

    Optional<ExternalTenureSummary> summaryOpt =
        adapter.getTenureSummary("TFL040", "CP4", "BLK4");

    assertThat(summaryOpt).contains(custom);
  }

  @Test
  @DisplayName("getTenureSummary returns empty for unregistered tenure")
  void getTenureSummary_unregistered_returnsEmpty() {
    Optional<ExternalTenureSummary> summaryOpt =
        adapter.getTenureSummary("UNREG", "CP0", "B0");

    assertThat(summaryOpt).isEmpty();
  }

  // -------------------------------------------------------------------------
  // Lifecycle & Fixture Management
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("clearFixtures removes all fixtures so baseline cut blocks return not found")
  void clearFixtures_removesAllFixtures() {
    adapter.clearFixtures();

    // Baseline TFL001 should now return not found
    ExternalCutBlockDetails details =
        adapter.verifyCutBlock("TFL001", "CP1", "CB001", "00010003");

    assertThat(details.exists()).isFalse();
  }

  @Test
  @DisplayName("reset restores baseline fixtures")
  void reset_restoresBaselineFixtures() {
    adapter.clearFixtures();
    adapter.reset();

    ExternalCutBlockDetails details =
        adapter.verifyCutBlock("TFL001", "CP1", "CB001", "00010003");

    assertThat(details.cbSkey()).isEqualTo(1001L);
    assertThat(details.exists()).isTrue();
  }

  // -------------------------------------------------------------------------
  // Key Generation
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("buildKey normalizes whitespace and nulls")
  void buildKey_normalizesCorrectly() {
    assertThat(MockTenureAdapter.buildKey(" TFL001 ", " CP1 ", " CB001 "))
        .isEqualTo("TFL001:CP1:CB001");
    assertThat(MockTenureAdapter.buildKey("TFL002", null, "CB002"))
        .isEqualTo("TFL002::CB002");
    assertThat(MockTenureAdapter.buildKey("TFL001", "CP1", "CB001", " 00012345 "))
        .isEqualTo("TFL001:CP1:CB001:00012345");
    assertThat(MockTenureAdapter.buildKey("TFL001", "CP1", "CB001", null))
        .isEqualTo("TFL001:CP1:CB001:");
  }
}

