package ca.bc.gov.restapi.results.oracle.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.dto.ExternalCutBlockDetails;
import ca.bc.gov.restapi.results.common.dto.ExternalTenureSummary;
import ca.bc.gov.restapi.results.common.exception.ExternalServiceErrorCode;
import ca.bc.gov.restapi.results.common.exception.ExternalServiceException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 * Unit tests for {@link OracleTenureJdbcAdapter} covering SQL parameter binding, row mapping
 * (including null-safe numeric keys and blank Oracle strings), short-circuit validation, and error
 * translation to {@link ExternalServiceException}.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | OracleTenureJdbcAdapter")
class OracleTenureJdbcAdapterTest {

  private static final String FOREST_FILE_ID = "A72929";
  private static final String CUTTING_PERMIT_ID = "001";
  private static final String CUT_BLOCK_ID = "028";
  private static final String CLIENT_NUMBER = "00010006";
  private static final String TIMBER_MARK = "AY5001";
  private static final String STATUS_S = "S";
  private static final String SERVICE_FTA = "FTA";
  private static final String COL_CB_SKEY = "CB_SKEY";
  private static final String COL_IS_LICENSEE = "IS_LICENSEE";
  private static final String COL_TIMBER_MARK = "TIMBER_MARK";
  private static final String COL_BLOCK_STATUS_ST = "BLOCK_STATUS_ST";
  private static final String COL_FOREST_FILE_ID = "FOREST_FILE_ID";
  private static final String COL_CUTTING_PERMIT_ID = "CUTTING_PERMIT_ID";
  private static final String COL_CUT_BLOCK_ID = "CUT_BLOCK_ID";
  private static final String COL_LICENSEE_CLIENT_NUMBER = "LICENSEE_CLIENT_NUMBER";
  private static final String PARAM_FOREST_FILE_ID = "forestFileId";
  private static final String PARAM_CUTTING_PERMIT_ID = "cuttingPermitId";
  private static final String PARAM_CUT_BLOCK_ID = "cutBlockId";
  private static final String PARAM_CLIENT_NUMBER = "clientNumber";

  @Mock private NamedParameterJdbcTemplate externalOracleJdbcTemplate;

  private OracleTenureJdbcAdapter adapter;

  @BeforeEach
  void setUp() {
    adapter = new OracleTenureJdbcAdapter(externalOracleJdbcTemplate);
  }

  @Test
  @DisplayName("verifyCutBlock returns verified authorized details and binds trimmed parameters")
  @SuppressWarnings("unchecked")
  void verifyCutBlock_whenAuthorizedClientFound_returnsVerifiedDetailsAndBindsParameters() {
    when(externalOracleJdbcTemplate.queryForObject(
            anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
        .thenAnswer(
            invocation -> {
              RowMapper<ExternalCutBlockDetails> mapper = invocation.getArgument(2);
              ResultSet rs = mockCutBlockResultSet(43589L, false, 1, false, TIMBER_MARK, STATUS_S);
              return mapper.mapRow(rs, 0);
            });

    ExternalCutBlockDetails result =
        adapter.verifyCutBlock(
            " " + FOREST_FILE_ID + " ",
            " " + CUTTING_PERMIT_ID + " ",
            " " + CUT_BLOCK_ID + " ",
            " " + CLIENT_NUMBER + " ");

    assertThat(result.exists()).isTrue();
    assertThat(result.cbSkey()).isEqualTo(43589L);
    assertThat(result.clientIsLicensee()).isTrue();
    assertThat(result.timberMark()).isEqualTo(TIMBER_MARK);
    assertThat(result.status()).isEqualTo(STATUS_S);

    ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<SqlParameterSource> paramCaptor =
        ArgumentCaptor.forClass(SqlParameterSource.class);
    verify(externalOracleJdbcTemplate)
        .queryForObject(sqlCaptor.capture(), paramCaptor.capture(), any(RowMapper.class));

    assertThat(sqlCaptor.getValue())
        .contains("FROM THE.CUT_BLOCK cb")
        .contains("FROM THE.CUT_BLOCK_CLIENT cbc")
        .contains("EXISTS (");

    MapSqlParameterSource params = (MapSqlParameterSource) paramCaptor.getValue();
    assertThat(params.getValue(PARAM_FOREST_FILE_ID)).isEqualTo(FOREST_FILE_ID);
    assertThat(params.getValue(PARAM_CUTTING_PERMIT_ID)).isEqualTo(CUTTING_PERMIT_ID);
    assertThat(params.getValue(PARAM_CUT_BLOCK_ID)).isEqualTo(CUT_BLOCK_ID);
    assertThat(params.getValue(PARAM_CLIENT_NUMBER)).isEqualTo(CLIENT_NUMBER);
    assertThat(params.getSqlType(PARAM_FOREST_FILE_ID)).isEqualTo(Types.VARCHAR);
    assertThat(params.getSqlType(PARAM_CUTTING_PERMIT_ID)).isEqualTo(Types.VARCHAR);
    assertThat(params.getSqlType(PARAM_CUT_BLOCK_ID)).isEqualTo(Types.VARCHAR);
    assertThat(params.getSqlType(PARAM_CLIENT_NUMBER)).isEqualTo(Types.VARCHAR);
  }

  @Test
  @DisplayName("verifyCutBlock sets clientIsLicensee false and trims whitespace when unauthorized")
  @SuppressWarnings("unchecked")
  void verifyCutBlock_whenUnauthorizedClient_returnsClientIsLicenseeFalse() {
    when(externalOracleJdbcTemplate.queryForObject(
            anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
        .thenAnswer(
            invocation -> {
              RowMapper<ExternalCutBlockDetails> mapper = invocation.getArgument(2);
              ResultSet rs =
                  mockCutBlockResultSet(
                      5321L, false, 0, false, " 77623 ", " " + STATUS_S + " ");
              return mapper.mapRow(rs, 0);
            });

    ExternalCutBlockDetails result =
        adapter.verifyCutBlock("A77623", "   ", "2", null);

    assertThat(result.exists()).isTrue();
    assertThat(result.cbSkey()).isEqualTo(5321L);
    assertThat(result.clientIsLicensee()).isFalse();
    assertThat(result.timberMark()).isEqualTo("77623");
    assertThat(result.status()).isEqualTo(STATUS_S);

    ArgumentCaptor<SqlParameterSource> paramCaptor =
        ArgumentCaptor.forClass(SqlParameterSource.class);
    verify(externalOracleJdbcTemplate)
        .queryForObject(anyString(), paramCaptor.capture(), any(RowMapper.class));
    MapSqlParameterSource params = (MapSqlParameterSource) paramCaptor.getValue();
    assertThat(params.getValue(PARAM_CUTTING_PERMIT_ID)).isNull();
    assertThat(params.getValue(PARAM_CLIENT_NUMBER)).isNull();
  }

  @Test
  @DisplayName("verifyCutBlock handles null CB_SKEY, IS_LICENSEE, TIMBER_MARK, and status safely")
  @SuppressWarnings("unchecked")
  void verifyCutBlock_whenNullableColumnsNull_mapsSafely() {
    when(externalOracleJdbcTemplate.queryForObject(
            anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
        .thenAnswer(
            invocation -> {
              RowMapper<ExternalCutBlockDetails> mapper = invocation.getArgument(2);
              ResultSet rs = mockCutBlockResultSet(0L, true, 0, true, "   ", null);
              return mapper.mapRow(rs, 0);
            });

    ExternalCutBlockDetails result =
        adapter.verifyCutBlock(FOREST_FILE_ID, null, CUT_BLOCK_ID, CLIENT_NUMBER);

    assertThat(result.exists()).isTrue();
    assertThat(result.cbSkey()).isNull();
    assertThat(result.clientIsLicensee()).isFalse();
    assertThat(result.timberMark()).isNull();
    assertThat(result.status()).isNull();
  }

  @Test
  @DisplayName("verifyCutBlock returns notFound when query yields no row")
  @SuppressWarnings("unchecked")
  void verifyCutBlock_whenNotFound_returnsNotFound() {
    when(externalOracleJdbcTemplate.queryForObject(
            anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
        .thenThrow(new EmptyResultDataAccessException(1));

    ExternalCutBlockDetails result =
        adapter.verifyCutBlock(FOREST_FILE_ID, CUTTING_PERMIT_ID, CUT_BLOCK_ID, CLIENT_NUMBER);

    assertThat(result).isEqualTo(ExternalCutBlockDetails.notFound());
  }

  @ParameterizedTest
  @MethodSource("blankIdentifierPairs")
  @DisplayName("verifyCutBlock short-circuits without querying when required identifiers are blank")
  void verifyCutBlock_whenRequiredIdentifiersBlank_returnsNotFoundWithoutQuery(
      String forestFileId, String cutBlockId) {
    ExternalCutBlockDetails result =
        adapter.verifyCutBlock(forestFileId, CUTTING_PERMIT_ID, cutBlockId, CLIENT_NUMBER);

    assertThat(result).isEqualTo(ExternalCutBlockDetails.notFound());
    verifyNoInteractions(externalOracleJdbcTemplate);
  }

  @Test
  @DisplayName("verifyCutBlock translates QueryTimeoutException into 503 EXTERNAL_SERVICE_TIMEOUT")
  @SuppressWarnings("unchecked")
  void verifyCutBlock_whenTimeout_throwsExternalServiceTimeout() {
    when(externalOracleJdbcTemplate.queryForObject(
            anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
        .thenThrow(new QueryTimeoutException("query timeout"));

    assertThatThrownBy(
            () ->
                adapter.verifyCutBlock(
                    FOREST_FILE_ID, CUTTING_PERMIT_ID, CUT_BLOCK_ID, CLIENT_NUMBER))
        .isInstanceOf(ExternalServiceException.class)
        .satisfies(
            ex -> {
              ExternalServiceException ese = (ExternalServiceException) ex;
              assertThat(ese.getErrorCode())
                  .isEqualTo(ExternalServiceErrorCode.EXTERNAL_SERVICE_TIMEOUT);
              assertThat(ese.getServiceName()).isEqualTo(SERVICE_FTA);
              assertThat(ese.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
            });
  }

  @Test
  @DisplayName(
      "verifyCutBlock translates DataAccessException into 503 EXTERNAL_SERVICE_UNAVAILABLE")
  @SuppressWarnings("unchecked")
  void verifyCutBlock_whenDataAccessFails_throwsExternalServiceUnavailable() {
    when(externalOracleJdbcTemplate.queryForObject(
            anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
        .thenThrow(new CannotGetJdbcConnectionException("oracle unreachable"));

    assertThatThrownBy(
            () ->
                adapter.verifyCutBlock(
                    FOREST_FILE_ID, CUTTING_PERMIT_ID, CUT_BLOCK_ID, CLIENT_NUMBER))
        .isInstanceOf(ExternalServiceException.class)
        .satisfies(
            ex -> {
              ExternalServiceException ese = (ExternalServiceException) ex;
              assertThat(ese.getErrorCode())
                  .isEqualTo(ExternalServiceErrorCode.EXTERNAL_SERVICE_UNAVAILABLE);
              assertThat(ese.getServiceName()).isEqualTo(SERVICE_FTA);
              assertThat(ese.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
            });
  }

  @Test
  @DisplayName("getTenureSummary returns null-safe mapped summary and avoids row-duplicating join")
  @SuppressWarnings("unchecked")
  void getTenureSummary_whenFound_returnsPopulatedSummary() {
    when(externalOracleJdbcTemplate.queryForObject(
            anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
        .thenAnswer(
            invocation -> {
              RowMapper<ExternalTenureSummary> mapper = invocation.getArgument(2);
              ResultSet rs =
                  mockSummaryResultSet(
                      "A77623", " ", "2", "77623", "00010008", STATUS_S, 5321L, false);
              return mapper.mapRow(rs, 0);
            });

    Optional<ExternalTenureSummary> result = adapter.getTenureSummary("A77623", null, "2");

    assertThat(result).isPresent();
    ExternalTenureSummary summary = result.orElseThrow();
    assertThat(summary.forestFileId()).isEqualTo("A77623");
    assertThat(summary.cuttingPermitId()).isNull();
    assertThat(summary.cutBlockId()).isEqualTo("2");
    assertThat(summary.timberMark()).isEqualTo("77623");
    assertThat(summary.licenseeClientNumber()).isEqualTo("00010008");
    assertThat(summary.status()).isEqualTo(STATUS_S);
    assertThat(summary.cbSkey()).isEqualTo(5321L);

    ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
    verify(externalOracleJdbcTemplate)
        .queryForObject(sqlCaptor.capture(), any(SqlParameterSource.class), any(RowMapper.class));
    assertThat(sqlCaptor.getValue())
        .contains("FROM THE.CUT_BLOCK cb")
        .contains("THE.CUT_BLOCK_CLIENT")
        .contains("CUT_BLOCK_CLIENT_TYPE_CODE IN ('R', 'O')")
        .contains("THE.FOREST_FILE_CLIENT")
        .contains("FOREST_FILE_CLIENT_TYPE_CODE = 'A'")
        .contains("DENSE_RANK FIRST")
        .doesNotContain("WHEN 'L'");
  }

  @Test
  @DisplayName("getTenureSummary handles null CB_SKEY and missing licensee client")
  @SuppressWarnings("unchecked")
  void getTenureSummary_whenNullableFieldsNull_mapsNullFields() {
    when(externalOracleJdbcTemplate.queryForObject(
            anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
        .thenAnswer(
            invocation -> {
              RowMapper<ExternalTenureSummary> mapper = invocation.getArgument(2);
              ResultSet rs =
                  mockSummaryResultSet(
                      FOREST_FILE_ID,
                      CUTTING_PERMIT_ID,
                      CUT_BLOCK_ID,
                      null,
                      null,
                      null,
                      0L,
                      true);
              return mapper.mapRow(rs, 0);
            });

    Optional<ExternalTenureSummary> result =
        adapter.getTenureSummary(FOREST_FILE_ID, CUTTING_PERMIT_ID, CUT_BLOCK_ID);

    assertThat(result).isPresent();
    ExternalTenureSummary summary = result.orElseThrow();
    assertThat(summary.cbSkey()).isNull();
    assertThat(summary.licenseeClientNumber()).isNull();
    assertThat(summary.timberMark()).isNull();
    assertThat(summary.status()).isNull();
    assertThat(summary.cuttingPermitId()).isEqualTo(CUTTING_PERMIT_ID);
  }

  @Test
  @DisplayName("getTenureSummary returns empty when cut block is not found")
  @SuppressWarnings("unchecked")
  void getTenureSummary_whenNotFound_returnsEmpty() {
    when(externalOracleJdbcTemplate.queryForObject(
            anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
        .thenThrow(new EmptyResultDataAccessException(1));

    Optional<ExternalTenureSummary> result =
        adapter.getTenureSummary(FOREST_FILE_ID, CUTTING_PERMIT_ID, CUT_BLOCK_ID);

    assertThat(result).isEmpty();
  }

  @ParameterizedTest
  @MethodSource("blankIdentifierPairs")
  @DisplayName("getTenureSummary short-circuits without querying when required identifiers blank")
  void getTenureSummary_whenRequiredIdentifiersBlank_returnsEmptyWithoutQuery(
      String forestFileId, String cutBlockId) {
    Optional<ExternalTenureSummary> result =
        adapter.getTenureSummary(forestFileId, CUTTING_PERMIT_ID, cutBlockId);

    assertThat(result).isEmpty();
    verifyNoInteractions(externalOracleJdbcTemplate);
  }

  @Test
  @DisplayName(
      "getTenureSummary translates QueryTimeoutException into 503 EXTERNAL_SERVICE_TIMEOUT")
  @SuppressWarnings("unchecked")
  void getTenureSummary_whenTimeout_throwsExternalServiceTimeout() {
    when(externalOracleJdbcTemplate.queryForObject(
            anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
        .thenThrow(new QueryTimeoutException("query timeout"));

    assertThatThrownBy(
            () -> adapter.getTenureSummary(FOREST_FILE_ID, CUTTING_PERMIT_ID, CUT_BLOCK_ID))
        .isInstanceOf(ExternalServiceException.class)
        .satisfies(
            ex -> {
              ExternalServiceException ese = (ExternalServiceException) ex;
              assertThat(ese.getErrorCode())
                  .isEqualTo(ExternalServiceErrorCode.EXTERNAL_SERVICE_TIMEOUT);
              assertThat(ese.getServiceName()).isEqualTo(SERVICE_FTA);
              assertThat(ese.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
            });
  }

  @Test
  @DisplayName(
      "getTenureSummary translates DataAccessException into 503 EXTERNAL_SERVICE_UNAVAILABLE")
  @SuppressWarnings("unchecked")
  void getTenureSummary_whenDataAccessFails_throwsExternalServiceUnavailable() {
    when(externalOracleJdbcTemplate.queryForObject(
            anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
        .thenThrow(new CannotGetJdbcConnectionException("oracle unreachable"));

    assertThatThrownBy(
            () -> adapter.getTenureSummary(FOREST_FILE_ID, CUTTING_PERMIT_ID, CUT_BLOCK_ID))
        .isInstanceOf(ExternalServiceException.class)
        .satisfies(
            ex -> {
              ExternalServiceException ese = (ExternalServiceException) ex;
              assertThat(ese.getErrorCode())
                  .isEqualTo(ExternalServiceErrorCode.EXTERNAL_SERVICE_UNAVAILABLE);
              assertThat(ese.getServiceName()).isEqualTo(SERVICE_FTA);
              assertThat(ese.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
            });
  }

  private static Stream<Arguments> blankIdentifierPairs() {
    return Stream.of(
        Arguments.of(null, CUT_BLOCK_ID),
        Arguments.of("   ", CUT_BLOCK_ID),
        Arguments.of(FOREST_FILE_ID, null),
        Arguments.of(FOREST_FILE_ID, " "));
  }

  private ResultSet mockCutBlockResultSet(
      long cbSkey,
      boolean cbSkeyWasNull,
      int isLicensee,
      boolean isLicenseeWasNull,
      String timberMark,
      String status)
      throws SQLException {
    ResultSet rs = mock(ResultSet.class);
    when(rs.getLong(COL_CB_SKEY)).thenReturn(cbSkey);
    when(rs.wasNull()).thenReturn(cbSkeyWasNull, isLicenseeWasNull);
    when(rs.getInt(COL_IS_LICENSEE)).thenReturn(isLicensee);
    when(rs.getString(COL_TIMBER_MARK)).thenReturn(timberMark);
    when(rs.getString(COL_BLOCK_STATUS_ST)).thenReturn(status);
    return rs;
  }

  private ResultSet mockSummaryResultSet(
      String forestFileId,
      String cuttingPermitId,
      String cutBlockId,
      String timberMark,
      String licenseeClientNumber,
      String status,
      long cbSkey,
      boolean cbSkeyWasNull)
      throws SQLException {
    ResultSet rs = mock(ResultSet.class);
    when(rs.getString(COL_FOREST_FILE_ID)).thenReturn(forestFileId);
    when(rs.getString(COL_CUTTING_PERMIT_ID)).thenReturn(cuttingPermitId);
    when(rs.getString(COL_CUT_BLOCK_ID)).thenReturn(cutBlockId);
    when(rs.getString(COL_TIMBER_MARK)).thenReturn(timberMark);
    when(rs.getString(COL_LICENSEE_CLIENT_NUMBER)).thenReturn(licenseeClientNumber);
    when(rs.getString(COL_BLOCK_STATUS_ST)).thenReturn(status);
    when(rs.getLong(COL_CB_SKEY)).thenReturn(cbSkey);
    when(rs.wasNull()).thenReturn(cbSkeyWasNull);
    return rs;
  }
}

