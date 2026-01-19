package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.common.projection.CutBlockOpenAdminProjection;
import ca.bc.gov.restapi.results.common.repository.CutBlockOpenAdminRepository;
import ca.bc.gov.restapi.results.oracle.SilvaOracleQueryConstants;
import ca.bc.gov.restapi.results.oracle.entity.CutBlockOpenAdminEntity;
import ca.bc.gov.restapi.results.common.projection.opening.OpeningTenureProjection;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * This interface holds methods for interacting with the CutBlockOpenAdminEntity table in the
 * database.
 */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
public interface CutBlockOpenAdminOracleRepository extends JpaRepository<CutBlockOpenAdminEntity, Long>,
    CutBlockOpenAdminRepository {

  @Override
  @Query("from CutBlockOpenAdminEntity where openingId in ?1")
  List<CutBlockOpenAdminProjection> findAllByOpeningIdIn(List<Long> openingIdList);

  @Override
  @Query(
      nativeQuery = true,
      value = SilvaOracleQueryConstants.GET_OPENING_TENURES,
      countQuery = SilvaOracleQueryConstants.GET_OPENING_TENURES_COUNT
  )
  Page<OpeningTenureProjection> findAllTenuresByOpeningId(
      Long openingId,
      String mainSearchTerm,
      Pageable pageable
  );

  @Override
  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_TENURE_PRIME)
  Optional<OpeningTenureProjection> findPrimeTenureByOpeningId(Long openingId);

}
