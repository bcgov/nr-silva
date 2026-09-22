package ca.bc.gov.restapi.results.postgres.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.postgres.dto.BecDataDto;
import ca.bc.gov.restapi.results.postgres.dto.BecValidationResponseDto;
import ca.bc.gov.restapi.results.postgres.entity.SiteSeriesCatalogueEntity;
import ca.bc.gov.restapi.results.postgres.enums.BecValidationErrorCode;
import ca.bc.gov.restapi.results.postgres.repository.SilvTreeSpeciesCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SiteSeriesCataloguePostgresRepository;
import jakarta.validation.Validation;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | StockingStandardBecValidationService")
class StockingStandardBecValidationServiceTest {

  @Mock private SiteSeriesCataloguePostgresRepository siteSeriesCatalogueRepository;
  @Mock private SilvTreeSpeciesCodePostgresRepository speciesCodeRepository;

  private StockingStandardBecValidationService service;

  @BeforeEach
  void setUp() {
    StockingStandardReferenceValidationService referenceValidationService =
        new StockingStandardReferenceValidationService(
            speciesCodeRepository, siteSeriesCatalogueRepository);
    service =
        new StockingStandardBecValidationService(
            referenceValidationService, Validation.buildDefaultValidatorFactory().getValidator());
  }

  @Test
  @DisplayName("Valid active BEC combination including site phase passes")
  void validBecCombinationIncludingSitePhase_passes() {
    BecDataDto bec = new BecDataDto("CWH", "wh", "1", null, "01", "A");
    when(siteSeriesCatalogueRepository.findMatchingBecCombo("CWH", "wh", "1", null, "01", "A"))
        .thenReturn(List.of(SiteSeriesCatalogueEntity.builder().id(1L).build()));

    BecValidationResponseDto result = service.validate(List.of(bec));

    assertThat(result.isValid()).isTrue();
    assertThat(result.validationResults())
        .containsExactly(
            new ca.bc.gov.restapi.results.postgres.dto.BecValidationResultDto(0, true, null, null));
    assertThat(result.duplicateConflicts()).isEmpty();
  }

  @Test
  @DisplayName("Unknown BEC combination is returned as a per-entry error")
  void unknownBecCombination_returnsPerEntryError() {
    BecDataDto bec = new BecDataDto("ZZZ", "zz", null, null, "99", null);
    when(siteSeriesCatalogueRepository.findMatchingBecCombo("ZZZ", "zz", null, null, "99", null))
        .thenReturn(List.of());

    BecValidationResponseDto result = service.validate(List.of(bec));

    assertThat(result.isValid()).isFalse();
    assertThat(result.validationResults().get(0).errorCode())
        .isEqualTo(BecValidationErrorCode.BEC_COMBINATION_NOT_FOUND);
    assertThat(result.validationResults().get(0).errorMessage())
        .isEqualTo("Unknown BEC combination(s): ZZZ/zz///99");
  }

  @Test
  @DisplayName("Malformed BEC entry is returned as a field error without a lookup")
  void malformedBecEntry_returnsFieldErrorWithoutLookup() {
    BecValidationResponseDto result =
        service.validate(List.of(new BecDataDto("", "wh", null, null, "01", null)));

    assertThat(result.isValid()).isFalse();
    assertThat(result.validationResults().get(0).errorCode())
        .isEqualTo(BecValidationErrorCode.FIELD_INVALID);
    assertThat(result.validationResults().get(0).errorMessage())
        .isEqualTo("bgcZoneCode: must not be blank");
    verify(siteSeriesCatalogueRepository, never())
        .findMatchingBecCombo(anyString(), anyString(), any(), any(), anyString(), any());
  }

  @Test
  @DisplayName("Duplicate BEC combinations are returned as a separate conflict")
  void duplicateBecCombinations_returnSeparateConflict() {
    BecDataDto bec = new BecDataDto("CWH", "wh", "1", null, "01", null);
    when(siteSeriesCatalogueRepository.findMatchingBecCombo("CWH", "wh", "1", null, "01", null))
        .thenReturn(List.of(SiteSeriesCatalogueEntity.builder().id(1L).build()));

    BecValidationResponseDto result = service.validate(List.of(bec, bec));

    assertThat(result.isValid()).isFalse();
    assertThat(result.validationResults()).allMatch(validationResult -> validationResult.isValid());
    assertThat(result.duplicateConflicts()).singleElement().satisfies(conflict -> {
      assertThat(conflict.duplicateIndices()).containsExactly(0, 1);
      assertThat(conflict.conflictCode())
          .isEqualTo(BecValidationErrorCode.DUPLICATE_BEC_COMBINATION);
      assertThat(conflict.reason()).isEqualTo("becData must not contain duplicate BEC combinations");
    });
  }

  @Test
  @DisplayName("Empty BEC input is rejected with a request-level error")
  void emptyBecInput_returnsRequestLevelError() {
    BecValidationResponseDto result = service.validate(List.of());

    assertThat(result.isValid()).isFalse();
    assertThat(result.validationResults()).isEmpty();
    assertThat(result.validationErrors()).singleElement().satisfies(error -> {
      assertThat(error.errorCode()).isEqualTo(BecValidationErrorCode.FIELD_INVALID);
      assertThat(error.errorMessage())
          .isEqualTo("At least one BEC entry is required when BEC information is selected");
    });
  }
}
