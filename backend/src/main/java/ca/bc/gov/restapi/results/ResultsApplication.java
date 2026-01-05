package ca.bc.gov.restapi.results;

import ca.bc.gov.restapi.results.config.NativeRuntimeHints;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportRuntimeHints;

/** Entry point for the RESULTS REST API application. */
@SpringBootApplication
@ImportRuntimeHints(NativeRuntimeHints.class)
@ComponentScan(basePackages = "ca.bc.gov.restapi.results")
public class ResultsApplication {

  public static void main(String[] args) {
    SpringApplication.run(ResultsApplication.class, args);
  }
}
