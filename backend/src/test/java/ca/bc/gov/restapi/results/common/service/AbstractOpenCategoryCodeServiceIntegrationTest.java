package ca.bc.gov.restapi.results.common.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;

@DisplayName("Integrated Test | OpenCategoryCodeService | Contract")
public abstract class AbstractOpenCategoryCodeServiceIntegrationTest<T extends OpenCategoryCodeService> extends AbstractTestContainerIntegrationTest {

  @Autowired
  protected OpenCategoryCodeService openCategoryCodeService;

  @Test
  @DisplayName("Find all categories include expired false should succeed")
  void findAllCategories_includeExpiredFalse_shouldSucceed() {

    List<CodeDescriptionDto> entities = openCategoryCodeService.findAllCategories(false);

    Assertions.assertNotNull(entities);
    Assertions.assertEquals(21, entities.size());
  }

  @Test
  @DisplayName("Find all categories include expired true should succeed")
  void findAllCategories_includeExpiredTrue_shouldSucceed() {
    List<CodeDescriptionDto> entities = openCategoryCodeService.findAllCategories(true);

    Assertions.assertNotNull(entities);
    Assertions.assertEquals(37, entities.size());
  }
}
