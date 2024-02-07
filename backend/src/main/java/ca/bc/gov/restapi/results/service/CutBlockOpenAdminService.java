package ca.bc.gov.restapi.results.service;

import ca.bc.gov.restapi.results.entity.CutBlockOpenAdminEntity;
import ca.bc.gov.restapi.results.repository.CutBlockOpenAdminRepository;
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
    return cutBlockOpenAdminRepository.findAllByOpeningIdIn(openingIdList);
  }
}
