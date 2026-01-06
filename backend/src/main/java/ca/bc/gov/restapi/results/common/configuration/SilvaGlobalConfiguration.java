package ca.bc.gov.restapi.results.common.configuration;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

import ca.bc.gov.restapi.results.common.dto.ForestClientAutocompleteResultDto;
import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.dto.ForestClientLocationDto;
import ca.bc.gov.restapi.results.common.enums.ForestClientStatusEnum;
import ca.bc.gov.restapi.results.common.enums.ForestClientTypeEnum;
import ca.bc.gov.restapi.results.common.enums.YesNoEnum;
import ca.bc.gov.restapi.results.oracle.converter.OpeningCategoryConverter;
import ca.bc.gov.restapi.results.oracle.converter.OpeningStatusConverter;
import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.oracle.dto.RecentOpeningDto;
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
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsBecDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsOverviewDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsOverviewMilestoneDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsOverviewOpeningDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsStockingDetailsDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsStockingDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsStockingLayerDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsStockingSpeciesDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsTenureDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsTenuresDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsTombstoneDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningDetailsTombstoneOverviewDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningTombstoneDto;
import ca.bc.gov.restapi.results.oracle.entity.ClientAcronymEntity;
import ca.bc.gov.restapi.results.oracle.entity.CutBlockOpenAdminEntity;
import ca.bc.gov.restapi.results.oracle.entity.OpenCategoryCodeEntity;
import ca.bc.gov.restapi.results.oracle.entity.OrgUnitEntity;
import ca.bc.gov.restapi.results.oracle.entity.ResultsElectronicSubmissionEntity;
import ca.bc.gov.restapi.results.oracle.entity.activities.OpeningActivityBaseProjection;
import ca.bc.gov.restapi.results.oracle.entity.activities.OpeningActivityJuvenileProjection;
import ca.bc.gov.restapi.results.oracle.entity.activities.OpeningActivityPruningProjection;
import ca.bc.gov.restapi.results.oracle.entity.activities.OpeningActivitySpeciesProjection;
import ca.bc.gov.restapi.results.oracle.entity.activities.OpeningActivitySurveyProjection;
import ca.bc.gov.restapi.results.oracle.entity.comments.CommentProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningActivitiesActivitiesProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningActivitiesDisturbanceProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningEntity;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningStockingDetailsProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningStockingLayerProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningStockingSpeciesProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningTenureProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningTombstoneOverviewMilestoneProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningTombstoneOverviewOpeningProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningTombstoneProjection;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningAttachmentEntity;
import ca.bc.gov.restapi.results.oracle.enums.AuditActionCodeEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import ca.bc.gov.restapi.results.postgres.dto.DashboardFiltersDto;
import ca.bc.gov.restapi.results.postgres.dto.MyRecentActionsRequestsDto;
import ca.bc.gov.restapi.results.postgres.dto.OpeningsPerYearDto;
import ca.bc.gov.restapi.results.postgres.dto.UserRecentOpeningDto;
import ca.bc.gov.restapi.results.postgres.entity.UserOpeningEntity;
import ca.bc.gov.restapi.results.postgres.entity.UserOpeningEntityId;
import ca.bc.gov.restapi.results.postgres.entity.UserRecentOpeningEntity;
import tools.jackson.databind.json.JsonMapper;
import org.geojson.Crs;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;
import org.geojson.GeoJsonObjectVisitor.Adapter;
import org.geojson.Geometry;
import org.geojson.GeometryCollection;
import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.geojson.MultiLineString;
import org.geojson.MultiPoint;
import org.geojson.MultiPolygon;
import org.geojson.Point;
import org.geojson.Polygon;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import tools.jackson.databind.json.JsonMapper;

@Configuration
@RegisterReflectionForBinding({
    ForestClientAutocompleteResultDto.class,
    ForestClientDto.class,
    ForestClientLocationDto.class,
    ForestClientStatusEnum.class,
    ForestClientTypeEnum.class,
    YesNoEnum.class,
    OpeningCategoryConverter.class,
    OpeningStatusConverter.class,
    CodeDescriptionDto.class,
    RecentOpeningDto.class,
    OpeningSearchFiltersDto.class,
    OpeningSearchResponseDto.class,
    OpeningTombstoneDto.class,
    ClientAcronymEntity.class,
    CutBlockOpenAdminEntity.class,
    OpenCategoryCodeEntity.class,
    OpeningAttachmentEntity.class,
    OpeningEntity.class,
    OrgUnitEntity.class,
    ResultsElectronicSubmissionEntity.class,
    AuditActionCodeEnum.class,
    OpeningCategoryEnum.class,
    OpeningStatusEnum.class,
    DashboardFiltersDto.class,
    MyRecentActionsRequestsDto.class,
    OpeningsPerYearDto.class,
    UserRecentOpeningDto.class,
    UserOpeningEntity.class,
    UserOpeningEntityId.class,
    UserRecentOpeningEntity.class,
    FeatureCollection.class,
    Feature.class,
    Polygon.class,
    MultiPolygon.class,
    Point.class,
    MultiPoint.class,
    LineString.class,
    MultiLineString.class,
    LngLatAlt.class,
    Geometry.class,
    GeometryCollection.class,
    GeoJsonObject.class,
    Crs.class,
    Adapter.class,
    OpeningActivitiesActivitiesProjection.class,
    OpeningActivitiesDisturbanceProjection.class,
    OpeningStockingDetailsProjection.class,
    OpeningStockingLayerProjection.class,
    OpeningStockingSpeciesProjection.class,
    OpeningTenureProjection.class,
    OpeningTombstoneOverviewMilestoneProjection.class,
    OpeningTombstoneOverviewOpeningProjection.class,
    OpeningTombstoneProjection.class,
    CommentProjection.class,
    OpeningActivityBaseProjection.class,
    OpeningActivityJuvenileProjection.class,
    OpeningActivityPruningProjection.class,
    OpeningActivitySpeciesProjection.class,
    OpeningActivitySurveyProjection.class,
    OpeningDetailsActivitiesActivitiesDto.class,
    OpeningDetailsActivitiesDisturbanceDto.class,
    OpeningDetailsBecDto.class,
    OpeningDetailsOverviewDto.class,
    OpeningDetailsOverviewMilestoneDto.class,
    OpeningDetailsOverviewOpeningDto.class,
    OpeningDetailsStockingDetailsDto.class,
    OpeningDetailsStockingDto.class,
    OpeningDetailsStockingLayerDto.class,
    OpeningDetailsStockingSpeciesDto.class,
    OpeningDetailsTenureDto.class,
    OpeningDetailsTenuresDto.class,
    OpeningDetailsTombstoneDto.class,
    OpeningDetailsTombstoneOverviewDto.class,
    OpeningSearchFiltersDto.class,
    OpeningSearchResponseDto.class,
    OpeningTombstoneDto.class,
    CommentDto.class,
    OpeningActivityBaseDto.class,
    OpeningActivityJuvenileDto.class,
    OpeningActivityPruningDto.class,
    OpeningActivitySitePrepDto.class,
    OpeningActivitySpeciesDetailsDto.class,
    OpeningActivitySpeciesDto.class,
    OpeningActivitySurveyDto.class
})
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class SilvaGlobalConfiguration {

  @Bean
  public JsonMapper objectMapper(JsonMapper.Builder builder) {
    return builder.build();
  }

}
