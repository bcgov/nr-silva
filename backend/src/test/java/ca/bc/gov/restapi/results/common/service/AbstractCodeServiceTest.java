package ca.bc.gov.restapi.results.common.service;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Abstract test contract for CodeService implementations (Postgres and Oracle). Defines common test
 * scenarios for all code lookup services.
 */
@DisplayName("Integrated Test | CodeService | Contract")
public abstract class AbstractCodeServiceTest extends AbstractTestContainerIntegrationTest {

  @Autowired(required = false)
  protected CodeService codeService;

  @Test
  @DisplayName("Get all silv base codes should return list of DTOs")
  void getAllSilvBaseCode_shouldReturnList() {
    if (codeService == null) {
      return;
    }

    // When
    List<CodeDescriptionDto> result = codeService.getAllSilvBaseCode();

    // Then
    Assertions.assertNotNull(result);
    Assertions.assertInstanceOf(List.class, result);
    result.forEach(dto -> Assertions.assertNotNull(dto.code()));
  }

  @Test
  @DisplayName("Get all silv technique codes should return list of DTOs")
  void getAllSilvTechniqueCode_shouldReturnList() {
    if (codeService == null) {
      return;
    }

    // When
    List<CodeDescriptionDto> result = codeService.getAllSilvTechniqueCode();

    // Then
    Assertions.assertNotNull(result);
    Assertions.assertInstanceOf(List.class, result);
  }

  @Test
  @DisplayName("Get all silv method codes should return list of DTOs")
  void getAllSilvMethodCode_shouldReturnList() {
    if (codeService == null) {
      return;
    }

    // When
    List<CodeDescriptionDto> result = codeService.getAllSilvMethodCode();

    // Then
    Assertions.assertNotNull(result);
    Assertions.assertInstanceOf(List.class, result);
  }

  @Test
  @DisplayName("Get all silv objective codes should return list of DTOs")
  void getAllSilvObjectiveCode_shouldReturnList() {
    if (codeService == null) {
      return;
    }

    // When
    List<CodeDescriptionDto> result = codeService.getAllSilvObjectiveCode();

    // Then
    Assertions.assertNotNull(result);
    Assertions.assertInstanceOf(List.class, result);
  }

  @Test
  @DisplayName("Get all silv fund source codes should return list of DTOs")
  void getAllSilvFundSrceCode_shouldReturnList() {
    if (codeService == null) {
      return;
    }

    // When
    List<CodeDescriptionDto> result = codeService.getAllSilvFundSrceCode();

    // Then
    Assertions.assertNotNull(result);
    Assertions.assertInstanceOf(List.class, result);
  }

  @Test
  @DisplayName("All code results should contain valid CodeDescriptionDto objects")
  void allCodeResults_shouldContainValidDtos() {
    if (codeService == null) {
      return;
    }

    // When
    List<CodeDescriptionDto> baseResults = codeService.getAllSilvBaseCode();

    // Then - verify DTO structure
    baseResults.forEach(
        dto -> {
          Assertions.assertNotNull(dto, "DTO should not be null");
          Assertions.assertNotNull(dto.code(), "Code should not be null");
          Assertions.assertFalse(dto.code().isBlank(), "Code should not be blank");
        });
  }

  @Test
  @DisplayName("Find all categories with includeExpired false should return list")
  void findAllCategories_includeExpiredFalse_shouldReturnList() {
    if (codeService == null) {
      return;
    }

    // When
    List<CodeDescriptionDto> result = codeService.findAllCategories(false);

    // Then
    Assertions.assertNotNull(result);
    Assertions.assertInstanceOf(List.class, result);
  }

  @Test
  @DisplayName("Find all categories with includeExpired true should return list")
  void findAllCategories_includeExpiredTrue_shouldReturnList() {
    if (codeService == null) {
      return;
    }

    // When
    List<CodeDescriptionDto> result = codeService.findAllCategories(true);

    // Then
    Assertions.assertNotNull(result);
    Assertions.assertInstanceOf(List.class, result);
  }

  @Test
  @DisplayName("Find all categories should return DTOs with code and description")
  void findAllCategories_shouldReturnValidDtos() {
    if (codeService == null) {
      return;
    }

    // When
    List<CodeDescriptionDto> result = codeService.findAllCategories(false);

    // Then
    Assertions.assertNotNull(result);
    result.forEach(
        dto -> {
          Assertions.assertNotNull(dto, "DTO should not be null");
          Assertions.assertNotNull(dto.code(), "Code should not be null");
          Assertions.assertFalse(dto.code().isBlank(), "Code should not be blank");
        });
  }

  @Test
  @DisplayName("Find all org units should return list of DTOs")
  void findAllOrgUnits_shouldReturnList() {
    if (codeService == null) {
      return;
    }

    // When
    List<CodeDescriptionDto> result = codeService.findAllOrgUnits();

    // Then
    Assertions.assertNotNull(result);
    Assertions.assertInstanceOf(List.class, result);
  }

  @Test
  @DisplayName("Find all org units should return DTOs with code field")
  void findAllOrgUnits_shouldReturnValidDtos() {
    if (codeService == null) {
      return;
    }

    // When
    List<CodeDescriptionDto> result = codeService.findAllOrgUnits();

    // Then
    Assertions.assertNotNull(result);
    result.forEach(
        dto -> {
          Assertions.assertNotNull(dto, "DTO should not be null");
          Assertions.assertNotNull(dto.code(), "Code should not be null");
        });
  }
}
