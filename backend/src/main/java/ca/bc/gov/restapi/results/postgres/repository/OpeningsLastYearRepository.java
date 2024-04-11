package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.OpeningsLastYearEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/** This interface provides access to the database for the OpeningsLastYearEntity entity. */
public interface OpeningsLastYearRepository extends JpaRepository<OpeningsLastYearEntity, Long> {

  @Query("from OpeningsLastYearEntity o where o.openingId in ?1")
  List<OpeningsLastYearEntity> findAllByOpeningIdInList(List<Long> openingIdList);
}
