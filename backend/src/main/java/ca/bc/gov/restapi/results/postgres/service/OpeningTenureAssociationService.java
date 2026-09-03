package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.postgres.dto.TenureRequestDto;
import ca.bc.gov.restapi.results.postgres.entity.CutBlockEntity;
import ca.bc.gov.restapi.results.postgres.entity.CutBlockOpenAdminEntity;
import ca.bc.gov.restapi.results.postgres.entity.activity.ActivityTreatmentUnitEntity;
import ca.bc.gov.restapi.results.postgres.entity.opening.OpeningEntity;
import ca.bc.gov.restapi.results.postgres.repository.ActivityTreatmentUnitPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.CutBlockOpenAdminPostgresRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/** Shares PostgreSQL CBOA association and derived-disturbance reconciliation for tenure changes. */
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class OpeningTenureAssociationService {
  private static final String DISTURBANCE_BASE_CODE = "DN";

  private final CutBlockOpenAdminPostgresRepository cboaRepository;
  private final ActivityTreatmentUnitPostgresRepository activityRepository;

  /**
   * Associates a tenure with an opening by reusing an available CBOA row or creating one.
   *
   * <p>Planned fields on a reused CBOA are intentionally retained. Opening-specific fields are
   * refreshed from the Silva opening record.
   */
  public CutBlockOpenAdminEntity associate(
      OpeningEntity opening,
      TenureRequestDto tenure,
      CutBlockEntity block,
      String userId,
      LocalDateTime now) {
    String fileId = tenure.fileId().trim();
    String cutBlockId = tenure.cutBlock().trim();
    String cuttingPermitId = normalizePermit(tenure.cuttingPermit());
    Optional<CutBlockOpenAdminEntity> reusable =
        cuttingPermitId == null
            ? cboaRepository
                .findFirstByForestFileIdAndCutBlockIdAndCuttingPermitIdIsNullAndOpeningIdIsNull(
                    fileId, cutBlockId)
            : cboaRepository
                .findFirstByForestFileIdAndCutBlockIdAndCuttingPermitIdAndOpeningIdIsNull(
                    fileId, cutBlockId, cuttingPermitId);

    CutBlockOpenAdminEntity row = reusable.orElseGet(CutBlockOpenAdminEntity::new);
    row.setOpeningId(opening.getId());
    row.setForestFileId(fileId);
    row.setCutBlockId(cutBlockId);
    row.setCuttingPermitId(cuttingPermitId);
    row.setTimberMark(block.getTimberMark());
    row.setCbSkey(block.getCbSkey());
    row.setOpeningPrimeLicenceInd(tenure.isPrimary() ? "Y" : "N");
    row.setOpeningGrossArea(opening.getOpeningGrossArea());
    row.setRevisionCount(row.getRevisionCount() == null ? 1 : row.getRevisionCount() + 1);
    if (row.getEntryUserId() == null) {
      row.setEntryUserId(userId);
      row.setEntryTimestamp(now);
    }
    row.setUpdateUserId(userId);
    row.setUpdateTimestamp(now);
    return cboaRepository.save(row);
  }

  /**
   * Recalculates CBOA values derived from DN activities and synchronizes the opening gross area.
   *
   * <p>An absent DN amount remains {@code null}; an actual zero amount remains {@code 0}. Values
   * from unassigned DN activities are included on the primary CBOA. Every final CBOA is updated and
   * has its revision incremented, matching RESULTS reconciliation behavior.
   */
  public void reconcile(
      OpeningEntity opening,
      List<CutBlockOpenAdminEntity> finalTenures,
      BigDecimal legacyCboaGrossArea,
      String userId,
      LocalDateTime now) {
    BigDecimal openingGrossArea =
        opening.getOpeningGrossArea() != null ? opening.getOpeningGrossArea() : legacyCboaGrossArea;
    List<ActivityTreatmentUnitEntity> disturbances =
        activityRepository.findByOpeningIdAndSilvBaseCode(opening.getId(), DISTURBANCE_BASE_CODE);
    DisturbanceSummary unassigned =
        summarizeDisturbance(
            disturbances.stream()
                .filter(activity -> activity.getCutBlockOpenAdminId() == null)
                .toList());

    CutBlockOpenAdminEntity primary =
        finalTenures.stream()
            .filter(row -> "Y".equals(row.getOpeningPrimeLicenceInd()))
            .findFirst()
            .orElse(null);
    for (CutBlockOpenAdminEntity row : finalTenures) {
      DisturbanceSummary summary =
          summarizeDisturbance(
              disturbances.stream()
                  .filter(activity -> row.getId().equals(activity.getCutBlockOpenAdminId()))
                  .toList());
      if (row == primary) {
        summary = summary.combine(unassigned);
      }
      row.setOpeningGrossArea(openingGrossArea);
      row.setDisturbanceGrossArea(summary.amount());
      row.setDisturbanceStartDate(summary.startDate());
      row.setDisturbanceEndDate(summary.endDate());
      row.setRevisionCount(row.getRevisionCount() == null ? 1 : row.getRevisionCount() + 1);
      row.setUpdateUserId(userId);
      row.setUpdateTimestamp(now);
      cboaRepository.save(row);
    }
  }

  private DisturbanceSummary summarizeDisturbance(List<ActivityTreatmentUnitEntity> activities) {
    BigDecimal amount =
        activities.stream()
            .map(ActivityTreatmentUnitEntity::getTreatmentAmount)
            .filter(java.util.Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    boolean hasAmount =
        activities.stream().anyMatch(activity -> activity.getTreatmentAmount() != null);
    LocalDate startDate =
        activities.stream()
            .map(ActivityTreatmentUnitEntity::getAtuStartDate)
            .filter(java.util.Objects::nonNull)
            .map(LocalDateTime::toLocalDate)
            .min(Comparator.naturalOrder())
            .orElse(null);
    LocalDate endDate =
        activities.stream()
            .map(ActivityTreatmentUnitEntity::getAtuCompletionDate)
            .filter(java.util.Objects::nonNull)
            .map(LocalDateTime::toLocalDate)
            .max(Comparator.naturalOrder())
            .orElse(null);
    return new DisturbanceSummary(hasAmount ? amount : null, startDate, endDate);
  }

  private String normalizePermit(String value) {
    return value == null || value.isBlank() ? null : value.trim();
  }

  private record DisturbanceSummary(BigDecimal amount, LocalDate startDate, LocalDate endDate) {
    private DisturbanceSummary combine(DisturbanceSummary other) {
      BigDecimal combinedAmount =
          amount == null ? other.amount : other.amount == null ? amount : amount.add(other.amount);
      LocalDate combinedStart =
          startDate == null
              ? other.startDate
              : other.startDate == null || !other.startDate.isBefore(startDate)
                  ? startDate
                  : other.startDate;
      LocalDate combinedEnd =
          endDate == null
              ? other.endDate
              : other.endDate == null || !other.endDate.isAfter(endDate) ? endDate : other.endDate;
      return new DisturbanceSummary(combinedAmount, combinedStart, combinedEnd);
    }
  }
}
