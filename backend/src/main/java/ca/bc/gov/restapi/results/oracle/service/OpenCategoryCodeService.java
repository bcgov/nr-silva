package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.oracle.entity.OpenCategoryCodeEntity;
import ca.bc.gov.restapi.results.oracle.repository.OpenCategoryCodeRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** This class contains methods to handle Opening Categories. */
@Slf4j
@Service
@RequiredArgsConstructor
public class OpenCategoryCodeService {

  private final OpenCategoryCodeRepository openCategoryCodeRepository;

  /**
   * Find all Opening categories. Option to include expired ones.
   *
   * @param includeExpired True to include expired, false otherwise.
   * @return List of {@link OpenCategoryCodeEntity} with found categories.
   */
  public List<OpenCategoryCodeEntity> findAllCategories(boolean includeExpired) {
    log.info("Getting all open category codes. Include expired: {}", includeExpired);

    if (includeExpired) {
      List<OpenCategoryCodeEntity> openCategoryCodes = openCategoryCodeRepository.findAll();
      log.info("Found {} open category codes (including expired)", openCategoryCodes.size());
      return openCategoryCodes;
    }

    List<OpenCategoryCodeEntity> openCategoryCodes =
        openCategoryCodeRepository.findAllByExpiryDateAfter(LocalDateTime.now());
    log.info("Found {} open category codes (excluding expired)", openCategoryCodes.size());
    return openCategoryCodes;
  }
}
