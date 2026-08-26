package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.security.LoggedUserHelper;
import ca.bc.gov.restapi.results.postgres.dto.DuplicateConflictDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureRequestDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureValidationResponseDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureValidationResultDto;
import ca.bc.gov.restapi.results.postgres.entity.CutBlockEntity;
import ca.bc.gov.restapi.results.postgres.enums.TenureValidationErrorCode;
import ca.bc.gov.restapi.results.postgres.repository.CutBlockClientPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.CutBlockOpenAdminPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.CutBlockPostgresRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service that validates tenure data including cut block existence, client auth, and duplicates.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class TenureValidationService {

  private final LoggedUserHelper loggedUserHelper;
  private final CutBlockPostgresRepository cutBlockRepository;
  private final CutBlockClientPostgresRepository cutBlockClientRepository;
  private final CutBlockOpenAdminPostgresRepository cutBlockOpenAdminRepository;

  /**
   * Validates a list of tenures: field constraints, JWT client auth, cut block existence, client
   * licensee membership, and CBOA duplicate-opening check. Throws 403 if the caller's JWT does not
   * carry a role matching the supplied client number.
   *
   * @param tenures the tenures to validate
   * @param clientNumber the 8-character client number the caller claims ownership of
   * @return a {@link TenureValidationResponseDto} with per-tenure results and duplicate conflicts
   */
  private record ValidatedTenure(TenureValidationResultDto result, CutBlockEntity block) {}

  public TenureValidationResponseDto validateTenures(
      List<TenureRequestDto> tenures, String clientNumber) {

    // Auth check fails fast — security boundary, not a data validation error
    if (!loggedUserHelper.hasRoleMatching(role -> role.endsWith("_" + clientNumber))) {
      throw new ResponseStatusException(
          HttpStatus.FORBIDDEN, "Not authorised for client number " + clientNumber);
    }

    List<TenureValidationResultDto> validationResults = new ArrayList<>();
    Map<Integer, CutBlockEntity> resolvedBlocks = new HashMap<>();
    List<TenureDto> tenuresWithTimberMark = new ArrayList<>();
    List<DuplicateConflictDto> duplicates = detectDuplicates(tenures);

    for (int i = 0; i < tenures.size(); i++) {
      ValidatedTenure vt = validateOneTenure(i, tenures.get(i), clientNumber);
      validationResults.add(vt.result());
      if (vt.block() != null) {
        resolvedBlocks.put(i, vt.block());
      }
    }

    boolean isValid =
        validationResults.stream().allMatch(TenureValidationResultDto::isValid)
            && duplicates.isEmpty();

    if (isValid) {
      for (int i = 0; i < tenures.size(); i++) {
        TenureRequestDto tenure = tenures.get(i);
        CutBlockEntity block = resolvedBlocks.get(i);
        tenuresWithTimberMark.add(
            new TenureDto(
                tenure.fileId(),
                tenure.cuttingPermit(),
                tenure.cutBlock(),
                tenure.isPrimary(),
                block != null ? block.getTimberMark() : null));
      }
    }

    return new TenureValidationResponseDto(
        validationResults, duplicates, isValid, tenuresWithTimberMark, resolvedBlocks);
  }

  /**
   * Detects duplicate tenures in the submitted list (fileId + cuttingPermit + cutBlock composite
   * key). Does not check the database.
   *
   * @param tenures the list of tenures to check
   * @return duplicate conflicts; empty if none
   */
  public List<DuplicateConflictDto> detectDuplicates(List<TenureRequestDto> tenures) {
    List<DuplicateConflictDto> conflicts = new ArrayList<>();
    Map<String, List<Integer>> keyToIndices = new HashMap<>();

    for (int i = 0; i < tenures.size(); i++) {
      TenureRequestDto tenure = tenures.get(i);
      if (tenure == null || tenure.fileId() == null || tenure.cutBlock() == null) {
        continue;
      }
      keyToIndices.computeIfAbsent(buildTenureKey(tenure), k -> new ArrayList<>()).add(i);
    }

    for (List<Integer> indices : keyToIndices.values()) {
      if (indices.size() > 1) {
        TenureRequestDto first = tenures.get(indices.get(0));
        String reason =
            String.format(
                "fileId=%s, cuttingPermit=%s, cutBlock=%s",
                first.fileId(), first.cuttingPermit(), first.cutBlock());
        conflicts.add(
            new DuplicateConflictDto(
                indices, reason, TenureValidationErrorCode.DUPLICATE_IN_REQUEST));
      }
    }

    return conflicts;
  }

  private ValidatedTenure validateOneTenure(
      int index, TenureRequestDto tenure, String clientNumber) {

    if (tenure == null) {
      return new ValidatedTenure(
          new TenureValidationResultDto(
              index, false, TenureValidationErrorCode.FIELD_INVALID, "tenure: must not be null"),
          null);
    }

    // Step 1: field constraints
    String fieldError = validateFields(tenure);
    if (fieldError != null) {
      return new ValidatedTenure(
          new TenureValidationResultDto(
              index, false, TenureValidationErrorCode.FIELD_INVALID, fieldError),
          null);
    }

    String fileId = tenure.fileId().trim();
    String cutBlock = tenure.cutBlock().trim();
    String cuttingPermit = tenure.cuttingPermit() != null ? tenure.cuttingPermit().trim() : null;
    if (cuttingPermit != null && cuttingPermit.isBlank()) {
      cuttingPermit = null;
    }

    // Step 2: cut block must exist in silva.cut_block
    Optional<CutBlockEntity> blockOpt =
        cutBlockRepository.findByTenure(fileId, cutBlock, cuttingPermit);
    if (blockOpt.isEmpty()) {
      return new ValidatedTenure(
          new TenureValidationResultDto(
              index,
              false,
              TenureValidationErrorCode.TENURE_NOT_FOUND,
              String.format(
                  "Cut block not found: fileId=%s, cuttingPermit=%s, cutBlock=%s",
                  fileId, cuttingPermit, cutBlock)),
          null);
    }

    // Step 3: caller's client must be a licensee for this cut block
    Long cbSkey = blockOpt.get().getCbSkey();
    if (!cutBlockClientRepository.existsByCbSkeyAndClientNumber(cbSkey, clientNumber)) {
      return new ValidatedTenure(
          new TenureValidationResultDto(
              index,
              false,
              TenureValidationErrorCode.CLIENT_NOT_LICENSEE,
              String.format(
                  "Client %s is not a licensee for cut block: fileId=%s, cutBlock=%s",
                  clientNumber, fileId, cutBlock)),
          null);
    }

    // Step 4: tenure must not already be linked to another opening in cut_block_open_admin
    if (cutBlockOpenAdminRepository.existsAllocatedByTenure(fileId, cutBlock, cuttingPermit)) {
      return new ValidatedTenure(
          new TenureValidationResultDto(
              index,
              false,
              TenureValidationErrorCode.TENURE_DUPLICATE_OPENING,
              String.format(
                  "Tenure already linked to an existing opening: fileId=%s, cuttingPermit=%s,"
                      + " cutBlock=%s",
                  fileId, cuttingPermit, cutBlock)),
          null);
    }

    return new ValidatedTenure(
        new TenureValidationResultDto(index, true, null, null), blockOpt.get());
  }

  private String validateFields(TenureRequestDto tenure) {
    if (tenure.fileId() == null || tenure.fileId().isBlank()) {
      return "fileId: must not be blank";
    }
    if (tenure.fileId().length() > 10) {
      return "fileId: size must be between 0 and 10";
    }
    if (tenure.cutBlock() == null || tenure.cutBlock().isBlank()) {
      return "cutBlock: must not be blank";
    }
    if (tenure.cutBlock().length() > 10) {
      return "cutBlock: size must be between 0 and 10";
    }
    if (tenure.cuttingPermit() != null && tenure.cuttingPermit().length() > 3) {
      return "cuttingPermit: size must be between 0 and 3";
    }
    return null;
  }

  private String buildTenureKey(TenureRequestDto tenure) {
    String permit = tenure.cuttingPermit() != null ? tenure.cuttingPermit().trim() : "";
    return String.format("%s|%s|%s", tenure.fileId().trim(), permit, tenure.cutBlock().trim());
  }
}
