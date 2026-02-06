package ca.bc.gov.restapi.results.postgres.service.opening.details;

import ca.bc.gov.restapi.results.common.repository.OpeningRepository;
import ca.bc.gov.restapi.results.common.service.opening.details.impl.AbstractOpeningDetailsNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
@Service
public class OpeningDetailsNotificationPostgresService
    extends AbstractOpeningDetailsNotificationService {

  public OpeningDetailsNotificationPostgresService(OpeningRepository<?> openingRepository) {
    super(openingRepository);
  }
}
