package ca.bc.gov.restapi.results.postgres.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.postgres.dto.StockingSpeciesDto;
import ca.bc.gov.restapi.results.postgres.dto.StockingSpeciesLayerValidationRequestDto;
import ca.bc.gov.restapi.results.postgres.dto.StockingSpeciesValidationResponseDto;
import ca.bc.gov.restapi.results.postgres.enums.SpeciesValidationErrorCode;
import ca.bc.gov.restapi.results.postgres.enums.StockingSpeciesType;
import ca.bc.gov.restapi.results.postgres.repository.SilvTreeSpeciesCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SiteSeriesCataloguePostgresRepository;
import jakarta.validation.Validation;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | StockingStandardSpeciesValidationService")
class StockingStandardSpeciesValidationServiceTest {

  @Mock private SilvTreeSpeciesCodePostgresRepository speciesCodeRepository;
  @Mock private SiteSeriesCataloguePostgresRepository siteSeriesCatalogueRepository;

  private StockingStandardSpeciesValidationService service;

  @BeforeEach
  void setUp() {
    StockingStandardReferenceValidationService referenceValidationService =
        new StockingStandardReferenceValidationService(
            speciesCodeRepository, siteSeriesCatalogueRepository);
    service =
        new StockingStandardSpeciesValidationService(
            referenceValidationService, Validation.buildDefaultValidatorFactory().getValidator());
  }

  @Test
  @DisplayName("Active species code is validated case-insensitively for layer 4")
  void activeSpeciesCode_forLayerFour_passes() {
    when(
            speciesCodeRepository
                .existsByCodeIgnoreCaseAndEffectiveDateLessThanEqualAndExpiryDateGreaterThan(
                    eq("cw"), any(), any()))
        .thenReturn(true);

    StockingSpeciesValidationResponseDto result =
        service.validate(
            List.of(
                new StockingSpeciesLayerValidationRequestDto(
                    "4",
                    List.of(
                        new StockingSpeciesDto(
                            "cw", StockingSpeciesType.PREFERRED, new BigDecimal("1.0"))))));

    assertThat(result.isValid()).isTrue();
    assertThat(result.validationResults()).singleElement().satisfies(layer -> {
      assertThat(layer.isValid()).isTrue();
      assertThat(layer.speciesValidationResults()).singleElement().satisfies(species -> {
        assertThat(species.speciesIndex()).isZero();
        assertThat(species.isValid()).isTrue();
      });
    });
  }

  @Test
  @DisplayName("Layer 4 species without minimum height is rejected")
  void layerFourSpecies_withoutMinimumHeight_isRejected() {
    StockingSpeciesValidationResponseDto result =
        service.validate(
            List.of(
                new StockingSpeciesLayerValidationRequestDto(
                    "4", List.of(new StockingSpeciesDto("CW", StockingSpeciesType.PREFERRED, null)))));

    assertThat(result.isValid()).isFalse();
    assertThat(result.validationResults().get(0).speciesValidationResults().get(0).errorCode())
        .isEqualTo(SpeciesValidationErrorCode.SPECIES_MIN_HEIGHT_REQUIRED);
    assertThat(result.validationResults().get(0).speciesValidationResults().get(0).errorMessage())
        .isEqualTo("minHeight is required for species in layer 4");
    verify(speciesCodeRepository, never())
        .existsByCodeIgnoreCaseAndEffectiveDateLessThanEqualAndExpiryDateGreaterThan(
            anyString(), any(), any());
  }

  @Test
  @DisplayName("Species minimum height outside the documented precision is rejected")
  void speciesMinimumHeight_withMoreThanOneDecimal_isRejected() {
    StockingSpeciesValidationResponseDto result =
        service.validate(
            List.of(
                new StockingSpeciesLayerValidationRequestDto(
                    "3",
                    List.of(
                        new StockingSpeciesDto(
                            "CW", StockingSpeciesType.PREFERRED, new BigDecimal("1.11"))))));

    assertThat(result.isValid()).isFalse();
    assertThat(result.validationResults().get(0).speciesValidationResults().get(0).errorCode())
        .isEqualTo(SpeciesValidationErrorCode.SPECIES_MIN_HEIGHT_INVALID);
    assertThat(result.validationResults().get(0).speciesValidationResults().get(0).errorMessage())
        .isEqualTo("minHeight must be between 0.0 and 99.9 metres with at most one decimal place");
  }

  @Test
  @DisplayName("Unknown species code is returned as a per-species error")
  void unknownSpeciesCode_returnsPerSpeciesError() {
    when(
            speciesCodeRepository
                .existsByCodeIgnoreCaseAndEffectiveDateLessThanEqualAndExpiryDateGreaterThan(
                    eq("ZZ"), any(), any()))
        .thenReturn(false);

    StockingSpeciesValidationResponseDto result =
        service.validate(
            List.of(
                new StockingSpeciesLayerValidationRequestDto(
                    "3",
                    List.of(
                        new StockingSpeciesDto(
                            "ZZ", StockingSpeciesType.ACCEPTABLE, null)))));

    assertThat(result.isValid()).isFalse();
    assertThat(result.validationResults().get(0).speciesValidationResults().get(0).errorCode())
        .isEqualTo(SpeciesValidationErrorCode.SPECIES_CODE_UNKNOWN_OR_INACTIVE);
    assertThat(result.validationResults().get(0).speciesValidationResults().get(0).errorMessage())
        .isEqualTo("Unknown or inactive species code(s): ZZ");
  }

  @Test
  @DisplayName("Duplicate species codes are conflicts only within the same layer")
  void duplicateSpeciesCodes_areConflictsOnlyWithinSameLayer() {
    when(
            speciesCodeRepository
                .existsByCodeIgnoreCaseAndEffectiveDateLessThanEqualAndExpiryDateGreaterThan(
                    anyString(), any(), any()))
        .thenReturn(true);
    StockingSpeciesDto cw =
        new StockingSpeciesDto("CW", StockingSpeciesType.PREFERRED, new BigDecimal("1.0"));
    StockingSpeciesDto spacedCw =
        new StockingSpeciesDto(" cw ", StockingSpeciesType.ACCEPTABLE, new BigDecimal("1.1"));

    StockingSpeciesValidationResponseDto result =
        service.validate(
            List.of(
                new StockingSpeciesLayerValidationRequestDto("4", List.of(cw, spacedCw)),
                new StockingSpeciesLayerValidationRequestDto(
                    "3", List.of(new StockingSpeciesDto("CW", StockingSpeciesType.PREFERRED, null)))));

    assertThat(result.isValid()).isFalse();
    assertThat(result.duplicateConflicts()).singleElement().satisfies(conflict -> {
      assertThat(conflict.layerIndex()).isZero();
      assertThat(conflict.layerCode()).isEqualTo("4");
      assertThat(conflict.duplicateSpeciesIndices()).containsExactly(0, 1);
      assertThat(conflict.conflictCode())
          .isEqualTo(SpeciesValidationErrorCode.DUPLICATE_SPECIES_CODE);
    });
    assertThat(result.validationResults().get(1).isValid()).isTrue();
  }

  @Test
  @DisplayName("Unsupported layer code is a layer error and skips species lookups")
  void unsupportedLayerCode_returnsLayerErrorWithoutSpeciesLookup() {
    StockingSpeciesValidationResponseDto result =
        service.validate(
            List.of(
                new StockingSpeciesLayerValidationRequestDto(
                    "5",
                    List.of(
                        new StockingSpeciesDto(
                            "CW", StockingSpeciesType.PREFERRED, new BigDecimal("1.0"))))));

    assertThat(result.isValid()).isFalse();
    assertThat(result.validationResults()).singleElement().satisfies(layer -> {
      assertThat(layer.layerErrorCode()).isEqualTo(SpeciesValidationErrorCode.LAYER_CODE_INVALID);
      assertThat(layer.speciesValidationResults()).isEmpty();
    });
    verify(speciesCodeRepository, never())
        .existsByCodeIgnoreCaseAndEffectiveDateLessThanEqualAndExpiryDateGreaterThan(
            anyString(), any(), any());
  }

  @Test
  @DisplayName("Null species payload is rejected while an empty list remains valid")
  void nullSpeciesPayload_isRejected() {
    assertThatThrownBy(() -> service.validate(null))
        .isInstanceOf(ResponseStatusException.class)
        .satisfies(
            exception ->
                assertThat(((ResponseStatusException) exception).getStatusCode())
                    .isEqualTo(HttpStatus.BAD_REQUEST));

    assertThat(service.validate(List.of()).isValid()).isTrue();
  }
}
