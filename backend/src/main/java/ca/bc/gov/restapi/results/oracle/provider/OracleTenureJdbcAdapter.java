package ca.bc.gov.restapi.results.oracle.provider;

import ca.bc.gov.restapi.results.common.dto.ExternalCutBlockDetails;
import ca.bc.gov.restapi.results.common.dto.ExternalTenureSummary;
import ca.bc.gov.restapi.results.common.exception.ExternalServiceErrorCode;
import ca.bc.gov.restapi.results.common.exception.ExternalServiceException;
import ca.bc.gov.restapi.results.common.provider.TenureProvider;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Live Oracle JDBC adapter implementing {@link TenureProvider} against legacy Forest Tenures
 * Administration (FTA) tables ({@code THE.CUT_BLOCK}, {@code THE.CUT_BLOCK_CLIENT}, and {@code
 * THE.FOREST_FILE_CLIENT}).
 *
 * <p>Uses the read-only {@code externalOracleJdbcTemplate} configured with an execution timeout so
 * both Oracle-primary and Postgres-primary deployments can query external FTA data through the
 * Anti-Corruption Layer without JPA entity coupling.
 */
@Slf4j
@Component("oracleTenureJdbcAdapter")
public class OracleTenureJdbcAdapter implements TenureProvider {

  private static final String SERVICE_NAME = "FTA";
  private static final String PARAM_FOREST_FILE_ID = "forestFileId";
  private static final String PARAM_CUTTING_PERMIT_ID = "cuttingPermitId";
  private static final String PARAM_CUT_BLOCK_ID = "cutBlockId";
  private static final String PARAM_CLIENT_NUMBER = "clientNumber";

  private static final String COL_CB_SKEY = "CB_SKEY";
  private static final String COL_FOREST_FILE_ID = "FOREST_FILE_ID";
  private static final String COL_CUTTING_PERMIT_ID = "CUTTING_PERMIT_ID";
  private static final String COL_CUT_BLOCK_ID = "CUT_BLOCK_ID";
  private static final String COL_TIMBER_MARK = "TIMBER_MARK";
  private static final String COL_BLOCK_STATUS_ST = "BLOCK_STATUS_ST";
  private static final String COL_IS_LICENSEE = "IS_LICENSEE";
  private static final String COL_LICENSEE_CLIENT_NUMBER = "LICENSEE_CLIENT_NUMBER";

  private static final String SQL_VERIFY_CUT_BLOCK =
      """
      SELECT
        cb.CB_SKEY,
        cb.TIMBER_MARK,
        cb.BLOCK_STATUS_ST,
        CASE WHEN EXISTS (
          SELECT 1
          FROM THE.CUT_BLOCK_CLIENT cbc
          WHERE cbc.CB_SKEY = cb.CB_SKEY
            AND cbc.CLIENT_NUMBER = :clientNumber
        ) THEN 1 ELSE 0 END AS IS_LICENSEE
      FROM THE.CUT_BLOCK cb
      WHERE cb.FOREST_FILE_ID = :forestFileId
        AND cb.CUT_BLOCK_ID = :cutBlockId
        AND (
          (:cuttingPermitId IS NULL AND TRIM(cb.CUTTING_PERMIT_ID) IS NULL)
          OR TRIM(cb.CUTTING_PERMIT_ID) = :cuttingPermitId
        )
      ORDER BY cb.CB_SKEY
      FETCH FIRST 1 ROWS ONLY
      """;

  private static final String SQL_GET_TENURE_SUMMARY =
      """
      SELECT
        cb.FOREST_FILE_ID,
        TRIM(cb.CUTTING_PERMIT_ID) AS CUTTING_PERMIT_ID,
        cb.CUT_BLOCK_ID,
        cb.TIMBER_MARK,
        COALESCE(
          (
            SELECT MAX(cbc.CLIENT_NUMBER) KEEP (
              DENSE_RANK FIRST ORDER BY
                CASE cbc.CUT_BLOCK_CLIENT_TYPE_CODE
                  WHEN 'R' THEN 1
                  WHEN 'O' THEN 2
                END,
                cbc.CBLK_CLIENT_SKEY
            )
            FROM THE.CUT_BLOCK_CLIENT cbc
            WHERE cbc.CB_SKEY = cb.CB_SKEY
              AND cbc.CUT_BLOCK_CLIENT_TYPE_CODE IN ('R', 'O')
          ),
          (
            SELECT MAX(ffc.CLIENT_NUMBER) KEEP (
              DENSE_RANK FIRST ORDER BY ffc.FOREST_FILE_CLIENT_SKEY
            )
            FROM THE.FOREST_FILE_CLIENT ffc
            WHERE ffc.FOREST_FILE_ID = cb.FOREST_FILE_ID
              AND ffc.FOREST_FILE_CLIENT_TYPE_CODE = 'A'
          )
        ) AS LICENSEE_CLIENT_NUMBER,
        cb.BLOCK_STATUS_ST,
        cb.CB_SKEY
      FROM THE.CUT_BLOCK cb
      WHERE cb.FOREST_FILE_ID = :forestFileId
        AND cb.CUT_BLOCK_ID = :cutBlockId
        AND (
          (:cuttingPermitId IS NULL AND TRIM(cb.CUTTING_PERMIT_ID) IS NULL)
          OR TRIM(cb.CUTTING_PERMIT_ID) = :cuttingPermitId
        )
      ORDER BY cb.CB_SKEY
      FETCH FIRST 1 ROWS ONLY
      """;

  private final NamedParameterJdbcTemplate externalOracleJdbcTemplate;

  /**
   * Constructs an {@link OracleTenureJdbcAdapter} using the dedicated external Oracle JDBC
   * template.
   *
   * @param externalOracleJdbcTemplate the read-only {@link NamedParameterJdbcTemplate} for external
   *     Oracle queries
   */
  public OracleTenureJdbcAdapter(
      @Qualifier("externalOracleJdbcTemplate")
          NamedParameterJdbcTemplate externalOracleJdbcTemplate) {
    this.externalOracleJdbcTemplate = externalOracleJdbcTemplate;
  }

  /**
   * {@inheritDoc}
   *
   * <p>Queries {@code THE.CUT_BLOCK} and evaluates licensee authorization via an {@code EXISTS}
   * subquery against {@code THE.CUT_BLOCK_CLIENT} to prevent row duplication when multiple client
   * records exist for a cut block.
   */
  @Override
  public ExternalCutBlockDetails verifyCutBlock(
      String forestFileId,
      String cuttingPermitId,
      String cutBlockId,
      String clientNumber) {
    String normalizedFileId = StringUtils.trimToNull(forestFileId);
    String normalizedCutBlockId = StringUtils.trimToNull(cutBlockId);
    if (normalizedFileId == null || normalizedCutBlockId == null) {
      return ExternalCutBlockDetails.notFound();
    }

    String normalizedPermitId = StringUtils.trimToNull(cuttingPermitId);
    String normalizedClientNumber = StringUtils.trimToNull(clientNumber);
    MapSqlParameterSource params =
        buildTenureParams(normalizedFileId, normalizedPermitId, normalizedCutBlockId)
            .addValue(PARAM_CLIENT_NUMBER, normalizedClientNumber, Types.VARCHAR);

    return executeQuery(
        () ->
            externalOracleJdbcTemplate.queryForObject(
                SQL_VERIFY_CUT_BLOCK, params, (rs, rowNum) -> mapCutBlockDetails(rs)),
        ExternalCutBlockDetails.notFound(),
        "verifying cut block");
  }

  /**
   * {@inheritDoc}
   *
   * <p>Queries {@code THE.CUT_BLOCK} and resolves the primary licensee client number using {@code
   * R > O > A} priority across {@code THE.CUT_BLOCK_CLIENT} ({@code R}, {@code O}) and {@code
   * THE.FOREST_FILE_CLIENT} ({@code A}) via aggregate subqueries so multiple client rows never
   * duplicate the cut block result.
   */
  @Override
  public Optional<ExternalTenureSummary> getTenureSummary(
      String forestFileId, String cuttingPermitId, String cutBlockId) {
    String normalizedFileId = StringUtils.trimToNull(forestFileId);
    String normalizedCutBlockId = StringUtils.trimToNull(cutBlockId);
    if (normalizedFileId == null || normalizedCutBlockId == null) {
      return Optional.empty();
    }

    String normalizedPermitId = StringUtils.trimToNull(cuttingPermitId);
    MapSqlParameterSource params =
        buildTenureParams(normalizedFileId, normalizedPermitId, normalizedCutBlockId);

    return executeQuery(
        () ->
            Optional.ofNullable(
                externalOracleJdbcTemplate.queryForObject(
                    SQL_GET_TENURE_SUMMARY, params, (rs, rowNum) -> mapTenureSummary(rs))),
        Optional.empty(),
        "fetching tenure summary");
  }

  private <T> T executeQuery(Supplier<T> queryAction, T notFoundValue, String operation) {
    try {
      return queryAction.get();
    } catch (EmptyResultDataAccessException e) {
      log.debug("Cut block not found in external Oracle FTA while {}", operation);
      return notFoundValue;
    } catch (QueryTimeoutException e) {
      log.error("External Oracle query timed out while {}: {}", operation, e.getMessage());
      throw new ExternalServiceException(
          ExternalServiceErrorCode.EXTERNAL_SERVICE_TIMEOUT,
          SERVICE_NAME,
          "External FTA Oracle query timed out while " + operation,
          e);
    } catch (DataAccessException e) {
      log.error("External Oracle query failed while {}: {}", operation, e.getMessage());
      throw new ExternalServiceException(
          ExternalServiceErrorCode.EXTERNAL_SERVICE_UNAVAILABLE,
          SERVICE_NAME,
          "External FTA Oracle integration is unavailable while " + operation,
          e);
    }
  }

  private static MapSqlParameterSource buildTenureParams(
      String forestFileId, String cuttingPermitId, String cutBlockId) {
    return new MapSqlParameterSource()
        .addValue(PARAM_FOREST_FILE_ID, forestFileId, Types.VARCHAR)
        .addValue(PARAM_CUTTING_PERMIT_ID, cuttingPermitId, Types.VARCHAR)
        .addValue(PARAM_CUT_BLOCK_ID, cutBlockId, Types.VARCHAR);
  }

  private static ExternalCutBlockDetails mapCutBlockDetails(ResultSet rs) throws SQLException {
    Long cbSkey = getNullableLong(rs, COL_CB_SKEY);
    int rawIsLicensee = rs.getInt(COL_IS_LICENSEE);
    boolean isLicensee = !rs.wasNull() && rawIsLicensee == 1;
    String timberMark = StringUtils.trimToNull(rs.getString(COL_TIMBER_MARK));
    String status = StringUtils.trimToNull(rs.getString(COL_BLOCK_STATUS_ST));
    return new ExternalCutBlockDetails(true, cbSkey, isLicensee, timberMark, status);
  }

  private static ExternalTenureSummary mapTenureSummary(ResultSet rs) throws SQLException {
    String forestFileId = StringUtils.trimToNull(rs.getString(COL_FOREST_FILE_ID));
    String cuttingPermitId = StringUtils.trimToNull(rs.getString(COL_CUTTING_PERMIT_ID));
    String cutBlockId = StringUtils.trimToNull(rs.getString(COL_CUT_BLOCK_ID));
    String timberMark = StringUtils.trimToNull(rs.getString(COL_TIMBER_MARK));
    String licenseeClientNumber = StringUtils.trimToNull(rs.getString(COL_LICENSEE_CLIENT_NUMBER));
    String status = StringUtils.trimToNull(rs.getString(COL_BLOCK_STATUS_ST));
    Long cbSkey = getNullableLong(rs, COL_CB_SKEY);
    return new ExternalTenureSummary(
        forestFileId,
        cuttingPermitId,
        cutBlockId,
        timberMark,
        licenseeClientNumber,
        status,
        cbSkey);
  }

  private static Long getNullableLong(ResultSet rs, String columnName) throws SQLException {
    long value = rs.getLong(columnName);
    return rs.wasNull() ? null : value;
  }
}

