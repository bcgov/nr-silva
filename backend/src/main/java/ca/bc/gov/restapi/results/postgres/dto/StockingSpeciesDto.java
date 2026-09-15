package ca.bc.gov.restapi.results.postgres.dto;

import ca.bc.gov.restapi.results.postgres.enums.StockingSpeciesMilestone;
import ca.bc.gov.restapi.results.postgres.enums.StockingSpeciesType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * A single species entry supplied for a Stocking Standard layer.
 *
 * @param speciesCode the tree species code (silv_tree_species_code)
 * @param speciesType whether the species is preferred, acceptable, or ecologically suitable
 * @param minHeight minimum height in metres, optional
 * @param milestone which milestone(s) this species entry applies to (defaults to BOTH on the
 *     frontend)
 */
public record StockingSpeciesDto(
    @NotBlank @Size(max = 8) String speciesCode,
    @NotNull StockingSpeciesType speciesType,
    @Digits(integer = 2, fraction = 1) BigDecimal minHeight,
    @NotNull StockingSpeciesMilestone milestone) {}
