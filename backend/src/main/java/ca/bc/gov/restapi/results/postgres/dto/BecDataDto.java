package ca.bc.gov.restapi.results.postgres.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * A single BEC (biogeoclimatic ecosystem classification) entry supplied for a new Stocking
 * Standard.
 *
 * @param bgcZoneCode the BGC zone code (e.g. "CWH")
 * @param bgcSubzoneCode the BGC subzone code (e.g. "wh1")
 * @param variant the BGC variant, optional
 * @param phase the BGC phase, optional
 * @param siteSeries the BEC site series code
 * @param sitePhase the BEC site series phase, optional
 */
public record BecDataDto(
    @NotBlank @Size(max = 4) String bgcZoneCode,
    @NotBlank @Size(max = 3) String bgcSubzoneCode,
    @Size(max = 1) String variant,
    @Size(max = 1) String phase,
    @NotBlank @Size(max = 4) String siteSeries,
    @Size(max = 3) String sitePhase) {}
