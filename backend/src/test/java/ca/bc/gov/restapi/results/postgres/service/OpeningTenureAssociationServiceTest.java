package ca.bc.gov.restapi.results.postgres.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | OpeningTenureAssociationService")
class OpeningTenureAssociationServiceTest {

  @Mock private CutBlockOpenAdminPostgresRepository cboaRepository;
  @Mock private ActivityTreatmentUnitPostgresRepository activityRepository;

  private OpeningTenureAssociationService service;

  @BeforeEach
  void setUp() {
    service = new OpeningTenureAssociationService(cboaRepository, activityRepository);
    when(cboaRepository.save(any(CutBlockOpenAdminEntity.class))).thenAnswer(call -> call.getArgument(0));
  }

  @Test
  @DisplayName("Association copies the Silva-owned opening gross area")
  void associate_copiesOpeningGrossArea() {
    OpeningEntity opening = OpeningEntity.builder().id(1L).openingGrossArea(new BigDecimal("12.3456")).build();
    TenureRequestDto tenure = new TenureRequestDto("FILE", " CP ", "BLOCK", true);
    CutBlockEntity block = CutBlockEntity.builder().cbSkey(5L).timberMark("TM").build();
    when(cboaRepository
            .findFirstByForestFileIdAndCutBlockIdAndCuttingPermitIdAndOpeningIdIsNull(
                "FILE", "BLOCK", "CP"))
        .thenReturn(Optional.empty());

    CutBlockOpenAdminEntity associated =
        service.associate(opening, tenure, block, "tester", LocalDateTime.of(2026, 1, 1, 0, 0));

    assertThat(associated.getOpeningGrossArea()).isEqualByComparingTo("12.3456");
    assertThat(associated.getOpeningPrimeLicenceInd()).isEqualTo("Y");
    assertThat(associated.getRevisionCount()).isEqualTo(1);
  }

  @Test
  @DisplayName("Reconciliation clears derived DN values when no DN value exists")
  void reconcile_noDnValues_setsNullDisturbanceArea() {
    OpeningEntity opening = OpeningEntity.builder().id(1L).openingGrossArea(new BigDecimal("12.3456")).build();
    CutBlockOpenAdminEntity primary =
        CutBlockOpenAdminEntity.builder()
            .id(10L)
            .openingPrimeLicenceInd("Y")
            .disturbanceGrossArea(BigDecimal.ONE)
            .revisionCount(2)
            .build();
    when(activityRepository.findByOpeningIdAndSilvBaseCode(1L, "DN")).thenReturn(List.of());

    service.reconcile(opening, List.of(primary), null, "tester", LocalDateTime.of(2026, 1, 1, 0, 0));

    assertThat(primary.getDisturbanceGrossArea()).isNull();
    assertThat(primary.getDisturbanceStartDate()).isNull();
    assertThat(primary.getDisturbanceEndDate()).isNull();
    assertThat(primary.getRevisionCount()).isEqualTo(3);
  }

  @Test
  @DisplayName("Reconciliation retains an explicit zero DN amount")
  void reconcile_zeroDnAmount_setsZero() {
    OpeningEntity opening = OpeningEntity.builder().id(1L).openingGrossArea(new BigDecimal("12.3456")).build();
    CutBlockOpenAdminEntity primary =
        CutBlockOpenAdminEntity.builder().id(10L).openingPrimeLicenceInd("Y").revisionCount(2).build();
    ActivityTreatmentUnitEntity disturbance =
        ActivityTreatmentUnitEntity.builder()
            .openingId(1L)
            .silvBaseCode("DN")
            .cutBlockOpenAdminId(null)
            .treatmentAmount(BigDecimal.ZERO)
            .atuStartDate(LocalDateTime.of(2026, 1, 2, 9, 0))
            .atuCompletionDate(LocalDateTime.of(2026, 1, 3, 17, 0))
            .build();
    when(activityRepository.findByOpeningIdAndSilvBaseCode(1L, "DN"))
        .thenReturn(List.of(disturbance));

    service.reconcile(opening, List.of(primary), null, "tester", LocalDateTime.of(2026, 1, 1, 0, 0));

    assertThat(primary.getDisturbanceGrossArea()).isEqualByComparingTo(BigDecimal.ZERO);
    assertThat(primary.getDisturbanceStartDate()).isEqualTo(LocalDate.of(2026, 1, 2));
    assertThat(primary.getDisturbanceEndDate()).isEqualTo(LocalDate.of(2026, 1, 3));
    verify(cboaRepository).save(primary);
  }
}
