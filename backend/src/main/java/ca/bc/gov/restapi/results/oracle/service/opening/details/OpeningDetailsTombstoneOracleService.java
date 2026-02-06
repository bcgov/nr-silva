package ca.bc.gov.restapi.results.oracle.service.opening.details;

import ca.bc.gov.restapi.results.common.repository.OpeningRepository;
import ca.bc.gov.restapi.results.common.repository.SilvicultureCommentRepository;
import ca.bc.gov.restapi.results.common.service.ForestClientService;
import ca.bc.gov.restapi.results.common.service.opening.details.OpeningDetailsNotificationService;
import ca.bc.gov.restapi.results.common.service.opening.details.impl.AbstractOpeningDetailsTombstoneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
@Service
public class OpeningDetailsTombstoneOracleService extends AbstractOpeningDetailsTombstoneService {

  public OpeningDetailsTombstoneOracleService(
      OpeningRepository<?> openingRepository,
      ForestClientService forestClientService,
      SilvicultureCommentRepository commentRepository,
      OpeningDetailsNotificationService openingDetailsNotificationService) {
    super(
        openingRepository,
        forestClientService,
        commentRepository,
        openingDetailsNotificationService);
  }
}
