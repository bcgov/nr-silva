package ca.bc.gov.restapi.results.common.dto;

import java.time.LocalDate;

import ca.bc.gov.restapi.results.common.enums.YesNoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

/**
 * This record represents a Forest Client Location object.
 */
@With
public record ForestClientLocationDto(
    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String clientNumber,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String locationCode,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String locationName,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String companyCode,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String address1,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String address2,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String address3,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String city,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String province,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String postalCode,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String country,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String businessPhone,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String homePhone,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String cellPhone,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String faxNumber,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String email,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    YesNoEnum expired,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    YesNoEnum trusted,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate returnedMailDate,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String comment
) {

}
