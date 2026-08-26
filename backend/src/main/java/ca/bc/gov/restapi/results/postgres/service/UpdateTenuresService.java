package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.exception.NotFoundGenericException;
import ca.bc.gov.restapi.results.common.security.LoggedUserHelper;
import ca.bc.gov.restapi.results.postgres.dto.TenureRemovalValidationResultDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureRequestDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureUpdateItemDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureUpdateValidationResponseDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureValidationResponseDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureValidationResultDto;
import ca.bc.gov.restapi.results.postgres.entity.CutBlockEntity;
import ca.bc.gov.restapi.results.postgres.entity.CutBlockOpenAdminEntity;
import ca.bc.gov.restapi.results.postgres.enums.TenureValidationErrorCode;
import ca.bc.gov.restapi.results.postgres.repository.ActivityTreatmentUnitPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.CutBlockOpenAdminPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningPostgresRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/** Replaces an opening's tenure list in one transaction. */
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class UpdateTenuresService {
  private static final String DISTURBANCE_BASE_CODE = "DN";

  private final OpeningPostgresRepository openingRepository;
  private final CutBlockOpenAdminPostgresRepository cboaRepository;
  private final ActivityTreatmentUnitPostgresRepository activityRepository;
  private final TenureValidationService tenureValidationService;
  private final OpeningTenureAssociationHistoryService historyService;
  private final LoggedUserHelper loggedUserHelper;

  /**
   * Replaces all CBOA associations for an opening in one transaction.
   *
   * <p>Validation and persistence proceed in this order:
   *
   * <ol>
   *   <li>Confirm the opening exists; an unknown opening cannot receive tenure associations.
   *   <li>Require a non-empty final list with exactly one primary tenure.
   *   <li>Run shared tenure validation: field format, duplicates, authorization, cut-block, and
   *       licensee rules. Existing allocations for this same opening are allowed; allocations for
   *       another opening are rejected.
   *   <li>Lock the opening's current CBOA rows before comparing client-supplied identity data.
   *   <li>Require each existing item to belong to this opening, occur only once in the request, and
   *       carry the current revision count; stale or foreign rows are rejected.
   *   <li>When any current tenure is removed or replaced, reject the request if an assigned
   *       disturbance activity has base code {@code DN}; DN blocks all tenure removals.
   *   <li>Update the primary flag for retained CBOA rows and increment revision only when it
   *       changes.
   *   <li>Associate new or replacement tenure keys by reusing an unassociated CBOA when available,
   *       otherwise creating one; record each association in history.
   *   <li>For removed rows, record the unassociation in history, clear the opening association,
   *       clear opening-specific values, and increment the revision count.
   * </ol>
   *
   * @param openingId opening whose tenure list is replaced
   * @param clientNumber client used by shared authorization and licensee validation
   * @param items final submitted list; existing rows carry CBOA ID and revision
   * @return validation body when request is invalid; empty after successful update
   */
  @Transactional
  public Optional<TenureUpdateValidationResponseDto> updateTenures(
      Long openingId, String clientNumber, List<TenureUpdateItemDto> items) {
    // Step 1: target opening must exist.
    if (!openingRepository.existsById(openingId)) {
      throw new NotFoundGenericException("Opening");
    }
    // Step 2: final list must retain exactly one primary tenure.
    if (items.isEmpty()) {
      throw new ResponseStatusException(
          HttpStatus.UNPROCESSABLE_ENTITY, "At least one tenure is required");
    }
    long primaryCount = items.stream().filter(TenureUpdateItemDto::isPrimary).count();
    if (primaryCount != 1) {
      throw new ResponseStatusException(
          HttpStatus.UNPROCESSABLE_ENTITY, "Exactly one primary tenure is required");
    }

    // Step 3: reuse authorization, field, duplicate, cut-block, and licensee validation.
    List<TenureRequestDto> tenures =
        items.stream().map(TenureUpdateItemDto::toTenureRequest).toList();
    TenureValidationResponseDto validation =
        tenureValidationService.validateTenures(tenures, clientNumber, openingId);
    if (!validation.isValid()) {
      return Optional.of(new TenureUpdateValidationResponseDto(validation, List.of()));
    }

    // Step 4: lock current associations before evaluating client identity metadata.
    List<CutBlockOpenAdminEntity> current = cboaRepository.findAllByOpeningId(openingId);
    Map<Long, CutBlockOpenAdminEntity> currentById =
        current.stream().collect(Collectors.toMap(CutBlockOpenAdminEntity::getId, value -> value));
    // Step 5: reject CBOA rows not owned by opening or changed since screen load.
    List<TenureValidationResultDto> identityErrors = validateExistingItems(items, currentById);
    if (!identityErrors.isEmpty()) {
      return Optional.of(
          new TenureUpdateValidationResponseDto(
              withIdentityValidationErrors(validation, identityErrors), List.of()));
    }

    // Step 6: DN activities block every removal or key replacement.
    Set<Long> removedIds = removedIds(items, current);
    if (!removedIds.isEmpty()
        && activityRepository.existsByOpeningIdAndSilvBaseCodeAndCutBlockOpenAdminIdIsNotNull(
            openingId, DISTURBANCE_BASE_CODE)) {
      List<TenureRemovalValidationResultDto> removalErrors =
          removedIds.stream()
              .map(
                  id ->
                      new TenureRemovalValidationResultDto(
                          id,
                          TenureValidationErrorCode.DISTURBANCE_EXISTS,
                          "Tenure cannot be removed while a disturbance activity exists"))
              .toList();
      return Optional.of(new TenureUpdateValidationResponseDto(validation, removalErrors));
    }

    // Step 7: update retained rows before changing membership.
    String userId = loggedUserHelper.getAuditUserId();
    LocalDateTime now = LocalDateTime.now();
    Map<Long, TenureUpdateItemDto> retained = retainedItems(items, currentById);
    for (CutBlockOpenAdminEntity row : current) {
      TenureUpdateItemDto item = retained.get(row.getId());
      if (item != null) {
        updatePrimary(row, item.isPrimary(), userId, now);
        cboaRepository.save(row);
      }
    }
    // Step 8: allocate added and key-replacement rows, recording association history.
    for (int index = 0; index < items.size(); index++) {
      TenureUpdateItemDto item = items.get(index);
      if (item.cboaId() == null || !retained.containsKey(item.cboaId())) {
        CutBlockOpenAdminEntity allocated =
            allocate(openingId, item, validation.resolvedBlocks().get(index), current, userId, now);
        cboaRepository.save(allocated);
        historyService.record("ASSOCIATED", openingId, allocated, userId);
      }
    }
    // Step 9: unassociate removed rows after a replacement primary exists.
    for (Long id : removedIds) {
      CutBlockOpenAdminEntity row = currentById.get(id);
      historyService.record("UNASSOCIATED", openingId, row, userId);
      unassociate(row, userId, now);
      cboaRepository.save(row);
    }
    return Optional.empty();
  }

  /**
   * Validates that each submitted existing CBOA belongs to this opening and is not stale. A row is
   * stale when its submitted revision count differs from the current persisted revision count,
   * meaning another request changed it after the edit screen loaded.
   */
  private List<TenureValidationResultDto> validateExistingItems(
      List<TenureUpdateItemDto> items, Map<Long, CutBlockOpenAdminEntity> currentById) {
    List<TenureValidationResultDto> errors = new ArrayList<>();
    Set<Long> seen = new HashSet<>();
    for (int index = 0; index < items.size(); index++) {
      TenureUpdateItemDto item = items.get(index);
      if (item.cboaId() == null) {
        continue;
      }
      CutBlockOpenAdminEntity current = currentById.get(item.cboaId());
      if (current == null || !seen.add(item.cboaId())) {
        errors.add(
            new TenureValidationResultDto(
                index,
                false,
                TenureValidationErrorCode.TENURE_NOT_ASSOCIATED,
                "Tenure is not associated with this opening"));
      } else if (!Objects.equals(current.getRevisionCount(), item.revisionCount())) {
        errors.add(
            new TenureValidationResultDto(
                index,
                false,
                TenureValidationErrorCode.STALE_TENURE,
                "Tenure was changed by another request"));
      }
    }
    return errors;
  }

  /**
   * Returns current CBOA IDs absent from the submitted list or submitted with a changed tenure key.
   */
  private Set<Long> removedIds(
      List<TenureUpdateItemDto> items, List<CutBlockOpenAdminEntity> current) {
    return current.stream()
        .filter(
            row ->
                items.stream()
                    .noneMatch(
                        item ->
                            item.cboaId() != null
                                && item.cboaId().equals(row.getId())
                                && sameKey(row, item)))
        .map(CutBlockOpenAdminEntity::getId)
        .collect(Collectors.toSet());
  }

  /** Maps submitted rows that retain both their CBOA ID and their tenure key. */
  private Map<Long, TenureUpdateItemDto> retainedItems(
      List<TenureUpdateItemDto> items, Map<Long, CutBlockOpenAdminEntity> currentById) {
    return items.stream()
        .filter(item -> item.cboaId() != null && sameKey(currentById.get(item.cboaId()), item))
        .collect(Collectors.toMap(TenureUpdateItemDto::cboaId, item -> item));
  }

  /** Returns whether the persisted CBOA and submitted item identify the same tenure. */
  private boolean sameKey(CutBlockOpenAdminEntity row, TenureUpdateItemDto item) {
    return row != null
        && Objects.equals(row.getForestFileId(), item.fileId().trim())
        && Objects.equals(row.getCutBlockId(), item.cutBlock().trim())
        && Objects.equals(
            normalizePermit(row.getCuttingPermitId()), normalizePermit(item.cuttingPermit()));
  }

  /** Reuses an unassociated matching CBOA or creates one, then associates it to the opening. */
  private CutBlockOpenAdminEntity allocate(
      Long openingId,
      TenureUpdateItemDto item,
      CutBlockEntity block,
      List<CutBlockOpenAdminEntity> current,
      String userId,
      LocalDateTime now) {
    String permit = normalizePermit(item.cuttingPermit());
    Optional<CutBlockOpenAdminEntity> reusable =
        permit == null
            ? cboaRepository
                .findFirstByForestFileIdAndCutBlockIdAndCuttingPermitIdIsNullAndOpeningIdIsNull(
                    item.fileId().trim(), item.cutBlock().trim())
            : cboaRepository
                .findFirstByForestFileIdAndCutBlockIdAndCuttingPermitIdAndOpeningIdIsNull(
                    item.fileId().trim(), item.cutBlock().trim(), permit);
    CutBlockOpenAdminEntity row = reusable.orElseGet(CutBlockOpenAdminEntity::new);
    row.setOpeningId(openingId);
    row.setForestFileId(item.fileId().trim());
    row.setCutBlockId(item.cutBlock().trim());
    row.setCuttingPermitId(permit);
    row.setTimberMark(block.getTimberMark());
    row.setCbSkey(block.getCbSkey());
    row.setOpeningPrimeLicenceInd(item.isPrimary() ? "Y" : "N");
    row.setOpeningGrossArea(current.isEmpty() ? null : current.get(0).getOpeningGrossArea());
    row.setRevisionCount(row.getRevisionCount() == null ? 1 : row.getRevisionCount() + 1);
    if (row.getEntryUserId() == null) {
      row.setEntryUserId(userId);
      row.setEntryTimestamp(now);
    }
    row.setUpdateUserId(userId);
    row.setUpdateTimestamp(now);
    return row;
  }

  /** Updates a retained row's primary flag and audit fields only when the flag changes. */
  private void updatePrimary(
      CutBlockOpenAdminEntity row, boolean primary, String userId, LocalDateTime now) {
    String value = primary ? "Y" : "N";
    if (!Objects.equals(value, row.getOpeningPrimeLicenceInd())) {
      row.setOpeningPrimeLicenceInd(value);
      row.setRevisionCount(row.getRevisionCount() + 1);
      row.setUpdateUserId(userId);
      row.setUpdateTimestamp(now);
    }
  }

  /** Clears the opening-specific CBOA values while retaining the tenure source record. */
  private void unassociate(CutBlockOpenAdminEntity row, String userId, LocalDateTime now) {
    row.setOpeningId(null);
    row.setOpeningPrimeLicenceInd(null);
    row.setOpeningGrossArea(null);
    row.setDisturbanceGrossArea(null);
    row.setDisturbanceStartDate(null);
    row.setDisturbanceEndDate(null);
    row.setRevisionCount(row.getRevisionCount() + 1);
    row.setUpdateUserId(userId);
    row.setUpdateTimestamp(now);
  }

  /** Converts blank cutting permits to {@code null} for CBOA key comparison and lookup. */
  private String normalizePermit(String value) {
    return value == null || value.isBlank() ? null : value.trim();
  }

  /**
   * Merges stale or foreign-CBOA item errors into an otherwise successful tenure validation. A
   * stale row has a submitted revision count different from its current persisted revision count.
   */
  private TenureValidationResponseDto withIdentityValidationErrors(
      TenureValidationResponseDto valid, List<TenureValidationResultDto> errors) {
    Map<Integer, TenureValidationResultDto> errorByIndex =
        errors.stream()
            .collect(Collectors.toMap(TenureValidationResultDto::tenureIndex, value -> value));
    return new TenureValidationResponseDto(
        valid.validationResults().stream()
            .map(result -> errorByIndex.getOrDefault(result.tenureIndex(), result))
            .toList(),
        valid.duplicateConflicts(),
        false,
        List.of(),
        valid.resolvedBlocks());
  }
}
