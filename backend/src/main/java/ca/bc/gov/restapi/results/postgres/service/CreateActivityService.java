package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.security.LoggedUserHelper;
import ca.bc.gov.restapi.results.postgres.dto.CreateActivityRequestDto;
import ca.bc.gov.restapi.results.postgres.dto.CreateActivityResponseDto;
import ca.bc.gov.restapi.results.postgres.entity.activity.ActivityTreatmentUnitEntity;
import ca.bc.gov.restapi.results.postgres.entity.activity.ActivityTuCommentLinkEntity;
import ca.bc.gov.restapi.results.postgres.entity.activity.ActivityTuFundSrceEntity;
import ca.bc.gov.restapi.results.postgres.entity.activity.ActivityTuObjectiveEntity;
import ca.bc.gov.restapi.results.postgres.entity.comment.SilvicultureCommentEntity;
import ca.bc.gov.restapi.results.postgres.entity.opening.OpeningEntity;
import ca.bc.gov.restapi.results.postgres.enums.ActivityStatus;
import ca.bc.gov.restapi.results.postgres.repository.ActivityTreatmentUnitPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.ActivityTuCommentLinkPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.ActivityTuFundSrcePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.ActivityTuObjectivePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningPostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvicultureCommentPostgresRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Creates a basic activity under an opening. */
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class CreateActivityService {

  // results_ind: 'N' is the planned set of values, 'Y' the actual set
  private static final String PLANNED_SET = "N";
  private static final String ACTUAL_SET = "Y";
  private static final String HECTARES = "HA";
  private static final String COMMENT_SOURCE_CODE = "RSLT";
  private static final String COMMENT_TYPE_CODE = "GENERAL";

  private final CreateActivityValidationService validationService;
  private final ActivityTreatmentUnitPostgresRepository activityRepository;
  private final ActivityTuObjectivePostgresRepository objectiveRepository;
  private final ActivityTuFundSrcePostgresRepository fundSrceRepository;
  private final SilvicultureCommentPostgresRepository commentRepository;
  private final ActivityTuCommentLinkPostgresRepository commentLinkRepository;
  private final OpeningPostgresRepository openingRepository;
  private final LoggedUserHelper loggedUserHelper;

  /**
   * Validates and creates the activity with its objectives, funding sources and comment.
   *
   * @param dto the request
   * @return the created activity
   */
  @Transactional
  public CreateActivityResponseDto createActivity(CreateActivityRequestDto dto) {
    OpeningEntity opening = validationService.validate(dto);
    String userId = loggedUserHelper.getAuditUserId();
    LocalDateTime now = LocalDateTime.now();
    String resultsInd = dto.status() == ActivityStatus.COMPLETED ? ACTUAL_SET : PLANNED_SET;

    Long activityId = activityRepository.nextActivityId();
    activityRepository.save(buildActivity(dto, opening, activityId, resultsInd, userId, now));
    saveObjectives(dto.objectiveCodes(), activityId, resultsInd, userId, now);
    saveFundingSources(dto.fundingSourceCodes(), activityId, resultsInd, userId, now);
    saveComment(dto.comment(), activityId, userId, now);
    touchOpening(opening, userId, now);

    return new CreateActivityResponseDto(activityId, opening.getId(), dto.status(), dto.baseCode());
  }

  private ActivityTreatmentUnitEntity buildActivity(
      CreateActivityRequestDto dto,
      OpeningEntity opening,
      Long activityId,
      String resultsInd,
      String userId,
      LocalDateTime now) {
    var activity =
        ActivityTreatmentUnitEntity.builder()
            .id(activityId)
            .isNew(true)
            .activityTuSeqNo(activityRepository.nextSequenceNumber(opening.getId()))
            .orgUnitNo(opening.getOrgUnitNo())
            .openingId(opening.getId())
            .silvBaseCode(dto.baseCode())
            .silvActivityMeasurementCode(HECTARES)
            .activityLicenseeId(dto.licenseeActivityId())
            .fiaProjectId(dto.projectNumber())
            .plannedDate(dto.plannedDate().atStartOfDay())
            .plannedTreatmentAmount(dto.plannedArea())
            .plannedTreatmentCost(toWholeDollars(dto.plannedCost()))
            .resultsInd(resultsInd)
            .entryUserid(userId)
            .entryTimestamp(now)
            .updateUserid(userId)
            .updateTimestamp(now)
            .revisionCount(1);

    if (dto.status() == ActivityStatus.COMPLETED) {
      activity
          .silvTechniqueCode(dto.techniqueCode())
          .silvMethodCode(dto.methodCode())
          .treatmentAmount(dto.treatedArea())
          .actualTreatmentCost(toWholeDollars(dto.actualCost()))
          .atuCompletionDate(dto.completionDate().atStartOfDay());
    } else {
      activity.planSilvTechniqueCode(dto.techniqueCode()).planSilvMethodCode(dto.methodCode());
    }
    return activity.build();
  }

  private void saveObjectives(
      List<String> codes, Long activityId, String resultsInd, String userId, LocalDateTime now) {
    if (codes == null) {
      return;
    }
    objectiveRepository.saveAll(
        IntStream.range(0, codes.size())
            .mapToObj(
                index ->
                    ActivityTuObjectiveEntity.builder()
                        .activityTreatmentUnitId(activityId)
                        .resultsInd(resultsInd)
                        .silvObjectiveCode(codes.get(index))
                        .objectiveOrder((short) (index + 1))
                        .entryUserid(userId)
                        .entryTimestamp(now)
                        .build())
            .toList());
  }

  private void saveFundingSources(
      List<String> codes, Long activityId, String resultsInd, String userId, LocalDateTime now) {
    fundSrceRepository.saveAll(
        IntStream.range(0, codes.size())
            .mapToObj(
                index ->
                    ActivityTuFundSrceEntity.builder()
                        .activityTreatmentUnitId(activityId)
                        .resultsInd(resultsInd)
                        .silvFundSrceCode(codes.get(index))
                        .fundSrceOrder((short) (index + 1))
                        .entryUserid(userId)
                        .entryTimestamp(now)
                        .build())
            .toList());
  }

  private void saveComment(String comment, Long activityId, String userId, LocalDateTime now) {
    if (comment == null || comment.isBlank()) {
      return;
    }
    Long commentId = commentRepository.nextCommentId();
    commentRepository.save(
        SilvicultureCommentEntity.builder()
            .id(commentId)
            .commentDate(now)
            .commentSourceCode(COMMENT_SOURCE_CODE)
            .commentTypeCode(COMMENT_TYPE_CODE)
            .comment(comment.trim())
            .entryUserId(userId)
            .entryTimestamp(now)
            .updateUserId(userId)
            .updateTimestamp(now)
            .revision(1L)
            .build());
    commentLinkRepository.save(
        ActivityTuCommentLinkEntity.builder()
            .activityTreatmentUnitId(activityId)
            .silvicultureCommentId(commentId)
            .build());
  }

  // RESULTS also bumps the opening revision when one of its activities is saved
  private void touchOpening(OpeningEntity opening, String userId, LocalDateTime now) {
    opening.setUpdateUserId(userId);
    opening.setUpdateTimestamp(now);
    opening.setRevisionCount(
        opening.getRevisionCount() == null ? 1 : opening.getRevisionCount() + 1);
    openingRepository.save(opening);
  }

  private Integer toWholeDollars(BigDecimal amount) {
    return amount == null ? null : amount.intValueExact();
  }
}
