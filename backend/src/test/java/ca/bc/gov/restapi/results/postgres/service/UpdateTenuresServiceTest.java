package ca.bc.gov.restapi.results.postgres.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.exception.NotFoundGenericException;
import ca.bc.gov.restapi.results.common.security.LoggedUserHelper;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | UpdateTenuresService")
class UpdateTenuresServiceTest {

  private static final long OPENING_ID = 101L;
  private static final String CLIENT_NUMBER = "12345678";

  @Mock private OpeningPostgresRepository openingRepository;
  @Mock private CutBlockOpenAdminPostgresRepository cboaRepository;
  @Mock private ActivityTreatmentUnitPostgresRepository activityRepository;
  @Mock private TenureValidationService tenureValidationService;
  @Mock private OpeningTenureAssociationHistoryService historyService;
  @Mock private LoggedUserHelper loggedUserHelper;

  private UpdateTenuresService service;

  @BeforeEach
  void setUp() {
    service =
        new UpdateTenuresService(
            openingRepository,
            cboaRepository,
            activityRepository,
            tenureValidationService,
            historyService,
            loggedUserHelper);
    when(openingRepository.existsById(OPENING_ID)).thenReturn(true);
    when(loggedUserHelper.getAuditUserId()).thenReturn("IDIR\\tester");
  }

  @Test
  @DisplayName("Missing opening is rejected before tenure validation")
  void updateTenures_missingOpening_throwsNotFound() {
    when(openingRepository.existsById(OPENING_ID)).thenReturn(false);

    assertThatThrownBy(
            () -> service.updateTenures(OPENING_ID, CLIENT_NUMBER, List.of(item(null, null, true))))
        .isInstanceOf(NotFoundGenericException.class);
    verify(tenureValidationService, never()).validateTenures(any(), anyString(), anyLong());
  }

  @Test
  @DisplayName("Empty list and non-exact primary counts are rejected")
  void updateTenures_invalidPrimaryList_throws422() {
    assertThatThrownBy(() -> service.updateTenures(OPENING_ID, CLIENT_NUMBER, List.of()))
        .isInstanceOf(ResponseStatusException.class)
        .satisfies(
            exception ->
                assertThat(((ResponseStatusException) exception).getStatusCode())
                    .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY));

    assertThatThrownBy(
            () ->
                service.updateTenures(
                    OPENING_ID,
                    CLIENT_NUMBER,
                    List.of(item(null, null, false), item(null, null, false))))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining("Exactly one primary");
  }

  @Test
  @DisplayName("Shared tenure validation failure returns its item errors without persistence")
  void updateTenures_sharedValidationFailure_returnsValidationBody() {
    List<TenureUpdateItemDto> items = List.of(item(null, null, true));
    TenureValidationResponseDto invalid = validation(false, items);
    when(tenureValidationService.validateTenures(any(), eq(CLIENT_NUMBER), eq(OPENING_ID)))
        .thenReturn(invalid);

    Optional<TenureUpdateValidationResponseDto> response =
        service.updateTenures(OPENING_ID, CLIENT_NUMBER, items);

    assertThat(response).isPresent();
    assertThat(response.orElseThrow().tenureValidation()).isSameAs(invalid);
    verify(cboaRepository, never()).findAllByOpeningId(anyLong());
    verify(cboaRepository, never()).save(any());
  }

  @Test
  @DisplayName("Foreign, duplicated, and stale CBOA IDs return item validation errors")
  void updateTenures_invalidExistingIdentity_returnsItemErrors() {
    CutBlockOpenAdminEntity current = cboa(1L, "FILE1", "CP1", "BLOCK1", "Y", 3);
    List<TenureUpdateItemDto> items =
        List.of(item(1L, 2, true), item(9L, 1, false), item(9L, 1, false));
    when(tenureValidationService.validateTenures(any(), eq(CLIENT_NUMBER), eq(OPENING_ID)))
        .thenReturn(validation(true, items));
    when(cboaRepository.findAllByOpeningId(OPENING_ID)).thenReturn(List.of(current));

    TenureUpdateValidationResponseDto response =
        service.updateTenures(OPENING_ID, CLIENT_NUMBER, items).orElseThrow();

    assertThat(response.tenureValidation().isValid()).isFalse();
    assertThat(response.tenureValidation().validationResults())
        .extracting(TenureValidationResultDto::errorCode)
        .containsExactly(
            TenureValidationErrorCode.STALE_TENURE,
            TenureValidationErrorCode.TENURE_NOT_ASSOCIATED,
            TenureValidationErrorCode.TENURE_NOT_ASSOCIATED);
    verify(cboaRepository, never()).save(any());
  }

  @Test
  @DisplayName("DN activity blocks a removed tenure and leaves CBOA rows unchanged")
  void updateTenures_dnWithRemoval_returnsRemovalError() {
    CutBlockOpenAdminEntity retained = cboa(1L, "FILE1", "CP1", "BLOCK1", "Y", 1);
    CutBlockOpenAdminEntity removed = cboa(2L, "FILE2", null, "BLOCK2", "N", 1);
    List<TenureUpdateItemDto> items = List.of(item(1L, 1, true));
    when(tenureValidationService.validateTenures(any(), eq(CLIENT_NUMBER), eq(OPENING_ID)))
        .thenReturn(validation(true, items));
    when(cboaRepository.findAllByOpeningId(OPENING_ID)).thenReturn(List.of(retained, removed));
    when(activityRepository.existsByOpeningIdAndSilvBaseCodeAndCutBlockOpenAdminIdIsNotNull(
            OPENING_ID, "DN"))
        .thenReturn(true);

    TenureUpdateValidationResponseDto response =
        service.updateTenures(OPENING_ID, CLIENT_NUMBER, items).orElseThrow();

    assertThat(response.removalErrors())
        .singleElement()
        .satisfies(
            error -> {
              assertThat(error.cboaId()).isEqualTo(2L);
              assertThat(error.errorCode()).isEqualTo(TenureValidationErrorCode.DISTURBANCE_EXISTS);
            });
    verify(cboaRepository, never()).save(any());
    verify(historyService, never()).record(anyString(), anyLong(), any(), anyString());
  }

  @Test
  @DisplayName("Primary-only change remains allowed when DN activity exists")
  void updateTenures_dnWithoutRemoval_updatesPrimary() {
    CutBlockOpenAdminEntity first = cboa(1L, "FILE1", "CP1", "BLOCK1", "Y", 1);
    CutBlockOpenAdminEntity second = cboa(2L, "FILE2", null, "BLOCK2", "N", 1);
    List<TenureUpdateItemDto> items = List.of(item(1L, 1, false), item(2L, 1, true));
    when(tenureValidationService.validateTenures(any(), eq(CLIENT_NUMBER), eq(OPENING_ID)))
        .thenReturn(validation(true, items));
    when(cboaRepository.findAllByOpeningId(OPENING_ID)).thenReturn(List.of(first, second));

    assertThat(service.updateTenures(OPENING_ID, CLIENT_NUMBER, items)).isEmpty();

    assertThat(first.getOpeningPrimeLicenceInd()).isEqualTo("N");
    assertThat(second.getOpeningPrimeLicenceInd()).isEqualTo("Y");
    assertThat(first.getRevisionCount()).isEqualTo(2);
    assertThat(second.getRevisionCount()).isEqualTo(2);
    verify(activityRepository, never())
        .existsByOpeningIdAndSilvBaseCodeAndCutBlockOpenAdminIdIsNotNull(anyLong(), anyString());
  }

  @Test
  @DisplayName("Replacement associates a new CBOA before unassociating old CBOA")
  void updateTenures_replacement_recordsBothHistoryEvents() {
    CutBlockOpenAdminEntity old = cboa(1L, "FILE1", "CP1", "BLOCK1", "Y", 2);
    List<TenureUpdateItemDto> items =
        List.of(new TenureUpdateItemDto(1L, 2, "FILE2", null, "BLOCK2", true));
    when(tenureValidationService.validateTenures(any(), eq(CLIENT_NUMBER), eq(OPENING_ID)))
        .thenReturn(validation(true, items));
    when(cboaRepository.findAllByOpeningId(OPENING_ID)).thenReturn(List.of(old));
    when(activityRepository.existsByOpeningIdAndSilvBaseCodeAndCutBlockOpenAdminIdIsNotNull(
            OPENING_ID, "DN"))
        .thenReturn(false);
    when(cboaRepository
            .findFirstByForestFileIdAndCutBlockIdAndCuttingPermitIdIsNullAndOpeningIdIsNull(
                "FILE2", "BLOCK2"))
        .thenReturn(Optional.empty());

    assertThat(service.updateTenures(OPENING_ID, CLIENT_NUMBER, items)).isEmpty();

    ArgumentCaptor<CutBlockOpenAdminEntity> saved =
        ArgumentCaptor.forClass(CutBlockOpenAdminEntity.class);
    verify(cboaRepository, org.mockito.Mockito.times(2)).save(saved.capture());
    assertThat(saved.getAllValues().get(0).getOpeningId()).isEqualTo(OPENING_ID);
    assertThat(saved.getAllValues().get(1).getOpeningId()).isNull();
    verify(historyService).record(eq("ASSOCIATED"), eq(OPENING_ID), any(), anyString());
    verify(historyService).record(eq("UNASSOCIATED"), eq(OPENING_ID), eq(old), anyString());
    assertThat(old.getRevisionCount()).isEqualTo(3);
  }

  @Test
  @DisplayName("New tenure reuses an unassociated matching CBOA")
  void updateTenures_newTenure_reusesUnassociatedCboa() {
    CutBlockOpenAdminEntity reusable = cboa(99L, "FILE1", null, "BLOCK1", null, 4);
    List<TenureUpdateItemDto> items = List.of(item(null, null, true));
    when(tenureValidationService.validateTenures(any(), eq(CLIENT_NUMBER), eq(OPENING_ID)))
        .thenReturn(validation(true, items));
    when(cboaRepository.findAllByOpeningId(OPENING_ID)).thenReturn(List.of());
    when(cboaRepository
            .findFirstByForestFileIdAndCutBlockIdAndCuttingPermitIdIsNullAndOpeningIdIsNull(
                "FILE1", "BLOCK1"))
        .thenReturn(Optional.of(reusable));

    assertThat(service.updateTenures(OPENING_ID, CLIENT_NUMBER, items)).isEmpty();

    assertThat(reusable.getOpeningId()).isEqualTo(OPENING_ID);
    assertThat(reusable.getRevisionCount()).isEqualTo(5);
    verify(historyService).record("ASSOCIATED", OPENING_ID, reusable, "IDIR\\tester");
  }

  private TenureUpdateItemDto item(Long cboaId, Integer revisionCount, boolean primary) {
    return new TenureUpdateItemDto(cboaId, revisionCount, "FILE1", "CP1", "BLOCK1", primary);
  }

  private CutBlockOpenAdminEntity cboa(
      Long id, String fileId, String permit, String block, String primary, int revision) {
    return CutBlockOpenAdminEntity.builder()
        .id(id)
        .openingId(OPENING_ID)
        .forestFileId(fileId)
        .cuttingPermitId(permit)
        .cutBlockId(block)
        .openingPrimeLicenceInd(primary)
        .revisionCount(revision)
        .openingGrossArea(new BigDecimal("10"))
        .build();
  }

  private TenureValidationResponseDto validation(boolean valid, List<TenureUpdateItemDto> items) {
    List<TenureValidationResultDto> results =
        java.util.stream.IntStream.range(0, items.size())
            .mapToObj(index -> new TenureValidationResultDto(index, valid, null, null))
            .toList();
    Map<Integer, CutBlockEntity> blocks =
        java.util.stream.IntStream.range(0, items.size())
            .boxed()
            .collect(
                java.util.stream.Collectors.toMap(
                    index -> index,
                    index -> CutBlockEntity.builder().cbSkey(1L).timberMark("TM").build()));
    return new TenureValidationResponseDto(results, List.of(), valid, List.of(), blocks);
  }
}
