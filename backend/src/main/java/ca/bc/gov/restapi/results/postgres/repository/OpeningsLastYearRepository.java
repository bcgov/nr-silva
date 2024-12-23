package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.OpeningsLastYearEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/** This interface provides access to the database for the OpeningsLastYearEntity entity. */
@Repository
public interface OpeningsLastYearRepository extends JpaRepository<OpeningsLastYearEntity, Long> {

  List<OpeningsLastYearEntity> findAllByOpeningIdIn(List<Long> openingIdList);

  @Query("from OpeningsLastYearEntity o where o.entryTimestamp >= ?1 or o.updateTimestamp >= ?1")
  List<OpeningsLastYearEntity> findAllFromLastYear(LocalDateTime baseDateTime, Sort sort);
}
