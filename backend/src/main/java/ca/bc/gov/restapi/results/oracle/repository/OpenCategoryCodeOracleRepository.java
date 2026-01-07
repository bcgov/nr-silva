package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.common.projection.OpenCategoryCodeProjection;
import ca.bc.gov.restapi.results.common.repository.OpenCategoryCodeRepository;
import ca.bc.gov.restapi.results.oracle.entity.OpenCategoryCodeEntity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** This interface provides methods to get, save, and manage data in the database. */
@Repository
public interface OpenCategoryCodeOracleRepository extends JpaRepository<OpenCategoryCodeEntity, String>,
    OpenCategoryCodeRepository {
  List<OpenCategoryCodeProjection> findAllBy();
  List<OpenCategoryCodeProjection> findAllByExpiryDateAfter(LocalDate now);
}
