package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.oracle.entity.OpenCategoryCodeEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/** This interface provides methods to get, save, and manage data in the database. */
public interface OpenCategoryCodeRepository extends JpaRepository<OpenCategoryCodeEntity, String> {

  List<OpenCategoryCodeEntity> findAllByExpiryDateAfter(LocalDateTime now);
}
