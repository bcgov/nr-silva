package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.oracle.entity.OrgUnitEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** This interface provides methods to get, save, and manage org unit data in the database. */
@Repository
public interface OrgUnitRepository extends JpaRepository<OrgUnitEntity, Long> {

  List<OrgUnitEntity> findAllByOrgUnitCodeIn(List<String> orgUnitCodes);
}
