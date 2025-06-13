package ca.bc.gov.restapi.results.oracle.enums;

public enum StockingMilestoneTypeEnum {
    FG("FG"),
    RG("RG"),
    PH("PH");

    private final String code;

    StockingMilestoneTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
