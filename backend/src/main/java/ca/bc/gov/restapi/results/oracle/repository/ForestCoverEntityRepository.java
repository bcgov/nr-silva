package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.oracle.SilvaOracleQueryConstants;
import ca.bc.gov.restapi.results.oracle.entity.cover.ForestCoverEntity;
import ca.bc.gov.restapi.results.oracle.entity.cover.ForestCoverProjection;
import ca.bc.gov.restapi.results.oracle.entity.cover.ForestCoverSpeciesProjection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ForestCoverEntityRepository extends JpaRepository<ForestCoverEntity,Long> {

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_FOREST_COVER_LIST)
  List<ForestCoverProjection> findByOpeningDetails(Long openingId, String mainSearchTerm);

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_OPENING_FOREST_COVER_LIST_SPECIES)
  List<ForestCoverSpeciesProjection> findByOpeningDetailsSpecies(Long forestCoverId, String coverLayerCode);


}
