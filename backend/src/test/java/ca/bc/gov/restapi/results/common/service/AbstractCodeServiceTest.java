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
    List<CodeDescriptionDto> result = codeService.getAllSilvBaseCode();
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
    List<CodeDescriptionDto> result = codeService.getAllSilvTechniqueCode();
    Assertions.assertNotNull(result);
    Assertions.assertInstanceOf(List.class, result);
  }

  @Test
  @DisplayName("Get all silv method codes should return list of DTOs")
  void getAllSilvMethodCode_shouldReturnList() {
    if (codeService == null) {
      return;
    }
    List<CodeDescriptionDto> result = codeService.getAllSilvMethodCode();
    Assertions.assertNotNull(result);
    Assertions.assertInstanceOf(List.class, result);
  }

  @Test
  @DisplayName("Get all silv objective codes should return list of DTOs")
  void getAllSilvObjectiveCode_shouldReturnList() {
    if (codeService == null) {
      return;
    }
    List<CodeDescriptionDto> result = codeService.getAllSilvObjectiveCode();
    Assertions.assertNotNull(result);
    Assertions.assertInstanceOf(List.class, result);
  }

  @Test
  @DisplayName("Get all silv fund source codes should return list of DTOs")
  void getAllSilvFundSrceCode_shouldReturnList() {
    if (codeService == null) {
      return;
    }
    List<CodeDescriptionDto> result = codeService.getAllSilvFundSrceCode();
    Assertions.assertNotNull(result);
    Assertions.assertInstanceOf(List.class, result);
  }

  @Test
  @DisplayName("All code results should contain valid CodeDescriptionDto objects")
  void allCodeResults_shouldContainValidDtos() {
    if (codeService == null) {
      return;
    }
    List<CodeDescriptionDto> baseResults = codeService.getAllSilvBaseCode();
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
    List<CodeDescriptionDto> result = codeService.findAllCategories(false);
    Assertions.assertNotNull(result);
    Assertions.assertInstanceOf(List.class, result);
  }

  @Test
  @DisplayName("Find all categories with includeExpired true should return list")
  void findAllCategories_includeExpiredTrue_shouldReturnList() {
    if (codeService == null) {
      return;
    }
    List<CodeDescriptionDto> result = codeService.findAllCategories(true);
    Assertions.assertNotNull(result);
    Assertions.assertInstanceOf(List.class, result);
  }

  @Test
  @DisplayName("Find all categories should return DTOs with code and description")
  void findAllCategories_shouldReturnValidDtos() {
    if (codeService == null) {
      return;
    }
    List<CodeDescriptionDto> result = codeService.findAllCategories(false);
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
    List<CodeDescriptionDto> result = codeService.findAllOrgUnits(false);
    Assertions.assertNotNull(result);
    Assertions.assertInstanceOf(List.class, result);
  }

  @Test
  @DisplayName("Find all district org units should return list of DTOs")
  void findAllOrgUnits_districtsOnly_shouldReturnList() {
    if (codeService == null) {
      return;
    }
    List<CodeDescriptionDto> result = codeService.findAllOrgUnits(true);
    Assertions.assertNotNull(result);
    Assertions.assertInstanceOf(List.class, result);
  }

  @Test
  @DisplayName("Find all org units should return DTOs with code field")
  void findAllOrgUnits_shouldReturnValidDtos() {
    if (codeService == null) {
      return;
    }
    List<CodeDescriptionDto> result = codeService.findAllOrgUnits(false);
    Assertions.assertNotNull(result);
    result.forEach(
        dto -> {
          Assertions.assertNotNull(dto, "DTO should not be null");
          Assertions.assertNotNull(dto.code(), "Code should not be null");
        });
  }

  @Test
  @DisplayName("Get all disturbance codes should return list of DTOs")
  void getAllDisturbanceCode_shouldReturnList() {
    if (codeService == null) {
      return;
    }
    List<CodeDescriptionDto> result = codeService.getAllDisturbanceCode();
    Assertions.assertNotNull(result);
    Assertions.assertInstanceOf(List.class, result);
  }

  @Test
  @DisplayName("Get all silv system codes should return list of DTOs")
  void getAllSilvSystemCode_shouldReturnList() {
    if (codeService == null) {
      return;
    }
    List<CodeDescriptionDto> result = codeService.getAllSilvSystemCode();
    Assertions.assertNotNull(result);
    Assertions.assertInstanceOf(List.class, result);
  }

  @Test
  @DisplayName("Get all silv system variant codes should return list of DTOs")
  void getAllSilvSystemVariantCode_shouldReturnList() {
    if (codeService == null) {
      return;
    }
    List<CodeDescriptionDto> result = codeService.getAllSilvSystemVariantCode();
    Assertions.assertNotNull(result);
    Assertions.assertInstanceOf(List.class, result);
  }

  @Test
  @DisplayName("Get all silv cut phase codes should return list of DTOs")
  void getAllSilvCutPhaseCode_shouldReturnList() {
    if (codeService == null) {
      return;
    }
    List<CodeDescriptionDto> result = codeService.getAllSilvCutPhaseCode();
    Assertions.assertNotNull(result);
    Assertions.assertInstanceOf(List.class, result);
  }

  @Test
  @DisplayName("Get all silv damage agent codes should return list of DTOs")
  void getAllSilvDamageAgentCode_shouldReturnList() {
    if (codeService == null) {
      return;
    }
    List<CodeDescriptionDto> result = codeService.getAllSilvDamageAgentCode();
    Assertions.assertNotNull(result);
    Assertions.assertInstanceOf(List.class, result);
  }

  @Test
  @DisplayName("Get all silv damage agent codes should return valid DTOs")
  void getAllSilvDamageAgentCode_shouldReturnValidDtos() {
    if (codeService == null) {
      return;
    }
    List<CodeDescriptionDto> result = codeService.getAllSilvDamageAgentCode();
    result.forEach(
        dto -> {
          Assertions.assertNotNull(dto, "DTO should not be null");
          Assertions.assertNotNull(dto.code(), "Code should not be null");
          Assertions.assertFalse(dto.code().isBlank(), "Code should not be blank");
        });
  }

  @Test
  @DisplayName("Get all stocking status codes should return list of DTOs")
  void getAllStockingStatusCode_shouldReturnList() {
    if (codeService == null) {
      return;
    }
    List<CodeDescriptionDto> result = codeService.getAllStockingStatusCode();
    Assertions.assertNotNull(result);
    Assertions.assertInstanceOf(List.class, result);
  }

  @Test
  @DisplayName("Get all stocking status codes should return valid DTOs")
  void getAllStockingStatusCode_shouldReturnValidDtos() {
    if (codeService == null) {
      return;
    }
    List<CodeDescriptionDto> result = codeService.getAllStockingStatusCode();
    result.forEach(
        dto -> {
          Assertions.assertNotNull(dto, "DTO should not be null");
          Assertions.assertNotNull(dto.code(), "Code should not be null");
          Assertions.assertFalse(dto.code().isBlank(), "Code should not be blank");
        });
  }

  @Test
  @DisplayName("Get all stocking type codes should return list of DTOs")
  void getAllStockingTypeCode_shouldReturnList() {
    if (codeService == null) {
      return;
    }
    List<CodeDescriptionDto> result = codeService.getAllStockingTypeCode();
    Assertions.assertNotNull(result);
    Assertions.assertInstanceOf(List.class, result);
  }

  @Test
  @DisplayName("Get all stocking type codes should return valid DTOs")
  void getAllStockingTypeCode_shouldReturnValidDtos() {
    if (codeService == null) {
      return;
    }
    List<CodeDescriptionDto> result = codeService.getAllStockingTypeCode();
    result.forEach(
        dto -> {
          Assertions.assertNotNull(dto, "DTO should not be null");
          Assertions.assertNotNull(dto.code(), "Code should not be null");
        });
  }

  @Test
  @DisplayName("Get all silv tree species codes should return list of DTOs")
  void getAllSilvTreeSpeciesCode_shouldReturnList() {
    if (codeService == null) {
      return;
    }
    List<CodeDescriptionDto> result = codeService.getAllSilvTreeSpeciesCode();
    Assertions.assertNotNull(result);
    Assertions.assertInstanceOf(List.class, result);
  }

  @Test
  @DisplayName("Get all silv tree species codes should return valid DTOs")
  void getAllSilvTreeSpeciesCode_shouldReturnValidDtos() {
    if (codeService == null) {
      return;
    }
    List<CodeDescriptionDto> result = codeService.getAllSilvTreeSpeciesCode();
    result.forEach(
        dto -> {
          Assertions.assertNotNull(dto, "DTO should not be null");
          Assertions.assertNotNull(dto.code(), "Code should not be null");
          Assertions.assertFalse(dto.code().isBlank(), "Code should not be blank");
        });
  }

  @Test
  @DisplayName("All code methods with effectiveOnly true should not include expired codes")
  void allCodeMethods_effectiveOnlyTrue_shouldExcludeExpired() {
    if (codeService == null) {
      return;
    }
    List<List<CodeDescriptionDto>> results =
        List.of(
            codeService.getAllSilvBaseCode(true),
            codeService.getAllSilvTechniqueCode(true),
            codeService.getAllSilvMethodCode(true),
            codeService.getAllSilvObjectiveCode(true),
            codeService.getAllSilvFundSrceCode(true),
            codeService.getAllOpenStatusCode(true),
            codeService.getAllDisturbanceCode(true),
            codeService.getAllSilvSystemCode(true),
            codeService.getAllSilvSystemVariantCode(true),
            codeService.getAllSilvCutPhaseCode(true),
            codeService.getAllSilvDamageAgentCode(true),
            codeService.getAllStockingStatusCode(true),
            codeService.getAllStockingTypeCode(true),
            codeService.getAllSilvTreeSpeciesCode(true));

    for (List<CodeDescriptionDto> list : results) {
      Assertions.assertNotNull(list);
      for (CodeDescriptionDto dto : list) {
        if (dto.description() != null) {
          Assertions.assertFalse(
              dto.description().endsWith(" (Expired)"),
              "Description should not end with '(Expired)' when effectiveOnly=true: " + dto.description());
        }
      }
    }
  }

  @Test
  @DisplayName("All code methods with effectiveOnly false should match no-arg default methods")
  void allCodeMethods_effectiveOnlyFalse_shouldMatchNoArg() {
    if (codeService == null) {
      return;
    }
    Assertions.assertEquals(
        codeService.getAllSilvBaseCode().size(), codeService.getAllSilvBaseCode(false).size());
    Assertions.assertEquals(
        codeService.getAllSilvTechniqueCode().size(),
        codeService.getAllSilvTechniqueCode(false).size());
    Assertions.assertEquals(
        codeService.getAllSilvMethodCode().size(), codeService.getAllSilvMethodCode(false).size());
    Assertions.assertEquals(
        codeService.getAllSilvObjectiveCode().size(),
        codeService.getAllSilvObjectiveCode(false).size());
    Assertions.assertEquals(
        codeService.getAllSilvFundSrceCode().size(),
        codeService.getAllSilvFundSrceCode(false).size());
    Assertions.assertEquals(
        codeService.getAllOpenStatusCode().size(), codeService.getAllOpenStatusCode(false).size());
    Assertions.assertEquals(
        codeService.getAllDisturbanceCode().size(),
        codeService.getAllDisturbanceCode(false).size());
    Assertions.assertEquals(
        codeService.getAllSilvSystemCode().size(), codeService.getAllSilvSystemCode(false).size());
    Assertions.assertEquals(
        codeService.getAllSilvSystemVariantCode().size(),
        codeService.getAllSilvSystemVariantCode(false).size());
    Assertions.assertEquals(
        codeService.getAllSilvCutPhaseCode().size(),
        codeService.getAllSilvCutPhaseCode(false).size());
    Assertions.assertEquals(
        codeService.getAllSilvDamageAgentCode().size(),
        codeService.getAllSilvDamageAgentCode(false).size());
    Assertions.assertEquals(
        codeService.getAllStockingStatusCode().size(),
        codeService.getAllStockingStatusCode(false).size());
    Assertions.assertEquals(
        codeService.getAllStockingTypeCode().size(),
        codeService.getAllStockingTypeCode(false).size());
    Assertions.assertEquals(
        codeService.getAllSilvTreeSpeciesCode().size(),
        codeService.getAllSilvTreeSpeciesCode(false).size());
  }
}
