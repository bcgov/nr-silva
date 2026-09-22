package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.postgres.dto.SpeciesDuplicateConflictDto;
import ca.bc.gov.restapi.results.postgres.dto.StockingSpeciesDto;
import ca.bc.gov.restapi.results.postgres.dto.StockingSpeciesLayerValidationRequestDto;
import ca.bc.gov.restapi.results.postgres.dto.StockingSpeciesLayerValidationResultDto;
import ca.bc.gov.restapi.results.postgres.dto.StockingSpeciesValidationResponseDto;
import ca.bc.gov.restapi.results.postgres.dto.StockingSpeciesValidationResultDto;
import ca.bc.gov.restapi.results.postgres.enums.SpeciesValidationErrorCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/** Provides layer-aware species validation feedback before stocking-standard submission. */
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class StockingStandardSpeciesValidationService {

  private static final Set<String> VALID_LAYER_CODES = Set.of("I", "4", "3", "2", "1");
  private static final String DUPLICATE_SPECIES_MESSAGE =
      "species must not contain duplicate species codes in layer ";

  private final StockingStandardReferenceValidationService referenceValidationService;
  private final Validator validator;

  /**
   * Validates species entries for each supplied layer without persisting data.
   *
   * @param layers species entries grouped by layer
   * @return layer-aware species validation outcomes and duplicate conflicts
   */
  public StockingSpeciesValidationResponseDto validate(
      List<StockingSpeciesLayerValidationRequestDto> layers) {
    if (layers == null || layers.isEmpty()) {
      return new StockingSpeciesValidationResponseDto(List.of(), List.of(), true);
    }

    List<StockingSpeciesLayerValidationResultDto> layerResults = new ArrayList<>();
    List<SpeciesDuplicateConflictDto> duplicateConflicts = new ArrayList<>();

    for (int layerIndex = 0; layerIndex < layers.size(); layerIndex++) {
      StockingSpeciesLayerValidationRequestDto layer = layers.get(layerIndex);
      if (layer == null || !isValidLayerCode(layer.layerCode())) {
        layerResults.add(invalidLayerResult(layerIndex, layer));
        continue;
      }

      List<StockingSpeciesValidationResultDto> speciesResults = new ArrayList<>();
      List<StockingSpeciesDto> structurallyValidSpecies = new ArrayList<>();
      List<Integer> structurallyValidIndices = new ArrayList<>();
      List<StockingSpeciesDto> species = layer.species();
      if (species != null) {
        for (int speciesIndex = 0; speciesIndex < species.size(); speciesIndex++) {
          StockingSpeciesDto speciesDto = species.get(speciesIndex);
          SpeciesFieldValidationError fieldError = validateFields(speciesDto);
          if (fieldError != null) {
            speciesResults.add(
                new StockingSpeciesValidationResultDto(
                    speciesIndex, false, fieldError.errorCode(), fieldError.errorMessage()));
            continue;
          }

          structurallyValidSpecies.add(speciesDto);
          structurallyValidIndices.add(speciesIndex);
          var outcome = referenceValidationService.validateSpecies(layer.layerCode(), speciesDto);
          if (outcome.minimumHeightError() != null) {
            speciesResults.add(
                new StockingSpeciesValidationResultDto(
                    speciesIndex,
                    false,
                    SpeciesValidationErrorCode.SPECIES_MIN_HEIGHT_REQUIRED,
                    outcome.minimumHeightError()));
          } else if (!outcome.exists()) {
            speciesResults.add(
                new StockingSpeciesValidationResultDto(
                    speciesIndex,
                    false,
                    SpeciesValidationErrorCode.SPECIES_CODE_UNKNOWN_OR_INACTIVE,
                    "Unknown or inactive species code(s): " + speciesDto.speciesCode()));
          } else {
            speciesResults.add(new StockingSpeciesValidationResultDto(speciesIndex, true, null, null));
          }
        }
      }

      List<SpeciesDuplicateConflictDto> layerConflicts =
          findDuplicateConflicts(
              layerIndex, layer.layerCode(), structurallyValidSpecies, structurallyValidIndices);
      duplicateConflicts.addAll(layerConflicts);
      boolean isLayerValid =
          speciesResults.stream().allMatch(StockingSpeciesValidationResultDto::isValid)
              && layerConflicts.isEmpty();
      layerResults.add(
          new StockingSpeciesLayerValidationResultDto(
              layerIndex, layer.layerCode(), isLayerValid, null, null, speciesResults));
    }

    boolean isValid =
        layerResults.stream().allMatch(StockingSpeciesLayerValidationResultDto::isValid)
            && duplicateConflicts.isEmpty();
    return new StockingSpeciesValidationResponseDto(layerResults, duplicateConflicts, isValid);
  }

  private boolean isValidLayerCode(String layerCode) {
    return layerCode != null && VALID_LAYER_CODES.contains(layerCode);
  }

  private StockingSpeciesLayerValidationResultDto invalidLayerResult(
      int layerIndex, StockingSpeciesLayerValidationRequestDto layer) {
    return new StockingSpeciesLayerValidationResultDto(
        layerIndex,
        layer != null ? layer.layerCode() : null,
        false,
        SpeciesValidationErrorCode.LAYER_CODE_INVALID,
        "layerCode must be one of I, 4, 3, 2, 1",
        List.of());
  }

  private SpeciesFieldValidationError validateFields(StockingSpeciesDto species) {
    if (species == null) {
      return new SpeciesFieldValidationError(
          SpeciesValidationErrorCode.FIELD_INVALID, "species: must not be null");
    }

    return validator.validate(species).stream()
        .sorted(Comparator.comparing(violation -> violation.getPropertyPath().toString()))
        .map(this::toFieldValidationError)
        .findFirst()
        .orElse(null);
  }

  private SpeciesFieldValidationError toFieldValidationError(
      ConstraintViolation<StockingSpeciesDto> violation) {
    if ("minHeight".equals(violation.getPropertyPath().toString())) {
      return new SpeciesFieldValidationError(
          SpeciesValidationErrorCode.SPECIES_MIN_HEIGHT_INVALID,
          "minHeight must be between 0.0 and 99.9 metres with at most one decimal place");
    }
    return new SpeciesFieldValidationError(
        SpeciesValidationErrorCode.FIELD_INVALID,
        violation.getPropertyPath() + ": " + violation.getMessage());
  }

  private List<SpeciesDuplicateConflictDto> findDuplicateConflicts(
      int layerIndex,
      String layerCode,
      List<StockingSpeciesDto> species,
      List<Integer> originalIndices) {
    Map<String, List<Integer>> indicesByCode = new LinkedHashMap<>();
    for (int index = 0; index < species.size(); index++) {
      String code = referenceValidationService.normalizeSpeciesCode(species.get(index).speciesCode());
      indicesByCode.computeIfAbsent(code, ignored -> new ArrayList<>()).add(originalIndices.get(index));
    }

    return indicesByCode.values().stream()
        .filter(indices -> indices.size() > 1)
        .map(
            indices ->
                new SpeciesDuplicateConflictDto(
                    layerIndex,
                    layerCode,
                    indices,
                    DUPLICATE_SPECIES_MESSAGE + layerCode,
                    SpeciesValidationErrorCode.DUPLICATE_SPECIES_CODE))
        .toList();
  }

  private record SpeciesFieldValidationError(
      SpeciesValidationErrorCode errorCode, String errorMessage) {}
}
