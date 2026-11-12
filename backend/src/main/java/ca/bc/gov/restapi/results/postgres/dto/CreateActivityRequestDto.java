package ca.bc.gov.restapi.results.postgres.dto;

import ca.bc.gov.restapi.results.postgres.enums.ActivityStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Request body (the {@code data} part) for creating an activity under an opening. The opening is
 * given by the request path.
 *
 * @param status whether the activity is planned or completed
 * @param baseCode the activity base code; the basic bases plus SP and SU are accepted
 * @param techniqueCode optional technique code
 * @param methodCode optional method code
 * @param objectiveCodes up to five objective codes
 * @param fundingSourceCodes one to five funding source codes
 * @param projectNumber optional project number (the "Intra-agency Number" in RESULTS)
 * @param licenseeActivityId optional identifier used by the licensee
 * @param comment optional comment
 * @param plannedDate planned date; required for planned activities, where it must be later than the
 *     current date, and optional (any date) for completed ones
 * @param plannedArea planned area in hectares; required for planned activities, optional for
 *     completed ones
 * @param plannedCost optional planned cost in whole dollars
 * @param completionDate completion date; required for completed activities and not allowed for
 *     planned ones
 * @param treatedArea treated area in hectares; required for completed activities and not allowed
 *     for planned ones
 * @param actualCost optional actual cost in whole dollars; not allowed for planned activities
 * @param targetPreparedSpots optional target prepared spots (4 digits); only for base SP
 * @param surveyPlannedNumPlots optional planned number of plots (4 digits); only for base SU
 * @param surveyActualNumPlots optional actual number of plots (4 digits); only for base SU and not
 *     allowed for planned activities
 * @param surveyMinPlotsPerStratum optional minimum plots per stratum; only for base SU
 */
public record CreateActivityRequestDto(
    @NotNull ActivityStatus status,
    @NotBlank @Size(max = 2) String baseCode,
    @Size(max = 2) String techniqueCode,
    @Size(max = 5) String methodCode,
    @Size(max = 5) List<@NotBlank @Size(max = 3) String> objectiveCodes,
    @NotEmpty @Size(max = 5) List<@NotBlank @Size(max = 3) String> fundingSourceCodes,
    @Size(max = 10) String projectNumber,
    @Size(max = 30) String licenseeActivityId,
    @Size(max = 2000) String comment,
    LocalDate plannedDate,
    @DecimalMin(value = "0", inclusive = false) @Digits(integer = 10, fraction = 1)
        BigDecimal plannedArea,
    @DecimalMin("0")
        @Digits(integer = 9, fraction = 0, message = "must be a whole number of dollars")
        BigDecimal plannedCost,
    LocalDate completionDate,
    @DecimalMin(value = "0", inclusive = false) @Digits(integer = 10, fraction = 1)
        BigDecimal treatedArea,
    @DecimalMin("0")
        @Digits(integer = 9, fraction = 0, message = "must be a whole number of dollars")
        BigDecimal actualCost,
    @Min(0) @Max(9999) Integer targetPreparedSpots,
    @Min(0) @Max(9999) Integer surveyPlannedNumPlots,
    @Min(0) @Max(9999) Integer surveyActualNumPlots,
    @Min(0) @Max(9999999999L) Long surveyMinPlotsPerStratum) {}
