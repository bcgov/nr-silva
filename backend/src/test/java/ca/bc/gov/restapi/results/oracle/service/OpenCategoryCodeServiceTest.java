package ca.bc.gov.restapi.results.oracle.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.oracle.entity.OpenCategoryCodeEntity;
import ca.bc.gov.restapi.results.oracle.repository.OpenCategoryCodeRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class OpenCategoryCodeServiceTest {

  @Mock OpenCategoryCodeRepository openCategoryCodeRepository;

  private OpenCategoryCodeService openCategoryCodeService;

  @BeforeEach
  void setup() {
    openCategoryCodeService = new OpenCategoryCodeService(openCategoryCodeRepository);
  }

  @Test
  @DisplayName("Find all categories include expired false should succeed")
  void findAllCategories_includeExpiredFalse_shouldSucceed() {
    OpenCategoryCodeEntity category = new OpenCategoryCodeEntity();
    category.setCode("FTML");
    category.setDescription("Free Growing");
    category.setEffectiveDate(LocalDate.now().minusYears(3L));
    category.setExpiryDate(LocalDate.now().plusYears(3L));
    category.setUpdateTimestamp(LocalDate.now());

    when(openCategoryCodeRepository.findAllByExpiryDateAfter(any())).thenReturn(List.of(category));
    List<OpenCategoryCodeEntity> entities = openCategoryCodeService.findAllCategories(false);

    Assertions.assertNotNull(entities);
    Assertions.assertEquals(1, entities.size());
    Assertions.assertTrue(entities.get(0).getExpiryDate().isAfter(LocalDate.now()));

    verify(openCategoryCodeRepository, times(0)).findAll();
  }

  @Test
  @DisplayName("Find all categories include expired true should succeed")
  void findAllCategories_includeExpiredTrue_shouldSucceed() {
    OpenCategoryCodeEntity category = new OpenCategoryCodeEntity();
    category.setCode("FTML");
    category.setDescription("Free Growing");
    category.setEffectiveDate(LocalDate.now().minusYears(3L));
    category.setExpiryDate(LocalDate.now().minusYears(1L));
    category.setUpdateTimestamp(LocalDate.now());

    when(openCategoryCodeRepository.findAll()).thenReturn(List.of(category));
    List<OpenCategoryCodeEntity> entities = openCategoryCodeService.findAllCategories(true);

    Assertions.assertNotNull(entities);
    Assertions.assertEquals(1, entities.size());
    Assertions.assertTrue(entities.get(0).getExpiryDate().isBefore(LocalDate.now()));

    verify(openCategoryCodeRepository, times(0)).findAllByExpiryDateAfter(any());
  }
}
