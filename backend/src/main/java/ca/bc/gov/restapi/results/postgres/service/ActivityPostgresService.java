package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.service.ForestClientService;
import ca.bc.gov.restapi.results.common.service.impl.AbstractActivityService;
import ca.bc.gov.restapi.results.postgres.repository.ActivityTreatmentUnitPostgresRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class ActivityPostgresService extends AbstractActivityService {
  public ActivityPostgresService(
      ActivityTreatmentUnitPostgresRepository activityTreatmentUnitRepository,
      ForestClientService forestClientService) {
    super(activityTreatmentUnitRepository, forestClientService);
  }
}
