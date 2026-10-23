package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.entity.GenericCodeEntity;
import ca.bc.gov.restapi.results.common.exception.NotFoundGenericException;
import ca.bc.gov.restapi.results.common.repository.GenericCodeRepository;
import ca.bc.gov.restapi.results.postgres.dto.CreateActivityRequestDto;
import ca.bc.gov.restapi.results.postgres.entity.CutBlockOpenAdminEntity;
import ca.bc.gov.restapi.results.postgres.entity.opening.OpeningEntity;
import ca.bc.gov.restapi.results.postgres.enums.ActivityStatus;
import ca.bc.gov.restapi.results.postgres.repository.CutBlockOpenAdminPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvBaseCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvFundSrceCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvMethodCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvObjectiveCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvTechniqueCodePostgresRepository;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Validates a create-activity request. All violations are reported together in the same format as
 * the opening creation: {@code "<field>: <message>, <field>: <message>"}.
 */
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class CreateActivityValidationService {

  /** Basic activity bases; any other base code is rejected. */
  public static final Set<String> BASIC_BASE_CODES =
      new TreeSet<>(
          Set.of(
              "AU", "BR", "CC", "CM", "EP", "FE", "HC", "HI", "JS", "LB", "PC", "PR", "RC", "RD",
              "ST", "SX"));

  private static final BigDecimal AREA_TOLERANCE_FACTOR = new BigDecimal("1.25");
  private static final String NO_REQUIREMENT_CATEGORY = "NREQ";
  // RESULTS exempts these bases from the area tolerance; more bases are exempt for NREQ openings
  private static final Set<String> AREA_EXEMPT_BASES = Set.of("PC", "FE");
  private static final Set<String> AREA_EXEMPT_BASES_NO_REQUIREMENT =
      Set.of("FE", "PC", "JS", "PR", "RC", "RD", "EP", "CC", "CM", "SX", "AU", "BR");
  private static final String PRIME_LICENCE = "Y";

  private final Validator validator;
  private final OpeningPostgresRepository openingRepository;
  private final CutBlockOpenAdminPostgresRepository cutBlockOpenAdminRepository;
  private final SilvBaseCodePostgresRepository baseCodeRepository;
  private final SilvTechniqueCodePostgresRepository techniqueCodeRepository;
  private final SilvMethodCodePostgresRepository methodCodeRepository;
  private final SilvObjectiveCodePostgresRepository objectiveCodeRepository;
  private final SilvFundSrceCodePostgresRepository fundSrceCodeRepository;

  /**
   * Validates the request and returns the opening the activity is created under.
   *
   * @param dto the request
   * @return the opening
   * @throws NotFoundGenericException when the opening does not exist
   * @throws ResponseStatusException with status 400 listing every violation
   */
  public OpeningEntity validate(CreateActivityRequestDto dto) {
    List<String> errors = new ArrayList<>();
    validator.validate(dto).stream()
        .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
        .sorted()
        .forEach(errors::add);

    if (dto.openingId() == null) {
      throw badRequest(errors);
    }
    OpeningEntity opening =
        openingRepository
            .findById(dto.openingId())
            .orElseThrow(() -> new NotFoundGenericException("Opening"));

    validateAreas(dto, opening, errors);
    validateStatusFields(dto, errors);
    validateBase(dto, errors);
    validateCodes(dto, errors);
    validatePlannedDate(dto, errors);

    if (!errors.isEmpty()) {
      throw badRequest(errors);
    }
    return opening;
  }

  private void validateStatusFields(CreateActivityRequestDto dto, List<String> errors) {
    if (dto.status() == ActivityStatus.COMPLETED) {
      requireValue(dto.completionDate() != null, "completionDate", errors);
      requireValue(dto.treatedArea() != null, "treatedArea", errors);
    } else if (dto.status() == ActivityStatus.PLANNED) {
      rejectValue(dto.completionDate() != null, "completionDate", errors);
      rejectValue(dto.treatedArea() != null, "treatedArea", errors);
      rejectValue(dto.actualCost() != null, "actualCost", errors);
    }
  }

  private void requireValue(boolean present, String field, List<String> errors) {
    if (!present && !hasError(errors, field)) {
      errors.add(field + ": must not be null");
    }
  }

  private void rejectValue(boolean present, String field, List<String> errors) {
    if (present) {
      errors.add(field + ": only allowed when the status is COMPLETED");
    }
  }

  private void validateBase(CreateActivityRequestDto dto, List<String> errors) {
    if (dto.baseCode() == null || hasError(errors, "baseCode")) {
      return;
    }
    if (!BASIC_BASE_CODES.contains(dto.baseCode())) {
      errors.add("baseCode: must be one of " + BASIC_BASE_CODES);
      return;
    }
    checkInEffect("baseCode", List.of(dto.baseCode()), baseCodeRepository, errors);
  }

  private void validateCodes(CreateActivityRequestDto dto, List<String> errors) {
    if (dto.techniqueCode() != null && !hasError(errors, "techniqueCode")) {
      checkInEffect("techniqueCode", List.of(dto.techniqueCode()), techniqueCodeRepository, errors);
    }
    if (dto.methodCode() != null && !hasError(errors, "methodCode")) {
      checkInEffect("methodCode", List.of(dto.methodCode()), methodCodeRepository, errors);
    }
    checkList("objectiveCodes", dto.objectiveCodes(), objectiveCodeRepository, errors);
    checkList("fundingSourceCodes", dto.fundingSourceCodes(), fundSrceCodeRepository, errors);
  }

  private void checkList(
      String field,
      List<String> codes,
      GenericCodeRepository<? extends GenericCodeEntity> repository,
      List<String> errors) {
    if (codes == null || hasError(errors, field)) {
      return;
    }
    if (new HashSet<>(codes).size() != codes.size()) {
      errors.add(field + ": duplicate values are not allowed");
      return;
    }
    checkInEffect(field, codes, repository, errors);
  }

  private void checkInEffect(
      String field,
      List<String> codes,
      GenericCodeRepository<? extends GenericCodeEntity> repository,
      List<String> errors) {
    LocalDate today = LocalDate.now();
    List<? extends GenericCodeEntity> found = repository.findAllById(codes);
    for (String code : codes) {
      boolean inEffect =
          found.stream()
              .filter(entity -> code.equals(entity.getCode()))
              .anyMatch(entity -> isInEffect(entity, today));
      if (!inEffect) {
        errors.add(field + ": '" + code + "' is not a valid code currently in effect");
      }
    }
  }

  private boolean isInEffect(GenericCodeEntity entity, LocalDate today) {
    boolean started =
        entity.getEffectiveDate() == null || !entity.getEffectiveDate().isAfter(today);
    boolean notExpired = entity.getExpiryDate() == null || !entity.getExpiryDate().isBefore(today);
    return started && notExpired;
  }

  private void validatePlannedDate(CreateActivityRequestDto dto, List<String> errors) {
    if (dto.plannedDate() != null && !dto.plannedDate().isAfter(LocalDate.now())) {
      errors.add("plannedDate: must be later than the current date");
    }
  }

  private void validateAreas(
      CreateActivityRequestDto dto, OpeningEntity opening, List<String> errors) {
    BigDecimal grossArea = resolveGrossArea(opening);
    if (grossArea == null || isAreaExempt(dto.baseCode(), opening.getCategory())) {
      return;
    }
    BigDecimal maxArea = grossArea.multiply(AREA_TOLERANCE_FACTOR);
    checkArea("plannedArea", "Planned area", dto.plannedArea(), maxArea, grossArea, errors);
    if (dto.status() == ActivityStatus.COMPLETED) {
      checkArea("treatedArea", "Treated area", dto.treatedArea(), maxArea, grossArea, errors);
    }
  }

  private void checkArea(
      String field,
      String label,
      BigDecimal area,
      BigDecimal maxArea,
      BigDecimal grossArea,
      List<String> errors) {
    if (area != null && !hasError(errors, field) && area.compareTo(maxArea) > 0) {
      errors.add(
          String.format(
              "%s: %s (%s ha) cannot exceed %s ha (125%% of the Opening's gross area of %s ha)",
              field, label, plain(area), plain(maxArea), plain(grossArea)));
    }
  }

  private boolean isAreaExempt(String baseCode, String openingCategory) {
    Set<String> exempt =
        NO_REQUIREMENT_CATEGORY.equals(openingCategory)
            ? AREA_EXEMPT_BASES_NO_REQUIREMENT
            : AREA_EXEMPT_BASES;
    return baseCode != null && exempt.contains(baseCode);
  }

  private BigDecimal resolveGrossArea(OpeningEntity opening) {
    if (opening.getOpeningGrossArea() != null) {
      return opening.getOpeningGrossArea();
    }
    return cutBlockOpenAdminRepository.findAllByOpeningId(opening.getId()).stream()
        .filter(tenure -> PRIME_LICENCE.equals(tenure.getOpeningPrimeLicenceInd()))
        .map(CutBlockOpenAdminEntity::getOpeningGrossArea)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null);
  }

  private String plain(BigDecimal value) {
    return value.stripTrailingZeros().toPlainString();
  }

  private boolean hasError(List<String> errors, String field) {
    return errors.stream()
        .anyMatch(error -> error.startsWith(field + ":") || error.startsWith(field + "["));
  }

  private ResponseStatusException badRequest(List<String> errors) {
    return new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", errors));
  }
}
