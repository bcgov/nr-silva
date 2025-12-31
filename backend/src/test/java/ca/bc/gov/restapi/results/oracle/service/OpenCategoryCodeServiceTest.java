package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.common.dto.CodeDescriptionDto;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("Integrated Test | Open Category Code Service")
class OpenCategoryCodeServiceTest extends AbstractTestContainerIntegrationTest {

  @Autowired
  private OpenCategoryCodeService openCategoryCodeService;

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
