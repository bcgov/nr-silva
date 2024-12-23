package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.OpeningsActivityEntity;
import ca.bc.gov.restapi.results.postgres.entity.OpeningsActivityEntityId;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface provides access to the database for the OpeningsActivityEntity entity.
 */
@Repository
public interface OpeningsActivityRepository
    extends JpaRepository<OpeningsActivityEntity, OpeningsActivityEntityId> {

  List<OpeningsActivityEntity> findAllByEntryUserid(String userId, Pageable page);
}
