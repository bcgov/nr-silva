package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.ForestFileClientEntity;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Repository for the {@code silva.forest_file_client} table. */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface ForestFileClientPostgresRepository
    extends JpaRepository<ForestFileClientEntity, Long> {

  /** Licensee (type A) client numbers of the forest files of the opening's tenures. */
  @Query(
      """
      SELECT DISTINCT ffc.clientNumber
      FROM CutBlockOpenAdminEntity cboa
      JOIN ForestFileClientEntity ffc ON ffc.forestFileId = cboa.forestFileId
      WHERE cboa.openingId = :openingId AND ffc.forestFileClientTypeCode = 'A'
      """)
  List<String> findLicenseeClientNumbersByOpeningId(@Param("openingId") Long openingId);
}
