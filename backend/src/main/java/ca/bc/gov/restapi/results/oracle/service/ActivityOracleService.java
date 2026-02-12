package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.service.ForestClientService;
import ca.bc.gov.restapi.results.common.service.impl.AbstractActivityService;
import ca.bc.gov.restapi.results.oracle.repository.ActivityTreatmentUnitOracleRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
@Service
public class ActivityOracleService extends AbstractActivityService {

  public ActivityOracleService(
      ActivityTreatmentUnitOracleRepository activityTreatmentUnitRepository,
      CodeOracleService codeService,
      ForestClientService forestClientService) {
    super(activityTreatmentUnitRepository, codeService, forestClientService);
  }
}
