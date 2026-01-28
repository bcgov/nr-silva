package ca.bc.gov.restapi.results.common.repository;

import ca.bc.gov.restapi.results.common.projection.CutBlockOpenAdminProjection;
import ca.bc.gov.restapi.results.common.projection.opening.OpeningTenureProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CutBlockOpenAdminRepository {
  List<CutBlockOpenAdminProjection> findAllByOpeningIdIn(List<Long> openingIdList);

  Page<OpeningTenureProjection> findAllTenuresByOpeningId(
      Long openingId,
      String mainSearchTerm,
      Pageable pageable
  );

  Optional<OpeningTenureProjection> findPrimeTenureByOpeningId(Long openingId);
}
