package ca.bc.gov.restapi.results;

import ca.bc.gov.restapi.results.config.NativeRuntimeHints;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportRuntimeHints;

/** Entry point for the RESULTS REST API application. */
@SpringBootApplication
@ImportRuntimeHints(NativeRuntimeHints.class)
@EntityScan(basePackages = {
    "ca.bc.gov.restapi.results.oracle.entity",
    "ca.bc.gov.restapi.results.postgres.entity"
})
@ComponentScan(basePackages = "ca.bc.gov.restapi.results")
public class ResultsApplication {

  public static void main(String[] args) {
    SpringApplication.run(ResultsApplication.class, args);
  }
}
