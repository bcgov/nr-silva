package ca.bc.gov.restapi.results;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Entry point for the RESULTS REST API application.
 */
@SpringBootApplication
@ComponentScan("ca.bc.gov.restapi.results")
@ComponentScan("ca.bc.gov.restapi.results.oracle.entity")
@ComponentScan("ca.bc.gov.restapi.results.postgres.entity")
public class ResultsApplication {

  public static void main(String[] args) {
    SpringApplication.run(ResultsApplication.class, args);
  }
}
