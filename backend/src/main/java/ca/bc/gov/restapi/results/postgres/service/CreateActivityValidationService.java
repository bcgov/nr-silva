package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.entity.GenericCodeEntity;
import ca.bc.gov.restapi.results.common.repository.GenericCodeRepository;
import ca.bc.gov.restapi.results.common.util.DateUtil;
import ca.bc.gov.restapi.results.postgres.dto.CreateActivityRequestDto;
import ca.bc.gov.restapi.results.postgres.enums.ActivityStatus;
import ca.bc.gov.restapi.results.postgres.repository.SilvBaseCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvFundSrceCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvMethodCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvObjectiveCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvTechniqueCodePostgresRepository;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
      Collections.unmodifiableSortedSet(
          new TreeSet<>(
              Set.of(
                  "AU", "BR", "CC", "CM", "EP", "FE", "HC", "HI", "JS", "LB", "PC", "PR", "RC",
                  "RD", "ST", "SX")));

  public static final String SITE_PREPARATION = "SP";
  public static final String SURVEY = "SU";

  /** Bases accepted by the create endpoint: the basic ones plus those with extra fields. */
  public static final Set<String> SUPPORTED_BASE_CODES =
      Collections.unmodifiableSortedSet(
          new TreeSet<>(
              Stream.concat(BASIC_BASE_CODES.stream(), Stream.of(SITE_PREPARATION, SURVEY))
                  .collect(Collectors.toSet())));

  private static final String COMPLETION_DATE = "completionDate";
  private static final String PLANNED_AREA = "plannedArea";
  private static final String TREATED_AREA = "treatedArea";

  private final Validator validator;
  private final SilvBaseCodePostgresRepository baseCodeRepository;
  private final SilvTechniqueCodePostgresRepository techniqueCodeRepository;
  private final SilvMethodCodePostgresRepository methodCodeRepository;
  private final SilvObjectiveCodePostgresRepository objectiveCodeRepository;
  private final SilvFundSrceCodePostgresRepository fundSrceCodeRepository;

  /**
   * Validates the request.
   *
   * @param dto the request
   * @throws ResponseStatusException with status 400 listing every violation
   */
  public void validate(CreateActivityRequestDto dto) {
    List<String> errors = new ArrayList<>();
    if (dto == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "data: must not be null");
    }
    validator.validate(dto).stream()
        .map(violation -> toMessage(violation.getPropertyPath().toString(), violation.getMessage()))
        .sorted()
        .forEach(errors::add);

    validateStatusFields(dto, errors);
    validateBase(dto, errors);
    validateCodes(dto, errors);
    validatePlannedDate(dto, errors);

    if (!errors.isEmpty()) {
      throw badRequest(errors);
    }
  }

  // List element violations have paths like "objectiveCodes[0].<list element>"
  private String toMessage(String path, String message) {
    return path.replace(".<list element>", "") + ": " + message;
  }

  private void validateStatusFields(CreateActivityRequestDto dto, List<String> errors) {
    if (dto.status() == ActivityStatus.COMPLETED) {
      requireValue(dto.completionDate() != null, COMPLETION_DATE, errors);
      requireValue(dto.treatedArea() != null, TREATED_AREA, errors);
    } else if (dto.status() == ActivityStatus.PLANNED) {
      requireValue(dto.plannedDate() != null, "plannedDate", errors);
      requireValue(dto.plannedArea() != null, PLANNED_AREA, errors);
      rejectValue(dto.completionDate() != null, COMPLETION_DATE, errors);
      rejectValue(dto.treatedArea() != null, TREATED_AREA, errors);
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
    if (!SUPPORTED_BASE_CODES.contains(dto.baseCode())) {
      errors.add("baseCode: must be one of " + SUPPORTED_BASE_CODES);
      return;
    }
    rejectBaseSpecific(dto, errors);
    checkInEffect("baseCode", List.of(dto.baseCode()), baseCodeRepository, errors);
  }

  private void rejectBaseSpecific(CreateActivityRequestDto dto, List<String> errors) {
    if (!SITE_PREPARATION.equals(dto.baseCode()) && dto.targetPreparedSpots() != null) {
      errors.add("targetPreparedSpots: only allowed when the base is SP");
    }
    if (!SURVEY.equals(dto.baseCode())) {
      if (dto.surveyNumPlots() != null) {
        errors.add("surveyNumPlots: only allowed when the base is SU");
      }
      if (dto.surveyMinPlotsPerStratum() != null) {
        errors.add("surveyMinPlotsPerStratum: only allowed when the base is SU");
      }
    }
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
    LocalDate today = DateUtil.todayInVancouver();
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

  // Only a planned activity must be planned for the future; a completed one can carry any date
  private void validatePlannedDate(CreateActivityRequestDto dto, List<String> errors) {
    if (dto.status() == ActivityStatus.PLANNED
        && dto.plannedDate() != null
        && !dto.plannedDate().isAfter(DateUtil.todayInVancouver())) {
      errors.add("plannedDate: must be later than the current date");
    }
  }

  private boolean hasError(List<String> errors, String field) {
    return errors.stream()
        .anyMatch(error -> error.startsWith(field + ":") || error.startsWith(field + "["));
  }

  private ResponseStatusException badRequest(List<String> errors) {
    return new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", errors));
  }
}
