package ca.bc.gov.restapi.results.common.repository;

import ca.bc.gov.restapi.results.common.projection.activity.*;
import ca.bc.gov.restapi.results.common.projection.opening.OpeningActivitiesActivitiesProjection;
import ca.bc.gov.restapi.results.common.projection.opening.OpeningActivitiesDisturbanceProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ActivityTreatmentUnitRepository {
  Page<OpeningActivitiesDisturbanceProjection> getOpeningActivitiesDisturbanceByOpeningId(
      Long openingId, Pageable page);

  Page<OpeningActivitiesActivitiesProjection> getOpeningActivitiesActivitiesByOpeningId(
      Long openingId, String mainSearchTerm, Pageable page);

  OpeningActivityBaseProjection getOpeningActivitiesBase(Long openingId, Long atuId);

  Optional<OpeningActivitySurveyProjection> getOpeningActivitySU(Long openingId, Long atuId);

  List<OpeningActivitySpeciesProjection> getOpeningActivitySpecies(Long openingId, Long atuId);

  Optional<OpeningActivityJuvenileProjection> getOpeningActivityJS(Long openingId, Long atuId);

  Optional<OpeningActivityPruningProjection> getOpeningActivityPR(Long openingId, Long atuId);

  Optional<Long> getOpeningActivitySP(Long openingId, Long atuId);
}
