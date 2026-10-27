package ca.bc.gov.restapi.results.postgres.service.opening.details;

import ca.bc.gov.restapi.results.common.service.opening.details.OpeningDetailsActivitiesService;
import ca.bc.gov.restapi.results.common.service.opening.details.OpeningDetailsAttachmentService;
import ca.bc.gov.restapi.results.common.service.opening.details.OpeningDetailsForestCoverService;
import ca.bc.gov.restapi.results.common.service.opening.details.OpeningDetailsStockingService;
import ca.bc.gov.restapi.results.common.service.opening.details.OpeningDetailsTenureService;
import ca.bc.gov.restapi.results.common.service.opening.details.OpeningDetailsTombstoneService;
import ca.bc.gov.restapi.results.common.service.opening.details.impl.AbstractOpeningDetailsService;
import ca.bc.gov.restapi.results.common.service.opening.history.OpeningForestCoverHistoryService;
import ca.bc.gov.restapi.results.common.service.opening.history.OpeningStandardUnitHistoryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
@Service
public class OpeningDetailsPostgresService extends AbstractOpeningDetailsService {
  public OpeningDetailsPostgresService(
      @Qualifier("openingDetailsTombstonePostgresService") OpeningDetailsTombstoneService tombstoneService,
      @Qualifier("openingDetailsStockingPostgresService") OpeningDetailsStockingService stockingService,
      @Qualifier("openingDetailsActivitiesPostgresService") OpeningDetailsActivitiesService activitiesService,
      @Qualifier("openingDetailsTenurePostgresService") OpeningDetailsTenureService tenureService,
      @Qualifier("openingDetailsForestCoverPostgresService") OpeningDetailsForestCoverService forestCoverService,
      @Qualifier("openingForestCoverHistoryPostgresService") OpeningForestCoverHistoryService forestCoverHistoryService,
      @Qualifier("openingDetailsAttachmentPostgresService") OpeningDetailsAttachmentService attachmentService,
      @Qualifier("openingStandardUnitHistoryPostgresService") OpeningStandardUnitHistoryService standardUnitHistoryService) {
    super(tombstoneService, stockingService, activitiesService, tenureService, forestCoverService,
        forestCoverHistoryService, attachmentService, standardUnitHistoryService);
  }
}
