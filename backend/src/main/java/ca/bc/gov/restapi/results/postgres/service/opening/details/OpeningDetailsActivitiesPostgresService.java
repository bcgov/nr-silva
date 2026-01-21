package ca.bc.gov.restapi.results.postgres.service.opening.details;

import ca.bc.gov.restapi.results.common.repository.ActivityTreatmentUnitRepository;
import ca.bc.gov.restapi.results.common.repository.SilvicultureCommentRepository;
import ca.bc.gov.restapi.results.common.service.ForestClientService;
import ca.bc.gov.restapi.results.common.service.opening.details.impl.AbstractOpeningDetailsActivitiesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
@Service
public class OpeningDetailsActivitiesPostgresService extends
    AbstractOpeningDetailsActivitiesService {

  public OpeningDetailsActivitiesPostgresService(ActivityTreatmentUnitRepository activityRepository,
      ForestClientService forestClientService, SilvicultureCommentRepository commentRepository) {
    super(activityRepository, forestClientService, commentRepository);
  }
}
