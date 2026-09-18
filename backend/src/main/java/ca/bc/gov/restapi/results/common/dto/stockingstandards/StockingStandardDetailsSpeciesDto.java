package ca.bc.gov.restapi.results.common.dto.stockingstandards;

import java.math.BigDecimal;

/** Species criteria for one stocking standard layer. */
public record StockingStandardDetailsSpeciesDto(
    String speciesCode, String speciesType, BigDecimal minHeight, String milestone) {}
