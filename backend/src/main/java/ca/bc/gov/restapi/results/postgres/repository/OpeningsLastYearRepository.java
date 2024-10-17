package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.OpeningsLastYearEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/** This interface provides access to the database for the OpeningsLastYearEntity entity. */
public interface OpeningsLastYearRepository extends JpaRepository<OpeningsLastYearEntity, Long> {

  List<OpeningsLastYearEntity> findAllByOpeningIdIn(List<Long> openingIdList);

  // This query can be replaced with a method name query, but the name would be huge, so we will
  // stick with the query.
  @Query("from OpeningsLastYearEntity o where o.entryTimestamp >= ?1 or o.updateTimestamp >= ?1")
  List<OpeningsLastYearEntity> findAllFromLastYear(LocalDateTime baseDateTime, Sort sort);
}
