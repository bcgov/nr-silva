package ca.bc.gov.restapi.results.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.oracle.entity.CutBlockOpenAdminEntity;
import ca.bc.gov.restapi.results.oracle.repository.CutBlockOpenAdminRepository;
import ca.bc.gov.restapi.results.oracle.service.CutBlockOpenAdminService;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class CutBlockOpenAdminServiceTest {

  @Mock CutBlockOpenAdminRepository cutBlockOpenAdminRepository;

  private CutBlockOpenAdminService cutBlockOpenAdminService;

  @BeforeEach
  void setup() {
    cutBlockOpenAdminService = new CutBlockOpenAdminService(cutBlockOpenAdminRepository);
  }

  @Test
  @DisplayName("Find all openings by opening id list")
  void findAllByOpeningIdIn_simpleFetch_shouldSucceed() {
    Long openingId = 12563L;
    CutBlockOpenAdminEntity entity = new CutBlockOpenAdminEntity();
    entity.setId(123L);
    entity.setOpeningId(openingId);

    when(cutBlockOpenAdminRepository.findAllByOpeningIdIn(any())).thenReturn(List.of(entity));

    List<CutBlockOpenAdminEntity> entityList =
        cutBlockOpenAdminService.findAllByOpeningIdIn(List.of(openingId));

    Assertions.assertNotNull(entityList);
    Assertions.assertEquals(1, entityList.size());
  }
}
