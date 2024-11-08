package ca.bc.gov.restapi.results.oracle.endpoint;

import ca.bc.gov.restapi.results.oracle.service.UserActionsService;
import ca.bc.gov.restapi.results.postgres.dto.MyRecentActionsRequestsDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserActionsEndpoint {

  private final UserActionsService userActionsService;

  @GetMapping("/recent-actions")
  public ResponseEntity<List<MyRecentActionsRequestsDto>> getUserRecentOpeningsActions() {
    List<MyRecentActionsRequestsDto> actionsDto =
        userActionsService.getResultsAuditActivity();

    log.info("Returning {} recent actions", actionsDto.size());

    if (actionsDto.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(actionsDto);
  }

}
