package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.entity.BaseCodeEntity;
import ca.bc.gov.restapi.results.common.entity.BaseOpeningEntity;
import ca.bc.gov.restapi.results.common.provider.ForestClientApiProvider;
import ca.bc.gov.restapi.results.common.repository.OpenCategoryCodeRepository;
import ca.bc.gov.restapi.results.common.repository.OpeningRepository;
import ca.bc.gov.restapi.results.common.repository.OpeningStatusCodeRepository;
import ca.bc.gov.restapi.results.common.security.LoggedUserHelper;
import ca.bc.gov.restapi.results.common.service.impl.AbstractOpeningSearchService;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningEntity;
import ca.bc.gov.restapi.results.postgres.service.UserOpeningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/** This class holds methods for fetching and handling {@link OpeningEntity} in general. */
@Slf4j
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
@Service
public class OpeningSearchOracleService extends AbstractOpeningSearchService {

  public OpeningSearchOracleService(
      OpeningRepository<? extends BaseOpeningEntity> openingRepository,
      OpenCategoryCodeRepository<? extends BaseCodeEntity> openCategoryCodeRepository,
      OpeningStatusCodeRepository<? extends BaseCodeEntity> openingStatusCodeRepository,
      LoggedUserHelper loggedUserHelper, ForestClientApiProvider forestClientApiProvider,
      UserOpeningService userOpeningService) {
    super(openingRepository, openCategoryCodeRepository, openingStatusCodeRepository,
        loggedUserHelper, forestClientApiProvider, userOpeningService);
  }
}
