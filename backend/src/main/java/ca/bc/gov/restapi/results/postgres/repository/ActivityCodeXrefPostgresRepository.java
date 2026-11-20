package ca.bc.gov.restapi.results.postgres.repository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Reads the cross-reference tables that say which technique, method, objectives and funding
 * sources can be used with an activity base.
 */
@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class ActivityCodeXrefPostgresRepository {

  private final JdbcTemplate jdbcTemplate;

  /**
   * Checks whether the base, technique and method combination is valid. A null technique or method
   * only matches a combination defined without one.
   *
   * @param baseCode the base code
   * @param techniqueCode the technique code, or null
   * @param methodCode the method code, or null
   * @return true if the combination exists
   */
  public boolean existsBaseTechniqueMethod(
      String baseCode, String techniqueCode, String methodCode) {
    return exists(
        """
        SELECT EXISTS (
          SELECT 1 FROM silva.silv_activity_xref
          WHERE silv_base_code = ?
            AND silv_technique_code IS NOT DISTINCT FROM ?
            AND silv_method_code IS NOT DISTINCT FROM ?)
        """,
        baseCode,
        techniqueCode,
        methodCode);
  }

  /**
   * Checks whether the technique can be used with the base, whatever the method.
   *
   * @param baseCode the base code
   * @param techniqueCode the technique code
   * @return true if the base and technique are part of at least one combination
   */
  public boolean existsBaseTechnique(String baseCode, String techniqueCode) {
    return exists(
        """
        SELECT EXISTS (
          SELECT 1 FROM silva.silv_activity_xref
          WHERE silv_base_code = ? AND silv_technique_code = ?)
        """,
        baseCode,
        techniqueCode);
  }

  /**
   * Returns the objectives, among the given ones, that can be used with the base.
   *
   * @param baseCode the base code
   * @param objectiveCodes the objective codes to check
   * @return the objective codes allowed for the base
   */
  public Set<String> findObjectivesForBase(String baseCode, Collection<String> objectiveCodes) {
    return findAllowed(
        "silva.silv_base_objective_xref", "silv_objective_code", baseCode, objectiveCodes);
  }

  /**
   * Returns the funding sources, among the given ones, that can be used with the base.
   *
   * @param baseCode the base code
   * @param fundingSourceCodes the funding source codes to check
   * @return the funding source codes allowed for the base
   */
  public Set<String> findFundingSourcesForBase(
      String baseCode, Collection<String> fundingSourceCodes) {
    return findAllowed(
        "silva.silv_base_funding_xref", "silv_fund_srce_code", baseCode, fundingSourceCodes);
  }

  private boolean exists(String sql, Object... args) {
    return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, args));
  }

  // Table and column are constants of this class, only the values are bound
  private Set<String> findAllowed(
      String table, String codeColumn, String baseCode, Collection<String> codes) {
    if (codes.isEmpty()) {
      return Set.of();
    }
    String placeholders = String.join(",", java.util.Collections.nCopies(codes.size(), "?"));
    Object[] args = new Object[codes.size() + 1];
    args[0] = baseCode;
    int index = 1;
    for (String code : codes) {
      args[index++] = code;
    }
    return new HashSet<>(
        jdbcTemplate.queryForList(
            "SELECT "
                + codeColumn
                + " FROM "
                + table
                + " WHERE silv_base_code = ? AND "
                + codeColumn
                + " IN ("
                + placeholders
                + ")",
            String.class,
            args));
  }
}
