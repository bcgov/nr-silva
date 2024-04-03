package ca.bc.gov.restapi.results.postgres.dto;

import java.math.BigDecimal;

/** This record represent a slice of the free growing milestone chart. */
public record FreeGrowingMilestonesDto(String label, BigDecimal percentage) {}