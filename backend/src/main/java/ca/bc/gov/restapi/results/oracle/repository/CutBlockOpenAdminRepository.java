package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.oracle.entity.CutBlockOpenAdminEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * This interface holds methods for interacting with the CutBlockOpenAdminEntity table in the
 * database.
 */
@Repository
public interface CutBlockOpenAdminRepository extends JpaRepository<CutBlockOpenAdminEntity, Long> {

  @Query("from CutBlockOpenAdminEntity where openingId in ?1")
  List<CutBlockOpenAdminEntity> findAllByOpeningIdIn(List<Long> openingIdList);
}
