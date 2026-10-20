package ca.bc.gov.restapi.results.postgres.service.opening.details;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.common.dto.opening.OpeningDetailsActivitiesActivitiesDto;
import ca.bc.gov.restapi.results.common.repository.ActivityTreatmentUnitRepository;
import ca.bc.gov.restapi.results.common.repository.SilvicultureCommentRepository;
import ca.bc.gov.restapi.results.common.service.ForestClientService;
import ca.bc.gov.restapi.results.common.service.opening.details.impl.AbstractOpeningDetailsActivitiesService;
import ca.bc.gov.restapi.results.postgres.repository.ActivityAdditionalCodeProjection;
import ca.bc.gov.restapi.results.postgres.repository.ActivityTuFundSrcePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.ActivityTuObjectivePostgresRepository;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
@Service
public class OpeningDetailsActivitiesPostgresService extends
    AbstractOpeningDetailsActivitiesService {

  private static final String COMPLETE_STATUS = "CPT";
  private static final String ACTUAL_SET = "Y";
  private static final String PLANNED_SET = "N";

  private final ActivityTuObjectivePostgresRepository objectiveRepository;
  private final ActivityTuFundSrcePostgresRepository fundSrceRepository;

  public OpeningDetailsActivitiesPostgresService(ActivityTreatmentUnitRepository activityRepository,
      ForestClientService forestClientService, SilvicultureCommentRepository commentRepository,
      ActivityTuObjectivePostgresRepository objectiveRepository,
      ActivityTuFundSrcePostgresRepository fundSrceRepository) {
    super(activityRepository, forestClientService, commentRepository);
    this.objectiveRepository = objectiveRepository;
    this.fundSrceRepository = fundSrceRepository;
  }

  /**
   * Activities created in Silva keep their objectives and funding sources in child tables. The
   * legacy activity columns win when they have a value, so migrated activities are untouched.
   */
  @Override
  protected List<OpeningDetailsActivitiesActivitiesDto> addAdditionalActivityCodes(
      List<OpeningDetailsActivitiesActivitiesDto> activities) {
    List<Long> withoutObjectives =
        activities.stream()
            .filter(activity -> activity.objectives().isEmpty())
            .map(OpeningDetailsActivitiesActivitiesDto::atuId)
            .toList();
    List<Long> withoutFunding =
        activities.stream()
            .filter(activity -> activity.fundingSources().isEmpty())
            .map(OpeningDetailsActivitiesActivitiesDto::atuId)
            .toList();

    Map<Long, List<ActivityAdditionalCodeProjection>> objectives =
        group(withoutObjectives, objectiveRepository::findCodesByActivityIds);
    Map<Long, List<ActivityAdditionalCodeProjection>> funding =
        group(withoutFunding, fundSrceRepository::findCodesByActivityIds);

    return activities.stream()
        .map(activity -> withObjectives(activity, objectives.get(activity.atuId())))
        .map(activity -> withFunding(activity, funding.get(activity.atuId())))
        .toList();
  }

  private Map<Long, List<ActivityAdditionalCodeProjection>> group(
      Collection<Long> activityIds,
      Function<Collection<Long>, List<ActivityAdditionalCodeProjection>> finder) {
    if (activityIds.isEmpty()) {
      return Map.of();
    }
    return finder.apply(activityIds).stream()
        .collect(
            Collectors.groupingBy(ActivityAdditionalCodeProjection::getActivityTreatmentUnitId));
  }

  private OpeningDetailsActivitiesActivitiesDto withObjectives(
      OpeningDetailsActivitiesActivitiesDto activity, List<ActivityAdditionalCodeProjection> rows) {
    List<CodeDescriptionDto> codes = codesOfActiveSet(activity, rows);
    if (codes.isEmpty()) {
      return activity;
    }
    return activity
        .withObjectives(codes)
        .withObjective1(codeAt(codes, 0))
        .withObjective2(codeAt(codes, 1))
        .withObjective3(codeAt(codes, 2));
  }

  private OpeningDetailsActivitiesActivitiesDto withFunding(
      OpeningDetailsActivitiesActivitiesDto activity, List<ActivityAdditionalCodeProjection> rows) {
    List<CodeDescriptionDto> codes = codesOfActiveSet(activity, rows);
    if (codes.isEmpty()) {
      return activity;
    }
    return activity.withFundingSources(codes).withFunding(codeAt(codes, 0));
  }

  private List<CodeDescriptionDto> codesOfActiveSet(
      OpeningDetailsActivitiesActivitiesDto activity, List<ActivityAdditionalCodeProjection> rows) {
    if (rows == null) {
      return List.of();
    }
    String resultsInd = COMPLETE_STATUS.equals(activity.status().code()) ? ACTUAL_SET : PLANNED_SET;
    return rows.stream()
        .filter(row -> resultsInd.equals(row.getResultsInd()))
        .map(row -> new CodeDescriptionDto(row.getCode(), row.getDescription()))
        .toList();
  }

  private CodeDescriptionDto codeAt(List<CodeDescriptionDto> codes, int index) {
    return index < codes.size() ? codes.get(index) : new CodeDescriptionDto(null, null);
  }
}
