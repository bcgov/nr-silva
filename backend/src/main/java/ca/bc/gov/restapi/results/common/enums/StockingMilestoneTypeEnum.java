package ca.bc.gov.restapi.results.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum StockingMilestoneTypeEnum {
    FG("FG", "Free Growing"),
    RG("RG", "Regeneration"),
    PH("PH", "Post Harvest"),
    NR("NR", "No Regeneration");

    private final String code;
    private final String description;
}
