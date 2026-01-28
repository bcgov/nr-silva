package ca.bc.gov.restapi.results.common.service.impl;

import ca.bc.gov.restapi.results.common.projection.CutBlockOpenAdminProjection;
import ca.bc.gov.restapi.results.common.repository.CutBlockOpenAdminRepository;
import ca.bc.gov.restapi.results.common.service.CutBlockOpenAdminService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AbstractCutBlockOpenAdminService implements CutBlockOpenAdminService {

  protected final CutBlockOpenAdminRepository cutBlockOpenAdminRepository;


   /**
   * Finds all openings by a list of opening id.
   *
   * @param openingIdList The id list to be fetched.
   * @return A list containing the results, or an empty list.
   */
  @Override
  public List<CutBlockOpenAdminProjection> findAllByOpeningIdIn(List<Long> openingIdList) {
    return cutBlockOpenAdminRepository.findAllByOpeningIdIn(openingIdList);
  }
}
