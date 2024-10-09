package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.oracle.entity.OpenCategoryCodeEntity;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

class OpenCategoryCodeRepositoryIntegrationTest extends AbstractTestContainerIntegrationTest {

  @Autowired private OpenCategoryCodeRepository openCategoryCodeRepository;

  @Test
  @DisplayName("Find all by expiry date after happy path should succeed")
  void findAllByExpiryDateAfter_happyPath_shouldSucceed() {
    List<OpenCategoryCodeEntity> list =
        openCategoryCodeRepository.findAllByExpiryDateAfter(LocalDate.now());

    Assertions.assertNotNull(list);
    Assertions.assertEquals(22, list.size());

    OpenCategoryCodeEntity category = list.get(0);
    Assertions.assertEquals("CONT", category.getCode());
    Assertions.assertEquals("SP as a part of contractual agreement", category.getDescription());
    Assertions.assertTrue(category.getExpiryDate().isAfter(LocalDate.now()));
  }
}
