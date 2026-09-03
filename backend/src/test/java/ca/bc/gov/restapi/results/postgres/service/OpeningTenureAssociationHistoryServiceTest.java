package ca.bc.gov.restapi.results.postgres.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import ca.bc.gov.restapi.results.postgres.entity.CutBlockOpenAdminEntity;
import ca.bc.gov.restapi.results.postgres.entity.OpeningTenureAssociationHistoryEntity;
import ca.bc.gov.restapi.results.postgres.repository.OpeningTenureAssociationHistoryPostgresRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | OpeningTenureAssociationHistoryService")
class OpeningTenureAssociationHistoryServiceTest {

  @Mock private OpeningTenureAssociationHistoryPostgresRepository repository;

  @Test
  @DisplayName("History stores CBOA revision as a snapshot, not a history revision")
  void record_snapshotsCboaRevision() {
    OpeningTenureAssociationHistoryService service =
        new OpeningTenureAssociationHistoryService(repository);
    CutBlockOpenAdminEntity tenure =
        CutBlockOpenAdminEntity.builder()
            .id(44L)
            .forestFileId("A12345")
            .cutBlockId("1")
            .openingPrimeLicenceInd("Y")
            .revisionCount(7)
            .build();

    service.record("ASSOCIATED", 101L, tenure, "IDIR\\tester");

    ArgumentCaptor<OpeningTenureAssociationHistoryEntity> saved =
        ArgumentCaptor.forClass(OpeningTenureAssociationHistoryEntity.class);
    verify(repository).save(saved.capture());
    assertThat(saved.getValue())
        .extracting(
            OpeningTenureAssociationHistoryEntity::getCutBlockOpenAdminId,
            OpeningTenureAssociationHistoryEntity::getOpeningId,
            OpeningTenureAssociationHistoryEntity::getAction,
            OpeningTenureAssociationHistoryEntity::getCboaRevisionCount,
            OpeningTenureAssociationHistoryEntity::getEventUserid)
        .containsExactly(44L, 101L, "ASSOCIATED", 7, "IDIR\\tester");
    assertThat(saved.getValue().getEventTimestamp()).isNotNull();
  }
}
