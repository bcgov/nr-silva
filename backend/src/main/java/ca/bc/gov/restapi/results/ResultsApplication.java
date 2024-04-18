package ca.bc.gov.restapi.results;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/** Entry point for the RESULTS REST API application. */
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class ResultsApplication {

  public static void main(String[] args) {
    SpringApplication.run(ResultsApplication.class, args);
  }
}
