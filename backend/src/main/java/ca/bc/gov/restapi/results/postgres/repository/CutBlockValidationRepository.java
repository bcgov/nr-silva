package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.common.projection.CutBlockValidationProjection;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for validating cut block / tenure combinations and retrieving associated data for
 * opening creation.
 */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface CutBlockValidationRepository
    extends JpaRepository<ca.bc.gov.restapi.results.postgres.entity.CutBlockOpenAdminEntity, Long> {

  /**
   * Find a cut block by its tenure identifiers and client details.
   *
   * <p>Joins {@code silva.cut_block} to {@code silva.cut_block_client} on {@code cb_skey} and
   * filters by all five supplied parameters. When {@code cuttingPermit} is {@code null} the column
   * must also be {@code null} in the table.
   *
   * @param fileId the forest file ID
   * @param cuttingPermit the cutting permit ID; may be {@code null}
   * @param cutBlockId the cut block ID
   * @param clientNumber the client number
   * @param clientLocnCode the client location code
   * @return the matching projection, or empty if no matching cut block is found
   */
  @Query(
      value =
          "SELECT cb.cb_skey AS cbSkey, cb.timber_mark AS timberMark"
              + " FROM silva.cut_block cb"
              + " JOIN silva.cut_block_client cbc ON cbc.cb_skey = cb.cb_skey"
              + " WHERE cb.forest_file_id = :fileId"
              + " AND (:cuttingPermit IS NULL AND cb.cutting_permit_id IS NULL"
              + "      OR cb.cutting_permit_id = :cuttingPermit)"
              + " AND cb.cut_block_id = :cutBlockId"
              + " AND cbc.client_number = :clientNumber"
              + " AND cbc.client_locn_code = :clientLocnCode"
              + " LIMIT 1",
      nativeQuery = true)
  Optional<CutBlockValidationProjection> findCutBlockByTenureAndClient(
      @Param("fileId") String fileId,
      @Param("cuttingPermit") String cuttingPermit,
      @Param("cutBlockId") String cutBlockId,
      @Param("clientNumber") String clientNumber,
      @Param("clientLocnCode") String clientLocnCode);
}
