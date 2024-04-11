package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.OpeningsActivityEntity;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

/** This interface provides access to the database for the OpeningsActivityEntity entity. */
public interface OpeningsActivityRepository extends JpaRepository<OpeningsActivityEntity, Long> {

  List<OpeningsActivityEntity> findAllByEntryUserid(String userId, Sort sort);
}
