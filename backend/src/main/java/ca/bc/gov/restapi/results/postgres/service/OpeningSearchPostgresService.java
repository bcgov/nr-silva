package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.provider.ForestClientApiProvider;
import ca.bc.gov.restapi.results.common.repository.OpeningRepository;
import ca.bc.gov.restapi.results.common.security.LoggedUserHelper;
import ca.bc.gov.restapi.results.common.service.impl.AbstractOpeningSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
@Service
public class OpeningSearchPostgresService extends AbstractOpeningSearchService {

  public OpeningSearchPostgresService(OpeningRepository openingRepository,
      LoggedUserHelper loggedUserHelper, ForestClientApiProvider forestClientApiProvider,
      UserOpeningService userOpeningService) {
    super(openingRepository, loggedUserHelper, forestClientApiProvider, userOpeningService);
  }
}
