package ca.bc.gov.restapi.results.postgres.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * Request body for creating a new opening via {@code POST /api/openings/create}.
 *
 * @param openingGrossArea total opening gross area in hectares
 * @param maxAllowablePermAccessPerc maximum allowable permanent access percentage; optional
 * @param clientNumber the 8-character client number for authorisation and tenure lookup
 * @param clientLocationCode the 2-character client location code for tenure lookup
 * @param orgUnitCode the organisation unit code identifying the managing district
 * @param openingCategoryCode the opening category code (e.g. "FTML")
 * @param licenseeOpeningId optional licensee-provided opening identifier
 * @param tenures list of cut-block tenures to link to the opening; exactly one must be primary
 */
public record CreateOpeningRequestDto(
    @NotNull @Digits(integer = 7, fraction = 4) BigDecimal openingGrossArea,
    @Digits(integer = 2, fraction = 1) BigDecimal maxAllowablePermAccessPerc,
    @NotBlank @Size(max = 8) String clientNumber,
    @NotBlank @Size(max = 2) String clientLocationCode,
    @NotBlank @Size(max = 6) String orgUnitCode,
    @NotBlank @Size(max = 7) String openingCategoryCode,
    @Size(max = 30) String licenseeOpeningId,
    @NotEmpty @Valid List<TenureRequestDto> tenures) {}
