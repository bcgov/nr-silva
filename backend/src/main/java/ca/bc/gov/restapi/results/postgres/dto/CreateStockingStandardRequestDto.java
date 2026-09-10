package ca.bc.gov.restapi.results.postgres.dto;

import ca.bc.gov.restapi.results.postgres.enums.StockingLayerType;
import ca.bc.gov.restapi.results.postgres.enums.StockingStandardAuthorityType;
import ca.bc.gov.restapi.results.postgres.enums.StockingType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * Request body for creating a new Stocking Standard via {@code POST /api/stocking-standards}.
 *
 * <p>Cross-field and reference-data validation (authority/org unit/client rules, BEC/species
 * existence, layer-type and layer-field applicability rules) is performed by {@code
 * StockingStandardValidationService}, not by bean validation annotations here — this DTO only
 * enforces per-field shape constraints.
 *
 * @param objective the objective of the Stocking Standard
 * @param name a descriptive name for the Stocking Standard, optional
 * @param location a description of the geographic area, optional
 * @param authorityType whether this is an Operational Plan or Ministry Default standard
 * @param orgUnitCodes org unit codes for Operational Plan authority; ignored for Ministry Default
 * @param clientNumbers client numbers for Operational Plan authority, optional
 * @param becInfoSelected whether BEC information applies to this standard
 * @param alternativeMethodSelected whether an alternative method applies to this standard
 * @param becData BEC entries; required (min 1) if becInfoSelected is true, otherwise must be empty
 * @param species species entries, optional
 * @param stockingType whether this standard is a Regen Obligation or a Stocking Requirement
 * @param regenDelayYears regen delay years; required if stockingType is REGEN_OBLIGATION
 * @param freeGrowingYears free growing years; required if stockingType is REGEN_OBLIGATION
 * @param earlyYears early years; required if stockingType is STOCKING_REQUIREMENT
 * @param lateYears late years; required if stockingType is STOCKING_REQUIREMENT
 * @param layerType whether Tree Stocking Criteria uses a single layer or four layers
 * @param singleLayer Tree Stocking Criteria for the single layer; required if layerType is SINGLE
 * @param multiLayers Tree Stocking Criteria for each of the four layers; required if layerType is
 *     MULTI
 * @param additionalStandards free-format additional standards text, optional
 */
public record CreateStockingStandardRequestDto(
    @NotBlank @Size(max = 50) String objective,
    @Size(max = 50) String name,
    @Size(max = 50) String location,
    @NotNull StockingStandardAuthorityType authorityType,
    List<@NotBlank @Size(max = 6) String> orgUnitCodes,
    List<@NotBlank @Size(max = 8) String> clientNumbers,
    @NotNull Boolean becInfoSelected,
    @NotNull Boolean alternativeMethodSelected,
    @Valid List<BecDataDto> becData,
    @Valid List<StockingSpeciesDto> species,
    @NotNull StockingType stockingType,
    Integer regenDelayYears,
    Integer freeGrowingYears,
    Integer earlyYears,
    Integer lateYears,
    @NotNull StockingLayerType layerType,
    @Valid StockingLayerDto singleLayer,
    @Valid List<StockingLayerDto> multiLayers,
    @Size(max = 4000) String additionalStandards) {}
