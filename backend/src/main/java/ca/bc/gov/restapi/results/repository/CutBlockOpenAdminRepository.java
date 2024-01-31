package ca.bc.gov.restapi.results.repository;

import ca.bc.gov.restapi.results.entity.CutBlockOpenAdminEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CutBlockOpenAdminRepository extends JpaRepository<CutBlockOpenAdminEntity, Long> {

  @Query("from CutBlockOpenAdminEntity where id in ?1")
  List<CutBlockOpenAdminEntity> findAllByOpeningIdIn(List<Long> openingIdList);
}
