package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.CutBlockEntity;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface CutBlockPostgresRepository extends JpaRepository<CutBlockEntity, Long> {

  @Query(
      """
      SELECT cb FROM CutBlockEntity cb
      WHERE cb.forestFileId = :fileId
        AND cb.cutBlockId = :cutBlockId
        AND (:cuttingPermit IS NULL AND cb.cuttingPermitId IS NULL
             OR cb.cuttingPermitId = :cuttingPermit)
      """)
  Optional<CutBlockEntity> findByTenure(
      @Param("fileId") String fileId,
      @Param("cutBlockId") String cutBlockId,
      @Param("cuttingPermit") String cuttingPermit);
}
