package ca.bc.gov.restapi.results.common.repository;

import ca.bc.gov.restapi.results.common.projection.OrgUnitProjection;
import java.time.LocalDate;
import java.util.List;

public interface OrgUnitRepository {
  List<OrgUnitProjection> findActiveDistricts(LocalDate today);

  List<OrgUnitProjection> findAllActive(LocalDate today);
}
