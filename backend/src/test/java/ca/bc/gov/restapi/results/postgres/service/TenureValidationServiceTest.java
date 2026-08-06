package ca.bc.gov.restapi.results.postgres.service;

import static org.assertj.core.api.Assertions.assertThat;

import ca.bc.gov.restapi.results.postgres.dto.DuplicateConflictDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureRequestDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureValidationResponseDto;
import ca.bc.gov.restapi.results.postgres.dto.TenureValidationResultDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | TenureValidationService")
class TenureValidationServiceTest {

  private TenureValidationService service;

  @BeforeEach
  void setUp() {
    service = new TenureValidationService();
  }

  // ========== validateTenures() ==========

  @Test
  @DisplayName("All valid tenures with no duplicates should return isValid=true")
  void validateTenures_allValid_returnsIsValidTrue() {
    List<TenureRequestDto> tenures =
        List.of(
            new TenureRequestDto("TFL001", "CP1", "CB001", true),
            new TenureRequestDto("TFL002", null, "CB002", false));

    TenureValidationResponseDto result = service.validateTenures(tenures);

    assertThat(result.isValid()).isTrue();
    assertThat(result.duplicateConflicts()).isEmpty();
    assertThat(result.validationResults()).hasSize(2);
    assertThat(result.validationResults()).allMatch(TenureValidationResultDto::isValid);
  }

  @Test
  @DisplayName(
      "Duplicate tenures (same fileId+cuttingPermit+cutBlock) should return isValid=false with"
          + " conflict")
  void validateTenures_withDuplicate_returnsIsValidFalse() {
    List<TenureRequestDto> tenures =
        List.of(
            new TenureRequestDto("TFL001", "CP1", "CB001", true),
            new TenureRequestDto("TFL001", "CP1", "CB001", false));

    TenureValidationResponseDto result = service.validateTenures(tenures);

    assertThat(result.isValid()).isFalse();
    assertThat(result.duplicateConflicts()).hasSize(1);
    DuplicateConflictDto conflict = result.duplicateConflicts().get(0);
    assertThat(conflict.duplicateIndices()).containsExactlyInAnyOrder(0, 1);
    assertThat(conflict.reason()).contains("TFL001").contains("CP1").contains("CB001");
  }

  @Test
  @DisplayName("Blank fileId should return fieldError containing 'fileId'")
  void validateTenures_blankFileId_returnsFieldError() {
    List<TenureRequestDto> tenures = List.of(new TenureRequestDto("  ", null, "CB001", true));

    TenureValidationResponseDto result = service.validateTenures(tenures);

    assertThat(result.isValid()).isFalse();
    TenureValidationResultDto r = result.validationResults().get(0);
    assertThat(r.isValid()).isFalse();
    assertThat(r.fieldError()).contains("fileId");
  }

  @Test
  @DisplayName("fileId exceeding 10 chars should return fieldError containing 'fileId'")
  void validateTenures_fileIdTooLong_returnsFieldError() {
    List<TenureRequestDto> tenures =
        List.of(new TenureRequestDto("TOOLONGFILEID", null, "CB001", true));

    TenureValidationResponseDto result = service.validateTenures(tenures);

    assertThat(result.isValid()).isFalse();
    assertThat(result.validationResults().get(0).fieldError()).contains("fileId");
  }

  @Test
  @DisplayName("Blank cutBlock should return fieldError containing 'cutBlock'")
  void validateTenures_blankCutBlock_returnsFieldError() {
    List<TenureRequestDto> tenures = List.of(new TenureRequestDto("TFL001", null, "", true));

    TenureValidationResponseDto result = service.validateTenures(tenures);

    assertThat(result.isValid()).isFalse();
    assertThat(result.validationResults().get(0).fieldError()).contains("cutBlock");
  }

  @Test
  @DisplayName("cutBlock exceeding 10 chars should return fieldError containing 'cutBlock'")
  void validateTenures_cutBlockTooLong_returnsFieldError() {
    List<TenureRequestDto> tenures =
        List.of(new TenureRequestDto("TFL001", null, "TOOLONGBLOCK", true));

    TenureValidationResponseDto result = service.validateTenures(tenures);

    assertThat(result.isValid()).isFalse();
    assertThat(result.validationResults().get(0).fieldError()).contains("cutBlock");
  }

  @Test
  @DisplayName(
      "cuttingPermit exceeding 3 chars should return fieldError containing 'cuttingPermit'")
  void validateTenures_cuttingPermitTooLong_returnsFieldError() {
    List<TenureRequestDto> tenures = List.of(new TenureRequestDto("TFL001", "LONG", "CB001", true));

    TenureValidationResponseDto result = service.validateTenures(tenures);

    assertThat(result.isValid()).isFalse();
    assertThat(result.validationResults().get(0).fieldError()).contains("cuttingPermit");
  }

  @Test
  @DisplayName("Null cuttingPermit should be valid (field is optional)")
  void validateTenures_nullCuttingPermit_isValid() {
    List<TenureRequestDto> tenures = List.of(new TenureRequestDto("TFL001", null, "CB001", true));

    TenureValidationResponseDto result = service.validateTenures(tenures);

    assertThat(result.isValid()).isTrue();
    assertThat(result.validationResults().get(0).isValid()).isTrue();
    assertThat(result.validationResults().get(0).fieldError()).isNull();
  }

  @Test
  @DisplayName("Empty tenure list should return isValid=true with no results and no conflicts")
  void validateTenures_emptyList_returnsIsValidTrue() {
    TenureValidationResponseDto result = service.validateTenures(List.of());

    assertThat(result.isValid()).isTrue();
    assertThat(result.validationResults()).isEmpty();
    assertThat(result.duplicateConflicts()).isEmpty();
  }

  @Test
  @DisplayName("Mixed valid/invalid tenures should make overall isValid=false")
  void validateTenures_oneValidOneInvalid_overallIsValidFalse() {
    List<TenureRequestDto> tenures =
        List.of(
            new TenureRequestDto("TFL001", null, "CB001", true),
            new TenureRequestDto("", null, "CB002", false));

    TenureValidationResponseDto result = service.validateTenures(tenures);

    assertThat(result.isValid()).isFalse();
    assertThat(result.validationResults().get(0).isValid()).isTrue();
    assertThat(result.validationResults().get(1).isValid()).isFalse();
  }

  // ========== detectDuplicates() ==========

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
  @DisplayName(
      "fileId/cutBlock with surrounding whitespace should be trimmed before key comparison")
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
