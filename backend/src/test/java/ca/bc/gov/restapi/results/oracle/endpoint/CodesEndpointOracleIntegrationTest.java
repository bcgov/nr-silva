package ca.bc.gov.restapi.results.oracle.endpoint;

import ca.bc.gov.restapi.results.common.endpoint.AbstractCodesEndpointIntegrationTest;
import ca.bc.gov.restapi.results.oracle.entity.OrgUnitEntity;
import ca.bc.gov.restapi.results.oracle.entity.code.OpenCategoryCodeEntity;
import ca.bc.gov.restapi.results.oracle.entity.code.SilvBaseCodeOracleEntity;
import ca.bc.gov.restapi.results.oracle.entity.code.SilvFundSrceCodeOracleEntity;
import ca.bc.gov.restapi.results.oracle.entity.code.SilvMethodCodeOracleEntity;
import ca.bc.gov.restapi.results.oracle.entity.code.SilvObjectiveCodeOracleEntity;
import ca.bc.gov.restapi.results.oracle.entity.code.SilvTechniqueCodeOracleEntity;
import ca.bc.gov.restapi.results.oracle.repository.OpenCategoryCodeOracleRepository;
import ca.bc.gov.restapi.results.oracle.repository.OrgUnitOracleRepository;
import ca.bc.gov.restapi.results.oracle.repository.SilvBaseCodeOracleRepository;
import ca.bc.gov.restapi.results.oracle.repository.SilvFundSrceCodeOracleRepository;
import ca.bc.gov.restapi.results.oracle.repository.SilvMethodCodeOracleRepository;
import ca.bc.gov.restapi.results.oracle.repository.SilvObjectiveCodeOracleRepository;
import ca.bc.gov.restapi.results.oracle.repository.SilvTechniqueCodeOracleRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "oracle")
@DisplayName("Integrated Test | Codes Endpoint (Oracle)")
class CodesEndpointOracleIntegrationTest extends AbstractCodesEndpointIntegrationTest {

  @Autowired protected SilvBaseCodeOracleRepository silvBaseCodeOracleRepository;

  @Autowired protected SilvTechniqueCodeOracleRepository silvTechniqueCodeOracleRepository;

  @Autowired protected SilvMethodCodeOracleRepository silvMethodCodeOracleRepository;

  @Autowired protected SilvObjectiveCodeOracleRepository silvObjectiveCodeOracleRepository;

  @Autowired protected SilvFundSrceCodeOracleRepository silvFundSrceCodeOracleRepository;

  @Autowired protected OpenCategoryCodeOracleRepository openCategoryCodeOracleRepository;

  @Autowired protected OrgUnitOracleRepository orgUnitOracleRepository;

  @Override
  protected void setupTestData() {
    LocalDate today = LocalDate.now();
    LocalDateTime now = LocalDateTime.now();

    if (silvBaseCodeOracleRepository.findAll().isEmpty()) {
      silvBaseCodeOracleRepository.save(
          SilvBaseCodeOracleEntity.builder()
              .code("BB")
              .description("Base Code 1")
              .effectiveDate(today)
              .expiryDate(today.plusYears(1))
              .updateTimestamp(now)
              .build());
    }

    if (silvTechniqueCodeOracleRepository.findAll().isEmpty()) {
      silvTechniqueCodeOracleRepository.save(
          SilvTechniqueCodeOracleEntity.builder()
              .code("TT")
              .description("Technique Code 1")
              .effectiveDate(today)
              .expiryDate(today.plusYears(1))
              .updateTimestamp(now)
              .build());
    }

    if (silvMethodCodeOracleRepository.findAll().isEmpty()) {
      silvMethodCodeOracleRepository.save(
          SilvMethodCodeOracleEntity.builder()
              .code("MET01")
              .description("Method Code 1")
              .effectiveDate(today)
              .expiryDate(today.plusYears(1))
              .updateTimestamp(now)
              .build());
    }

    if (silvObjectiveCodeOracleRepository.findAll().isEmpty()) {
      silvObjectiveCodeOracleRepository.save(
          SilvObjectiveCodeOracleEntity.builder()
              .code("OBJ")
              .description("Objective Code 1")
              .effectiveDate(today)
              .expiryDate(today.plusYears(1))
              .updateTimestamp(now)
              .build());
    }

    if (silvFundSrceCodeOracleRepository.findAll().isEmpty()) {
      silvFundSrceCodeOracleRepository.save(
          SilvFundSrceCodeOracleEntity.builder()
              .code("FUN")
              .description("Fund Source Code 1")
              .effectiveDate(today)
              .expiryDate(today.plusYears(1))
              .updateTimestamp(now)
              .build());
    }

    if (openCategoryCodeOracleRepository.findAll().isEmpty()) {
      openCategoryCodeOracleRepository.save(
          OpenCategoryCodeEntity.builder()
              .code("OC1")
              .description("Opening Category 1")
              .effectiveDate(today)
              .expiryDate(today.plusYears(1))
              .updateTimestamp(now)
              .build());
    }

    if (orgUnitOracleRepository.findAll().isEmpty()) {
      orgUnitOracleRepository.save(
          OrgUnitEntity.builder()
              .orgUnitCode("OU01")
              .orgUnitName("Test Org Unit")
              .locationCode("LOC")
              .areaCode("AREA")
              .telephoneNo("1234567")
              .orgLevelCode('1')
              .officeNameCode("01")
              .rollupRegionNo(1L)
              .rollupRegionCode("RR01")
              .rollupDistNo(1L)
              .rollupDistCode("RD01")
              .effectiveDate(today)
              .expiryDate(today.plusYears(1))
              .updateTimestamp(today)
              .build());
    }
  }
}
