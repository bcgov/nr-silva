package ca.bc.gov.restapi.results.common.projection.cover.history;

import ca.bc.gov.restapi.results.common.projection.cover.ForestCoverProjection;

import java.time.LocalDate;

public interface ForestCoverHistoryProjection extends ForestCoverProjection {
    LocalDate getArchiveDate();
}
