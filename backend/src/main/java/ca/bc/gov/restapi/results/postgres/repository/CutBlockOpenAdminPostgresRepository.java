package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.common.projection.CutBlockOpenAdminProjection;
import ca.bc.gov.restapi.results.common.projection.opening.OpeningTenureProjection;
import ca.bc.gov.restapi.results.common.repository.CutBlockOpenAdminRepository;
import ca.bc.gov.restapi.results.postgres.SilvaPostgresQueryConstants;
import ca.bc.gov.restapi.results.postgres.entity.CutBlockOpenAdminEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for CRUD operations and custom queries against the
 * `silva.cut_block_open_admin` table in PostgreSQL.
 */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface CutBlockOpenAdminPostgresRepository
    extends JpaRepository<CutBlockOpenAdminEntity, Long>, CutBlockOpenAdminRepository {

  @Override
  @Query("from CutBlockOpenAdminEntity where openingId in ?1")
  List<CutBlockOpenAdminProjection> findAllByOpeningIdIn(List<Long> openingIdList);

  @Override
  @Query(
      nativeQuery = true,
      value = SilvaPostgresQueryConstants.GET_OPENING_TENURES,
      countQuery = SilvaPostgresQueryConstants.GET_OPENING_TENURES_COUNT)
  Page<OpeningTenureProjection> findAllTenuresByOpeningId(
      Long openingId, String mainSearchTerm, Pageable pageable);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_OPENING_TENURE_PRIME)
  Optional<OpeningTenureProjection> findPrimeTenureByOpeningId(Long openingId);

  // Mirrors Oracle edit_tenure duplicate-opening check: opening_id IS NOT NULL
  @Query(
      """
      SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
      FROM CutBlockOpenAdminEntity c
      WHERE c.forestFileId = :fileId
        AND c.cutBlockId = :cutBlockId
        AND (:cuttingPermit IS NULL AND c.cuttingPermitId IS NULL
             OR c.cuttingPermitId = :cuttingPermit)
        AND c.openingId IS NOT NULL
      """)
  boolean existsAllocatedByTenure(
      @Param("fileId") String fileId,
      @Param("cutBlockId") String cutBlockId,
      @Param("cuttingPermit") String cuttingPermit);
}
