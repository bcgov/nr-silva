package ca.bc.gov.restapi.results.oracle.enums;

public enum StockingMilestoneTypeEnum {
    FG("FG", "Free Growing"),
    RG("RG", "Regeneration"),
    PH("PH", "Post Harvest"),
    NR("NR", "No Regeneration");

    private final String code;
    private final String description;

    StockingMilestoneTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
