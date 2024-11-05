package ca.bc.gov.restapi.results.oracle.endpoint;
import ca.bc.gov.restapi.results.oracle.service.OpeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** This class holds endpoints for the Home Screen. */
@RestController
@RequestMapping(path = "/api/openings", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class OpeningEndpoint {

  private final OpeningService openingService;
}
