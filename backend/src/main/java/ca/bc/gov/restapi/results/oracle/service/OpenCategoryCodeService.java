package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.oracle.entity.OpenCategoryCodeEntity;
import ca.bc.gov.restapi.results.oracle.repository.OpenCategoryCodeRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

/**
 * This class contains methods to handle Opening Categories.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OpenCategoryCodeService {

  private final OpenCategoryCodeRepository openCategoryCodeRepository;

  /**
   * Find all Opening categories. Option to include expired ones.
   *
   * @param includeExpired True to include expired, false otherwise.
   * @return List of {@link CodeDescriptionDto} with found categories.
   */
  public List<CodeDescriptionDto> findAllCategories(boolean includeExpired) {
    log.info("Getting all open category codes. Include expired: {}", includeExpired);

    List<OpenCategoryCodeEntity> openCategoryCodes =
        includeExpired
            ? openCategoryCodeRepository.findAll()
            : openCategoryCodeRepository.findAllByExpiryDateAfter(LocalDate.now());

    log.info("Found {} open category codes ({}cluding expired)",
        openCategoryCodes.size(),
        BooleanUtils.toString(includeExpired, "in", "ex")
    );
    return openCategoryCodes
        .stream()
        .map(entity -> new CodeDescriptionDto(
                entity.getCode(),
                entity.isExpired()
                    ? entity.getDescription() + " (Expired)"
                    : entity.getDescription()
            )
        )
        .toList();
  }
}
