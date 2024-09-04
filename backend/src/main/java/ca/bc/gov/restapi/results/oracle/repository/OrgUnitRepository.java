package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.oracle.entity.OrgUnitEntity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/** This interface provides methods to get, save, and manage org unit data in the database. */
public interface OrgUnitRepository extends JpaRepository<OrgUnitEntity, Long> {

  List<OrgUnitEntity> findAllByExpiryDateAfter(LocalDate now);

  List<OrgUnitEntity> findAllByOrgUnitCodeIn(List<String> orgUnitCodes);
}
