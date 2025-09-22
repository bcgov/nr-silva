package ca.bc.gov.restapi.results.postgres.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record GeoMetaDataDto(
    String orgUnit,
    String openingCategory,
    @Schema(
            description = "Opening gross area (NUMBER(11,4))",
            example = "12345.6789",
            minimum = "0.0",
            maximum = "999999999.9999")
        BigDecimal openingGrossArea,
    @Schema(
            description = "Maximum allowable permanent access percentage (NUMBER(3,1))",
            example = "9.9",
            minimum = "0.0",
            maximum = "100.0")
        BigDecimal maxAllowablePermAccessPerc) {}
