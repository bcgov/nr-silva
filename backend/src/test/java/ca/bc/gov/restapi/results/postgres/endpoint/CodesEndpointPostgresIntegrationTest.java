package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.common.endpoint.AbstractCodesEndpointIntegrationTest;
import ca.bc.gov.restapi.results.postgres.entity.code.SilvBaseCodePostgresEntity;
import ca.bc.gov.restapi.results.postgres.entity.code.SilvFundSrceCodePostgresEntity;
import ca.bc.gov.restapi.results.postgres.entity.code.SilvMethodCodePostgresEntity;
import ca.bc.gov.restapi.results.postgres.entity.code.SilvObjectiveCodePostgresEntity;
import ca.bc.gov.restapi.results.postgres.entity.code.SilvTechniqueCodePostgresEntity;
import ca.bc.gov.restapi.results.postgres.repository.SilvBaseCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvFundSrceCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvMethodCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvObjectiveCodePostgresRepository;
import ca.bc.gov.restapi.results.postgres.repository.SilvTechniqueCodePostgresRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@DisplayName("Integrated Test | Codes Endpoint (Postgres)")
class CodesEndpointPostgresIntegrationTest extends AbstractCodesEndpointIntegrationTest {

  @Autowired protected SilvBaseCodePostgresRepository silvBaseCodePostgresRepository;

  @Autowired protected SilvTechniqueCodePostgresRepository silvTechniqueCodePostgresRepository;

  @Autowired protected SilvMethodCodePostgresRepository silvMethodCodePostgresRepository;

  @Autowired protected SilvObjectiveCodePostgresRepository silvObjectiveCodePostgresRepository;

  @Autowired protected SilvFundSrceCodePostgresRepository silvFundSrceCodePostgresRepository;

  @Override
  protected void setupTestData() {
    LocalDate today = LocalDate.now();
    LocalDateTime now = LocalDateTime.now();

    if (silvBaseCodePostgresRepository.findAll().isEmpty()) {
      silvBaseCodePostgresRepository.save(
          SilvBaseCodePostgresEntity.builder()
              .code("BB")
              .description("Base Code 1")
              .effectiveDate(today)
              .expiryDate(today.plusYears(1))
              .updateTimestamp(now)
              .build());
    }

    if (silvTechniqueCodePostgresRepository.findAll().isEmpty()) {
      silvTechniqueCodePostgresRepository.save(
          SilvTechniqueCodePostgresEntity.builder()
              .code("TT")
              .description("Technique Code 1")
              .effectiveDate(today)
              .expiryDate(today.plusYears(1))
              .updateTimestamp(now)
              .build());
    }

    if (silvMethodCodePostgresRepository.findAll().isEmpty()) {
      silvMethodCodePostgresRepository.save(
          SilvMethodCodePostgresEntity.builder()
              .code("MET01")
              .description("Method Code 1")
              .effectiveDate(today)
              .expiryDate(today.plusYears(1))
              .updateTimestamp(now)
              .build());
    }

    if (silvObjectiveCodePostgresRepository.findAll().isEmpty()) {
      silvObjectiveCodePostgresRepository.save(
          SilvObjectiveCodePostgresEntity.builder()
              .code("OBJ")
              .description("Objective Code 1")
              .effectiveDate(today)
              .expiryDate(today.plusYears(1))
              .updateTimestamp(now)
              .build());
    }

    if (silvFundSrceCodePostgresRepository.findAll().isEmpty()) {
      silvFundSrceCodePostgresRepository.save(
          SilvFundSrceCodePostgresEntity.builder()
              .code("FUN")
              .description("Fund Source Code 1")
              .effectiveDate(today)
              .expiryDate(today.plusYears(1))
              .updateTimestamp(now)
              .build());
    }
  }
}
