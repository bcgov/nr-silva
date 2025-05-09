package ca.bc.gov.restapi.results.oracle.service.opening.details;

import ca.bc.gov.restapi.results.common.service.ForestClientService;
import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.oracle.dto.activity.OpeningActivityBaseDto;
import ca.bc.gov.restapi.results.oracle.dto.activity.OpeningActivityJuvenileDto;
import ca.bc.gov.restapi.results.oracle.dto.activity.OpeningActivityPruningDto;
import ca.bc.gov.restapi.results.oracle.dto.activity.OpeningActivitySitePrepDto;
import ca.bc.gov.restapi.results.oracle.dto.activity.OpeningActivitySpeciesDetailsDto;
import ca.bc.gov.restapi.results.oracle.dto.activity.OpeningActivitySpeciesDto;
import ca.bc.gov.restapi.results.oracle.dto.activity.OpeningActivitySurveyDto;
import ca.bc.gov.restapi.results.oracle.dto.comment.CommentDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsActivitiesActivitiesDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsActivitiesDisturbanceDto;
import ca.bc.gov.restapi.results.oracle.repository.ActivityTreatmentUnitRepository;
import ca.bc.gov.restapi.results.oracle.repository.SilvicultureCommentRepository;
import ca.bc.gov.restapi.results.oracle.service.conversion.opening.OpeningDetailsCommentConverter;
import ca.bc.gov.restapi.results.oracle.util.PaginationUtil;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpeningDetailsActivitiesService {

  private final ActivityTreatmentUnitRepository activityRepository;
  private final ForestClientService forestClientService;
  private final SilvicultureCommentRepository commentRepository;

  private static final Map<String, String> DISTURBANCE_SORT_FIELDS = Map.of(
      "atuId", "atu.ACTIVITY_TREATMENT_UNIT_ID",
      "disturbance", "atu.DISTURBANCE_CODE",
      "variant", "atu.SILV_SYSTEM_VARIANT_CODE",
      "system", "atu.SILV_SYSTEM_CODE",
      "cutPhase", "atu.SILV_CUT_PHASE_CODE",
      "disturbanceArea", "atu.TREATMENT_AMOUNT",
      "startDate", "atu.ATU_START_DATE",
      "endDate", "atu.ATU_COMPLETION_DATE",
      "lastUpdate", "atu.UPDATE_TIMESTAMP"
  );
  private static final Map<String, String> ACTIVITIES_SORT_FIELDS = Map.of(
      "atuId", "atu.ACTIVITY_TREATMENT_UNIT_ID",
      "base", "atu.SILV_BASE_CODE",
      "tech", "atu.SILV_TECHNIQUE_CODE",
      "method", "atu.SILV_METHOD_CODE",
      "area", "atu.TREATMENT_AMOUNT",
      "funding", "atu.SILV_FUND_SRCE_CODE",
      "projectId", "atu.SILVICULTURE_PROJECT_ID",
      "startDate", "atu.ATU_START_DATE",
      "endDate", "atu.ATU_COMPLETION_DATE",
      "lastUpdate", "atu.UPDATE_TIMESTAMP"
  );

  public Page<OpeningDetailsActivitiesDisturbanceDto> getOpeningActivitiesDisturbances(
      Long openingId, Pageable pageable) {
    return
        activityRepository
            .getOpeningActivitiesDisturbanceByOpeningId(
                openingId,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                    PaginationUtil.resolveSort(
                        pageable.getSort(),
                        "atu.ACTIVITY_TU_SEQ_NO",
                        DISTURBANCE_SORT_FIELDS
                    )
                )
            )
            .map(projection ->
                new OpeningDetailsActivitiesDisturbanceDto(
                    projection.getAtuId(),
                    new CodeDescriptionDto(
                        projection.getDisturbanceCode(),
                        projection.getDisturbanceName()
                    ),
                    new CodeDescriptionDto(
                        projection.getSystemCode(),
                        projection.getSystemName()
                    ),
                    new CodeDescriptionDto(
                        projection.getVariantCode(),
                        projection.getVariantName()
                    ),
                    new CodeDescriptionDto(
                        projection.getCutPhaseCode(),
                        projection.getCutPhaseName()
                    ),
                    projection.getDisturbanceArea(),
                    projection.getLastUpdate(),
                    projection.getStartDate(),
                    projection.getEndDate(),
                    projection.getLicenseeActivityId(),
                    forestClientService
                        .getClientByNumber(projection.getDisturbanceLocationClient())
                        .orElse(null),
                    forestClientService
                        .getClientLocation(projection.getDisturbanceLocationClient(),
                            projection.getDisturbanceLocationCode())
                        .orElse(null),
                    projection.getLicenceNumber(),
                    projection.getCuttingPermitId(),
                    projection.getCutBlock(),
                    List.of()
                )
            )
            .map(dto -> dto.withComments(getComments(dto.atuId())));
  }

  public Page<OpeningDetailsActivitiesActivitiesDto> getOpeningActivitiesActivities(
      Long openingId,
      String mainSearchTerm,
      Pageable pageable) {
    return
        activityRepository
            .getOpeningActivitiesActivitiesByOpeningId(
                openingId,
                Optional.ofNullable(mainSearchTerm).map(String::toUpperCase).orElse(null),
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                    PaginationUtil.resolveSort(
                        pageable.getSort(),
                        "atu.ACTIVITY_TU_SEQ_NO",
                        ACTIVITIES_SORT_FIELDS
                    )
                )
            )
            .map(projection ->
                new OpeningDetailsActivitiesActivitiesDto(
                    projection.getAtuId(),
                    new CodeDescriptionDto(
                        projection.getStatusCode(),
                        getStatusDescription(projection.getStatusCode())
                    ),
                    new CodeDescriptionDto(
                        projection.getBaseCode(),
                        projection.getBaseName()
                    ),
                    new CodeDescriptionDto(
                        projection.getTechCode(),
                        projection.getTechName()
                    ),
                    new CodeDescriptionDto(
                        projection.getMethodCode(),
                        projection.getMethodName()
                    ),
                    new CodeDescriptionDto(
                        projection.getObjective1Code(),
                        projection.getObjective1Name()
                    ),
                    new CodeDescriptionDto(
                        projection.getObjective2Code(),
                        projection.getObjective2Name()
                    ),
                    new CodeDescriptionDto(
                        projection.getObjective3Code(),
                        projection.getObjective3Name()
                    ),
                    projection.getArea(),
                    new CodeDescriptionDto(
                        projection.getFundingCode(),
                        projection.getFundingName()
                    ),
                    projection.getProjectId(),
                    projection.getLastUpdate(),
                    projection.getPlannedDate(),
                    projection.getEndDate()
                )
            );

  }

  public OpeningActivityBaseDto getOpeningActivitiesActivity(Long openingId, Long atuId) {

    var baseProjection = activityRepository.getOpeningActivitiesBase(openingId, atuId);
    var baseDto = new OpeningActivityBaseDto(
        baseProjection.getLicenseeActivityId(),
        baseProjection.getIntraAgencyNumber(),
        forestClientService
            .getClientByNumber(baseProjection.getActivityClient())
            .orElse(null),
        forestClientService
            .getClientLocation(baseProjection.getActivityClient(),
                baseProjection.getActivityLocation())
            .orElse(null),
        baseProjection.getPlannedAmount(),
        baseProjection.getTreatedAmount(),
        baseProjection.getPlannedCost(),
        baseProjection.getActualCost(),
        baseProjection.getTotalPlanting(),
        getComments(atuId)
    );

    return switch (baseProjection.getKind()) {
      case "DS" -> new OpeningActivitySpeciesDto(baseDto)
          .withSpecies(
              activityRepository
                  .getOpeningActivitySpecies(openingId, atuId)
                  .stream()
                  .map(projection ->
                      new OpeningActivitySpeciesDetailsDto(
                          new CodeDescriptionDto(
                              projection.getSpeciesCode(),
                              projection.getSpeciesName()
                          ),
                          projection.getPlantedNumber(),
                          projection.getNumberBeyondTransferLimit(),
                          projection.isCbst(),
                          null,
                          projection.getLot(),
                          null
                      )
                  )
                  .toList()
          );

      case "JS" -> activityRepository
          .getOpeningActivityJS(openingId, atuId)
          .map(projection -> new OpeningActivityJuvenileDto(baseDto)
              .withTargetIntertreeDistance(projection.getTargetIntertreeDistance())
              .withAllowableVariationDistance(projection.getAllowableVariationDistance())
              .withAllowableTreePerLot(projection.getAllowableTreePerLot())
              .withSpacingPerHa(projection.getSpacingPerHa())
              .withTotalPlanting(null)
          )
          .orElse(new OpeningActivityJuvenileDto(baseDto));

      case "PL" -> new OpeningActivitySpeciesDto(baseDto)
          .withSpecies(
              activityRepository
                  .getOpeningActivitySpecies(openingId, atuId)
                  .stream()
                  .map(projection -> new OpeningActivitySpeciesDetailsDto(
                          new CodeDescriptionDto(
                              projection.getSpeciesCode(),
                              projection.getSpeciesName()
                          ),
                          projection.getPlantedNumber(),
                          projection.getNumberBeyondTransferLimit(),
                          projection.isCbst(),
                          projection.getRequestId(),
                          projection.getLot(),
                          projection.getBidPricePerTree()
                      )
                  )
                  .toList()
          );

      case "PR" -> activityRepository
          .getOpeningActivityPR(openingId, atuId)
          .map(projection -> new OpeningActivityPruningDto(baseDto)
              .withTotalStemsPerHa(projection.getTotalStemsPerHa())
              .withStemsPerHaToPrune(projection.getStemsPerHaToPrune())
              .withTargetIntertreeDistance(projection.getTargetIntertreeDistance())
              .withMinimumIntertreeDistance(projection.getMinimumIntertreeDistance())
              .withHeightAboveGround(projection.getHeightAboveGround())
              .withMinimumLiveCrown(projection.getMinimumLiveCrown())
              .withTotalPlanting(null)
          )
          .orElse(new OpeningActivityPruningDto(baseDto));

      case "SP" -> activityRepository
          .getOpeningActivitySP(openingId, atuId)
          .map(projection -> new OpeningActivitySitePrepDto(baseDto)
              .withTargetSpot(projection)
              .withTotalPlanting(null)
          )
          .orElse(new OpeningActivitySitePrepDto(baseDto));

      case "SU" -> activityRepository
          .getOpeningActivitySU(openingId, atuId)
          .map(projection -> new OpeningActivitySurveyDto(baseDto)
              .withPlotsCount(projection.getPlotsCount())
              .withSurveyMinPlotsPerStratum(projection.getSurveyMinPlotsPerStratum())
              .withTotalPlanting(null)
          )
          .orElse(new OpeningActivitySurveyDto(baseDto));

      default -> baseDto.withTotalPlanting(null);
    };
  }

  private String getStatusDescription(String statusCode) {
    return switch (statusCode) {
      case "CPT" -> "Complete";
      case "PLN" -> "Planned";
      default -> "Invalid";
    };
  }

  private List<CommentDto> getComments(Long atuId) {
    return commentRepository
        .getCommentById(
            null,
            atuId,
            null,
            null,
            null,
            null
        )
        .stream()
        .map(OpeningDetailsCommentConverter.mapComments())
        .toList();
  }

}
