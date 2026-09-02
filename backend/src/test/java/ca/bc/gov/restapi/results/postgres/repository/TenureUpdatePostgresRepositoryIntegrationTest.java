package ca.bc.gov.restapi.results.postgres.repository;

import static org.assertj.core.api.Assertions.assertThat;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.postgres.entity.OpeningTenureAssociationHistoryEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@DisplayName("Integration Test | Tenure update PostgreSQL repositories")
class TenureUpdatePostgresRepositoryIntegrationTest extends AbstractTestContainerIntegrationTest {

  @Autowired private CutBlockOpenAdminPostgresRepository cboaRepository;
  @Autowired private ActivityTreatmentUnitPostgresRepository activityRepository;
  @Autowired private OpeningTenureAssociationHistoryPostgresRepository historyRepository;

  @Test
  @DisplayName("Duplicate check allows same opening and rejects another opening")
  void existsAllocatedByTenureForAnotherOpening_distinguishesOpening() {
    assertThat(
            cboaRepository.existsAllocatedByTenureForAnotherOpening(
                "A72929", "024", "001", 1589595L))
        .isFalse();
    assertThat(cboaRepository.existsAllocatedByTenureForAnotherOpening("A72929", "024", "001", -1L))
        .isTrue();
  }

  @Test
  @DisplayName("DN check requires an activity assigned to a CBOA")
  void existsDnActivity_requiresAssignedCboa() {
    assertThat(
            activityRepository.existsByOpeningIdAndSilvBaseCodeAndCutBlockOpenAdminIdIsNotNull(
                1589595L, "DN"))
        .isTrue();
    assertThat(
            activityRepository.existsByOpeningIdAndSilvBaseCodeAndCutBlockOpenAdminIdIsNotNull(
                60000L, "DN"))
        .isFalse();
  }

  @Test
  @DisplayName("History persists the CBOA revision snapshot column")
  void history_savePersistsCboaRevisionSnapshot() {
    OpeningTenureAssociationHistoryEntity saved =
        historyRepository.save(
            OpeningTenureAssociationHistoryEntity.builder()
                .cutBlockOpenAdminId(258074L)
                .openingId(1589595L)
                .action("ASSOCIATED")
                .cboaRevisionCount(70)
                .eventUserid("IDIR\\tester")
                .eventTimestamp(java.time.LocalDateTime.now())
                .build());

    assertThat(saved.getId()).isNotNull();
    assertThat(historyRepository.findById(saved.getId()).orElseThrow().getCboaRevisionCount())
        .isEqualTo(70);
  }
}
