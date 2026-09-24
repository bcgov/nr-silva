package ca.bc.gov.restapi.results.postgres.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/** Species entries supplied for one stocking-standard layer validation. */
public record StockingSpeciesLayerValidationRequestDto(
    @NotBlank @Size(max = 2) String layerCode,
    List<@NotNull @Valid StockingSpeciesDto> species) {}
