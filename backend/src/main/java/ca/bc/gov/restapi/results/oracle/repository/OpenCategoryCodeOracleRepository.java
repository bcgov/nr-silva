package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.common.projection.OpenCategoryCodeProjection;
import ca.bc.gov.restapi.results.common.repository.OpenCategoryCodeRepository;
import ca.bc.gov.restapi.results.oracle.entity.OpenCategoryCodeEntity;
import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** This interface provides methods to get, save, and manage data in the database. */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
public interface OpenCategoryCodeOracleRepository extends JpaRepository<OpenCategoryCodeEntity, String>,
    OpenCategoryCodeRepository {
  @Override
  List<OpenCategoryCodeProjection> findAllBy();

  @Override
  List<OpenCategoryCodeProjection> findAllByExpiryDateAfter(LocalDate now);
}
