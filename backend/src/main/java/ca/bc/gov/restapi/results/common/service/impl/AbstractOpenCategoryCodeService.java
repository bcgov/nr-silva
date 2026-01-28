package ca.bc.gov.restapi.results.common.service.impl;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.projection.OpenCategoryCodeProjection;
import ca.bc.gov.restapi.results.common.repository.OpenCategoryCodeRepository;
import ca.bc.gov.restapi.results.common.service.OpenCategoryCodeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AbstractOpenCategoryCodeService implements OpenCategoryCodeService {

  protected final OpenCategoryCodeRepository openCategoryCodeRepository;

  /**
   * Find all Opening categories. Option to include expired ones.
   *
   * @param includeExpired True to include expired, false otherwise.
   * @return List of {@link CodeDescriptionDto} with found categories.
   */
  @Override
  public List<CodeDescriptionDto> findAllCategories(boolean includeExpired) {
    log.info("Getting all open category codes. Include expired: {}", includeExpired);

    List<OpenCategoryCodeProjection> openCategoryCodes =
        includeExpired
            ? openCategoryCodeRepository.findAllBy()
            : openCategoryCodeRepository.findAllByExpiryDateAfter(LocalDate.now());

    log.info("Found {} open category codes ({}cluding expired)",
        openCategoryCodes.size(),
        BooleanUtils.toString(includeExpired, "in", "ex")
    );
    return openCategoryCodes
        .stream()
        .map(entity -> new CodeDescriptionDto(
                entity.getCode(),
                entity.getIsExpired()
                    ? entity.getDescription() + " (Expired)"
                    : entity.getDescription()
            )
        )
        .toList();
  }
}
