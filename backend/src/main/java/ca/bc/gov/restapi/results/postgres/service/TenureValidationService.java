package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.postgres.dto.DuplicateConflictDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureRequestDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureValidationResponseDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureValidationResultDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** Service that validates tenure data and detects duplicates. */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenureValidationService {

  /**
   * Validates a list of tenures and detects duplicates.
   *
   * @param tenures the list of tenures to validate
   * @return a {@link TenureValidationResponseDto} containing per-tenure validation results and
   *     duplicate conflicts
   */
  public TenureValidationResponseDto validateTenures(List<TenureRequestDto> tenures) {
    List<TenureValidationResultDto> validationResults = new ArrayList<>();
    List<DuplicateConflictDto> duplicates = detectDuplicates(tenures);

    // Validate each tenure individually
    for (int i = 0; i < tenures.size(); i++) {
      TenureRequestDto tenure = tenures.get(i);
      String error = validateSingleTenure(tenure);
      TenureValidationResultDto result = new TenureValidationResultDto(i, error == null, error);
      validationResults.add(result);
    }

    // Overall validity: all tenures must be valid AND no duplicates
    boolean isValid =
        validationResults.stream().allMatch(TenureValidationResultDto::isValid)
            && duplicates.isEmpty();

    return new TenureValidationResponseDto(validationResults, duplicates, isValid);
  }

  /**
   * Detects duplicate tenures based on fileId and cutBlock combination.
   *
   * @param tenures the list of tenures to check
   * @return a list of duplicate conflicts; empty if no duplicates found
   */
  public List<DuplicateConflictDto> detectDuplicates(List<TenureRequestDto> tenures) {
    List<DuplicateConflictDto> conflicts = new ArrayList<>();
    Map<String, List<Integer>> keyToIndices = new HashMap<>();

    for (int i = 0; i < tenures.size(); i++) {
      TenureRequestDto tenure = tenures.get(i);
      String key = buildTenureKey(tenure);
      keyToIndices.computeIfAbsent(key, k -> new ArrayList<>()).add(i);
    }

    for (List<Integer> indices : keyToIndices.values()) {
      if (indices.size() > 1) {
        TenureRequestDto firstTenure = tenures.get(indices.get(0));
        String reason =
            String.format("fileId=%s, cutBlock=%s", firstTenure.fileId(), firstTenure.cutBlock());
        conflicts.add(new DuplicateConflictDto(indices, reason));
      }
    }

    return conflicts;
  }

  /**
   * Validates a single tenure for bean validation errors.
   *
   * @param tenure the tenure to validate
   * @return the first validation error message, or null if valid
   */
  private String validateSingleTenure(TenureRequestDto tenure) {
    // fileId validation
    if (tenure.fileId() == null || tenure.fileId().isBlank()) {
      return "fileId: must not be blank";
    }
    if (tenure.fileId().length() > 10) {
      return "fileId: size must be between 0 and 10";
    }

    // cutBlock validation
    if (tenure.cutBlock() == null || tenure.cutBlock().isBlank()) {
      return "cutBlock: must not be blank";
    }
    if (tenure.cutBlock().length() > 10) {
      return "cutBlock: size must be between 0 and 10";
    }

    // cuttingPermit validation (optional but bounded)
    if (tenure.cuttingPermit() != null && tenure.cuttingPermit().length() > 3) {
      return "cuttingPermit: size must be between 0 and 3";
    }

    return null;
  }

  /**
   * Builds a composite key for duplicate detection: fileId + cutBlock.
   *
   * @param tenure the tenure to key
   * @return the composite key
   */
  private String buildTenureKey(TenureRequestDto tenure) {
    return String.format("%s|%s", tenure.fileId().trim(), tenure.cutBlock().trim());
  }
}
