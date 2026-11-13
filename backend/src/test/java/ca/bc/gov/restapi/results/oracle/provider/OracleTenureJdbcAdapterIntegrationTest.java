package ca.bc.gov.restapi.results.oracle.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ca.bc.gov.restapi.results.common.dto.ExternalCutBlockDetails;
import ca.bc.gov.restapi.results.common.dto.ExternalTenureSummary;
import ca.bc.gov.restapi.results.common.exception.ExternalServiceErrorCode;
import ca.bc.gov.restapi.results.common.exception.ExternalServiceException;
import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * Integration tests verifying {@link OracleTenureJdbcAdapter} SQL execution against real Oracle
 * {@code THE.CUT_BLOCK}, {@code THE.CUT_BLOCK_CLIENT}, and {@code THE.FOREST_FILE_CLIENT} tables
 * provisioned in TestContainers.
 */
@EnabledIfSystemProperty(named = "server.primary-db", matches = "oracle")
@DisplayName("Integration Test | OracleTenureJdbcAdapter")
class OracleTenureJdbcAdapterIntegrationTest extends AbstractTestContainerIntegrationTest {

  private static final String TEST_PROPERTY_SOURCE = "oracle-tenure-adapter-test-override";
  private static final String EXTERNAL_ORACLE_ENABLED_ENV = "EXTERNAL_ORACLE_ENABLED";
  private static final long TEST_CB_SKEY_NO_CLIENTS = 189922L;
  private static final List<Long> TEMP_CLIENT_SKEYS = List.of(9990001L, 9990002L, 9990003L);

  @Autowired private OracleTenureJdbcAdapter adapter;

  @Autowired
  @Qualifier("externalOracleJdbcTemplate")
  private NamedParameterJdbcTemplate externalOracleJdbcTemplate;

  @Autowired private ConfigurableEnvironment environment;

  @BeforeEach
  void configureTenureTestEnvironment() {
    setExternalOracleEnabled("true");
    cleanupSyntheticClients();
  }

  @AfterEach
  void tearDown() {
    setExternalOracleEnabled("true");
    cleanupSyntheticClients();
    environment.getPropertySources().remove(TEST_PROPERTY_SOURCE);
  }

  @Test
  @DisplayName("verifyCutBlock returns verified details for authorized licensee in THE.CUT_BLOCK")
  void verifyCutBlock_whenAuthorizedClientExists_returnsVerifiedDetails() {
    ExternalCutBlockDetails details = adapter.verifyCutBlock("A72929", "001", "028", "00010006");

    assertThat(details.exists()).isTrue();
    assertThat(details.cbSkey()).isEqualTo(43589L);
    assertThat(details.clientIsLicensee()).isTrue();
    assertThat(details.timberMark()).isEqualTo("AY5001");
    assertThat(details.status()).isEqualTo("S");
  }

  @Test
  @DisplayName("verifyCutBlock sets clientIsLicensee false when client is not on CUT_BLOCK_CLIENT")
  void verifyCutBlock_whenUnauthorizedClient_returnsClientIsLicenseeFalse() {
    ExternalCutBlockDetails details = adapter.verifyCutBlock("A72929", "001", "028", "99999999");

    assertThat(details.exists()).isTrue();
    assertThat(details.cbSkey()).isEqualTo(43589L);
    assertThat(details.clientIsLicensee()).isFalse();
    assertThat(details.timberMark()).isEqualTo("AY5001");
    assertThat(details.status()).isEqualTo("S");
  }

  @Test
  @DisplayName("verifyCutBlock matches legacy single-space CUTTING_PERMIT_ID when input is null")
  void verifyCutBlock_whenLegacySpaceCuttingPermitAndNullInput_matchesRow() {
    ExternalCutBlockDetails details = adapter.verifyCutBlock("A77623", null, "2", "00010008");

    assertThat(details.exists()).isTrue();
    assertThat(details.cbSkey()).isEqualTo(5321L);
    assertThat(details.clientIsLicensee()).isTrue();
    assertThat(details.timberMark()).isEqualTo("77623");
    assertThat(details.status()).isEqualTo("S");
  }

  @Test
  @DisplayName("verifyCutBlock returns notFound when cut block does not exist")
  void verifyCutBlock_whenCutBlockMissing_returnsNotFound() {
    ExternalCutBlockDetails details = adapter.verifyCutBlock("NONEXIST", "001", "999", "00010006");

    assertThat(details).isEqualTo(ExternalCutBlockDetails.notFound());
  }

  @Test
  @DisplayName(
      "getTenureSummary falls back to FOREST_FILE_CLIENT type A when cut block has no R/O clients")
  void getTenureSummary_whenNoCutBlockClientsLinked_fallsBackToForestFileClientA() {
    Optional<ExternalTenureSummary> result = adapter.getTenureSummary("TFL47", "12K", "12-11");

    assertThat(result)
        .isPresent()
        .get()
        .satisfies(
            summary -> {
              assertThat(summary.forestFileId()).isEqualTo("TFL47");
              assertThat(summary.cuttingPermitId()).isEqualTo("12K");
              assertThat(summary.cutBlockId()).isEqualTo("12-11");
              assertThat(summary.timberMark()).isEqualTo("47/12K");
              assertThat(summary.licenseeClientNumber()).isEqualTo("00010003");
              assertThat(summary.status()).isEqualTo("S");
              assertThat(summary.cbSkey()).isEqualTo(TEST_CB_SKEY_NO_CLIENTS);
            });
  }

  @Test
  @DisplayName(
      "Multiple CUT_BLOCK_CLIENT rows do not duplicate results and summary prioritizes R > O > A")
  void multipleClients_doNotDuplicateRowsAndSummaryPrioritizesResponsibleClient() {
    insertSyntheticClient(9990001L, TEST_CB_SKEY_NO_CLIENTS, "L", "00010001");

    ExternalCutBlockDetails authorized =
        adapter.verifyCutBlock("TFL47", "12K", "12-11", "00010001");
    assertThat(authorized.exists()).isTrue();
    assertThat(authorized.cbSkey()).isEqualTo(TEST_CB_SKEY_NO_CLIENTS);
    assertThat(authorized.clientIsLicensee()).isTrue();

    ExternalCutBlockDetails unauthorized =
        adapter.verifyCutBlock("TFL47", "12K", "12-11", "99999999");
    assertThat(unauthorized.exists()).isTrue();
    assertThat(unauthorized.clientIsLicensee()).isFalse();

    // Type 'L' is ignored by getTenureSummary, so summary still resolves FOREST_FILE_CLIENT 'A'.
    Optional<ExternalTenureSummary> fallbackSummary =
        adapter.getTenureSummary("TFL47", "12K", "12-11");
    assertThat(fallbackSummary)
        .isPresent()
        .get()
        .satisfies(item -> assertThat(item.licenseeClientNumber()).isEqualTo("00010003"));

    insertSyntheticClient(9990002L, TEST_CB_SKEY_NO_CLIENTS, "O", "00010002");
    Optional<ExternalTenureSummary> obligationSummary =
        adapter.getTenureSummary("TFL47", "12K", "12-11");
    assertThat(obligationSummary)
        .isPresent()
        .get()
        .satisfies(item -> assertThat(item.licenseeClientNumber()).isEqualTo("00010002"));

    insertSyntheticClient(9990003L, TEST_CB_SKEY_NO_CLIENTS, "R", "00010004");
    Optional<ExternalTenureSummary> responsibleSummary =
        adapter.getTenureSummary("TFL47", "12K", "12-11");
    assertThat(responsibleSummary)
        .isPresent()
        .get()
        .satisfies(
            item -> {
              assertThat(item.cbSkey()).isEqualTo(TEST_CB_SKEY_NO_CLIENTS);
              assertThat(item.licenseeClientNumber()).isEqualTo("00010004");
            });
  }

  @Test
  @DisplayName("Fails fast with 503 ExternalServiceException when EXTERNAL_ORACLE_ENABLED is false")
  void verifyCutBlock_whenExternalOracleDisabledAtRuntime_throwsExternalServiceUnavailable() {
    setExternalOracleEnabled("false");

    assertThatThrownBy(() -> adapter.verifyCutBlock("A72929", "001", "028", "00010006"))
        .isInstanceOf(ExternalServiceException.class)
        .satisfies(
            ex -> {
              ExternalServiceException ese = (ExternalServiceException) ex;
              assertThat(ese.getErrorCode())
                  .isEqualTo(ExternalServiceErrorCode.EXTERNAL_SERVICE_UNAVAILABLE);
              assertThat(ese.getServiceName()).isEqualTo("FTA");
              assertThat(ese.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
            });
  }

  private void setExternalOracleEnabled(String value) {
    environment.getPropertySources().remove(TEST_PROPERTY_SOURCE);
    environment
        .getPropertySources()
        .addFirst(
            new MapPropertySource(
                TEST_PROPERTY_SOURCE, Map.of(EXTERNAL_ORACLE_ENABLED_ENV, value)));
  }

  private void insertSyntheticClient(
      long cblkClientSkey, long cbSkey, String clientTypeCode, String clientNumber) {
    String sql =
        """
        INSERT INTO THE.CUT_BLOCK_CLIENT (
            CBLK_CLIENT_SKEY,
            CB_SKEY,
            CUT_BLOCK_CLIENT_TYPE_CODE,
            LICENSEE_START_DATE,
            CLIENT_LOCN_CODE,
            CLIENT_NUMBER,
            REVISION_COUNT,
            ENTRY_USERID,
            ENTRY_TIMESTAMP,
            UPDATE_USERID,
            UPDATE_TIMESTAMP
        ) VALUES (
            :skey,
            :cbSkey,
            :typeCode,
            SYSDATE,
            '00',
            :clientNumber,
            1,
            'TEST_USER',
            SYSDATE,
            'TEST_USER',
            SYSDATE
        )
        """;
    externalOracleJdbcTemplate.update(
        sql,
        Map.of(
            "skey", cblkClientSkey,
            "cbSkey", cbSkey,
            "typeCode", clientTypeCode,
            "clientNumber", clientNumber));
  }

  private void cleanupSyntheticClients() {
    String sql = "DELETE FROM THE.CUT_BLOCK_CLIENT WHERE CBLK_CLIENT_SKEY IN (:skeys)";
    externalOracleJdbcTemplate.update(sql, Map.of("skeys", TEMP_CLIENT_SKEYS));
  }
}
