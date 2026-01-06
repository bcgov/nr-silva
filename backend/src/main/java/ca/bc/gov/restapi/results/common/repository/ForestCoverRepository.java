package ca.bc.gov.restapi.results.common.repository;

import ca.bc.gov.restapi.results.common.projection.cover.*;
import ca.bc.gov.restapi.results.common.projection.cover.history.*;

import java.util.List;
import java.util.Optional;

public interface ForestCoverRepository {
  List<ForestCoverProjection> findByOpeningDetails(Long openingId, String mainSearchTerm);

  List<ForestCoverSpeciesProjection> findByOpeningDetailsSpecies(Long forestCoverId, String coverLayerCode);

  Optional<ForestCoverPolygonProjection> findByOpeningDetailsPolygon(Long forestCoverId);

  List<ForestCoverUnmappedProjection> findByOpeningDetailsUnmapped(Long forestCoverId);

  List<ForestCoverDetailsLayerProjection> findByOpeningDetailsLayer(Long forestCoverId);

  List<ForestCoverDetailedSpeciesProjection> findByOpeningDetailsDetailedSpecies(Long forestCoverLayerId);

  List<ForestCoverDetailsDamageProjection> findByOpeningDetailsDamage(Long forestCoverLayerId);

  List<ForestCoverHistoryOverviewProjection> findHistoryOverviewByOpeningId(Long openingId);

  List<ForestCoverHistoryProjection> findHistoryByOpeningDetails(Long openingId, String updateDate, String mainSearchTerm);

  List<ForestCoverHistorySpeciesProjection> findHistoryByOpeningDetailsSpecies(
      Long forestCoverId, String coverLayerCode, String archiveDate);

  Optional<ForestCoverHistoryPolygonProjection> findHistoryByOpeningDetailsPolygon(
      Long forestCoverId, String archiveDate);

  List<ForestCoverHistoryUnmappedProjection> findHistoryByOpeningDetailsUnmapped(
      Long forestCoverId, String archiveDate);

  List<ForestCoverHistoryDetailsLayerProjection> findHistoryByOpeningDetailsLayer(
      Long forestCoverId, String archiveDate);

  List<ForestCoverHistoryDetailedSpeciesProjection> findHistoryByOpeningDetailsDetailedSpecies(
      Long forestCoverLayerId, String archiveDate);

  List<ForestCoverHistoryDetailsDamageProjection> findHistoryByOpeningDetailsDamage(
      Long forestCoverLayerId, String archiveDate);
}
