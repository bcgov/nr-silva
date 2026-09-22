package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.postgres.dto.BecDataDto;
import ca.bc.gov.restapi.results.postgres.dto.BecDuplicateConflictDto;
import ca.bc.gov.restapi.results.postgres.dto.BecValidationErrorDto;
import ca.bc.gov.restapi.results.postgres.dto.BecValidationResponseDto;
import ca.bc.gov.restapi.results.postgres.dto.BecValidationResultDto;
import ca.bc.gov.restapi.results.postgres.enums.BecValidationErrorCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/** Provides per-entry BEC validation feedback before stocking-standard submission. */
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class StockingStandardBecValidationService {

  private static final String EMPTY_BEC_MESSAGE =
      "At least one BEC entry is required when BEC information is selected";
  private static final String DUPLICATE_BEC_MESSAGE =
      "becData must not contain duplicate BEC combinations";

  private final StockingStandardReferenceValidationService referenceValidationService;
  private final Validator validator;

  /**
   * Validates each supplied BEC entry without persisting data.
   *
   * @param becData BEC combinations from the ecology step
   * @return individual validation outcomes and duplicate conflicts
   */
  public BecValidationResponseDto validate(List<BecDataDto> becData) {
    if (becData == null || becData.isEmpty()) {
      return new BecValidationResponseDto(
          List.of(),
          List.of(),
          List.of(new BecValidationErrorDto(BecValidationErrorCode.FIELD_INVALID, EMPTY_BEC_MESSAGE)),
          false);
    }

    List<BecValidationResultDto> results = new ArrayList<>();
    List<BecDataDto> structurallyValidBecData = new ArrayList<>();
    List<Integer> structurallyValidIndices = new ArrayList<>();

    for (int index = 0; index < becData.size(); index++) {
      BecDataDto bec = becData.get(index);
      String fieldError = validateFields(bec);
      if (fieldError != null) {
        results.add(
            new BecValidationResultDto(
                index, false, BecValidationErrorCode.FIELD_INVALID, fieldError));
        continue;
      }

      structurallyValidBecData.add(bec);
      structurallyValidIndices.add(index);
      if (referenceValidationService.validateBec(bec).exists()) {
        results.add(new BecValidationResultDto(index, true, null, null));
      } else {
        results.add(
            new BecValidationResultDto(
                index,
                false,
                BecValidationErrorCode.BEC_COMBINATION_NOT_FOUND,
                "Unknown BEC combination(s): " + formatBecCombination(bec)));
      }
    }

    List<BecDuplicateConflictDto> duplicateConflicts =
        findDuplicateConflicts(structurallyValidBecData, structurallyValidIndices);
    boolean isValid =
        results.stream().allMatch(BecValidationResultDto::isValid) && duplicateConflicts.isEmpty();
    return new BecValidationResponseDto(results, duplicateConflicts, List.of(), isValid);
  }

  private String validateFields(BecDataDto bec) {
    if (bec == null) {
      return "becData: must not be null";
    }
    return validator.validate(bec).stream()
        .sorted(Comparator.comparing(violation -> violation.getPropertyPath().toString()))
        .map(this::formatConstraintViolation)
        .findFirst()
        .orElse(null);
  }

  private String formatConstraintViolation(ConstraintViolation<BecDataDto> violation) {
    return violation.getPropertyPath() + ": " + violation.getMessage();
  }

  private List<BecDuplicateConflictDto> findDuplicateConflicts(
      List<BecDataDto> becData, List<Integer> originalIndices) {
    Map<String, List<Integer>> indicesByCombination = new LinkedHashMap<>();
    for (int index = 0; index < becData.size(); index++) {
      String combination = referenceValidationService.normalizeBecCombination(becData.get(index));
      indicesByCombination.computeIfAbsent(combination, ignored -> new ArrayList<>()).add(originalIndices.get(index));
    }

    return indicesByCombination.values().stream()
        .filter(indices -> indices.size() > 1)
        .map(
            indices ->
                new BecDuplicateConflictDto(
                    indices, DUPLICATE_BEC_MESSAGE, BecValidationErrorCode.DUPLICATE_BEC_COMBINATION))
        .toList();
  }

  private String formatBecCombination(BecDataDto bec) {
    return bec.bgcZoneCode()
        + "/"
        + bec.bgcSubzoneCode()
        + "/"
        + StringUtils.defaultString(bec.variant())
        + "/"
        + StringUtils.defaultString(bec.phase())
        + "/"
        + bec.siteSeries();
  }
}
