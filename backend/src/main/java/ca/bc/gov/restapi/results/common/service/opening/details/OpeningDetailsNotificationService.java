package ca.bc.gov.restapi.results.common.service.opening.details;

import ca.bc.gov.restapi.results.common.dto.opening.OpeningDetailsNotificationDto;

import java.util.List;

public interface OpeningDetailsNotificationService {
  List<OpeningDetailsNotificationDto> getNotifications(Long openingId);
}
