package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.postgres.dto.BecDataDto;
import ca.bc.gov.restapi.results.postgres.dto.StockingSpeciesDto;
import ca.bc.gov.restapi.results.postgres.repository.SilvTreeSpeciesCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SiteSeriesCataloguePostgresRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Shared BEC and species reference-data validation for stocking standards.
 *
 * <p>The create workflow uses these outcomes to retain its fail-fast error contract. Standalone
 * validation endpoints can use the same outcomes to return feedback for every submitted item.
 */
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class StockingStandardReferenceValidationService {

  private static final BigDecimal MAX_SPECIES_MIN_HEIGHT = new BigDecimal("99.9");

  private final SilvTreeSpeciesCodePostgresRepository speciesCodeRepository;
  private final SiteSeriesCataloguePostgresRepository siteSeriesCatalogueRepository;

  /** Result of looking up one BEC combination in the active catalogue. */
  public record BecValidationOutcome(boolean exists) {}

  /** Result of validating one species entry against its layer and active code catalogue. */
  public record SpeciesValidationOutcome(boolean exists, String minimumHeightError) {}

  public BecValidationOutcome validateBec(BecDataDto bec) {
    BecDataDto normalizedBec = normalizeBecForLookup(bec);
    boolean exists =
        !siteSeriesCatalogueRepository
            .findMatchingBecCombo(
                normalizedBec.bgcZoneCode(),
                normalizedBec.bgcSubzoneCode(),
                normalizedBec.variant(),
                normalizedBec.phase(),
                normalizedBec.siteSeries(),
                normalizedBec.sitePhase())
            .isEmpty();
    return new BecValidationOutcome(exists);
  }

  public SpeciesValidationOutcome validateSpecies(String layerCode, StockingSpeciesDto species) {
    String minimumHeightError = getSpeciesMinimumHeightError(layerCode, species);
    if (minimumHeightError != null) {
      return new SpeciesValidationOutcome(false, minimumHeightError);
    }

    boolean exists =
        speciesCodeRepository
            .existsByCodeIgnoreCaseAndEffectiveDateLessThanEqualAndExpiryDateGreaterThan(
                species.speciesCode().trim(), LocalDate.now(), LocalDate.now());
    return new SpeciesValidationOutcome(exists, null);
  }

  public boolean hasDuplicateBecCombinations(List<BecDataDto> becData) {
    Set<String> normalizedCombinations = new HashSet<>();
    for (BecDataDto bec : becData) {
      if (!normalizedCombinations.add(normalizeBecCombination(bec))) {
        return true;
      }
    }
    return false;
  }

  public boolean hasDuplicateSpeciesCodes(List<StockingSpeciesDto> species) {
    Set<String> normalizedCodes = new HashSet<>();
    for (StockingSpeciesDto speciesDto : species) {
      if (!normalizedCodes.add(normalizeSpeciesCode(speciesDto.speciesCode()))) {
        return true;
      }
    }
    return false;
  }

  public String normalizeBecCombination(BecDataDto bec) {
    return String.join(
        "|",
        normalizeBecValue(bec.bgcZoneCode()),
        normalizeBecValue(bec.bgcSubzoneCode()),
        normalizeBecValue(bec.variant()),
        normalizeBecValue(bec.phase()),
        normalizeBecValue(bec.siteSeries()),
        normalizeBecValue(bec.sitePhase()));
  }

  private BecDataDto normalizeBecForLookup(BecDataDto bec) {
    return new BecDataDto(
        StringUtils.trim(bec.bgcZoneCode()),
        StringUtils.trim(bec.bgcSubzoneCode()),
        StringUtils.trimToNull(bec.variant()),
        StringUtils.trimToNull(bec.phase()),
        StringUtils.trim(bec.siteSeries()),
        StringUtils.trimToNull(bec.sitePhase()));
  }

  public String normalizeSpeciesCode(String speciesCode) {
    return speciesCode.trim().toUpperCase(Locale.ROOT);
  }

  private String getSpeciesMinimumHeightError(String layerCode, StockingSpeciesDto species) {
    if (("I".equals(layerCode) || "4".equals(layerCode)) && species.minHeight() == null) {
      return "minHeight is required for species in layer " + layerCode;
    }
    if (species.minHeight() != null
        && (species.minHeight().compareTo(BigDecimal.ZERO) < 0
            || species.minHeight().compareTo(MAX_SPECIES_MIN_HEIGHT) > 0
            || species.minHeight().scale() > 1)) {
      return "minHeight must be between 0.0 and 99.9 metres with at most one decimal place";
    }
    return null;
  }

  private String normalizeBecValue(String value) {
    return StringUtils.trimToEmpty(value).toUpperCase(Locale.ROOT);
  }
}
