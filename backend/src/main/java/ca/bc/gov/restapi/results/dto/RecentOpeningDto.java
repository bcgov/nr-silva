package ca.bc.gov.restapi.results.dto;

import ca.bc.gov.restapi.results.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.enums.OpeningStatusEnum;

import java.time.LocalDate;

public record RecentOpeningDto(
    String openingId,
    String fileId,
    String cuttingPermit,
    String timberMark,
    String cutBlock,
    String grossAreaHa,
    OpeningStatusEnum status,
    OpeningCategoryEnum category,
    LocalDate disturbanceStart) {}
