package ca.bc.gov.restapi.results.common.dto;

import ca.bc.gov.restapi.results.common.enums.ForestClientStatusEnum;
import ca.bc.gov.restapi.results.common.enums.ForestClientTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;

/** This record represents a Forest Client object. */
@Schema(description = "One of the many agencies that work with the ministry.")
public record ForestClientDto(
    @Schema(description = "An eight-digit number that identifies the client.", example = "00149081")
        String clientNumber,
    @Schema(
            description =
                "The client last name if it's an individual or the company name if it's a company.",
            example = "WESTERN FOREST PRODUCTS INC.")
        String clientName,
    @Schema(
            description = "The first name of the individual, or null if it's a company.",
            example = "Maria")
        String legalFirstName,
    @Schema(
            description = "The middle name of the individual, or null if it's a company",
            example = "Bricks")
        String legalMiddleName,
    @Schema(
            description =
                "A code indicating the status of ministry client. Examples include but are not"
                    + " limited to: Active, Deactivated, Deceased...",
            example = "ACT")
        ForestClientStatusEnum clientStatusCode,
    @Schema(
            description =
                "A code indicating a type of ministry client. Examples include but are not limited"
                    + " to: Corporation, Individual, Association, First Nation..",
            example = "C")
        ForestClientTypeEnum clientTypeCode,
    @Schema(
            description = "An acronym for this client; works as an alternative identifier.",
            example = "WFP")
        String acronym) {}
