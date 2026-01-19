package ca.bc.gov.restapi.results.oracle.service.opening.details;

import ca.bc.gov.restapi.results.common.service.opening.details.impl.AbstractOpeningDetailsService;
import ca.bc.gov.restapi.results.common.service.opening.details.OpeningDetailsActivitiesService;
import ca.bc.gov.restapi.results.common.service.opening.details.OpeningDetailsAttachmentService;
import ca.bc.gov.restapi.results.common.service.opening.details.OpeningDetailsForestCoverService;
import ca.bc.gov.restapi.results.common.service.opening.details.OpeningDetailsStockingService;
import ca.bc.gov.restapi.results.common.service.opening.details.OpeningDetailsTenureService;
import ca.bc.gov.restapi.results.common.service.opening.details.OpeningDetailsTombstoneService;
import ca.bc.gov.restapi.results.common.service.opening.history.OpeningForestCoverHistoryService;


import ca.bc.gov.restapi.results.common.service.opening.history.OpeningStandardUnitHistoryService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
@Service
public class OpeningDetailsOracleService extends AbstractOpeningDetailsService {
  public OpeningDetailsOracleService(OpeningDetailsTombstoneService tombstoneService,
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
