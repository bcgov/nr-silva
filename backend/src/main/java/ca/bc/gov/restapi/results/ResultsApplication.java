package ca.bc.gov.restapi.results;

import ca.bc.gov.restapi.results.config.NativeRuntimeHints;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportRuntimeHints;

/**
 * Entry point for the RESULTS REST API application.
 */
@SpringBootApplication
@ComponentScan(
  basePackages = {
    "ca.bc.gov.restapi.results",
    "ca.bc.gov.restapi.results.oracle.entity",
    "ca.bc.gov.restapi.results.postgres.entity"
})
@ImportRuntimeHints(NativeRuntimeHints.class)
public class ResultsApplication {

  public static void main(String[] args) {
    SpringApplication.run(ResultsApplication.class, args);
  }
}
