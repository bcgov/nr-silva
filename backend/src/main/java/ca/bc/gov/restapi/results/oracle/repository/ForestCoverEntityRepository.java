package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.oracle.SilvaOracleQueryConstants;
import ca.bc.gov.restapi.results.oracle.entity.cover.ForestCoverDetailedSpeciesProjection;
import ca.bc.gov.restapi.results.oracle.entity.cover.ForestCoverDetailsDamageProjection;
import ca.bc.gov.restapi.results.oracle.entity.cover.ForestCoverDetailsLayerProjection;
import ca.bc.gov.restapi.results.oracle.entity.cover.ForestCoverEntity;
import ca.bc.gov.restapi.results.oracle.entity.cover.ForestCoverPolygonProjection;
import ca.bc.gov.restapi.results.oracle.entity.cover.ForestCoverProjection;
import ca.bc.gov.restapi.results.oracle.entity.cover.ForestCoverSpeciesProjection;
import ca.bc.gov.restapi.results.oracle.entity.cover.ForestCoverUnmappedProjection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ForestCoverEntityRepository extends JpaRepository<ForestCoverEntity,Long> {

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_FOREST_COVER_LIST)
  List<ForestCoverProjection> findByOpeningDetails(Long openingId, String mainSearchTerm);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_FOREST_COVER_LIST_SPECIES)
  List<ForestCoverSpeciesProjection> findByOpeningDetailsSpecies(Long forestCoverId, String coverLayerCode);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_FOREST_COVER_POLYGON)
  Optional<ForestCoverPolygonProjection> findByOpeningDetailsPolygon(Long forestCoverId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_FOREST_COVER_UNMAPPED)
  Optional<ForestCoverUnmappedProjection> findByOpeningDetailsUnmapped(Long forestCoverId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_FOREST_COVER_LAYER)
  List<ForestCoverDetailsLayerProjection> findByOpeningDetailsLayer(Long forestCoverId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_FOREST_COVER_DETAILS_SPECIES)
  List<ForestCoverDetailedSpeciesProjection> findByOpeningDetailsDetailedSpecies(Long forestCoverLayerId);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_FOREST_COVER_DAMAGE)
  Optional<ForestCoverDetailsDamageProjection> findByOpeningDetailsDamage(Long forestCoverLayerId);


}
