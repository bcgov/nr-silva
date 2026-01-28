package ca.bc.gov.restapi.results.postgres.service.opening.details;

import ca.bc.gov.restapi.results.common.service.opening.details.*;
import ca.bc.gov.restapi.results.common.service.opening.details.impl.AbstractOpeningDetailsService;
import ca.bc.gov.restapi.results.common.service.opening.history.OpeningForestCoverHistoryService;
import ca.bc.gov.restapi.results.common.service.opening.history.OpeningStandardUnitHistoryService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
@Service
public class OpeningDetailsPostgresService extends AbstractOpeningDetailsService {
  public OpeningDetailsPostgresService(OpeningDetailsTombstoneService tombstoneService,
      OpeningDetailsStockingService stockingService,
      OpeningDetailsActivitiesService activitiesService, OpeningDetailsTenureService tenureService,
      OpeningDetailsForestCoverService forestCoverService,
      OpeningForestCoverHistoryService forestCoverHistoryService,
      OpeningDetailsAttachmentService attachmentService,
      OpeningStandardUnitHistoryService standardUnitHistoryService) {
    super(tombstoneService, stockingService, activitiesService, tenureService, forestCoverService,
        forestCoverHistoryService, attachmentService, standardUnitHistoryService);
  }
}
