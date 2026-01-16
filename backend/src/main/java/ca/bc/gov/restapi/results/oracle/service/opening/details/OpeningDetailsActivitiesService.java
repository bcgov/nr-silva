package ca.bc.gov.restapi.results.oracle.service.opening.details;

import ca.bc.gov.restapi.results.common.repository.ActivityTreatmentUnitRepository;
import ca.bc.gov.restapi.results.common.repository.SilvicultureCommentRepository;
import ca.bc.gov.restapi.results.common.service.ForestClientService;
import ca.bc.gov.restapi.results.common.service.opening.details.impl.AbstractOpeningDetailsActivitiesService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OpeningDetailsActivitiesService extends AbstractOpeningDetailsActivitiesService {
  public OpeningDetailsActivitiesService(ActivityTreatmentUnitRepository activityRepository,
      ForestClientService forestClientService, SilvicultureCommentRepository commentRepository) {
    super(activityRepository, forestClientService, commentRepository);
  }
}
