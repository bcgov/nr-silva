package ca.bc.gov.restapi.results.common.service;

import ca.bc.gov.restapi.results.common.projection.CutBlockOpenAdminProjection;

import java.util.List;

public interface CutBlockOpenAdminService {
  List<CutBlockOpenAdminProjection> findAllByOpeningIdIn(List<Long> openingIdList);
}
