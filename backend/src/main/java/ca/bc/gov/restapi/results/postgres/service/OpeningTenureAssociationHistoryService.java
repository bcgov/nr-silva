package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.postgres.entity.CutBlockOpenAdminEntity;
import ca.bc.gov.restapi.results.postgres.entity.OpeningTenureAssociationHistoryEntity;
import ca.bc.gov.restapi.results.postgres.repository.OpeningTenureAssociationHistoryPostgresRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class OpeningTenureAssociationHistoryService {
  private final OpeningTenureAssociationHistoryPostgresRepository repository;

  /**
   * Appends an immutable record of one CBOA association change.
   *
   * @param action {@code ASSOCIATED} or {@code UNASSOCIATED}
   * @param openingId opening involved in change
   * @param tenure CBOA state to snapshot
   * @param userId user performing change
   */
  public void record(String action, Long openingId, CutBlockOpenAdminEntity tenure, String userId) {
    repository.save(
        OpeningTenureAssociationHistoryEntity.builder()
            .cutBlockOpenAdminId(tenure.getId())
            .openingId(openingId)
            .action(action)
            .forestFileId(tenure.getForestFileId())
            .cuttingPermitId(tenure.getCuttingPermitId())
            .cutBlockId(tenure.getCutBlockId())
            .timberMark(tenure.getTimberMark())
            .openingPrimeLicenceInd(tenure.getOpeningPrimeLicenceInd())
            .cboaRevisionCount(tenure.getRevisionCount())
            .eventUserid(userId)
            .eventTimestamp(LocalDateTime.now())
            .build());
  }
}
