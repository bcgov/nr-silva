package ca.bc.gov.restapi.results.oracle.entity.cover.history;

import java.time.LocalDateTime;

public interface ForestCoverHistoryOverviewProjection {
    Long getOpeningId();
    LocalDateTime getFcDate();
    Double getNp();
    Double getNsr();
    Double getImm();
    Double getOther();
    Double getTotal();
    Boolean getHasDetails();
    Boolean getIsCurrentHistory();
}
