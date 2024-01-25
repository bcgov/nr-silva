package ca.bc.gov.restapi.silva;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** This class contains a simple controler with a single endpoint for testing purposes. */
@Tag(name = "Controller", description = "Simple controller class for testing")
@RestController
@RequestMapping("/test")
@Slf4j
public class Controller {

  /**
   * Simply logs the current date and time.
   *
   * @return A string containing current date and time.
   */
  @GetMapping
  public String testRequest() {
    String template = "Hello, current date time is %s";
    String finalMessage = String.format(template, LocalDateTime.now());
    log.info(finalMessage);
    return finalMessage;
  }
}
