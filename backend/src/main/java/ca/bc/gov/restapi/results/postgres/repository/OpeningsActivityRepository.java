package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.OpeningsActivityEntity;
import ca.bc.gov.restapi.results.postgres.entity.OpeningsActivityEntityId;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/** This interface provides access to the database for the OpeningsActivityEntity entity. */
public interface OpeningsActivityRepository
    extends JpaRepository<OpeningsActivityEntity, OpeningsActivityEntityId> {

  @Query("from OpeningsActivityEntity o where o.openingId in (?1)")
  List<OpeningsActivityEntity> findAllByOpeningId(List<Long> openingIds);

  List<OpeningsActivityEntity> findAllByEntryUserid(String userId, Sort sort);
}
