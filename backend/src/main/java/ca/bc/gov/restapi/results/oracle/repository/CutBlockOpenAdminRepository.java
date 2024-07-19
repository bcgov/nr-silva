package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.oracle.entity.CutBlockOpenAdminEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface holds methods for interacting with the CutBlockOpenAdminEntity table in the
 * database.
 */
public interface CutBlockOpenAdminRepository extends JpaRepository<CutBlockOpenAdminEntity, Long> {

  List<CutBlockOpenAdminEntity> findAllByOpeningEntity_idIn(List<Long> openingIdList);
}
