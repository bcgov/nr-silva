package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.configuration.SilvaConfiguration;
import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.oracle.repository.ResultsAuditActivityRepository;
import ca.bc.gov.restapi.results.postgres.dto.MyRecentActionsRequestsDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserActionsService {

  private final ResultsAuditActivityRepository auditActivityRepository;
  private final LoggedUserService loggedUserService;
  private final SilvaConfiguration silvaConfiguration;

  public List<MyRecentActionsRequestsDto> getResultsAuditActivity() {
    String userId = loggedUserService.getLoggedUserId();

    log.info("Getting recent audit events for user {}", userId);

    return
        auditActivityRepository
            .findUserRecentAuditEvents(userId, silvaConfiguration.getLimits().getMaxActionsResults())
            .stream()
            .map(entity ->
                new MyRecentActionsRequestsDto(
                    entity.getActionCodeDescription(),
                    entity.getOpeningId(),
                    entity.getCategoryCode(),
                    entity.getCategoryCodeDescription(),
                    entity.getEntryTimestamp()
                )
            )
            .toList();
  }

}
