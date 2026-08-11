package ca.bc.gov.restapi.results.postgres.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.security.LoggedUserHelper;
import ca.bc.gov.restapi.results.postgres.dto.DuplicateConflictDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureRequestDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureValidationResponseDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureValidationResultDto;
import ca.bc.gov.restapi.results.postgres.entity.CutBlockEntity;
import ca.bc.gov.restapi.results.postgres.enums.TenureValidationErrorCode;
import ca.bc.gov.restapi.results.postgres.repository.CutBlockClientPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.CutBlockOpenAdminPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.CutBlockPostgresRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Unit Test | TenureValidationService")
class TenureValidationServiceTest {

  @Mock private LoggedUserHelper loggedUserHelper;
  @Mock private CutBlockPostgresRepository cutBlockRepository;
  @Mock private CutBlockClientPostgresRepository cutBlockClientRepository;
  @Mock private CutBlockOpenAdminPostgresRepository cutBlockOpenAdminRepository;

  private TenureValidationService service;

  private static final String CLIENT_NUMBER = "12345678";
  private static final CutBlockEntity DUMMY_BLOCK =
      CutBlockEntity.builder().cbSkey(99L).timberMark("TM001").build();

  @BeforeEach
  void setUp() {
    service =
        new TenureValidationService(
            loggedUserHelper,
            cutBlockRepository,
            cutBlockClientRepository,
            cutBlockOpenAdminRepository);
    // happy-path stubs
    when(loggedUserHelper.hasRoleMatching(any())).thenReturn(true);
    when(cutBlockRepository.findByTenure(anyString(), anyString(), any()))
        .thenReturn(Optional.of(DUMMY_BLOCK));
    when(cutBlockClientRepository.existsByCbSkeyAndClientNumber(anyLong(), anyString()))
        .thenReturn(true);
    when(cutBlockOpenAdminRepository.existsAllocatedByTenure(anyString(), anyString(), any()))
        .thenReturn(false);
  }

  // ========== validateTenures() — happy path ==========

  @Test
  @DisplayName("All valid tenures with no duplicates should return isValid=true")
  void validateTenures_allValid_returnsIsValidTrue() {
    List<TenureRequestDto> tenures =
        List.of(
            new TenureRequestDto("TFL001", "CP1", "CB001", true),
            new TenureRequestDto("TFL002", null, "CB002", false));

    TenureValidationResponseDto result = service.validateTenures(tenures, CLIENT_NUMBER);

    assertThat(result.isValid()).isTrue();
    assertThat(result.duplicateConflicts()).isEmpty();
    assertThat(result.validationResults()).hasSize(2);
    assertThat(result.validationResults()).allMatch(TenureValidationResultDto::isValid);
  }

  @Test
  @DisplayName("Empty tenure list should return isValid=true with no results and no conflicts")
  void validateTenures_emptyList_returnsIsValidTrue() {
    TenureValidationResponseDto result = service.validateTenures(List.of(), CLIENT_NUMBER);

    assertThat(result.isValid()).isTrue();
    assertThat(result.validationResults()).isEmpty();
    assertThat(result.duplicateConflicts()).isEmpty();
  }

  // ========== validateTenures() — field errors ==========

  @Test
  @DisplayName("Blank fileId should return errorCode FIELD_INVALID and errorMessage with 'fileId'")
  void validateTenures_blankFileId_returnsFieldError() {
    List<TenureRequestDto> tenures = List.of(new TenureRequestDto("  ", null, "CB001", true));

    TenureValidationResponseDto result = service.validateTenures(tenures, CLIENT_NUMBER);

    assertThat(result.isValid()).isFalse();
    TenureValidationResultDto r = result.validationResults().get(0);
    assertThat(r.isValid()).isFalse();
    assertThat(r.errorCode()).isEqualTo(TenureValidationErrorCode.FIELD_INVALID);
    assertThat(r.errorMessage()).contains("fileId");
  }

  @Test
  @DisplayName("fileId exceeding 10 chars should return FIELD_INVALID")
  void validateTenures_fileIdTooLong_returnsFieldError() {
    List<TenureRequestDto> tenures =
        List.of(new TenureRequestDto("TOOLONGFILEID", null, "CB001", true));

    TenureValidationResponseDto result = service.validateTenures(tenures, CLIENT_NUMBER);

    assertThat(result.isValid()).isFalse();
    assertThat(result.validationResults().get(0).errorCode())
        .isEqualTo(TenureValidationErrorCode.FIELD_INVALID);
    assertThat(result.validationResults().get(0).errorMessage()).contains("fileId");
  }

  @Test
  @DisplayName("Blank cutBlock should return FIELD_INVALID with 'cutBlock' in errorMessage")
  void validateTenures_blankCutBlock_returnsFieldError() {
    List<TenureRequestDto> tenures = List.of(new TenureRequestDto("TFL001", null, "", true));

    TenureValidationResponseDto result = service.validateTenures(tenures, CLIENT_NUMBER);

    assertThat(result.isValid()).isFalse();
    assertThat(result.validationResults().get(0).errorMessage()).contains("cutBlock");
  }

  @Test
  @DisplayName("cutBlock exceeding 10 chars should return FIELD_INVALID")
  void validateTenures_cutBlockTooLong_returnsFieldError() {
    List<TenureRequestDto> tenures =
        List.of(new TenureRequestDto("TFL001", null, "TOOLONGBLOCK", true));

    TenureValidationResponseDto result = service.validateTenures(tenures, CLIENT_NUMBER);

    assertThat(result.isValid()).isFalse();
    assertThat(result.validationResults().get(0).errorMessage()).contains("cutBlock");
  }

  @Test
  @DisplayName("cuttingPermit exceeding 3 chars should return FIELD_INVALID")
  void validateTenures_cuttingPermitTooLong_returnsFieldError() {
    List<TenureRequestDto> tenures = List.of(new TenureRequestDto("TFL001", "LONG", "CB001", true));

    TenureValidationResponseDto result = service.validateTenures(tenures, CLIENT_NUMBER);

    assertThat(result.isValid()).isFalse();
    assertThat(result.validationResults().get(0).errorMessage()).contains("cuttingPermit");
  }

  @Test
  @DisplayName("Null cuttingPermit should be valid (optional field)")
  void validateTenures_nullCuttingPermit_isValid() {
    List<TenureRequestDto> tenures = List.of(new TenureRequestDto("TFL001", null, "CB001", true));

    TenureValidationResponseDto result = service.validateTenures(tenures, CLIENT_NUMBER);

    assertThat(result.isValid()).isTrue();
    assertThat(result.validationResults().get(0).isValid()).isTrue();
    assertThat(result.validationResults().get(0).errorMessage()).isNull();
  }

  @Test
  @DisplayName("Mixed valid/invalid tenures should make overall isValid=false")
  void validateTenures_oneValidOneInvalid_overallIsValidFalse() {
    List<TenureRequestDto> tenures =
        List.of(
            new TenureRequestDto("TFL001", null, "CB001", true),
            new TenureRequestDto("", null, "CB002", false));

    TenureValidationResponseDto result = service.validateTenures(tenures, CLIENT_NUMBER);

    assertThat(result.isValid()).isFalse();
    assertThat(result.validationResults().get(0).isValid()).isTrue();
    assertThat(result.validationResults().get(1).isValid()).isFalse();
  }

  // ========== validateTenures() — auth ==========

  @Test
  @DisplayName("JWT without matching client role should throw 403")
  void validateTenures_jwtRoleMismatch_throws403() {
    when(loggedUserHelper.hasRoleMatching(any())).thenReturn(false);

    List<TenureRequestDto> tenures = List.of(new TenureRequestDto("TFL001", null, "CB001", true));

    assertThatThrownBy(() -> service.validateTenures(tenures, CLIENT_NUMBER))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining("403");
  }

  // ========== validateTenures() — DB checks ==========

  @Test
  @DisplayName("Cut block not found in DB should return TENURE_NOT_FOUND")
  void validateTenures_blockNotFound_returnsTenureNotFound() {
    when(cutBlockRepository.findByTenure(anyString(), anyString(), any()))
        .thenReturn(Optional.empty());

    List<TenureRequestDto> tenures = List.of(new TenureRequestDto("TFL001", null, "CB001", true));

    TenureValidationResponseDto result = service.validateTenures(tenures, CLIENT_NUMBER);

    assertThat(result.isValid()).isFalse();
    assertThat(result.validationResults().get(0).errorCode())
        .isEqualTo(TenureValidationErrorCode.TENURE_NOT_FOUND);
  }

  @Test
  @DisplayName("Client not licensee for cut block should return CLIENT_NOT_LICENSEE")
  void validateTenures_clientNotLicensee_returnsClientNotLicensee() {
    when(cutBlockClientRepository.existsByCbSkeyAndClientNumber(anyLong(), anyString()))
        .thenReturn(false);

    List<TenureRequestDto> tenures = List.of(new TenureRequestDto("TFL001", null, "CB001", true));

    TenureValidationResponseDto result = service.validateTenures(tenures, CLIENT_NUMBER);

    assertThat(result.isValid()).isFalse();
    assertThat(result.validationResults().get(0).errorCode())
        .isEqualTo(TenureValidationErrorCode.CLIENT_NOT_LICENSEE);
  }

  @Test
  @DisplayName("Tenure already linked to existing opening should return TENURE_DUPLICATE_OPENING")
  void validateTenures_cboaDupFound_returnsTenureDuplicateOpening() {
    when(cutBlockOpenAdminRepository.existsAllocatedByTenure(anyString(), anyString(), any()))
        .thenReturn(true);

    List<TenureRequestDto> tenures = List.of(new TenureRequestDto("TFL001", null, "CB001", true));

    TenureValidationResponseDto result = service.validateTenures(tenures, CLIENT_NUMBER);

    assertThat(result.isValid()).isFalse();
    assertThat(result.validationResults().get(0).errorCode())
        .isEqualTo(TenureValidationErrorCode.TENURE_DUPLICATE_OPENING);
  }

  // ========== detectDuplicates() — duplicate detection ==========

  @Test
  @DisplayName(
      "Duplicate tenures in submitted list should return conflict with DUPLICATE_IN_REQUEST")
  void validateTenures_withDuplicate_returnsIsValidFalseWithConflict() {
    List<TenureRequestDto> tenures =
        List.of(
            new TenureRequestDto("TFL001", "CP1", "CB001", true),
            new TenureRequestDto("TFL001", "CP1", "CB001", false));

    TenureValidationResponseDto result = service.validateTenures(tenures, CLIENT_NUMBER);

    assertThat(result.isValid()).isFalse();
    assertThat(result.duplicateConflicts()).hasSize(1);
    DuplicateConflictDto conflict = result.duplicateConflicts().get(0);
    assertThat(conflict.duplicateIndices()).containsExactlyInAnyOrder(0, 1);
    assertThat(conflict.conflictCode()).isEqualTo(TenureValidationErrorCode.DUPLICATE_IN_REQUEST);
    assertThat(conflict.reason()).contains("TFL001").contains("CP1").contains("CB001");
  }

  @Test
  @DisplayName("Tenures with distinct fileId+cutBlock pairs should return empty list")
  void detectDuplicates_noDuplicates_returnsEmptyList() {
    List<TenureRequestDto> tenures =
        List.of(
            new TenureRequestDto("TFL001", null, "CB001", true),
            new TenureRequestDto("TFL002", null, "CB001", false),
            new TenureRequestDto("TFL001", null, "CB002", false));

    List<DuplicateConflictDto> result = service.detectDuplicates(tenures);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("Three tenures where two share fileId+cutBlock should return one conflict")
  void detectDuplicates_threeTenuresTwoAlike_returnsOneConflict() {
    List<TenureRequestDto> tenures =
        List.of(
            new TenureRequestDto("TFL001", null, "CB001", true),
            new TenureRequestDto("TFL002", null, "CB002", false),
            new TenureRequestDto("TFL001", null, "CB001", false));

    List<DuplicateConflictDto> result = service.detectDuplicates(tenures);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).duplicateIndices()).containsExactlyInAnyOrder(0, 2);
  }

  @Test
  @DisplayName("Three identical tenures should produce one conflict with all three indices")
  void detectDuplicates_allSameTenure_returnsOneConflictAllIndices() {
    List<TenureRequestDto> tenures =
        List.of(
            new TenureRequestDto("TFL001", null, "CB001", true),
            new TenureRequestDto("TFL001", null, "CB001", false),
            new TenureRequestDto("TFL001", null, "CB001", false));

    List<DuplicateConflictDto> result = service.detectDuplicates(tenures);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).duplicateIndices()).containsExactlyInAnyOrder(0, 1, 2);
  }

  @Test
  @DisplayName("Whitespace around fileId/cutBlock should be trimmed before key comparison")
  void detectDuplicates_whitespaceTrimmingInKey_detectsDuplicate() {
    List<TenureRequestDto> tenures =
        List.of(
            new TenureRequestDto(" TFL001 ", null, " CB001 ", true),
            new TenureRequestDto("TFL001", null, "CB001", false));

    List<DuplicateConflictDto> result = service.detectDuplicates(tenures);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).duplicateIndices()).containsExactlyInAnyOrder(0, 1);
  }
}
