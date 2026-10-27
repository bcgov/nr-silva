package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.exception.NotFoundGenericException;
import ca.bc.gov.restapi.results.common.security.LoggedUserHelper;
import ca.bc.gov.restapi.results.common.util.DateUtil;
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
import ca.bc.gov.restapi.results.postgres.security.OperationAccessPolicy;
import ca.bc.gov.restapi.results.postgres.security.ProtectedOperation;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;
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
  private final ActivityGeometryService geometryService;
  private final ActivityTreatmentUnitPostgresRepository activityRepository;
  private final ActivityTuObjectivePostgresRepository objectiveRepository;
  private final ActivityTuFundSrcePostgresRepository fundSrceRepository;
  private final SilvicultureCommentPostgresRepository commentRepository;
  private final ActivityTuCommentLinkPostgresRepository commentLinkRepository;
  private final OpeningPostgresRepository openingRepository;
  private final OperationAccessPolicy accessPolicy;
  private final LoggedUserHelper loggedUserHelper;

  /**
   * Validates and creates the activity with its objectives, funding sources and comment.
   *
   * @param openingId the opening the activity is created under
   * @param dto the request
   * @return the created activity
   */
  @Transactional
  public CreateActivityResponseDto createActivity(Long openingId, CreateActivityRequestDto dto) {
    return create(openingId, dto, null, null);
  }

  /**
   * Validates and creates the activity with its objectives, funding sources, comment and the
   * optional spatial file. Nothing is saved when the request or the file is invalid.
   *
   * @param openingId the opening the activity is created under
   * @param dto the request
   * @param fileName the original name of the spatial file, or null when there is no file
   * @param fileReader reads the spatial file, checks its size and scans it for viruses, or null
   *     when there is no file. It is only called once the opening is known to exist and the user
   *     has access to it, and before the request is validated or anything is saved.
   * @return the created activity
   */
  @Transactional
  public CreateActivityResponseDto createActivity(
      Long openingId,
      CreateActivityRequestDto dto,
      String fileName,
      Supplier<byte[]> fileReader) {
    return create(openingId, dto, fileName, fileReader);
  }

  // Shared by both transactional entry points so one does not call the other through 'this'
  private CreateActivityResponseDto create(
      Long openingId,
      CreateActivityRequestDto dto,
      String fileName,
      Supplier<byte[]> fileReader) {
    // 404 for an unknown opening first, then 403, then the file is read and scanned; only then the
    // opening row is locked and the request validated, so users without access can neither make
    // the server process a file nor hold the lock
    if (!openingRepository.existsById(openingId)) {
      throw new NotFoundGenericException("Opening");
    }
    accessPolicy.requireOpeningRole(ProtectedOperation.CREATE_ACTIVITY, openingId);
    byte[] fileBytes = fileReader == null ? null : fileReader.get();
    // The lock serialises creates under the same opening: the activity sequence number is
    // MAX + 1 and the opening revision count is read-modify-write, both would race otherwise
    OpeningEntity opening =
        openingRepository
            .findByIdForUpdate(openingId)
            .orElseThrow(() -> new NotFoundGenericException("Opening"));
    validationService.validate(opening, dto);
    var geometry = fileBytes == null ? null : geometryService.process(fileName, fileBytes);
    String userId = loggedUserHelper.getAuditUserId();
    LocalDateTime now = DateUtil.nowInVancouver();
    String resultsInd = dto.status() == ActivityStatus.COMPLETED ? ACTUAL_SET : PLANNED_SET;

    Long activityId = activityRepository.nextActivityId();
    activityRepository.save(buildActivity(dto, opening, activityId, resultsInd, userId, now));
    if (geometry != null) {
      geometryService.save(activityId, geometry, userId, now);
    }
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
            .plannedDate(dto.plannedDate() == null ? null : dto.plannedDate().atStartOfDay())
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
