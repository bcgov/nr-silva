package ca.bc.gov.restapi.results.oracle.dto;

import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.With;

/**
 * Represents a Recent Opening in the Home screen.
 */
@Builder
@With
public record RecentOpeningDto(
    Long openingId,
    String fileId,
    String cuttingPermit,
    String timberMark,
    String cutBlock,
    BigDecimal grossAreaHa,
    OpeningStatusEnum status,
    OpeningCategoryEnum category,
    LocalDate disturbanceStart,
    LocalDateTime entryTimestamp,
    LocalDateTime updateTimestamp
) {

}
