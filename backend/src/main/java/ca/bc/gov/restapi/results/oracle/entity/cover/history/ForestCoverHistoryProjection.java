package ca.bc.gov.restapi.results.oracle.entity.cover.history;

import ca.bc.gov.restapi.results.oracle.entity.cover.ForestCoverProjection;

import java.time.LocalDate;

public interface ForestCoverHistoryProjection extends ForestCoverProjection {
    LocalDate getArchiveDate();
}
