package ca.bc.gov.restapi.results.common.repository;

import ca.bc.gov.restapi.results.common.projection.OrgUnitProjection;

import java.util.List;

public interface OrgUnitRepository {
  List<OrgUnitProjection> findAllByOrgUnitCodeIn(List<String> orgUnitCodes);
}
