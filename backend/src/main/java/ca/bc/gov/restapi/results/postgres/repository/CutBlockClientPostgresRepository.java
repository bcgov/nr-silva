package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.CutBlockClientEntity;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Repository for the {@code silva.cut_block_client} table. */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface CutBlockClientPostgresRepository
    extends JpaRepository<CutBlockClientEntity, Long> {

  boolean existsByCbSkeyAndClientNumber(Long cbSkey, String clientNumber);

  /** Obligation (O) and responsibility (R) client numbers of the cut blocks of the opening. */
  @Query(
      """
      SELECT DISTINCT cbc.clientNumber
      FROM CutBlockOpenAdminEntity cboa
      JOIN CutBlockClientEntity cbc ON cbc.cbSkey = cboa.cbSkey
      WHERE cboa.openingId = :openingId AND cbc.cutBlockClientTypeCode IN ('O', 'R')
      """)
  List<String> findObligationClientNumbersByOpeningId(@Param("openingId") Long openingId);
}
