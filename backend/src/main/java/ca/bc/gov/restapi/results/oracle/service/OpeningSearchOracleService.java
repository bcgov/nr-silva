package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.provider.ForestClientApiProvider;
import ca.bc.gov.restapi.results.common.repository.OpeningRepository;
import ca.bc.gov.restapi.results.common.security.LoggedUserHelper;
import ca.bc.gov.restapi.results.common.service.impl.AbstractOpeningSearchService;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningEntity;
import ca.bc.gov.restapi.results.postgres.service.UserOpeningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** This class holds methods for fetching and handling {@link OpeningEntity} in general. */
@Slf4j
@Service
public class OpeningSearchOracleService extends AbstractOpeningSearchService {

  public OpeningSearchOracleService(OpeningRepository openingRepository,
      LoggedUserHelper loggedUserHelper, ForestClientApiProvider forestClientApiProvider,
      UserOpeningService userOpeningService) {
    super(openingRepository, loggedUserHelper, forestClientApiProvider, userOpeningService);
  }
}
