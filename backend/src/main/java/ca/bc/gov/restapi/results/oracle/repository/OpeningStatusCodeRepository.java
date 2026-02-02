package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.oracle.entity.OpeningStatusCodeEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** This interface provides methods to get, save, and manage data in the database. */
@Repository
public interface OpeningStatusCodeRepository
    extends JpaRepository<OpeningStatusCodeEntity, String> {

  List<OpeningStatusCodeEntity> findAll();
}
