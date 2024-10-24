package ca.bc.gov.restapi.results;

import ca.bc.gov.restapi.results.common.configuration.CorsConfiguration;
import ca.bc.gov.restapi.results.common.configuration.DataSourceConfiguration;
import ca.bc.gov.restapi.results.common.configuration.ExternalApiConfiguration;
import ca.bc.gov.restapi.results.common.configuration.ProvidersConfiguration;
import ca.bc.gov.restapi.results.common.configuration.SecurityConfiguration;
import ca.bc.gov.restapi.results.common.configuration.SilvaHikariConfiguration;
import ca.bc.gov.restapi.results.common.configuration.SwaggerConfiguration;
import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.dto.OracleExtractionDto;
import ca.bc.gov.restapi.results.common.dto.OracleExtractionParamsDto;
import ca.bc.gov.restapi.results.common.dto.OracleLogDto;
import ca.bc.gov.restapi.results.common.dto.WmsLayersWhitelistUserDto;
import ca.bc.gov.restapi.results.common.enums.ForestClientStatusEnum;
import ca.bc.gov.restapi.results.common.enums.ForestClientTypeEnum;
import ca.bc.gov.restapi.results.common.exception.ForestClientNotFoundException;
import ca.bc.gov.restapi.results.common.exception.MaxPageSizeException;
import ca.bc.gov.restapi.results.common.exception.NotFoundGenericException;
import ca.bc.gov.restapi.results.common.exception.OpeningNotFoundException;
import ca.bc.gov.restapi.results.common.exception.UserNotFoundException;
import ca.bc.gov.restapi.results.common.exception.UserOpeningNotFoundException;
import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.common.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.common.security.IdentityProvider;
import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.common.security.UserAuthenticationHelper;
import ca.bc.gov.restapi.results.common.security.UserInfo;
import ca.bc.gov.restapi.results.common.util.TimestampUtil;
import ca.bc.gov.restapi.results.oracle.configuration.OracleGracefulShutdownConfiguration;
import ca.bc.gov.restapi.results.oracle.configuration.OracleJpaConfiguration;
import ca.bc.gov.restapi.results.oracle.configuration.OraclePersistenceConfiguration;
import ca.bc.gov.restapi.results.oracle.converter.OpeningCategoryConverter;
import ca.bc.gov.restapi.results.oracle.converter.OpeningStatusConverter;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.dto.RecentOpeningDto;
import ca.bc.gov.restapi.results.oracle.entity.ClientAcronymEntity;
import ca.bc.gov.restapi.results.oracle.entity.CutBlockOpenAdminEntity;
import ca.bc.gov.restapi.results.oracle.entity.OpenCategoryCodeEntity;
import ca.bc.gov.restapi.results.oracle.entity.OpeningAttachmentEntity;
import ca.bc.gov.restapi.results.oracle.entity.OpeningEntity;
import ca.bc.gov.restapi.results.oracle.entity.OrgUnitEntity;
import ca.bc.gov.restapi.results.oracle.entity.ResultsElectronicSubmissionEntity;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import ca.bc.gov.restapi.results.oracle.util.PaginationUtil;
import ca.bc.gov.restapi.results.postgres.configuration.DashboardUserManagerConfiguration;
import ca.bc.gov.restapi.results.postgres.configuration.PostgresGracefulShutdownConfiguration;
import ca.bc.gov.restapi.results.postgres.configuration.PostgresJpaConfiguration;
import ca.bc.gov.restapi.results.postgres.configuration.PostgresPersistenceConfiguration;
import ca.bc.gov.restapi.results.postgres.dto.DashboardFiltersDto;
import ca.bc.gov.restapi.results.postgres.dto.FreeGrowingMilestonesDto;
import ca.bc.gov.restapi.results.postgres.dto.MyRecentActionsRequestsDto;
import ca.bc.gov.restapi.results.postgres.dto.OpeningsPerYearDto;
import ca.bc.gov.restapi.results.postgres.entity.OpeningsActivityEntity;
import ca.bc.gov.restapi.results.postgres.entity.OpeningsActivityEntityId;
import ca.bc.gov.restapi.results.postgres.entity.OpeningsLastYearEntity;
import ca.bc.gov.restapi.results.postgres.entity.OracleExtractionLogsEntity;
import ca.bc.gov.restapi.results.postgres.entity.UserOpeningEntity;
import ca.bc.gov.restapi.results.postgres.entity.UserOpeningEntityId;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/** Entry point for the RESULTS REST API application. */
@SpringBootApplication
@EnableScheduling
@EnableAsync
@RegisterReflectionForBinding({
    ForestClientDto.class,
    OracleExtractionDto.class,
    OracleExtractionParamsDto.class,
    OracleLogDto.class,
    WmsLayersWhitelistUserDto.class,
    ForestClientStatusEnum.class,
    ForestClientTypeEnum.class,
    ForestClientNotFoundException.class,
    MaxPageSizeException.class,
    NotFoundGenericException.class,
    OpeningNotFoundException.class,
    UserNotFoundException.class,
    UserOpeningNotFoundException.class,
    PaginatedResult.class,
    PaginationParameters.class,
    CorsConfiguration.class,
    DataSourceConfiguration.class,
    ExternalApiConfiguration.class,
    ProvidersConfiguration.class,
    SecurityConfiguration.class,
    SilvaHikariConfiguration.class,
    SwaggerConfiguration.class,
    IdentityProvider.class,
    LoggedUserService.class,
    UserAuthenticationHelper.class,
    UserInfo.class,
    TimestampUtil.class,
    OracleGracefulShutdownConfiguration.class,
    OracleJpaConfiguration.class,
    OraclePersistenceConfiguration.class,
    OpeningCategoryConverter.class,
    OpeningStatusConverter.class,
    OpeningSearchFiltersDto.class,
    OpeningSearchResponseDto.class,
    RecentOpeningDto.class,
    ClientAcronymEntity.class,
    CutBlockOpenAdminEntity.class,
    OpenCategoryCodeEntity.class,
    OpeningAttachmentEntity.class,
    OpeningEntity.class,
    OrgUnitEntity.class,
    ResultsElectronicSubmissionEntity.class,
    OpeningCategoryEnum.class,
    OpeningStatusEnum.class,
    PaginationUtil.class,
    DashboardUserManagerConfiguration.class,
    PostgresGracefulShutdownConfiguration.class,
    PostgresJpaConfiguration.class,
    PostgresPersistenceConfiguration.class,
    DashboardFiltersDto.class,
    FreeGrowingMilestonesDto.class,
    MyRecentActionsRequestsDto.class,
    OpeningsPerYearDto.class,
    OpeningsActivityEntity.class,
    OpeningsActivityEntityId.class,
    OpeningsLastYearEntity.class,
    OracleExtractionLogsEntity.class,
    UserOpeningEntity.class,
    UserOpeningEntityId.class

})
public class ResultsApplication {

  public static void main(String[] args) {
    SpringApplication.run(ResultsApplication.class, args);
  }
}
