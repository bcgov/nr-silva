package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.oracle.entity.CutBlockOpenAdminEntity;
import ca.bc.gov.restapi.results.oracle.repository.CutBlockOpenAdminRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** This class holds methods for handling {@link CutBlockOpenAdminEntity}. */
@Service
@RequiredArgsConstructor
public class CutBlockOpenAdminService {

  private final CutBlockOpenAdminRepository cutBlockOpenAdminRepository;

  /**
   * Finds all openings by a list of opening id.
   *
   * @param openingIdList The id list to be fetched.
   * @return A list containing the results, or an empty list.
   */
  public List<CutBlockOpenAdminEntity> findAllByOpeningIdIn(List<Long> openingIdList) {
    return cutBlockOpenAdminRepository.findAllByOpeningEntity_idIn(openingIdList);
  }
}
